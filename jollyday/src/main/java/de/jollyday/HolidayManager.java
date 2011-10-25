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
import java.io.InputStream;
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
import org.joda.time.ReadableInterval;

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
	 * Logger.
	 */
	private static final Logger LOG = Logger.getLogger(HolidayManager.class
			.getName());
	/**
	 * System property to define overriding configuration file.
	 */
	private static final String SYSTEM_CONFIG_PROPERTY = "de.jollyday.config";
	/**
	 * Configuration property for the implementing Manager class.
	 */
	private static final String MANAGER_IMPL_CLASS_PREFIX = "manager.impl";
	/**
	 * The name of the configuration file.
	 */
	private static final String CONFIG_FILE = "jollyday.properties";
	/**
	 * Signifies if caching of manager instances is enabled. If not every call
	 * to getInstance will return a newly instantiated and initialized manager.
	 */
	private static boolean managerCachingEnabled = true;
	/**
	 * This map represents a cache for manager instances on a per country basis.
	 */
	private static final Map<String, HolidayManager> MANAGER_CHACHE = new HashMap<String, HolidayManager>();

	/**
	 * Caches the holidays for a given year and state/region.
	 */
	private Map<String, Set<Holiday>> holidaysPerYear = new HashMap<String, Set<Holiday>>();
	/**
	 * The configuration properties.
	 */
	private Properties properties = new Properties();

	/**
	 * Returns a HolidayManager instance by calling getInstance(NULL) and thus
	 * using the default locales country code. code.
	 * 
	 * @return default locales HolidayManager
	 */
	public static final HolidayManager getInstance() {
		return getInstance((String) null);
	}

	/**
	 * Returns a HolidayManager for the provided country.
	 * 
	 * @param c
	 *            Country
	 * @return HolidayManager
	 */
	public static final HolidayManager getInstance(final HolidayCalendar c) {
		return getInstance(c.getId());
	}

	/**
	 * Creates an HolidayManager instance. The implementing HolidayManager class
	 * will be read from the jollyday.properties file. If the country is NULL or
	 * an empty string the default locales country code will be used.
	 * 
	 * @param country
	 * @return HolidayManager implementation for the provided country.
	 */
	public static final HolidayManager getInstance(String country) {
		country = prepareCountryCode(country);
		HolidayManager m = isManagerCachingEnabled() ? getFromCache(country)
				: null;
		if (m == null) {
			m = createManager(country);
		}
		return m;
	}

	/**
	 * Creates a new <code>HolidayManager</code> instance for the country and
	 * puts it to the manager cache.
	 * 
	 * @param country
	 *            <code>HolidayManager</code> instance for the country
	 * @return new
	 */
	private static HolidayManager createManager(final String country) {
		HolidayManager m;
		if (LOG.isLoggable(Level.FINER)) {
			LOG.finer("Creating HolidayManager for country '" + country
					+ "'. Caching enabled: " + isManagerCachingEnabled());
		}
		Properties props = readProperties();
		String managerImplClass = null;
		if (props.containsKey(MANAGER_IMPL_CLASS_PREFIX + "." + country)) {
			managerImplClass = props.getProperty(MANAGER_IMPL_CLASS_PREFIX
					+ "." + country);
		} else if (props.containsKey(MANAGER_IMPL_CLASS_PREFIX)) {
			managerImplClass = props.getProperty(MANAGER_IMPL_CLASS_PREFIX);
		} else {
			throw new IllegalStateException("Missing configuration '"
					+ MANAGER_IMPL_CLASS_PREFIX + "'. Cannot create manager.");
		}
		try {
			m = (HolidayManager) Class.forName(managerImplClass).newInstance();
		} catch (Exception e) {
			throw new IllegalStateException("Cannot create manager class "
					+ managerImplClass, e);
		}
		m.setProperties(props);
		m.init(country);
		if (isManagerCachingEnabled()) {
			putToCache(country, m);
		}
		return m;
	}

	/**
	 * Handles NULL or empty country codes and returns the default locals
	 * country codes for those. For all others the country code will be trimmed
	 * and set to lower case letters.
	 * 
	 * @param country
	 * @return trimmed and lower case country code.
	 */
	private static String prepareCountryCode(String country) {
		if (country == null || "".equals(country.trim())) {
			country = Locale.getDefault().getCountry().toLowerCase();
		} else {
			country = country.trim().toLowerCase();
		}
		return country;
	}

	/**
	 * Caches the manager instance for this country.
	 * 
	 * @param country
	 * @param manager
	 */
	private static void putToCache(final String country,
			final HolidayManager manager) {
		synchronized (MANAGER_CHACHE) {
			MANAGER_CHACHE.put(country, manager);
		}
	}

	/**
	 * Tries to retrieve a manager instance from cache by country.
	 * 
	 * @param country
	 * @return Manager instance for this country. NULL if none is cached yet.
	 */
	private static HolidayManager getFromCache(final String country) {
		synchronized (MANAGER_CHACHE) {
			return MANAGER_CHACHE.get(country);
		}
	}

	/**
	 * If true, instantiated managers will be cached. If false every call to
	 * getInstance will create new manager. True by default.
	 * 
	 * @param managerCachingEnabled
	 *            the managerCachingEnabled to set
	 */
	public static void setManagerCachingEnabled(boolean managerCachingEnabled) {
		HolidayManager.managerCachingEnabled = managerCachingEnabled;
	}

	/**
	 * @return the managerCachingEnabled
	 */
	public static boolean isManagerCachingEnabled() {
		return managerCachingEnabled;
	}

	/**
	 * Clears the manager cache from all cached manager instances.
	 */
	public static void clearManagerCache() {
		synchronized (MANAGER_CHACHE) {
			MANAGER_CHACHE.clear();
		}
	}

	/**
	 * Reads all configuration properties from classpath and config file and
	 * merges them.
	 * 
	 * @return Merged config properties.
	 * @throws IOException
	 */
	private static Properties readProperties() {
		Properties props = readPropertiesFromClasspath();
		props.putAll(readPropertiesFromConfigFile());
		return props;
	}

	/**
	 * Opens the default configuration file from classpath.
	 * 
	 * @return Properties
	 */
	private static Properties readPropertiesFromClasspath() {
		Properties props = new Properties();
		InputStream stream = null;
		try {
			try {
				stream = HolidayManager.class.getClassLoader()
						.getResourceAsStream(CONFIG_FILE);
				if (stream != null) {
					props.load(stream);
				} else {
					LOG.warning("Could not load properties file '"
							+ CONFIG_FILE + "' from classpath.");
				}
			} finally {
				if (stream != null) {
					stream.close();
				}
			}
		} catch (IOException e) {
			throw new IllegalStateException(
					"Could not load properties from classpath.", e);
		}
		return props;
	}

	/**
	 * Tries to read a configuration file from an eventually defined system
	 * property. Exceptions will be caught and put into the log.
	 * 
	 * @return Properties
	 */
	private static Properties readPropertiesFromConfigFile() {
		Properties p = new Properties();
		Properties systemProps = System.getProperties();
		if (systemProps.containsKey(SYSTEM_CONFIG_PROPERTY)) {
			String configFileName = systemProps
					.getProperty(SYSTEM_CONFIG_PROPERTY);
			InputStream input = null;
			try {
				input = new FileInputStream(configFileName);
				p.load(input);
			} catch (IOException e) {
				if (LOG.isLoggable(Level.WARNING)) {
					LOG.warning("Cannot read specified configuration file "
							+ configFileName + ". " + e.getMessage());
				}
			} finally {
				if (input != null) {
					try {
						input.close();
					} catch (IOException e) {
						LOG.warning("Could not close input stream for loading properties "
								+ configFileName + ".");
					}
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
	public boolean isHoliday(final Calendar c, final String... args) {
		return isHoliday(CalendarUtil.create(c), args);
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
	public boolean isHoliday(final LocalDate c, final String... args) {
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
	 * Returns a set of all currently supported calendar codes.
	 * 
	 * @return Set of supported calendar codes.
	 */
	public static Set<String> getSupportedCalendarCodes() {
		Set<String> supportedCalendars = new HashSet<String>();
		for (HolidayCalendar c : HolidayCalendar.values()) {
			supportedCalendars.add(c.getId());
		}
		return supportedCalendars;
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
	 * Returns the holidays for the requested year and hierarchy structure.
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
	 * Returns the holidays for the requested interval and hierarchy structure.
	 * 
	 * @param interval
	 *            the interval in which the holidays lie.
	 * @param args
	 * @return list of holidays within the interval
	 */
	abstract public Set<Holiday> getHolidays(ReadableInterval interval,
			String... args);

	/**
	 * Initializes the implementing manager for the provided country.
	 * 
	 * @param country
	 *            i.e. us, uk, de
	 */
	abstract public void init(String country);

	/**
	 * Returns the configured hierarchy structure for the specific manager. This
	 * hierarchy shows how the configured holidays are structured and can be
	 * retrieved.
	 * 
	 * @return Current calendars hierarchy
	 */
	abstract public CalendarHierarchy getCalendarHierarchy();

}
