/**
 * Copyright 2010 Sven Diedrichsen 
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 * 
 * http://www.apache.org/licenses/LICENSE-2.0 
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an 
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either 
 * express or implied. See the License for the specific language 
 * governing permissions and limitations under the License. 
 */
package de.jollyday;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.joda.time.LocalDate;

import de.jollyday.util.CalendarUtil;

/**
 * Abstract base class for all holiday manager implementations. Upon call of
 * getInstance method the implementing class will be read from the
 * jollyday.properties file and instantiated.
 * 
 * @author Sven Diedrichsen
 * 
 */
public abstract class HolidayManager {

	/**
	 * Logger
	 */
	private static final Logger LOG = Logger.getLogger(HolidayManager.class.getName());
	/**
	 * System property to define overriding configuration file.
	 */
	private static final String SYSTEM_CONFIG_PROPERTY = "de.jollyday.config";
	/**
	 *  Configuration property for the list of supported countries.
	 */
	private static final String MANAGER_SUPPORTED_COUNTRIES = "manager.supported.countries";
	/**
	 * Configuration property for the implementing Manager class
	 */
	private static final String MANAGER_IMPL_CLASS_PREFIX = "manager.impl";
	/**
	 * The name of the configuration file.
	 */
	private static final String CONFIG_FILE = "jollyday.properties";
	/**
	 * Caches the holidays for a given year and state/region.
	 */
	private Map<String, Set<Holiday>> holidaysPerYear = new HashMap<String, Set<Holiday>>();
	/**
	 * The configuration properties
	 */
	private Properties properties = new Properties();

	/**
	 * Returns a HolidayManager instance by calling getInstance(NULL) and thus using
	 * the default locales country code.
	 * code.
	 * 
	 * @return default locales HolidayManager
	 * @throws Exception
	 */
	public static final HolidayManager getInstance() throws Exception {
		return getInstance(null);
	}

	/**
	 * Creates an HolidayManager instance. The implementing HolidayManager class will be read
	 * from the jollyday.properties file. If the country is NULL or an empty string the
	 * default locales country code will be used.
	 * 
	 * @param country
	 * @return HolidayManager implementation for the provided country.
	 * @throws Exception
	 */
	public static final HolidayManager getInstance(String country) throws Exception {
		Properties props = readProperties();
		country = prepareCountryCode(country);
		String managerImplClass = null;
		if (props.stringPropertyNames().contains(
				MANAGER_IMPL_CLASS_PREFIX + "." + country)) {
			managerImplClass = props.getProperty(MANAGER_IMPL_CLASS_PREFIX
					+ "." + country);
		} else if(props.stringPropertyNames().contains(MANAGER_IMPL_CLASS_PREFIX)) {
			managerImplClass = props.getProperty(MANAGER_IMPL_CLASS_PREFIX);
		} else {
			throw new IllegalStateException("Missing configuration '"+MANAGER_IMPL_CLASS_PREFIX+"'. Cannot create manager.");
		}
		HolidayManager m = (HolidayManager) Class.forName(managerImplClass).newInstance();
		m.init(country);
		m.setProperties(props);
		return m;
	}

	/**
	 * Handles NULL or empty country codes and returns the default locales country
	 * codes for those. For all others the country code will be trimmed and set to 
	 * lower case letters.
	 *  
	 * @param country
	 * @return trimmed and lower case country code.
	 */
	private static String prepareCountryCode(String country) {
		if(country == null || "".equals(country.trim())){
			country = Locale.getDefault().getCountry().toLowerCase();
		}
		else{
			country = country.trim().toLowerCase();
		}
		return country;
	}

	/**
	 * Reads all configuration properties from classpath and config file
	 * and merges them.
	 * @return Merged config properties.
	 * @throws IOException
	 */
	private static Properties readProperties() throws IOException {
		Properties props = readPropertiesFromClasspath();
		props.putAll(readPropertiesFromConfigFile());
		return props;
	}

