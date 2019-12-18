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

import java.time.LocalDate;
import java.util.Set;

import de.jollyday.Holiday;
import de.jollyday.parser.AbstractHolidayParser;
import de.jollyday.spi.FixedWeekdayBetweenFixed;

/**
 * Parses the configuration for fixed weekdays between two fixed dates.
 *
 * @author Sven Diedrichsen
 * @version $Id: $
 */
public class FixedWeekdayBetweenFixedParser extends AbstractHolidayParser {

	/**
	 * {@inheritDoc}
	 *
	 * Parses the provided configuration and creates holidays for the provided
	 * year.
	 */
	@Override
	public void parse(int year, Set<Holiday> holidays, FixedWeekdayBetweenFixed fwm) {
		if (!isValid(fwm, year)) {
			continue;
		}
		LocalDate from = calendarUtil.create(year, fwm.from());
		LocalDate to = calendarUtil.create(year, fwm.to());
		LocalDate result = null;
		while (!from.isAfter(to)) {
			if (from.getDayOfWeek() == fwm.weekday()) {
				result = from;
				break;
			}
			from = from.plusDays(1);
		}
		if (result != null) {
			holidays.add(new Holiday(result, fwm.descriptionPropertiesKey(), fwm.type()));
		}
	}

}
