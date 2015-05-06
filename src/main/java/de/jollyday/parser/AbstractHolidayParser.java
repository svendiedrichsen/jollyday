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
package de.jollyday.parser;

import static java.time.temporal.TemporalAdjusters.nextOrSame;
import static java.time.temporal.TemporalAdjusters.previousOrSame;

import de.jollyday.config.*;
import de.jollyday.util.CalendarUtil;
import de.jollyday.util.XMLUtil;

import java.time.DayOfWeek;
import java.time.LocalDate;

/**
 * The abstract base class for all HolidayParser implementations.
 *
 * @author Sven Diedrichsen
 */
public abstract class AbstractHolidayParser implements HolidayParser {

	private static final String EVERY_YEAR = "EVERY_YEAR";
	private static final String ODD_YEARS = "ODD_YEARS";
	private static final String EVEN_YEARS = "EVEN_YEARS";
	private static final String TWO_YEARS = "2_YEARS";
	private static final String THREE_YEARS = "3_YEARS";
	private static final String FOUR_YEARS = "4_YEARS";
	private static final String FIVE_YEARS = "5_YEARS";
	private static final String SIX_YEARS = "6_YEARS";

	/**
	 * Calendar utility class.
	 */
	protected CalendarUtil calendarUtil = new CalendarUtil();
	/**
	 * XML utility class.
	 */
	protected XMLUtil xmlUtil = new XMLUtil();

	/**
	 * Evaluates if the provided <code>Holiday</code> instance is valid for the
	 * provided year.
	 *
	 * @param <T> a {@link Holiday} subclass
	 * @param h
	 *            The holiday configuration entry to validate
	 * @param year
	 *            The year to validate against.
	 * @return is valid for the year.
	 */
	protected <T extends Holiday> boolean isValid(T h, int year) {
		return isValidInYear(h, year) && isValidForCycle(h, year);
	}

	/**
	 * Checks cyclic holidays and checks if the requested year is hit within the
	 * cycles.
	 *
	 * @param h Holiday to be valid in cycle
	 * @param year the year for the holiday to be valid in
	 * @return is valid
	 */
	private <T extends Holiday> boolean isValidForCycle(T h, int year) {
		if (h.getEvery() != null) {
			if (!EVERY_YEAR.equals(h.getEvery())) {
				if (ODD_YEARS.equals(h.getEvery())) {
					return year % 2 != 0;
				} else if (EVEN_YEARS.equals(h.getEvery())) {
					return year % 2 == 0;
				} else {
					if (h.getValidFrom() != null) {
						int cycleYears;
						if (TWO_YEARS.equalsIgnoreCase(h.getEvery())) {
							cycleYears = 2;
						} else if (THREE_YEARS.equalsIgnoreCase(h.getEvery())) {
							cycleYears = 3;
						} else if (FOUR_YEARS.equalsIgnoreCase(h.getEvery())) {
							cycleYears = 4;
						} else if (FIVE_YEARS.equalsIgnoreCase(h.getEvery())) {
							cycleYears = 5;
						} else if (SIX_YEARS.equalsIgnoreCase(h.getEvery())) {
							cycleYears = 6;
						} else {
							throw new IllegalArgumentException("Cannot handle unknown cycle type '" + h.getEvery()
									+ "'.");
						}
						return (year - h.getValidFrom()) % cycleYears == 0;
					}
				}
			}
		}
		return true;
	}

	/**
	 * Checks whether the holiday is within the valid date range.
	 *
	 * @param h the holiday to check for validity
	 * @param year the year to check the holiday to be valid in
	 * @return the holiday is valid
	 */
	private <T extends Holiday> boolean isValidInYear(T h, int year) {
		return (h.getValidFrom() == null || h.getValidFrom() <= year)
				&& (h.getValidTo() == null || h.getValidTo() >= year);
	}

	/**
	 * Moves a date if there are any moving conditions for this holiday and any
	 * of them fit.
	 *
	 * @param fm
	 *            a {@link de.jollyday.config.MoveableHoliday} object.
	 * @param fixed
	 *            a {@link LocalDate} object.
	 * @return the moved date
	 */
	protected LocalDate moveDate(MoveableHoliday fm, LocalDate fixed) {
		for (MovingCondition mc : fm.getMovingCondition()) {
			if (shallBeMoved(fixed, mc)) {
				fixed = moveDate(mc, fixed);
				break;
			}
		}
		return fixed;
	}

	/**
	 * Determines if the provided date shall be substituted.
	 *
	 * @param fixed
	 *            a {@link LocalDate} object.
	 * @param mc
	 *            a {@link de.jollyday.config.MovingCondition} object.
	 * @return a boolean.
	 */
	protected boolean shallBeMoved(LocalDate fixed, MovingCondition mc) {
		return fixed.getDayOfWeek() == xmlUtil.getWeekday(mc.getSubstitute());
	}

	/**
	 * Moves the date using the FixedMoving information
	 *
	 * @param mc the moving condition
	 * @param fixed the date to move
	 * @return the eventually moved date
	 */
	private LocalDate moveDate(MovingCondition mc, LocalDate fixed) {
		DayOfWeek weekday = xmlUtil.getWeekday(mc.getWeekday());
		
		return fixed.with(mc.getWith() == With.NEXT ? nextOrSame(weekday) : 
				previousOrSame(weekday));
	}

	/**
	 * <p>
	 * getEasterSunday.
	 * </p>
	 *
	 * @param year
	 *            a int.
	 * @param ct
	 *            a {@link de.jollyday.config.ChronologyType} object.
	 * @return a {@link LocalDate} object.
	 */
	protected LocalDate getEasterSunday(int year, ChronologyType ct) {
		LocalDate easterSunday;
		if (ct == ChronologyType.JULIAN) {
			easterSunday = calendarUtil.getJulianEasterSunday(year);
		} else if (ct == ChronologyType.GREGORIAN) {
			easterSunday = calendarUtil.getGregorianEasterSunday(year);
		} else {
			easterSunday = calendarUtil.getEasterSunday(year);
		}
		return easterSunday;
	}

}
