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
package de.jollyday.schema;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import de.jollyday.config.Configuration;
import de.jollyday.util.XMLUtil;

/**
 * @author sven
 * 
 */
public class ValidateSchemaTest {

	private String path = "src/main/resources/holidays";
	private String[] files;

	@Before
	public void setup() {
		File directory = new File(path);
		File absDirectory = new File(directory.getAbsolutePath());
		files = absDirectory.list(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.startsWith("Holidays") && name.endsWith(".xml");
			}
		});
	}

	@Test
	public void testSchemaConformance() {
		Assert.assertNotNull("Holiday files not found.", files);
		Assert.assertTrue(files.length > 0);
		for (String fileName : files) {
			FileInputStream fs = null;
			try {
				fs = new FileInputStream(path + "/" + fileName);
				Configuration unmarshallConfiguration = XMLUtil.unmarshallConfiguration(fs);
				Assert.assertNotNull(unmarshallConfiguration);
			} catch (Exception e) {
				Assert.fail("Error parsing file " + fileName + ": " + e.getMessage());
			} finally {
				if (fs != null) {
					try {
						fs.close();
					} catch (IOException e) {
						e.printStackTrace(System.err);
					}
				}
			}
		}
	}
}
