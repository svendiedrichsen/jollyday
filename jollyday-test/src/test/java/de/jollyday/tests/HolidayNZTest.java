/**
 * Copyright 2010 Sven Diedrichsen
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

import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.util.Set;

import org.junit.Test;

import de.jollyday.Holiday;
import de.jollyday.HolidayCalendar;
import de.jollyday.HolidayManager;
import de.jollyday.ManagerParameters;
import de.jollyday.tests.base.AbstractCountryTestBase;
import de.jollyday.util.CalendarUtil;

public class HolidayNZTest extends AbstractCountryTestBase {

	private static final String ISO_CODE = "nz";
	private static final int YEAR = 2018;

	private final CalendarUtil calendarUtil = new CalendarUtil();
	private final HolidayManager holidayManager = HolidayManager
			.getInstance(ManagerParameters.create(HolidayCalendar.NEW_ZEALAND));

	@Test
	public void testManagerNZStructure() throws Exception {
		validateCalendarData(ISO_CODE, YEAR);
	}

	@Test
	public void testSouthlandAnniversary2011() {
		// Monday closest to 17 January
		LocalDate expected = calendarUtil.create(2011, 1, 17);
		Set<Holiday> holidays = holidayManager.getHolidays(2011, "stl");

		boolean found = holidays.stream().anyMatch(holiday -> holiday.getPropertiesKey().equals("SOUTHLAND_ANNIVERSARY")
				&& holiday.getDate().equals(expected));
		assertTrue("Did not find expected Southland Anniversary day at " + expected + " in 2011: " + holidays + "",
				found);
	}

	@Test
	public void testSouthlandAnniversary2012() {
		// Easter Tuesday
		LocalDate expected = calendarUtil.create(2012, 4, 10);
		Set<Holiday> holidays = holidayManager.getHolidays(2012, "stl");

		boolean found = holidays.stream().anyMatch(holiday -> holiday.getPropertiesKey().equals("SOUTHLAND_ANNIVERSARY")
				&& holiday.getDate().equals(expected));
		assertTrue("Did not find expected Southland Anniversary day at " + expected + " in 2012: " + holidays + "",
				found);
	}

}
