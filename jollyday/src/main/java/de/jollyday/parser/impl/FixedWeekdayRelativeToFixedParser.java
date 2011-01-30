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
package de.jollyday.parser.impl;

import java.util.Set;

import org.joda.time.LocalDate;

import de.jollyday.Holiday;
import de.jollyday.HolidayType;
import de.jollyday.config.FixedWeekdayRelativeToFixed;
import de.jollyday.config.Holidays;
import de.jollyday.config.When;
import de.jollyday.parser.AbstractHolidayParser;
import de.jollyday.util.CalendarUtil;
import de.jollyday.util.XMLUtil;

/**
 * Parses fixed weekday relative to fixed date.
 * 
 * @author Sven Diedrichsen
 * 
 */
public class FixedWeekdayRelativeToFixedParser extends AbstractHolidayParser {

	/**
	 * Parses the provided configuration and creates holidays for the provided
	 * year.
	 */
	public void parse(int year, Set<Holiday> holidays, Holidays config) {
		for (FixedWeekdayRelativeToFixed f : config
				.getFixedWeekdayRelativeToFixed()) {
			if (!isValid(f, year)) {
				continue;
			}
			// parsing fixed day
			LocalDate day = CalendarUtil.create(year, f.getDay());
			do {
				// move fixed to first occurrence of weekday
				day = f.getWhen() == When.AFTER ? day.plusDays(1) : day
						.minusDays(1);
			} while (day.getDayOfWeek() != XMLUtil.getWeekday(f.getWeekday()));
			int days = 0;
			switch (f.getWhich()) {
			case SECOND:
				days = 7;
				break;
			case THIRD:
				days = 14;
				break;
			case FOURTH:
				days = 21;
				break;
			}
			// move day further if it is second, third or fourth weekday
			day = f.getWhen() == When.AFTER ? day.plusDays(days) : day
					.minusDays(days);
			HolidayType type = XMLUtil.getType(f.getLocalizedType());
			holidays.add(new Holiday(day, f.getDescriptionPropertiesKey(), type));
		}
	}

}
