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
import java.util.Properties;
import java.util.Set;

import org.joda.time.LocalDate;

/**
 * Abstract base class for all holiday manager implementations. Upon call of getInstance
 * method the implementing class will be read from the application.properties file and
 * instantiated.
 * @author Sven Diedrichsen
 *
 */
public abstract class Manager {

	/**
	 * Configuration property for implementing Manager class
	 */
	private static final String MANAGER_IMPL_CLASS_PREFIX = "manager.impl";
	/**
	 * The name of the configuration file.
	 */
	private static final String CONFIG_FILE = "application.properties";

	/**
	 * Creates an Manager instance. The implementing Manager class will be read from the
	 * application.properties file.
	 * @param country
	 * @return Manager implementation for the provided country.
	 * @throws Exception
	 */
	public static final Manager getInstance(String country) throws Exception {
		Properties props = new Properties();
		props.load(ClassLoader.getSystemResourceAsStream(CONFIG_FILE));
		String managerImplClass = null;
		if (props.stringPropertyNames().contains(MANAGER_IMPL_CLASS_PREFIX + "." + country)) {
			managerImplClass = props.getProperty(MANAGER_IMPL_CLASS_PREFIX + "." + country);
		} else {
			managerImplClass = props.getProperty(MANAGER_IMPL_CLASS_PREFIX);
		}
		Manager m = (Manager) Class.forName(managerImplClass).newInstance();
		m.init(country);
		return m;
	}

	/**
	 * Calls isHoliday with JODA time object.
	 * @see Manager.isHoliday(LocalDate c, String... args)
	 */
	public boolean isHoliday(Calendar c, String... args) {
		return isHoliday(new LocalDate(c), args);
	}

	/**
	 * Show if the requested date is a holiday.
	 * @param c The potential holiday.
	 * @param args Hierarchy to request the holidays for. i.e. args = {'ny'} -> New York holidays
	 * @return is a holiday in the state/region
	 */
	public boolean isHoliday(LocalDate c, String... args) {
		return getHolidays(c.getYear(), args).contains(c);
	}

	/**
	 * Returns the the holidays for the requested year and hierarchy structure. 
	 * @param year i.e. 2010
	 * @param args i.e. args = {'ny'}. returns US/New York holidays. 
	 * No args -> holidays common to whole country
	 * @return the list of holidays for the requested year
	 */
	abstract public Set<LocalDate> getHolidays(int year, String... args);

	/**
	 * Initializes the implementing manager for the provided country.
	 * @param country i.e. us, uk, de
	 * @throws Exception
	 */
	abstract public void init(String country) throws Exception;
	
	/**
	 * Returns the configured hierarchy structure for the specific manager.
	 * This hierarchy shows how the configured holidays are structured and
	 * can be retrieved.
	 * @return
	 */
	abstract public Hierarchy getHierarchy();

}
