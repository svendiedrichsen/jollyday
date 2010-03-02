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
import de.jollyday.config.ChristianHoliday;
import de.jollyday.config.ChronologyType;
import de.jollyday.config.Holidays;
import de.jollyday.parser.AbstractHolidayParser;
import de.jollyday.util.CalendarUtil;

/**
 * This parser creates christian holidays for the given year relative to easter
 * sunday.
 * 
 * @author Sven Diedrichsen
 * 
 */
public class ChristianHolidayParser extends AbstractHolidayParser {

	/**
	 * Parses all christian holidays relative to eastern.
	 */
	public void parse(int year, Set<Holiday> holidays, Holidays config) {
		for (ChristianHoliday ch : config.getChristianHoliday()) {
			if (!isValid(ch, year))
				continue;
			LocalDate easterSunday = null;
			if (ch.getChronology() == ChronologyType.JULIAN) {
				easterSunday = CalendarUtil.getJulianEasterSunday(year);
			} else if (ch.getChronology() == ChronologyType.GREGORIAN) {
				easterSunday = CalendarUtil.getGregorianEasterSunday(year);
			} else {
				easterSunday = CalendarUtil.getEasterSunday(year);
			}
			switch (ch.getType()) {
			case CLEAN_MONDAY:
			case SHROVE_MONDAY:
				easterSunday = easterSunday.minusDays(48);
				break;
			case MARDI_GRAS:
			case CARNIVAL:
				easterSunday = easterSunday.minusDays(47);
				break;
			case ASH_WEDNESDAY:
				easterSunday = easterSunday.minusDays(46);
				break;
			case MAUNDY_THURSDAY:
				easterSunday = easterSunday.minusDays(3);
				break;
			case GOOD_FRIDAY:
				easterSunday = easterSunday.minusDays(2);
				break;
			case EASTER_SATURDAY:
				easterSunday = easterSunday.minusDays(1);
				break;
			case EASTER_MONDAY:
				easterSunday = easterSunday.plusDays(1);
				break;
			case EASTER_TUESDAY:
				easterSunday = easterSunday.plusDays(2);
				break;
			case GENERAL_PRAYER_DAY:
				easterSunday = easterSunday.plusDays(26);
				break;
			case ASCENSION_DAY:
				easterSunday = easterSunday.plusDays(39);
				break;
			case PENTECOST:
				easterSunday = easterSunday.plusDays(49);
				break;
			case WHIT_MONDAY:
				easterSunday = easterSunday.plusDays(50);
				break;
			case CORPUS_CHRISTI:
				easterSunday = easterSunday.plusDays(60);
				break;
			case SACRED_HEART:
				easterSunday = easterSunday.plusDays(68);
				break;
			default:
				throw new IllegalArgumentException(
						"Unknown christian holiday type " + ch.getType());
			}
			String propertiesKey = "holiday.description.christian."
					+ ch.getType().name();
			LocalDate convertedDate = CalendarUtil
					.convertToGregorianDate(easterSunday);
			Holiday h = new Holiday(convertedDate, propertiesKey);
			holidays.add(h);
		}
	}

}
