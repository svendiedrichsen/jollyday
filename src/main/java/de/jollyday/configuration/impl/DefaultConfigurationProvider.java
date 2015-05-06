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
package de.jollyday.configuration.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Logger;

import de.jollyday.configuration.ConfigurationProvider;
import de.jollyday.util.ResourceUtil;

/**
 * Provider which adds jollydays default configuration file
 * 'jollyday.properties' by reading it from the classpath by using the currents
 * threads classloader.
 * 
 * @author Sven Diedrichsen
 */
public class DefaultConfigurationProvider implements ConfigurationProvider {

	private static final Logger LOG = Logger
			.getLogger(DefaultConfigurationProvider.class.getName());

	/**
	 * The name of the configuration file.
	 */
	private static final String CONFIG_FILE = "jollyday.properties";
	/**
	 * The utility to load resources.
	 */
	private final ResourceUtil resourceUtil = new ResourceUtil();

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.jollyday.configuration.ConfigurationProvider#addConfiguration(java
	 * .util.Properties)
	 */
	@Override
	public Properties getProperties() {
		Properties properties = new Properties();
		try {
			InputStream stream = null;
			try {
				stream = resourceUtil.getResource(CONFIG_FILE).openStream();
				if (stream != null) {
					properties.load(stream);
				} else {
					LOG.warning("Could not load default properties file '"
							+ CONFIG_FILE + "' from classpath.");
				}
			} finally {
				closeStream(stream);
			}
			return properties;
		} catch (IOException e) {
			throw new IllegalStateException(
					"Could not load default properties from classpath.", e);
		}
	}

	private void closeStream(InputStream stream) {
		if (stream != null) {
			try {
				stream.close();
			} catch (IOException e) {
				LOG.warning("Could not close configurations input stream.");
			}
		}
	}

}
