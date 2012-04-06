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
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.joda.time.DateTimeConstants;
import org.joda.time.LocalDate;
import org.joda.time.chrono.ISOChronology;
import org.junit.Before;
import org.junit.Test;

import de.jollyday.Holiday;
import de.jollyday.config.ChristianHoliday;
import de.jollyday.config.ChristianHolidayType;
import de.jollyday.config.ChronologyType;

/**
 * @author sven
 * 
 */
public class ChristianHolidayProcessorTest {

	private ChristianHoliday christianHoliday = new ChristianHoliday();
	private ChristianHolidayProcessor christianHolidayProcessor = new ChristianHolidayProcessor(christianHoliday);
	private Map<ChristianHolidayType, Integer> daysToEasterByChristianHolidayType = new HashMap<ChristianHolidayType, Integer>();

	@Before
	public void setup() {
		daysToEasterByChristianHolidayType.put(ChristianHolidayType.EASTER, Integer.valueOf(0));
		daysToEasterByChristianHolidayType.put(ChristianHolidayType.CLEAN_MONDAY, Integer.valueOf(-48));
		daysToEasterByChristianHolidayType.put(ChristianHolidayType.SHROVE_MONDAY, Integer.valueOf(-48));
		daysToEasterByChristianHolidayType.put(ChristianHolidayType.MARDI_GRAS, Integer.valueOf(-47));
		daysToEasterByChristianHolidayType.put(ChristianHolidayType.CARNIVAL, Integer.valueOf(-47));
		daysToEasterByChristianHolidayType.put(ChristianHolidayType.ASH_WEDNESDAY, Integer.valueOf(-46));
		daysToEasterByChristianHolidayType.put(ChristianHolidayType.MAUNDY_THURSDAY, Integer.valueOf(-3));
		daysToEasterByChristianHolidayType.put(ChristianHolidayType.GOOD_FRIDAY, Integer.valueOf(-2));
		daysToEasterByChristianHolidayType.put(ChristianHolidayType.EASTER_SATURDAY, Integer.valueOf(-1));
		daysToEasterByChristianHolidayType.put(ChristianHolidayType.EASTER_MONDAY, Integer.valueOf(1));
		daysToEasterByChristianHolidayType.put(ChristianHolidayType.EASTER_TUESDAY, Integer.valueOf(2));
		daysToEasterByChristianHolidayType.put(ChristianHolidayType.GENERAL_PRAYER_DAY, Integer.valueOf(26));
		daysToEasterByChristianHolidayType.put(ChristianHolidayType.ASCENSION_DAY, Integer.valueOf(39));
		daysToEasterByChristianHolidayType.put(ChristianHolidayType.PENTECOST, Integer.valueOf(49));
		daysToEasterByChristianHolidayType.put(ChristianHolidayType.WHIT_SUNDAY, Integer.valueOf(49));
		daysToEasterByChristianHolidayType.put(ChristianHolidayType.WHIT_MONDAY, Integer.valueOf(50));
		daysToEasterByChristianHolidayType.put(ChristianHolidayType.PENTECOST_MONDAY, Integer.valueOf(50));
		daysToEasterByChristianHolidayType.put(ChristianHolidayType.CORPUS_CHRISTI, Integer.valueOf(60));
		daysToEasterByChristianHolidayType.put(ChristianHolidayType.SACRED_HEART, Integer.valueOf(68));
	}

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

		for (int year = 2000; year < 2100; year++) {
			LocalDate easterSunday = christianHolidayProcessor.getEasterSunday(year, ChronologyType.GREGORIAN);
			for (ChristianHolidayType christianHolidayType : ChristianHolidayType.values()) {

				assertTrue("Holiday type is not configured.",
						daysToEasterByChristianHolidayType.containsKey(christianHolidayType));

				christianHoliday.setType(christianHolidayType);

				Set<Holiday> process = christianHolidayProcessor.process(year);

				assertEquals("Wrong number of dates.", 1, process.size());
				Integer daysToEaster = daysToEasterByChristianHolidayType.get(christianHolidayType);

				Holiday resultDate = process.iterator().next();
				assertEquals("Wrong date for " + christianHolidayType + " " + year,
						easterSunday.plusDays(daysToEaster), resultDate.getDate());
			}
		}
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

}
