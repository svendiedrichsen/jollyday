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
import java.util.Set;

import org.joda.time.Chronology;
import org.joda.time.DateTimeConstants;
import org.joda.time.Interval;
import org.joda.time.LocalDate;
import org.joda.time.chrono.CopticChronology;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.chrono.IslamicChronology;
import org.joda.time.chrono.JulianChronology;

import de.jollyday.Holiday;
import de.jollyday.config.Fixed;
import de.jollyday.config.Weekday;

/**
 * Utility class for date operations.
 * 
 * @author Sven Diedrichsen
 * @version $Id: $
 */
public abstract class CalendarUtil {

	/**
	 * Returns the {@link ISOChronology} with UTC timezone.
	 * 
	 * @return {@link ISOChronology} with UTC timezone
	 */
	public static ISOChronology getISOChronology() {
		return ISOChronology.getInstanceUTC();
	}

	/**
	 * Creates the current date as {@link LocalDate} within the ISO calendar
	 * UTC.
	 * 
	 * @return today
	 */
	public static LocalDate create() {
		return new LocalDate(getISOChronology());
	}

	/**
	 * Creates the given date within the julian/gregorian chronology.
	 * 
	 * @param year
	 *            a int.
	 * @param month
	 *            a int.
	 * @param day
	 *            a int.
	 * @return Gregorian/julian date.
	 */
	public static LocalDate create(int year, int month, int day) {
		Chronology c = getChronology(year);
		return create(year, month, day, c);
	}

	/**
	 * Returns the Chronology depending on the provided year. year <= 1583 ->
	 * {@link JulianChronology}, {@link ISOChronology} otherwise.
	 * 
	 * @param year
	 *            a int.
	 * @return Chronology
	 */
	public static Chronology getChronology(int year) {
		return (year <= 1583 ? JulianChronology.getInstance() : getISOChronology());
	}

	/**
	 * Creates the date within the provided chronology.
	 * 
	 * @param year
	 *            a int.
	 * @param month
	 *            a int.
	 * @param day
	 *            a int.
	 * @param c
	 *            a {@link org.joda.time.Chronology} object.
	 * @return date
	 */
	public static LocalDate create(int year, int month, int day, final Chronology c) {
		return new LocalDate(year, month, day, c);
	}

	/**
	 * Creates the date from the month/day within the specified year.
	 * 
	 * @param year
	 *            a int.
	 * @param fixed
	 *            a {@link de.jollyday.config.Fixed} object.
	 * @return A local date instance.
	 */
	public static LocalDate create(int year, Fixed fixed) {
		return create(year, XMLUtil.getMonth(fixed.getMonth()), fixed.getDay());
	}

	/**
	 * Creates a LocalDate. Does not use the Chronology of the Calendar.
	 * 
	 * @param c
	 *            a {@link java.util.Calendar} object.
	 * @return The local date representing the provided date.
	 */
	public static LocalDate create(final Calendar c) {
		return new LocalDate(c, getChronology(c.get(Calendar.YEAR)));
	}

	/**
	 * Returns the easter sunday for a given year.
	 * 
	 * @param year
	 *            a int.
	 * @return Easter sunday.
	 */
	public static LocalDate getEasterSunday(int year) {
		if (year <= 1583) {
			return getJulianEasterSunday(year);
		} else {
			return getISOEasterSunday(year);
		}
	}

	/**
	 * Returns the easter sunday within the julian chronology.
	 * 
	 * @param year
	 *            a int.
	 * @return julian easter sunday
	 */
	public static LocalDate getJulianEasterSunday(int year) {
		int a, b, c, d, e;
		int x, month, day;
		a = year % 4;
		b = year % 7;
		c = year % 19;
		d = (19 * c + 15) % 30;
		e = (2 * a + 4 * b - d + 34) % 7;
		x = d + e + 114;
		month = x / 31;
		day = (x % 31) + 1;
		int datesMonth = month == 3 ? DateTimeConstants.MARCH : DateTimeConstants.APRIL;
		return create(year, datesMonth, day, getISOChronology());
	}

	/**
	 * Returns the easter sunday within the ISO chronology.
	 * 
	 * @param year
	 *            a int.
	 * @return ISO easter sunday.
	 */
	public static LocalDate getISOEasterSunday(int year) {
		int a, b, c, d, e, f, g, h, i, j, k, l;
		int x, month, day;
		a = year % 19;
		b = year / 100;
		c = year % 100;
		d = b / 4;
		e = b % 4;
		f = (b + 8) / 25;
		g = (b - f + 1) / 3;
		h = (19 * a + b - d - g + 15) % 30;
		i = c / 4;
		j = c % 4;
		k = (32 + 2 * e + 2 * i - h - j) % 7;
		l = (a + 11 * h + 22 * k) / 451;
		x = h + k - 7 * l + 114;
		month = x / 31;
		day = (x % 31) + 1;
		int datesMonth = month == 3 ? DateTimeConstants.MARCH : DateTimeConstants.APRIL;
		return create(year, datesMonth, day, getISOChronology());
	}

