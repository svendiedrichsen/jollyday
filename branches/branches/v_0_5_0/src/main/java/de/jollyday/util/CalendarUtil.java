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
import org.joda.time.chrono.GregorianChronology;
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
	 * Creates the current date within the gregorian calendar.
	 *
	 * @return today
	 */
	public static LocalDate create() {
		return new LocalDate(GregorianChronology.getInstance());
	}

	/**
	 * Creates the given date within the julian/gregorian chronology.
	 *
	 * @param year a int.
	 * @param month a int.
	 * @param day a int.
	 * @return Gregorian/julian date.
	 */
	public static LocalDate create(int year, int month, int day) {
		Chronology c = getChronology(year);
		return create(year, month, day, c);
	}

	/**
	 * Returns the Chronology depending on the provided year. year <= 1583 ->
	 * Julian, Gregorian otherwise.
	 *
	 * @param year a int.
	 * @return Chronology
	 */
	public static Chronology getChronology(int year) {
		return (year <= 1583 ? JulianChronology.getInstance()
				: GregorianChronology.getInstance());
	}

	/**
	 * Creates the date within the provided chronology.
	 *
	 * @param year a int.
	 * @param month a int.
	 * @param day a int.
	 * @param c a {@link org.joda.time.Chronology} object.
	 * @return date
	 */
	public static LocalDate create(int year, int month, int day,
			final Chronology c) {
		return new LocalDate(year, month, day, c);
	}

	/**
	 * Creates the date from the month/day within the specified year.
	 *
	 * @param year a int.
	 * @param fixed a {@link de.jollyday.config.Fixed} object.
	 * @return A local date instance.
	 */
	public static LocalDate create(int year, Fixed fixed) {
		return create(year, XMLUtil.getMonth(fixed.getMonth()), fixed.getDay());
	}

	/**
	 * Creates a LocalDate. Does not use the Chronology of the Calendar.
	 *
	 * @param c a {@link java.util.Calendar} object.
	 * @return The local date representing the provided date.
	 */
	public static LocalDate create(final Calendar c) {
		return new LocalDate(c, getChronology(c.get(Calendar.YEAR)));
	}

	/**
	 * Returns the easter sunday for a given year.
	 *
	 * @param year a int.
	 * @return Easter sunday.
	 */
	public static LocalDate getEasterSunday(int year) {
		if (year <= 1583) {
			return getJulianEasterSunday(year);
		} else {
			return getGregorianEasterSunday(year);
		}
	}

	/**
	 * Returns the easter sunday within the julian chronology.
	 *
	 * @param year a int.
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
		return create(year, (month == 3 ? DateTimeConstants.MARCH
				: DateTimeConstants.APRIL), day, JulianChronology.getInstance());
	}

	/**
	 * Returns the easter sunday within the gregorian chronology.
	 *
	 * @param year a int.
	 * @return gregorian easter sunday.
	 */
	public static LocalDate getGregorianEasterSunday(int year) {
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
		return create(year, (month == 3 ? DateTimeConstants.MARCH
				: DateTimeConstants.APRIL), day,
				GregorianChronology.getInstance());
	}

	/**
	 * Returns if this date is on a wekkend.
	 *
	 * @param date a {@link org.joda.time.LocalDate} object.
	 * @return is weekend
	 */
	public static boolean isWeekend(final LocalDate date) {
		return date.getDayOfWeek() == DateTimeConstants.SATURDAY
				|| date.getDayOfWeek() == DateTimeConstants.SUNDAY;
	}

	/**
	 * Returns a set of gregorian dates within a gregorian year which equal the
	 * islamic month and day. Because the islamic year is about 11 days shorter
	 * than the gregorian there may be more than one occurrence of an islamic
	 * date in an gregorian year. i.e.: In the gregorian year 2008 there were
	 * two 1/1. They occurred on 1/10 and 12/29.
	 *
	 * @param gregorianYear a int.
	 * @param islamicMonth a int.
	 * @param islamicDay a int.
	 * @return List of gregorian dates for the islamic month/day.
	 */
	public static Set<LocalDate> getIslamicHolidaysInGregorianYear(
			int gregorianYear, int islamicMonth, int islamicDay) {
		return getDatesFromChronologyWithinGregorianYear(islamicMonth,
				islamicDay, gregorianYear, IslamicChronology.getInstanceUTC());
	}

	/**
	 * Returns a set of gregorian dates within a gregorian year which equal the
	 * ethiopian orthodox month and day. Because the ethiopian orthodox year
	 * different from the gregorian there may be more than one occurrence of an
	 * ethiopian orthodox date in an gregorian year.
	 *
	 * @param gregorianYear a int.
	 * @return List of gregorian dates for the ethiopian orthodox month/day.
	 * @param eoMonth a int.
	 * @param eoDay a int.
	 */
	public static Set<LocalDate> getEthiopianOrthodoxHolidaysInGregorianYear(
			int gregorianYear, int eoMonth, int eoDay) {
		return getDatesFromChronologyWithinGregorianYear(eoMonth, eoDay,
				gregorianYear, CopticChronology.getInstanceUTC());
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
	private static Set<LocalDate> getDatesFromChronologyWithinGregorianYear(
			int targetMonth, int targetDay, int gregorianYear,
			Chronology targetChronoUTC) {
		Set<LocalDate> holidays = new HashSet<LocalDate>();
		LocalDate firstGregorianDate = new LocalDate(gregorianYear,
				DateTimeConstants.JANUARY, 1,
				GregorianChronology.getInstanceUTC());
		LocalDate lastGregorianDate = new LocalDate(gregorianYear,
				DateTimeConstants.DECEMBER, 31,
				GregorianChronology.getInstanceUTC());

		LocalDate firstTargetDate = new LocalDate(firstGregorianDate
				.toDateTimeAtStartOfDay().getMillis(), targetChronoUTC);
		LocalDate lastTargetDate = new LocalDate(lastGregorianDate
				.toDateTimeAtStartOfDay().getMillis(), targetChronoUTC);

		Interval interv = new Interval(
				firstTargetDate.toDateTimeAtStartOfDay(), lastTargetDate
						.plusDays(1).toDateTimeAtStartOfDay());

		int targetYear = firstTargetDate.getYear();

		for (; targetYear <= lastTargetDate.getYear();) {
			LocalDate d = new LocalDate(targetYear, targetMonth, targetDay,
					targetChronoUTC);
			if (interv.contains(d.toDateTimeAtStartOfDay())) {
				holidays.add(convertToGregorianDate(d));
			}
			targetYear++;
		}
		return holidays;
	}

	/**
	 * Takes converts the provided date into a date within the gregorian
	 * chronology. If it is already a gregorian date it will return it.
	 *
	 * @param date a {@link org.joda.time.LocalDate} object.
	 * @return a {@link org.joda.time.LocalDate} object.
	 */
	public static LocalDate convertToGregorianDate(final LocalDate date) {
		if (!(date.getChronology() instanceof GregorianChronology)) {
			return new LocalDate(date.toDateTimeAtStartOfDay().getMillis(),
					GregorianChronology.getInstance());
		}
		return date;
	}

	/**
	 * Shows if the requested dat is contained in the Set of holidays.
	 *
	 * @param holidays a {@link java.util.Set} object.
	 * @param date a {@link org.joda.time.LocalDate} object.
	 * @return contains this date
	 */
	public static boolean contains(final Set<Holiday> holidays,
			final LocalDate date) {
		for (Holiday h : holidays) {
			if (h.getDate().equals(date)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Returns <code>true</code> if the dates weekday equals the provided
	 * weekday enum.
	 * @param date date to check
	 * @param weekday the weekday to compare
	 * @return has the same wekkday.
	 */
	public static boolean isWeekday(LocalDate date, Weekday weekday){
		return Weekday.values()[date.getDayOfWeek() - 1] == weekday;
	}

}