	/**
	 * Opens the default configuration file from classpath.
	 * @return Properties
	 * @throws IOException
	 */
	private static Properties readPropertiesFromClasspath()
			throws IOException {
		Properties props = new Properties();
		props.load(Thread.currentThread().getContextClassLoader().getResourceAsStream(CONFIG_FILE));
		return props;
	}

	/**
	 * Tries to read a configuration file from an eventually defined
	 * system property. Exceptions will be caught and put into the log.
	 * @return Properties
	 */
	private static Properties readPropertiesFromConfigFile() {
		Properties p = new Properties();
		Properties systemProps = System.getProperties();
		if (systemProps.stringPropertyNames().contains(SYSTEM_CONFIG_PROPERTY)) {
			String configFileName = 
				systemProps.getProperty(SYSTEM_CONFIG_PROPERTY);
			try {
				p.load(new FileInputStream(configFileName));
			} catch (IOException e) {
				if (LOG.isLoggable(Level.WARNING)) {
					LOG.warning("Cannot read specified configuration file "
							+ configFileName + ". " + e.getMessage());
				}
			}
		}
		return p;
	}

	/**
	 * Calls isHoliday with JODA time object.
	 * 
	 * @see Manager.isHoliday(LocalDate c, String... args)
	 */
	public boolean isHoliday(Calendar c, String... args) {
		return isHoliday(new LocalDate(c), args);
	}

	/**
	 * Show if the requested date is a holiday.
	 * 
	 * @param c
	 *            The potential holiday.
	 * @param args
	 *            Hierarchy to request the holidays for. i.e. args = {'ny'} ->
	 *            New York holidays
	 * @return is a holiday in the state/region
	 */
	public boolean isHoliday(LocalDate c, String... args) {
		StringBuilder keyBuilder = new StringBuilder();
		keyBuilder.append(c.getYear());
		for (String arg : args) {
			keyBuilder.append("_");
			keyBuilder.append(arg);
		}
		String key = keyBuilder.toString();
		if (!holidaysPerYear.containsKey(key)) {
			Set<Holiday> holidays = getHolidays(c.getYear(), args);
			holidaysPerYear.put(key, holidays);
		}
		return CalendarUtil.contains(holidaysPerYear.get(key), c);
	}
	
	/**
	 * Returns a set of all currently supported country codes.
	 * @return Set of supported country codes.
 	 */
	public static Set<String> getSupportedCountryCodes() throws Exception{
		Set<String> supportedCountries = new HashSet<String>();
		Properties p = readProperties();
		if(p.stringPropertyNames().contains(MANAGER_SUPPORTED_COUNTRIES)){
			String strSupportedCountries = p.getProperty(MANAGER_SUPPORTED_COUNTRIES);
			if(null != strSupportedCountries){
				supportedCountries.addAll(Arrays.asList(strSupportedCountries.split(",")));
			}
		}
		return supportedCountries;
	}


	/**
	 * @return the configuration properties
	 */
	protected Properties getProperties() {
		return properties;
	}

	/**
	 * @param properties
	 *            the configuration properties to set
	 */
	protected void setProperties(Properties properties) {
		this.properties.putAll(properties);
	}

	/**
	 * Returns the the holidays for the requested year and hierarchy structure.
	 * 
	 * @param year
	 *            i.e. 2010
	 * @param args
	 *            i.e. args = {'ny'}. returns US/New York holidays. No args ->
	 *            holidays common to whole country
	 * @return the list of holidays for the requested year
	 */
	abstract public Set<Holiday> getHolidays(int year, String... args);

	/**
	 * Initializes the implementing manager for the provided country.
	 * 
	 * @param country
	 *            i.e. us, uk, de
	 * @throws Exception
	 */
	abstract public void init(String country) throws Exception;

	/**
	 * Returns the configured hierarchy structure for the specific manager. This
	 * hierarchy shows how the configured holidays are structured and can be
	 * retrieved.
	 * 
	 * @return
	 */
	abstract public CountryHierarchy getHierarchy();

}
