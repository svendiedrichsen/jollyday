/**
 * Copyright 2010-2019 Sven Diedrichsen
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
package de.jollyday.parser.impl;

import de.jollyday.Holiday;
import de.jollyday.parser.AbstractHolidayParser;
import de.jollyday.spi.FixedWeekdayInMonth;
import de.jollyday.spi.Occurrance;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Set;

import static java.time.temporal.TemporalAdjusters.dayOfWeekInMonth;
import static java.time.temporal.TemporalAdjusters.lastInMonth;

/**
 * The Class FixedWeekdayInMonthParser.
 *
 * @author tboven
 * @version $Id: $
 */
public class FixedWeekdayInMonthParser extends AbstractHolidayParser {

	/*
	 * (non-Javadoc)
	 *
	 * @see de.jollyday.parser.HolidayParser#parse(int, java.util.Set,
	 * de.jollyday.config.Holidays)
	 */
	/** {@inheritDoc} */
	@Override
	public void parse(int year, Set<Holiday> holidays, FixedWeekdayInMonth fwm) {
		if (!isValid(fwm, year)) {
			continue;
		}
		LocalDate date = parse(year, fwm);
		holidays.add(new Holiday(date, fwm.descriptionPropertiesKey(), fwm.type()));
	}

	/**
	 * Parses the {@link FixedWeekdayInMonth}.
	 *
	 * @param year
	 *            the year
	 * @param fwm
	 *            the fwm
	 * @return the local date
	 */
	protected LocalDate parse(int year, FixedWeekdayInMonth fwm) {
		final DayOfWeek weekday = fwm.weekday();
		final LocalDate date = LocalDate.of(year, fwm.month(), 1);

		if (Occurrance.LAST == fwm.which()) {
			return date.with(lastInMonth(weekday));
		}

		return date.with(dayOfWeekInMonth(fwm.which().ordinal() + 1, weekday));
	}
}
