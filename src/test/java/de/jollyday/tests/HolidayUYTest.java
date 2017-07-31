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

import de.jollyday.Holiday;
import de.jollyday.HolidayCalendar;
import de.jollyday.HolidayManager;
import de.jollyday.ManagerParameters;
import de.jollyday.tests.base.AbstractCountryTestBase;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.Assert.assertTrue;

/**
 * Tests for Uruguay holidays agreed with Law N. 17.414
 * 
 * @see <a href="http://www.impo.com.uy/bases/leyes/17414-2001">Ley Nº
 *      17.414<a/>
 * @author jmoreno
 */
public class HolidayUYTest extends AbstractCountryTestBase {

	private HolidayManager holidayManager;

	@Before
	public void init() {
		holidayManager = HolidayManager.getInstance(ManagerParameters.create(HolidayCalendar.URUGUAY));
	}

	@Test
	public void testManagerUYStructure() throws Exception {
		validateCalendarData("uy", 2016);
	}

	@Test
	public void testManagerUYLanding33EasternersMovingDaysWhenLanding33EasternersOnTuesday() {
		int year = 2016;
		LocalDate landing33Easterners = LocalDate.of(year, 4, 18);
		assertTrue("There should be LANDING_33_EASTERNERS on " + landing33Easterners,
				contains(landing33Easterners, holidayManager.getHolidays(year)));
	}

	@Test
	public void testManagerUYLanding33EasternersMovingDaysWhenLanding33EasternersOnThursday() {
		int year = 2007;
		LocalDate landing33Easterners = LocalDate.of(year, 4, 23);
		assertTrue("There should be LANDING_33_EASTERNERS on " + landing33Easterners,
				contains(landing33Easterners, holidayManager.getHolidays(year)));
	}

	@Test
	public void testManagerUYRaceMovingDaysWhenRaceOnWednesday() {
		int year = 2016;
		LocalDate race = LocalDate.of(year, 10, 10);
		assertTrue("There should be RACE on " + race, contains(race, holidayManager.getHolidays(year)));
	}

	@Test
	public void testManagerUYRaceMovingDaysWhenRaceOnFriday() {
		int year = 2007;
		LocalDate race = LocalDate.of(year, 10, 15);
		assertTrue("There should be RACE on " + race, contains(race, holidayManager.getHolidays(year)));
	}

	@Test
	public void testManagerUYLasPiedrasMovingDaysWhenLasPiedrasOnTuesday() {
		int year = 2016;
		LocalDate lasPiedras = LocalDate.of(year, 5, 16);
		assertTrue("There should be LAS_PIEDRAS on " + lasPiedras,
				contains(lasPiedras, holidayManager.getHolidays(year)));
	}

	@Test
	public void testManagerUYLasPiedrasMovingDaysWhenLasPiedrasOnFriday() {
		int year = 2007;
		LocalDate lasPiedras = LocalDate.of(year, 5, 21);
		assertTrue("There should be LAS_PIEDRAS on " + lasPiedras,
				contains(lasPiedras, holidayManager.getHolidays(year)));
	}

	private boolean contains(LocalDate localDate, Set<Holiday> holidays) {
		for (Holiday h : holidays) {
			if (localDate.equals(h.getDate())) {
				return true;
			}
		}
		return false;
	}

}
