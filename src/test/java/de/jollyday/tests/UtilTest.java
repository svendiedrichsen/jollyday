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
import de.jollyday.util.CalendarUtil;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import static java.time.Month.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Sven
 *
 */
public class UtilTest {

	private CalendarUtil calendarUtil = new CalendarUtil();

	@Test
	public void testWeekend() {
		LocalDate dateFriday = LocalDate.of(2010, MARCH, 12);
		LocalDate dateSaturday = LocalDate.of(2010, MARCH, 13);
		LocalDate dateSunday = LocalDate.of(2010, MARCH, 14);
		LocalDate dateMonday = LocalDate.of(2010, MARCH, 15);
		assertFalse(calendarUtil.isWeekend(dateFriday));
		assertTrue(calendarUtil.isWeekend(dateSaturday));
		assertTrue(calendarUtil.isWeekend(dateSunday));
		assertFalse(calendarUtil.isWeekend(dateMonday));
	}

	@Test
	public void testCalendarIslamicNewYear() {
		Set<LocalDate> expected = new HashSet<>();
		expected.add(LocalDate.of(2008, JANUARY, 10));
		expected.add(LocalDate.of(2008, DECEMBER, 29));
		Set<LocalDate> holidays = calendarUtil.getIslamicHolidaysInGregorianYear(2008, 1, 1);
		assertNotNull(holidays);
		assertEquals(expected.size(), holidays.size(), "Wrong number of islamic new years in 2008.");
		assertEquals(expected, holidays, "Wrong islamic New Year holidays in 2008.");
	}

	@Test
	public void testCalendarIslamicAschura2008() {
		Set<LocalDate> expected = new HashSet<>();
		expected.add(LocalDate.of(2008, JANUARY, 19));
		Set<LocalDate> holidays = calendarUtil.getIslamicHolidaysInGregorianYear(2008, 1, 10);
		assertNotNull(holidays);
		assertEquals(expected.size(), holidays.size(), "Wrong number of islamic Aschura holidays in 2008.");
		assertEquals(expected, holidays, "Wrong islamic Aschura holidays in 2008.");
	}

	@Test
	public void testCalendarIslamicAschura2009() {
		Set<LocalDate> expected = new HashSet<>();
		expected.add(LocalDate.of(2009, JANUARY, 7));
		expected.add(LocalDate.of(2009, DECEMBER, 27));
		Set<LocalDate> holidays = calendarUtil.getIslamicHolidaysInGregorianYear(2009, 1, 10);
		assertNotNull(holidays);
		assertEquals(expected.size(), holidays.size(), "Wrong number of islamic Aschura holidays in 2009.");
		assertEquals(expected, holidays, "Wrong islamic Aschura holidays in 2009.");
	}

	@Test
	public void testCalendarIslamicIdAlFitr2008() {
		Set<LocalDate> expected = new HashSet<>();
		expected.add(LocalDate.of(2008, OCTOBER, 1));
		Set<LocalDate> holidays = calendarUtil.getIslamicHolidaysInGregorianYear(2008, 10, 1);
		assertNotNull(holidays);
		assertEquals(expected.size(), holidays.size(), "Wrong number of islamic IdAlFitr holidays in 2008.");
		assertEquals(expected, holidays, "Wrong islamic IdAlFitr holidays in 2008.");
	}

	@Test
	public void testCalendarIslamicIdAlFitr2009() {
		Set<LocalDate> expected = new HashSet<>();
		expected.add(LocalDate.of(2009, SEPTEMBER, 20));
		Set<LocalDate> holidays = calendarUtil.getIslamicHolidaysInGregorianYear(2009, 10, 1);
		assertNotNull(holidays);
		assertEquals(expected.size(), holidays.size(), "Wrong number of islamic IdAlFitr holidays in 2009.");
		assertEquals(expected, holidays, "Wrong islamic IdAlFitr holidays in 2009.");
	}

	@Test
	public void testEaster2000() {
		checkEasterDate(2000, 4, 23);
	}

	@Test
	public void testEaster2001() {
		checkEasterDate(2001, 4, 15);
	}

	@Test
	public void testEaster2002() {
		checkEasterDate(2002, 3, 31);
	}

	@Test
	public void testEaster2003() {
		checkEasterDate(2003, 4, 20);
	}

	@Test
	public void testEaster2004() {
		checkEasterDate(2004, 4, 11);
	}

	@Test
	public void testEaster2005() {
		checkEasterDate(2005, 3, 27);
	}

	@Test
	public void testEaster2006() {
		checkEasterDate(2006, 4, 16);
	}

	@Test
	public void testEaster2007() {
		checkEasterDate(2007, 4, 8);
	}

	@Test
	public void testEaster2008() {
		checkEasterDate(2008, 3, 23);
	}

	@Test
	public void testEaster2009() {
		checkEasterDate(2009, 4, 12);
	}

	@Test
	public void testEaster2010() {
		checkEasterDate(2010, 4, 4);
	}

	@Test
	public void testEaster2011() {
		checkEasterDate(2011, 4, 24);
	}

	@Test
	public void testEaster2012() {
		checkEasterDate(2012, 4, 8);
	}

	@Test
	public void testEaster2013() {
		checkEasterDate(2013, 3, 31);
	}

	private void checkEasterDate(Integer year, int month, int day) {
		assertEquals(LocalDate.of(year, month, day),
				calendarUtil.getEasterSunday(year),
				"Wrong easter date.");
	}

	@Test
	public void testCalendarUtilEasterJulian() {
		assertEquals(LocalDate.of(1583, 4, 10),
				calendarUtil.getEasterSunday(1583),
				"Wrong easter date.");
	}

	@Test
	public void testCalendarUtilEasterGregorian() {
		assertEquals(LocalDate.of(1584, 4, 1),
				calendarUtil.getEasterSunday(1584),
				"Wrong easter date.");
	}

	@Test
	public void testUmlaut() {
		final LocalDate aDate = LocalDate.of(2010, JANUARY, 6);
		final HolidayManager aMgr = HolidayManager.getInstance(HolidayCalendar.AUSTRIA);
		final Set<Holiday> hs = aMgr.getHolidays(aDate, aDate.plusDays(1));
		assertNotNull(hs);
		assertEquals(1, hs.size());
		assertEquals("Heilige Drei K\u00F6nige", hs.iterator().next().getDescription(Locale.GERMANY));
	}

}
