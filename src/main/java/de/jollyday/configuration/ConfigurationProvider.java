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
package de.jollyday.configuration;

import java.util.Properties;

/**
 * The interface for jollyday configuration provider.
 *
 * @author sven
 *
 */
public interface ConfigurationProvider {

	/**
	 * System property to define a comma separated list of custom
	 * {@link ConfigurationProvider} implementations to use for jollyday
	 * configuration.
	 */
	String CONFIG_PROVIDERS_PROPERTY = "de.jollyday.config.providers";
	/**
	 * System property to define URLs to overriding jollyday configuration
	 * files.
	 */
	String CONFIG_URLS_PROPERTY = "de.jollyday.config.urls";

	/**
	 * @return the configuration properties for jollyday.
	 */
	Properties getProperties();

}
