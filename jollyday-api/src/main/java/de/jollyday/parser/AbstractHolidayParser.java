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

import de.jollyday.spi.Limited;
import de.jollyday.spi.Movable;
import de.jollyday.spi.MovingCondition;
import de.jollyday.spi.With;
import de.jollyday.util.CalendarUtil;
import de.jollyday.util.XMLUtil;
import org.threeten.extra.chrono.JulianChronology;

import java.time.LocalDate;
import java.time.chrono.Chronology;
import java.time.chrono.IsoChronology;
import java.util.Objects;

import static java.time.temporal.TemporalAdjusters.nextOrSame;
import static java.time.temporal.TemporalAdjusters.previousOrSame;

/**
 * The abstract base class for all HolidayParser implementations.
 *
 * @author Sven Diedrichsen
 */
public abstract class AbstractHolidayParser implements HolidayParser {

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
	 * @param h {@link Limited} to validate
	 * @param year The year to validate against.
	 * @return is valid for the year.
	 */
	protected boolean isValid(Limited h, int year) {
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
	private boolean isValidForCycle(Limited h, int year) {
		switch (h.cycle()) {
			case EVERY_YEAR:
				return true;
			case ODD_YEARS:
				return year % 2 != 0;
			case EVEN_YEARS:
				return year % 2 == 0;
			default:
				if (h.validFrom() != null) {
					int cycleYears;
					switch (h.cycle()) {
						case TWO_YEARS:
							cycleYears = 2;
							break;
						case THREE_YEARS:
							cycleYears = 3;
							break;
						case FOUR_YEARS:
							cycleYears = 4;
							break;
						case FIVE_YEARS:
							cycleYears = 5;
							break;
						case SIX_YEARS:
							cycleYears = 6;
							break;
						default:
							throw new IllegalArgumentException("Cannot handle unknown cycle type '" + h.cycle() + "'.");
					}
					return (year - h.validFrom().getValue()) % cycleYears == 0;
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
	private boolean isValidInYear(Limited h, int year) {
		return (h.validFrom() == null || h.validFrom().getValue() <= year)
				&& (h.validTo() == null || h.validTo().getValue() >= year);
	}

	/**
	 * Moves a date if there are any moving conditions for this holiday and any
	 * of them fit.
	 *
	 * @param fm
	 *            a {@link Movable} object.
	 * @param fixed
	 *            a {@link LocalDate} object.
	 * @return the moved date
	 */
	protected LocalDate moveDate(Movable fm, final LocalDate fixed) {
		return fm.conditions().stream()
				.filter(mc -> shallBeMoved(fixed, mc))
				.map(mc -> moveDate(mc, fixed))
				.findFirst().orElse(fixed);
	}

	/**
	 * Determines if the provided date shall be substituted.
	 *
	 * @param fixed
	 *            a {@link LocalDate} object.
	 * @param mc
	 *            a {@link MovingCondition} object.
	 * @return a boolean.
	 */
	protected boolean shallBeMoved(LocalDate fixed, MovingCondition mc) {
		return Objects.equals(fixed.getDayOfWeek(), mc.substitute());
	}

	/**
	 * Moves the date using the FixedMoving information
	 *
	 * @param mc the moving condition
	 * @param fixed the date to move
	 * @return the eventually moved date
	 */
	private LocalDate moveDate(MovingCondition mc, LocalDate fixed) {
		return fixed.with(
				mc.with() == With.NEXT
						? nextOrSame(mc.weekday())
						: previousOrSame(mc.weekday())
		);
	}

	/**
	 * <p>
	 * getEasterSunday.
	 * </p>
	 *
	 * @param year
	 *            a int.
	 * @param ct
	 *            a {@link Chronology} object.
	 * @return a {@link LocalDate} object.
	 */
	protected LocalDate getEasterSunday(int year, Chronology ct) {
		LocalDate easterSunday;
		if (ct == JulianChronology.INSTANCE) {
			easterSunday = calendarUtil.getJulianEasterSunday(year);
		} else if (ct == IsoChronology.INSTANCE) {
			easterSunday = calendarUtil.getGregorianEasterSunday(year);
		} else {
			easterSunday = calendarUtil.getEasterSunday(year);
		}
		return easterSunday;
	}

}
