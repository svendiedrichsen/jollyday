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
package org.joda.time.chrono;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.joda.time.Chronology;
import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeZone;
import org.joda.time.Days;
import org.joda.time.Hours;
import org.joda.time.chrono.hebrew.MoladPeriod;
import org.joda.time.chrono.hebrew.Parts;

/**
 * @author Sven Diedrichsen
 * 
 */
public class HebrewChronology extends BasicChronology {

	private static final long serialVersionUID = -4095519300332478503L;

	private static DateMidnight MIN_TISHRI = new DateMidnight(108,
			Calendar.SEPTEMBER, 22, GregorianChronology.getInstanceUTC());
	private static MoladPeriod MIN_TISHRI_MOLAD = new MoladPeriod(Days.days(7),
			Hours.hours(9), Parts.parts(957));

	/** The lowest year that can be fully supported. */
	private static final int MIN_YEAR = -292269054;

	/** The highest year that can be fully supported. */
	private static final int MAX_YEAR = 292272992;

	private static final long PARTS_OF_AN_HOUR = 1080L;
	private static final long SYNODIC_MONTH_MILLIS = 29L
			* DateTimeConstants.MILLIS_PER_DAY + 12L
			* DateTimeConstants.MILLIS_PER_HOUR
			+ DateTimeConstants.MILLIS_PER_HOUR * 793L / PARTS_OF_AN_HOUR;

	private static final long LEAP_CYCLE_YEARS = 19L;
	private static final long LEAP_CYCLE_MILLIS = 12L * 12L
			* SYNODIC_MONTH_MILLIS + 7L * 13L * SYNODIC_MONTH_MILLIS;

	private static Map<DateTimeZone, HebrewChronology> cCache = new HashMap<DateTimeZone, HebrewChronology>();

	/* Caches the calculated millis for a given year */
	private final Map<Integer, Long> YEAR_MILLIS = new HashMap<Integer, Long>();
	/* Caches the calculated days for a given year */
	private final Map<Integer, Long> YEAR_DAYS = new HashMap<Integer, Long>();

	/** Singleton instance of a UTC HinduChronology */
	private static final HebrewChronology INSTANCE_UTC;

	static {
		INSTANCE_UTC = getInstance(DateTimeZone.UTC);
	}

	public static HebrewChronology getInstance() {
		return getInstance(DateTimeZone.getDefault());
	}

	public static HebrewChronology getInstanceUTC() {
		return INSTANCE_UTC;
	}

	public static HebrewChronology getInstance(DateTimeZone zone) {
		if (zone == null) {
			zone = DateTimeZone.getDefault();
		}
		if (!cCache.containsKey(zone)) {
			HebrewChronology chrono = null;
			if (zone == DateTimeZone.UTC) {
				// First create without a lower limit.
				chrono = new HebrewChronology(null, null);
				// Impose lower limit and make another HebrewChronology.
				DateTime lowerLimit = new DateTime(1, 1, 1, 0, 0, 0, 0, chrono);
				chrono = new HebrewChronology(LimitChronology.getInstance(
						chrono, lowerLimit, null), null);
			} else {
				chrono = getInstance(DateTimeZone.UTC);
				chrono = new HebrewChronology(ZonedChronology.getInstance(
						chrono, zone), null);
			}
			cCache.put(zone, chrono);
		}
		return cCache.get(zone);
	}

	/**
	 * @param base
	 * @param param
	 * @param minDaysInFirstWeek
	 */
	HebrewChronology(Chronology base, Object param) {
		super(base, param, 4);
	}

