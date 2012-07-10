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

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author sven
 * 
 */
public class HolidayDescriptionTest {

	@Test
	public void testHolidayDescriptionsCompleteness() throws Exception {

		File folder = new File("src/main/resources/descriptions");
		Assert.assertTrue(folder.isDirectory());
		File[] descriptions = folder.listFiles(new FilenameFilter() {

			public boolean accept(File dir, String name) {
				return name.startsWith("holiday_descriptions") && name.endsWith(".properties");
			}
		});
		Assert.assertNotNull(descriptions);
		Assert.assertTrue(descriptions.length > 0);

		Set<String> propertiesNames = new HashSet<String>();
		Map<String, Properties> descriptionProperties = new HashMap<String, Properties>();

		for (File descriptionFile : descriptions) {
			Properties props = new Properties();
			props.load(new FileInputStream(descriptionFile));
			propertiesNames.addAll(props.stringPropertyNames());
			descriptionProperties.put(descriptionFile.getName(), props);
		}

		Map<String, Set<String>> missingProperties = new HashMap<String, Set<String>>();

		for (Map.Entry<String, Properties> entry : descriptionProperties.entrySet()) {
			if (!entry.getValue().stringPropertyNames().containsAll(propertiesNames)) {
				Set<String> remainingProps = new HashSet<String>(propertiesNames);
				remainingProps.removeAll(entry.getValue().stringPropertyNames());
				missingProperties.put(entry.getKey(), remainingProps);
			}
		}

		Assert.assertTrue("Following files are lacking properties: " + missingProperties, missingProperties.isEmpty());

	}

}
