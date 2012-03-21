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
package de.jollyday.util;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import junit.framework.Assert;

import org.joda.time.DateTimeConstants;
import org.joda.time.Interval;
import org.joda.time.LocalDate;
import org.joda.time.chrono.GregorianChronology;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.chrono.JulianChronology;
import org.junit.Test;

import de.jollyday.Holiday;
import de.jollyday.HolidayCalendar;
import de.jollyday.HolidayManager;
import de.jollyday.util.CalendarUtil;

/**
 * @author Sven
 * 
 */
public class CalendarUtilTest {

	@Test
	public void testWeekend() {
		LocalDate dateFriday = CalendarUtil.create(2010,
				DateTimeConstants.MARCH, 12);
		LocalDate dateSaturday = CalendarUtil.create(2010,
				DateTimeConstants.MARCH, 13);
		LocalDate dateSunday = CalendarUtil.create(2010,
				DateTimeConstants.MARCH, 14);
		LocalDate dateMonday = CalendarUtil.create(2010,
				DateTimeConstants.MARCH, 15);
		Assert.assertFalse(CalendarUtil.isWeekend(dateFriday));
		Assert.assertTrue(CalendarUtil.isWeekend(dateSaturday));
		Assert.assertTrue(CalendarUtil.isWeekend(dateSunday));
		Assert.assertFalse(CalendarUtil.isWeekend(dateMonday));
	}

	@Test
	public void testCalendarIslamicNewYear() {
		Set<LocalDate> expected = new HashSet<LocalDate>();
		expected.add(CalendarUtil.create(2008, DateTimeConstants.JANUARY, 10));
		expected.add(CalendarUtil.create(2008, DateTimeConstants.DECEMBER, 29));
		Set<LocalDate> holidays = CalendarUtil
				.getIslamicHolidaysInGregorianYear(2008, 1, 1);
		Assert.assertNotNull(holidays);
		Assert.assertEquals("Wrong number of islamic new years in 2008.",
				expected.size(), holidays.size());
		Assert.assertEquals("Wrong islamic New Year holidays in 2008.",
				expected, holidays);
	}

	@Test
	public void testCalendarIslamicAschura2008() {
		Set<LocalDate> expected = new HashSet<LocalDate>();
		expected.add(CalendarUtil.create(2008, DateTimeConstants.JANUARY, 19));
		Set<LocalDate> holidays = CalendarUtil
				.getIslamicHolidaysInGregorianYear(2008, 1, 10);
		Assert.assertNotNull(holidays);
		Assert.assertEquals(
				"Wrong number of islamic Aschura holidays in 2008.",
				expected.size(), holidays.size());
		Assert.assertEquals("Wrong islamic Aschura holidays in 2008.",
				expected, holidays);
	}

	@Test
	public void testCalendarIslamicAschura2009() {
		Set<LocalDate> expected = new HashSet<LocalDate>();
		expected.add(CalendarUtil.create(2009, DateTimeConstants.JANUARY, 7));
		expected.add(CalendarUtil.create(2009, DateTimeConstants.DECEMBER, 27));
		Set<LocalDate> holidays = CalendarUtil
				.getIslamicHolidaysInGregorianYear(2009, 1, 10);
		Assert.assertNotNull(holidays);
		Assert.assertEquals(
				"Wrong number of islamic Aschura holidays in 2009.",
				expected.size(), holidays.size());
		Assert.assertEquals("Wrong islamic Aschura holidays in 2009.",
				expected, holidays);
	}

	@Test
	public void testCalendarIslamicIdAlFitr2008() {
		Set<LocalDate> expected = new HashSet<LocalDate>();
		expected.add(CalendarUtil.create(2008, DateTimeConstants.OCTOBER, 2));
		Set<LocalDate> holidays = CalendarUtil
				.getIslamicHolidaysInGregorianYear(2008, 10, 1);
		Assert.assertNotNull(holidays);
		Assert.assertEquals(
				"Wrong number of islamic IdAlFitr holidays in 2008.",
				expected.size(), holidays.size());
		Assert.assertEquals("Wrong islamic IdAlFitr holidays in 2008.",
				expected, holidays);
	}

	@Test
	public void testCalendarIslamicIdAlFitr2009() {
		Set<LocalDate> expected = new HashSet<LocalDate>();
		expected.add(CalendarUtil.create(2009, DateTimeConstants.SEPTEMBER, 21));
		Set<LocalDate> holidays = CalendarUtil
				.getIslamicHolidaysInGregorianYear(2009, 10, 1);
		Assert.assertNotNull(holidays);
		Assert.assertEquals(
				"Wrong number of islamic IdAlFitr holidays in 2009.",
				expected.size(), holidays.size());
		Assert.assertEquals("Wrong islamic IdAlFitr holidays in 2009.",
				expected, holidays);
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

	private static void checkEasterDate(Integer year, int month, int day) {
		Assert.assertEquals("Wrong easter date.",
				CalendarUtil.create(year, month, day),
				CalendarUtil.getEasterSunday(year));
	}

	@Test
	public void testCalendarUtilChronology() {
		for (int i = 0; i <= 1583; i++) {
			Assert.assertEquals("Wrong chronology.",
					JulianChronology.getInstance(),
					CalendarUtil.getChronology(i));
		}
		for (int i = 1584; i <= 2500; i++) {
			Assert.assertEquals("Wrong chronology.",
					ISOChronology.getInstance(),
					CalendarUtil.getChronology(i));
		}
	}

	@Test
	public void testCalendarUtilEasterJulian() {
		Assert.assertEquals("Wrong easter date.", CalendarUtil.create(1583, 3,
				31, JulianChronology.getInstance()), CalendarUtil
				.getEasterSunday(1583));
	}

	@Test
	public void testCalendarUtilEasterGregorian() {
		Assert.assertEquals(
				"Wrong easter date.",
				CalendarUtil.create(1584, 4, 1,
						ISOChronology.getInstance()),
				CalendarUtil.getEasterSunday(1584));
	}

	@Test
	public void testCalendarUtilToday() {
		LocalDate today = new LocalDate(Calendar.getInstance(),
				GregorianChronology.getInstance());
		Assert.assertEquals("Wrong date.", today, CalendarUtil.create());
	}

	@Test
	public void testUmlaut() {
		final LocalDate aDate = CalendarUtil.create(2010,
				DateTimeConstants.JANUARY, 6);
		final HolidayManager aMgr = HolidayManager.getInstance(HolidayCalendar.AUSTRIA);
		final Set<Holiday> hs = aMgr.getHolidays(new Interval(aDate
				.toDateTimeAtStartOfDay(), aDate.toDateTimeAtStartOfDay()
				.plusDays(1)));
		Assert.assertNotNull(hs);
		Assert.assertEquals(1, hs.size());
		Assert.assertEquals("Heilige Drei K\u00F6nige", hs.iterator().next()
				.getDescription(Locale.GERMANY));
	}

}