	@Override
	int getYear(long instant) {
		long hebrewMillis = instant - MIN_TISHRI.getMillis();
		long year = ((hebrewMillis / SYNODIC_MONTH_MILLIS - hebrewMillis
				/ LEAP_CYCLE_MILLIS * 7L) / 12L) + 1;
		return (int) year;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.joda.time.chrono.BasicChronology#calculateFirstDayOfYearMillis(int)
	 */
	@Override
	long calculateFirstDayOfYearMillis(int year) {
		return (getHebrewCalendarElapsedDays(year) - 1)
				* DateTimeConstants.MILLIS_PER_DAY;
	}

	private long getHebrewCalendarElapsedDays(int year) {
		if (!YEAR_DAYS.containsKey(Integer.valueOf(year))) {
			long monthsElapsed = (235L * ((year - 1) / LEAP_CYCLE_YEARS)) // Months
																			// in
																			// complete
					// cycles so far.//
					+ (12 * ((year - 1) % LEAP_CYCLE_YEARS)) // Regular months
																// in this
																// cycle.//
					+ (7 * ((year - 1) % LEAP_CYCLE_YEARS) + 1)
					/ LEAP_CYCLE_YEARS; // Leap months this cycle//
			long partsElapsed = 204L + 793L * (monthsElapsed % PARTS_OF_AN_HOUR);
			long hoursElapsed = 5L + 12L * monthsElapsed + 793L
					* (monthsElapsed / PARTS_OF_AN_HOUR) + partsElapsed
					/ PARTS_OF_AN_HOUR;
			long conjunctionDay = 1 + 29 * monthsElapsed + hoursElapsed / 24;
			long conjunctionParts = PARTS_OF_AN_HOUR * (hoursElapsed % 24)
					+ partsElapsed % PARTS_OF_AN_HOUR;
			long alternativeDay;
			if ((conjunctionParts >= 19440) // If new moon is at or after
											// midday,//
					|| (((conjunctionDay % 7) == 2) // ...or is on a
													// Tuesday...//
							&& (conjunctionParts >= 9924) // at 9 hours, 204
															// parts
					// or later...//
					&& !isLeapYear(year)) // ...of a common year,//
					|| (((conjunctionDay % 7) == 1) // ...or is on a Monday
													// at...//
							&& (conjunctionParts >= 16789) // 15 hours, 589
															// parts or
					// later...//
					&& (isLeapYear(year - 1))))// at the end of a leap year//
				// Then postpone Rosh HaShanah one day//
				alternativeDay = conjunctionDay + 1;
			else
				alternativeDay = conjunctionDay;
			if (((alternativeDay % 7) == 0)// If Rosh HaShanah would occur on
					// Sunday,//
					|| ((alternativeDay % 7) == 3) // or Wednesday,//
					|| ((alternativeDay % 7) == 5)) // or Friday//
				// Then postpone it one (more) day//
				alternativeDay = (1 + alternativeDay);

			YEAR_DAYS.put(Integer.valueOf(year), Long.valueOf(alternativeDay));
		}
		return YEAR_DAYS.get(Integer.valueOf(year));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.joda.time.chrono.BasicChronology#getApproxMillisAtEpochDividedByTwo()
	 */
	@Override
	long getApproxMillisAtEpochDividedByTwo() {
		return MIN_TISHRI.getMillis() / 2;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.joda.time.chrono.BasicChronology#getAverageMillisPerMonth()
	 */
	@Override
	long getAverageMillisPerMonth() {
		return getAverageMillisPerYear() / 12L;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.joda.time.chrono.BasicChronology#getAverageMillisPerYear()
	 */
	@Override
	long getAverageMillisPerYear() {
		return (12L * 12L * SYNODIC_MONTH_MILLIS + 7L * 13L * SYNODIC_MONTH_MILLIS)
				/ LEAP_CYCLE_YEARS;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.joda.time.chrono.BasicChronology#getAverageMillisPerYearDividedByTwo
	 * ()
	 */
	@Override
	long getAverageMillisPerYearDividedByTwo() {
		return getAverageMillisPerYear() / 2;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.joda.time.chrono.BasicChronology#getDaysInMonthMax(int)
	 */
	@Override
	int getDaysInMonthMax(int month) {
		return month % 2 == 0 ? 29 : 30;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.joda.time.chrono.BasicChronology#getDaysInYearMonth(int, int)
	 */
	@Override
	int getDaysInYearMonth(int year, int month) {
		if ((month == 2) || (month == 4) || (month == 6)
				|| ((month == 8) && !(isCheshvanLong(year)))
				|| ((month == 9) && isKislevShort(year)) || (month == 10)
				|| ((month == 12) && !(isLeapYear(year))) || (month == 13))
			return 29;
		else
			return 30;
	}

	// ND+ER //
	// True if Heshvan is long in Hebrew year. //
	boolean isCheshvanLong(int hYear) {
		if ((getDaysInHebrewYear(hYear) % 10) == 5)
			return true;
		else
			return false;
	}

	// ND+ER //
	// True if Kislev is short in Hebrew year.//
	boolean isKislevShort(int hYear) {
		if ((getDaysInHebrewYear(hYear) % 10) == 3)
			return true;
		else
			return false;
	}

	long getDaysInHebrewYear(int hYear) {
		return ((getHebrewCalendarElapsedDays(hYear + 1)) - (getHebrewCalendarElapsedDays(hYear)));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.joda.time.chrono.BasicChronology#getMaxYear()
	 */
	@Override
	int getMaxYear() {
		return MAX_YEAR;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.joda.time.chrono.BasicChronology#getMinYear()
	 */
	@Override
	int getMinYear() {
		return MIN_TISHRI.getYear();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.joda.time.chrono.BasicChronology#getMaxMonth(int)
	 */
	@Override
	int getMaxMonth(int year) {
		return isLeapYear(year) ? 13 : 12;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.joda.time.chrono.BasicChronology#getMaxMonth()
	 */
	@Override
	int getMaxMonth() {
		return 13;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.joda.time.chrono.BasicChronology#getDaysInYear(int)
	 */
	@Override
	int getDaysInYear(int year) {
		return (int) (((long) getMaxMonth(year)) * SYNODIC_MONTH_MILLIS / DateTimeConstants.MILLIS_PER_DAY);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.joda.time.chrono.BasicChronology#getDaysInMonthMax(long)
	 */
	@Override
	int getDaysInMonthMax(long instant) {
		int year = getYear(instant);
		int month = getMonthOfYear(instant, year);
		return getDaysInYearMonth(year, month);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.joda.time.chrono.BasicChronology#getDaysInYearMax()
	 */
	@Override
	int getDaysInYearMax() {
		return 385;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.joda.time.chrono.BasicChronology#getDaysInMonthMax()
	 */
	@Override
	int getDaysInMonthMax() {
		return 30;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.joda.time.chrono.BasicChronology#getMonthOfYear(long, int)
	 */
	@Override
	int getMonthOfYear(long millis, int year) {
		long millisInYear = millis - getYearMillis(year);
		int month = 0;
		do {
			month++;
			millisInYear -= ((long) getDaysInYearMonth(year, month))
					* DateTimeConstants.MILLIS_PER_DAY;
		} while (millisInYear > 0);
		if (millisInYear == 0)
			month++;
		return month;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.joda.time.chrono.BasicChronology#getYearMillis(int)
	 */
	@Override
	long getYearMillis(int year) {
		if (!YEAR_MILLIS.containsKey(Integer.valueOf(year))) {
			long millis = MIN_TISHRI.getMillis();
			for (int y = 1; y < year; y++) {
				millis += ((long) getDaysInHebrewYear(y))
						* DateTimeConstants.MILLIS_PER_DAY;
			}
			YEAR_MILLIS.put(Integer.valueOf(year), Long.valueOf(millis));
		}
		return YEAR_MILLIS.get(Integer.valueOf(year)).longValue();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.joda.time.chrono.BasicChronology#getTotalMillisByYearMonth(int,
	 * int)
	 */
	@Override
	long getTotalMillisByYearMonth(int year, int month) {
		long millis = 0;
		for (int i = 1; i < month; i++) {
			millis += ((long) getDaysInYearMonth(year, i))
					* DateTimeConstants.MILLIS_PER_DAY;
		}
		return millis;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.joda.time.chrono.BasicChronology#getYearDifference(long, long)
	 */
	@Override
	long getYearDifference(long minuendInstant, long subtrahendInstant) {
		return (minuendInstant - subtrahendInstant) / SYNODIC_MONTH_MILLIS
				/ 12L;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.joda.time.chrono.BasicChronology#isLeapYear(int)
	 */
	@Override
	boolean isLeapYear(int year) {
		int mod = year % 19;
		return mod == 0 || mod == 3 || mod == 6 || mod == 8 || mod == 11
				|| mod == 14 || mod == 17;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.joda.time.chrono.BasicChronology#setYear(long, int)
	 */
	@Override
	long setYear(long instant, int year) {
		// optimsed implementation of set, due to fixed months
		int thisYear = getYear(instant);
		int dayOfYear = getDayOfYear(instant, thisYear);
		int millisOfDay = getMillisOfDay(instant);

		instant = getYearMonthDayMillis(year, 1, dayOfYear);
		instant += millisOfDay;
		return instant;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.joda.time.chrono.BaseChronology#withUTC()
	 */
	@Override
	public Chronology withUTC() {
		return INSTANCE_UTC;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.joda.time.chrono.BaseChronology#withZone(org.joda.time.DateTimeZone)
	 */
	@Override
	public Chronology withZone(DateTimeZone zone) {
		if (zone == null) {
			zone = DateTimeZone.getDefault();
		}
		if (zone == getZone()) {
			return this;
		}
		return getInstance(zone);
	}

}
