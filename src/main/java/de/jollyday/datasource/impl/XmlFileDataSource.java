/**
 * Copyright 2013 Sven Diedrichsen 
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
package de.jollyday.datasource.impl;

import java.io.InputStream;
import java.net.URL;

import de.jollyday.config.Configuration;
import de.jollyday.datasource.ConfigurationDataSource;
import de.jollyday.util.ResourceUtil;
import de.jollyday.util.XMLUtil;
/**
 * This {@link ConfigurationDataSource} implementation reads XML files as resources
 * from the classpath.
 */
public class XmlFileDataSource implements ConfigurationDataSource {
	/**
	 * prefix of the config files.
	 */
	private static final String FILE_PREFIX = "holidays/Holidays";
	/**
	 * suffix of the config files.
	 */
	private static final String FILE_SUFFIX = ".xml";
	/**
	 * XML utility class.
	 */
	private XMLUtil xmlUtil = new XMLUtil();
	/**
	 * The utility to load resources.
	 */
	private ResourceUtil resourceUtil = new ResourceUtil();

	public Configuration getConfiguration(String calendar) {
		String configurationFileName = getConfigurationFileName(calendar);
		URL urlDestination = resourceUtil.getResource(configurationFileName);
		try {
			final InputStream inputStream = urlDestination.openStream();
			return xmlUtil.unmarshallConfiguration(inputStream);
		} catch (Exception e) {
			throw new IllegalStateException("Cannot instantiate configuration.", e);
		}
	}
	
	/**
	 * Returns the configuration file name for the country.
	 * 
	 * @param country
	 *            a {@link java.lang.String} object.
	 * @return file name
	 */
	public static String getConfigurationFileName(final String country) {
		return FILE_PREFIX + "_" + country + FILE_SUFFIX;
	}


}
