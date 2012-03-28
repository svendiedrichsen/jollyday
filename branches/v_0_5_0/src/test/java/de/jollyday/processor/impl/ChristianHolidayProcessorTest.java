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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.joda.time.DateTimeConstants;
import org.joda.time.LocalDate;
import org.joda.time.chrono.ISOChronology;
import org.junit.Test;

import de.jollyday.config.ChristianHoliday;
import de.jollyday.config.ChronologyType;

/**
 * @author sven
 * 
 */
public class ChristianHolidayProcessorTest {

	private ChristianHoliday christianHoliday = new ChristianHoliday();
	private ChristianHolidayProcessor christianHolidayProcessor = new ChristianHolidayProcessor(christianHoliday);

	/**
	 * Test method for
	 * {@link de.jollyday.processor.impl.ChristianHolidayProcessor#ChristianHolidayProcessor(de.jollyday.config.ChristianHoliday)}
	 * .
	 */
	@Test(expected = NullPointerException.class)
	public void testChristianHolidayProcessor() {
		new ChristianHolidayProcessor(null);
	}

	/**
	 * Test method for
	 * {@link de.jollyday.processor.impl.ChristianHolidayProcessor#init()}.
	 */
	@Test
	public void testInit() {
		christianHolidayProcessor.init();
	}

	/**
	 * Test method for
	 * {@link de.jollyday.processor.impl.ChristianHolidayProcessor#process(int, java.lang.String[])}
	 * .
	 */
	@Test
	public void testProcess() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link de.jollyday.processor.impl.ChristianHolidayProcessor#getEasterSunday(int, de.jollyday.config.ChronologyType)}
	 * .
	 */
	@Test
	public void testGetEasterSunday() {
		LocalDate easterSunday2001 = christianHolidayProcessor.getEasterSunday(2001, ChronologyType.GREGORIAN);
		assertEquals("Wrong easter sunday.",
				new LocalDate(2001, DateTimeConstants.APRIL, 15, ISOChronology.getInstanceUTC()), easterSunday2001);
	}

	/**
	 * Test method for
	 * {@link de.jollyday.processor.impl.ChristianHolidayProcessor#createChrstianHoliday(org.joda.time.LocalDate, java.lang.String, de.jollyday.config.HolidayType)}
	 * .
	 */
	@Test
	public void testCreateChrstianHoliday() {
		fail("Not yet implemented");
	}

}
