/**
 * Copyright 2012 Sven Diedrichsen 
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
package de.jollyday.persistence;

import java.io.IOException;
import java.net.URL;

import de.jollyday.config.Configuration;
import de.jollyday.util.XMLUtil;

/**
 * @author sven
 * 
 */
public class ClasspathPersistenceManager implements IPersistenceManager {

	/**
	 * prefix of the config files.
	 */
	private static final String FILE_PREFIX = "holidays/Holidays";
	/**
	 * suffix of the config files.
	 */
	private static final String FILE_SUFFIX = ".xml";

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.jollyday.persistence.IPersistenceManager#getConfiguration(java.lang
	 * .String)
	 */
	public Configuration getConfiguration(String calendarName) {
		String fileName = getConfigurationFileName(calendarName);
		ClassLoader classLoader = getClass().getClassLoader();
		URL resource = classLoader.getResource(fileName);
		try {
			Configuration configuration = XMLUtil.unmarshallConfiguration(resource.openStream());
			return configuration;
		} catch (IOException e) {
			throw new IllegalStateException("Cannot read configuration for calendar " + calendarName + ".", e);
		}
	}

	/**
	 * Returns the configuration file name for the country.
	 * 
	 * @param calendarName
	 *            the calendar name
	 * @return file name
	 */
	private static String getConfigurationFileName(final String calendarName) {
		return FILE_PREFIX + "_" + calendarName + FILE_SUFFIX;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.jollyday.persistence.IPersistenceManager#flushCache()
	 */
	public void flushCache() {
		// TODO Auto-generated method stub

	}

}