	/**
	 * Returns if this date is on a wekkend.
	 * 
	 * @param date
	 *            a {@link org.joda.time.LocalDate} object.
	 * @return is weekend
	 */
	public static boolean isWeekend(final LocalDate date) {
		return date.getDayOfWeek() == DateTimeConstants.SATURDAY || date.getDayOfWeek() == DateTimeConstants.SUNDAY;
	}

	/**
	 * Returns a set of gregorian dates within a gregorian year which equal the
	 * islamic month and day. Because the islamic year is about 11 days shorter
	 * than the gregorian there may be more than one occurrence of an islamic
	 * date in an gregorian year. i.e.: In the gregorian year 2008 there were
	 * two 1/1. They occurred on 1/10 and 12/29.
	 * 
	 * @param gregorianYear
	 *            a int.
	 * @param islamicMonth
	 *            a int.
	 * @param islamicDay
	 *            a int.
	 * @return List of gregorian dates for the islamic month/day.
	 */
	public static Set<LocalDate> getIslamicHolidaysInGregorianYear(int gregorianYear, int islamicMonth, int islamicDay) {
		return getDatesFromChronologyWithinGregorianYear(islamicMonth, islamicDay, gregorianYear,
				IslamicChronology.getInstanceUTC());
	}

	/**
	 * Returns a set of gregorian dates within a gregorian year which equal the
	 * ethiopian orthodox month and day. Because the ethiopian orthodox year
	 * differes from the gregorian there may be more than one occurrence of an
	 * ethiopian orthodox date in an gregorian year.
	 * 
	 * @param gregorianYear
	 *            a int.
	 * @return List of gregorian dates for the ethiopian orthodox month/day.
	 * @param eoMonth
	 *            a int.
	 * @param eoDay
	 *            a int.
	 */
	public static Set<LocalDate> getEthiopianOrthodoxHolidaysInGregorianYear(int gregorianYear, int eoMonth, int eoDay) {
		return getDatesFromChronologyWithinGregorianYear(eoMonth, eoDay, gregorianYear,
				CopticChronology.getInstanceUTC());
	}

	/**
	 * Searches for the occurrences of a month/day in one chronology within one
	 * gregorian year.
	 * 
	 * @param targetMonth
	 * @param targetDay
	 * @param gregorianYear
	 * @param targetChronoUTC
	 * @return the list of gregorian dates.
	 */
	private static Set<LocalDate> getDatesFromChronologyWithinGregorianYear(int targetMonth, int targetDay,
			int gregorianYear, Chronology targetChronoUTC) {
		Set<LocalDate> holidays = new HashSet<LocalDate>();
		LocalDate firstGregorianDate = create(gregorianYear, DateTimeConstants.JANUARY, 1, getISOChronology());
		LocalDate lastGregorianDate = create(gregorianYear, DateTimeConstants.DECEMBER, 31, getISOChronology());

		LocalDate firstTargetDate = new LocalDate(firstGregorianDate.toDateTimeAtStartOfDay().getMillis(),
				targetChronoUTC);
		LocalDate lastTargetDate = new LocalDate(lastGregorianDate.toDateTimeAtStartOfDay().getMillis(),
				targetChronoUTC);

		Interval interv = new Interval(firstTargetDate.toDateTimeAtStartOfDay(), lastTargetDate.plusDays(1)
				.toDateTimeAtStartOfDay());

		for (int targetYear = firstTargetDate.getYear(); targetYear <= lastTargetDate.getYear(); targetYear++) {
			LocalDate d = create(targetYear, targetMonth, targetDay, targetChronoUTC);
			if (interv.contains(d.toDateTimeAtStartOfDay())) {
				holidays.add(convertToISODate(d));
			}
		}
		return holidays;
	}

	/**
	 * Takes converts the provided date into a date within the
	 * {@link ISOChronology}. If it is already a iso date it will return it.
	 * 
	 * @param date
	 *            a {@link org.joda.time.LocalDate} object.
	 * @return a {@link org.joda.time.LocalDate} object.
	 */
	public static LocalDate convertToISODate(final LocalDate date) {
		if (!(date.getChronology() instanceof ISOChronology)) {
			return new LocalDate(date.toDateTimeAtStartOfDay().getMillis(), getISOChronology());
		}
		return date;
	}

	/**
	 * Shows if the requested date is contained in the Set of holidays by using
	 * {@link LocalDate.isEqual}
	 * 
	 * @param holidays
	 *            a {@link java.util.Set} object.
	 * @param date
	 *            a {@link org.joda.time.LocalDate} object.
	 * @return contains this date
	 */
	public static boolean contains(final Set<Holiday> holidays, final LocalDate date) {
		Check.notNull(holidays, "Holidays");
		for (Holiday h : holidays) {
			if (h.getDate().isEqual(date)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns <code>true</code> if the dates weekday equals the provided
	 * weekday enum.
	 * 
	 * @param date
	 *            date to check
	 * @param weekday
	 *            the weekday to compare
	 * @return has the same wekkday.
	 */
	public static boolean isWeekday(LocalDate date, Weekday weekday) {
		return Weekday.values()[date.getDayOfWeek() - 1] == weekday;
	}

}