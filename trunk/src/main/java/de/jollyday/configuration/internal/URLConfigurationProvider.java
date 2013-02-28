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
package de.jollyday.configuration.internal;

import java.io.InputStream;
import java.net.URL;
import java.util.Properties;
import java.util.logging.Logger;

import de.jollyday.configuration.ConfigurationProvider;

/**
 * An {@link ConfigurationProvider} implementation which reads a list of URLs
 * provided by the system property 'de.jollyday.config.urls' in order they are
 * provided.
 * 
 * @author sven
 * 
 */
public class URLConfigurationProvider implements ConfigurationProvider {

	private static final Logger LOG = Logger.getLogger(URLConfigurationProvider.class.getName());

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.jollyday.configuration.ConfigurationProvider#addConfiguration(java
	 * .util.Properties)
	 */
	public Properties getProperties() {
		Properties properties = new Properties();
		Properties systemProps = System.getProperties();
		String configURLs = systemProps.getProperty(CONFIG_URLS_PROPERTY);
		if (configURLs != null) {
			String[] strConfigURLs = configURLs.split(",");
			if (strConfigURLs != null) {
				for (String strURL : strConfigURLs) {
					if (strURL == null || "".equals(strURL))
						continue;
					InputStream inputStream = null;
					try {
						URL configURL = new URL(strURL.trim());
						inputStream = configURL.openStream();
						properties.load(inputStream);
					} catch (Exception e) {
						LOG.warning("Cannot read configuration from '" + strURL + "'. " + e.getClass().getSimpleName()
								+ " (" + e.getMessage() + ").");
					} finally {
						if (inputStream != null) {
							try {
								inputStream.close();
							} catch (Exception e) {
								LOG.warning("Cannot close stream for configuration URL " + strURL + ".");
							}
						}
					}

				}
			}
		}
		return properties;
	}

}
