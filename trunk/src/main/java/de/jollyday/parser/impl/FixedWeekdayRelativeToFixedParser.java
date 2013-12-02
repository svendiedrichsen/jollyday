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

/**
 * Parses fixed weekday relative to fixed date.
 * 
 * @author Sven Diedrichsen
 * @version $Id: $
 */
public class FixedWeekdayRelativeToFixedParser extends AbstractHolidayParser {

	/**
	 * Parses the provided configuration and creates holidays for the provided
	 * year.
	 */
	public void parse(int year, Set<Holiday> holidays, final Holidays config) {
		for (FixedWeekdayRelativeToFixed f : config.getFixedWeekdayRelativeToFixed()) {
			if (!isValid(f, year)) {
				continue;
			}
			LocalDate day = calendarUtil.create(year, f.getDay());
			day = moveDateToFirstOccurenceOfWeekday(f, day);
			int days = determineNumberOfDays(f);
			day = f.getWhen() == When.AFTER ? day.plusDays(days) : day.minusDays(days);
			HolidayType type = xmlUtil.getType(f.getLocalizedType());
			holidays.add(new Holiday(day, f.getDescriptionPropertiesKey(), type));
		}
	}

	/**
	 * Moves the day to the first/next occurrence of the weekday and direction specified 
	 * @param f the specification of the weekday and direction of movement
	 * @param day the day to move
	 * @return the day moved to the weekday and in the direction as specified 
	 */
	private LocalDate moveDateToFirstOccurenceOfWeekday(FixedWeekdayRelativeToFixed f, LocalDate day) {
		LocalDate movingDay = day;
		do {
			movingDay = f.getWhen() == When.AFTER ? movingDay.plusDays(1) : movingDay.minusDays(1);
		} while (movingDay.getDayOfWeek() != xmlUtil.getWeekday(f.getWeekday()));
		return movingDay;
	}

	/**
	 * Determines the number of days to move from the XML enumeration.
	 * @param f the enumeration value
	 * @return the number of days
	 */
	private int determineNumberOfDays(FixedWeekdayRelativeToFixed f) {
		switch (f.getWhich()) {
		case SECOND:
			return 7;
		case THIRD:
			return 14;
		case FOURTH:
			return 21;
		default:
			return 0;
		}
	}

}
