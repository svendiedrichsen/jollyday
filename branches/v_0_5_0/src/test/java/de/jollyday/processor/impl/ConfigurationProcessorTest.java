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
package de.jollyday.processor.impl;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertSame;
import static junit.framework.Assert.assertTrue;

import java.util.Set;

import org.junit.Test;

import de.jollyday.Holiday;
import de.jollyday.config.Configuration;
import de.jollyday.config.Fixed;
import de.jollyday.config.HolidayRule;
import de.jollyday.config.Month;

/**
 * @author sven
 * 
 */
public class ConfigurationProcessorTest {

	private Configuration configuration = new Configuration();
	private ConfigurationProcessor configurationProcessor;

	@Test(expected = NullPointerException.class)
	public void testConstructorNullCheck() throws Exception {
		configurationProcessor = new ConfigurationProcessor(null);
	}

	@Test
	public void testGetConfigurationIsSame() throws Exception {
		configurationProcessor = new ConfigurationProcessor(configuration);
		assertSame(configuration, configurationProcessor.getConfiguration());
	}

	@Test
	public void testInit() throws Exception {
		configurationProcessor = new ConfigurationProcessor(configuration);
		addConfiguration();
		configurationProcessor.init();
		assertEquals("Wrong number of rule processors.", 1, configurationProcessor.getRuleProcessors().size());
		assertTrue("Subconfiguration missing.",
				configurationProcessor.getSubConfigurationProcessors().containsKey("TEST"));
	}

	private void addConfiguration() {
		HolidayRule holidayRule = new HolidayRule();
		Fixed fixedHoliday = new Fixed();
		fixedHoliday.setDay(1);
		fixedHoliday.setMonth(Month.JANUARY);
		holidayRule.setFixed(fixedHoliday);
		configuration.getHoliday().add(holidayRule);
		Configuration subConfiguration = new Configuration();
		subConfiguration.setHierarchy("TEST");
		configuration.getSubConfigurations().add(subConfiguration);
	}

	@Test
	public void testProcess() throws Exception {
		configurationProcessor = new ConfigurationProcessor(configuration);
		addConfiguration();
		configurationProcessor.init();
		Set<Holiday> holidays = configurationProcessor.process(2001, "TEST");
		assertEquals("Unexpected holidays found.", 1, holidays.size());
	}

}
