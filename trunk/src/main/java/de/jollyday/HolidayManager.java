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

import java.util.Calendar;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.joda.time.LocalDate;
import org.joda.time.ReadableInterval;

import de.jollyday.caching.HolidayManagerValueHandler;
import de.jollyday.configuration.ConfigurationProviderManager;
import de.jollyday.datasource.ConfigurationDataSource;
import de.jollyday.util.Cache;
import de.jollyday.util.Cache.ValueHandler;
import de.jollyday.util.CalendarUtil;

/**
 * Abstract base class for all holiday manager implementations. Upon call of
 * getInstance method the implementing class will be read from the
 * jollyday.properties file and instantiated.
 * 
 * @author Sven Diedrichsen
 * @version $Id: $
 */
public abstract class HolidayManager {

	private static final Logger LOG = Logger.getLogger(HolidayManager.class
			.getName());
	/**
	 * Signifies if caching of manager instances is enabled. If not every call
	 * to getInstance will return a newly instantiated and initialized manager.
	 */
	private static boolean CACHING_ENABLED = true;
	/**
	 * Cache for manager instances on a per country basis.
	 */
	private static final Cache<HolidayManager> MANAGER_CHACHE = new Cache<HolidayManager>();
	/**
	 * Manager for configuration providers. Delivers the jollyday configuration.
	 */
	private static ConfigurationProviderManager configurationProviderManager = new ConfigurationProviderManager();
	/**
	 * the holiday cache
	 */
	private Cache<Set<Holiday>> holidayCache = new Cache<Set<Holiday>>();
	/**
	 * Utility for calendar operations
	 */
	protected CalendarUtil calendarUtil = new CalendarUtil();
	/**
	 * The datasource to get the holiday data from.
	 */
	private ConfigurationDataSource configurationDataSource;
	/**
	 * the manager parameter
	 */
	private ManagerParameter managerParameter;

	/**
	 * Creates a HolidayManager instance for the default locale country using
	 * the configured properties from the configuration file.
	 * @return a eventually cached HolidayManager instance
	 */
	public static final HolidayManager getInstance() {
		return getInstance(ManagerParameters.create((String)null, null));
	}

	/**
	 * Creates a HolidayManager instance for the default locale country using
	 * the provided properties.
	 * @param properties the overriding configuration properties.
	 * @return a eventually cached HolidayManager instance
	 */
	public static final HolidayManager getInstance(Properties properties) {
		return getInstance(ManagerParameters.create((String)null, properties));
	}

	@Deprecated
	public static final HolidayManager getInstance(final HolidayCalendar c) {
		return getInstance(ManagerParameters.create(c, null));
	}

	@Deprecated
	public static final HolidayManager getInstance(final HolidayCalendar c,
			Properties properties) {
		return getInstance(ManagerParameters.create(c, properties));
	}

	@Deprecated
	public static final HolidayManager getInstance(final String calendar) {
		return getInstance(ManagerParameters.create(calendar, null));
	}

	@Deprecated
	public static final HolidayManager getInstance(final String calendar,
			Properties properties) {
		return getInstance(ManagerParameters.create(calendar, properties));
	}

	/**
	 * Creates and returns a {@link HolidayManager} for the provided
	 * {@link ManagerParameters}
	 * 
	 * @param parameters
	 *            the {@link ManagerParameters} to create the manager with
	 * @return the {@link HolidayManager} instance
	 */
	public static final HolidayManager getInstance(ManagerParameter parameter) {
		return createManager(parameter);
	}

	/**
	 * Creates a new <code>HolidayManager</code> instance for the country and
	 * puts it to the manager cache.
	 * 
	 * @param calendar
	 *            <code>HolidayManager</code> instance for the calendar
	 * @return new
	 */
	private static HolidayManager createManager(final ManagerParameter parameter) {
		if (LOG.isLoggable(Level.FINER)) {
			LOG.finer("Creating HolidayManager for calendar '" + parameter
					+ "'. Caching enabled: " + isManagerCachingEnabled());
		}
		configurationProviderManager.mergeConfigurationProperties(parameter);
		final String managerImplClassName = readManagerImplClassName(parameter);
		HolidayManagerValueHandler holidayManagerValueHandler = new HolidayManagerValueHandler(
				parameter, managerImplClassName);		
		if(isManagerCachingEnabled()){
			return MANAGER_CHACHE.get(holidayManagerValueHandler);
		}else{
			return holidayManagerValueHandler.createValue();
		}
	}


