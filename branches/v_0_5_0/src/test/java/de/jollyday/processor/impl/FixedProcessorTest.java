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

import java.util.Set;

import org.joda.time.DateTimeConstants;
import org.joda.time.LocalDate;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import de.jollyday.Holiday;
import de.jollyday.config.Fixed;
import de.jollyday.config.Month;

/**
 * @author sven
 * 
 */
public class FixedProcessorTest {

	private Fixed fixed;
	private FixedProcessor fixedProcessor;

	@Before
	public void setup() throws Exception {
		fixed = new Fixed();
		fixedProcessor = new FixedProcessor(fixed);
	}

	@Test(expected = NullPointerException.class)
	public void testConstructorNullCheck() throws Exception {
		new FixedProcessor(null);
	}

	@Test
	public void testFixedDate() throws Exception {
		fixed.setDay(3);
		fixed.setMonth(Month.FEBRUARY);
		Set<Holiday> holidays = fixedProcessor.process(2012);
		Assert.assertFalse(holidays.isEmpty());
		Assert.assertEquals(new LocalDate(2012, DateTimeConstants.FEBRUARY, 3), holidays.iterator().next().getDate());
	}

}
