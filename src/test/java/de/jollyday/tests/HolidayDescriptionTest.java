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
package de.jollyday.tests;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author sven
 *
 */
public class HolidayDescriptionTest {

	@Test
	public void testHolidayDescriptionsCompleteness() throws Exception {

		File folder = new File("src/main/resources/descriptions");
		assertTrue(folder.isDirectory());

		final String baseName = "descriptions.holiday_descriptions";
		Set<String> props = getLocalisedResourceBundleKeys();
		ResourceBundle root = getRootResourceBundle(baseName);

		// Test that the ROOT bundle contains the superset of keys
		Set<String> missingProps = new HashSet<>();

		for (String prop: props) {
			if (!root.containsKey(prop)) {
				missingProps.add(prop);
			}
		}

		assertTrue(missingProps.isEmpty(), "Root bundle is lacking properties: " + missingProps);
	}

	protected Set<String> getLocalisedResourceBundleKeys() throws IOException {
		File folder = new File("src/main/resources/descriptions");
		assertTrue(folder.isDirectory());
		// Collect all localised descriptions
		File[] descriptions = folder.listFiles(
			(dir, name) -> name.startsWith("holiday_descriptions_") && name.endsWith(".properties"));
		assertNotNull(descriptions);
		assertTrue(descriptions.length > 0);

		Set<String> propertiesNames = new HashSet<>();

		for (File descriptionFile : descriptions) {
			Properties props = new Properties();
			props.load(new FileInputStream(descriptionFile));
			propertiesNames.addAll(props.stringPropertyNames());
		}

		return propertiesNames;
	}

	protected ResourceBundle getRootResourceBundle(String baseName) {
		return ResourceBundle.getBundle(baseName, Locale.ROOT);
	}

}