	/**
	 * Reads the managers implementation class from the properties config file.
	 * 
	 * @param calendar
	 *            the calendar name
	 * @param props
	 *            properties to read from
	 * @return the manager implementation class name
	 */
	private static String readManagerImplClassName(ManagerParameter parameter) {
		String className = parameter.getManangerImplClassName();
		if (className == null) {
			throw new IllegalStateException("Missing configuration '"
					+ ManagerParameter.MANAGER_IMPL_CLASS_PREFIX
					+ "'. Cannot create manager.");
		}
		return className;
	}

	/**
	 * If true, instantiated managers will be cached. If false every call to
	 * getInstance will create new manager. True by default.
	 * 
	 * @param CACHING_ENABLED
	 *            the CACHING_ENABLED to set
	 */
	public static void setManagerCachingEnabled(boolean managerCachingEnabled) {
		CACHING_ENABLED = managerCachingEnabled;
	}

	/**
	 * <p>
	 * isManagerCachingEnabled.
	 * </p>
	 * 
	 * @return the CACHING_ENABLED
	 */
	public static boolean isManagerCachingEnabled() {
		return CACHING_ENABLED;
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
	 * Calls isHoliday with JODA time object.
	 * 
	 * @param c
	 *            a {@link java.util.Calendar} object.
	 * @param args
	 *            a {@link java.lang.String} object.
	 * @return a boolean.
	 */
	public boolean isHoliday(final Calendar c, final String... args) {
		return isHoliday(calendarUtil.create(c), args);
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
		final StringBuilder keyBuilder = new StringBuilder();
		keyBuilder.append(c.getYear());
		for (String arg : args) {
			keyBuilder.append("_");
			keyBuilder.append(arg);
		}
		Set<Holiday> holidays = holidayCache.get(new ValueHandler<Set<Holiday>>() {
			@Override
			public String getKey() {
				return keyBuilder.toString();
			}
			@Override
			public Set<Holiday> createValue() {
				return getHolidays(c.getYear(), args);
			}
		});
		return calendarUtil.contains(holidays,c);
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
	 * Sets the configuration datasource with this holiday manager.
	 * 
	 * @param configurationDataSource
	 *            the {@link ConfigurationDataSource} to use.
	 */
	public void setConfigurationDataSource(
			ConfigurationDataSource configurationDataSource) {
		this.configurationDataSource = configurationDataSource;
	}

	/**
	 * Returns the {@link ConfigurationDataSource} to be used to retrieve
	 * holiday data.
	 * 
	 * @return the {@link ConfigurationDataSource} to use.
	 */
	public ConfigurationDataSource getConfigurationDataSource() {
		return configurationDataSource;
	}

	public ManagerParameter getManagerParameter() {
		return managerParameter;
	}

	/**
	 * Initializes the implementing manager for the provided calendar.
	 * 
	 * @param calendar
	 *            i.e. us, uk, de
	 */
	public void init(ManagerParameter parameters) {
		this.managerParameter = parameters;
		this.doInit();
	}

	abstract public void doInit();

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
	 *            a {@link java.lang.String} object.
	 * @return list of holidays within the interval
	 */
	abstract public Set<Holiday> getHolidays(ReadableInterval interval,
			String... args);

	/**
	 * Returns the configured hierarchy structure for the specific manager. This
	 * hierarchy shows how the configured holidays are structured and can be
	 * retrieved.
	 * 
	 * @return Current calendars hierarchy
	 */
	abstract public CalendarHierarchy getCalendarHierarchy();

}
