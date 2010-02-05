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

import de.jollyday.config.ChristianHoliday;
import de.jollyday.config.ChronologyType;
import de.jollyday.config.Holidays;
import de.jollyday.parser.HolidayParser;
import de.jollyday.util.CalendarUtil;

/**
 * This parser creates christian holidays for the given year relative to easter
 * sunday.
 * 
 * @author Sven Diedrichsen
 * 
 */
public class ChristianHolidayParser implements HolidayParser {

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.jollyday.parser.HolidayParser#parse(int, java.util.Set,
	 * de.jollyday.config.Holidays)
	 */
	public void parse(int year, Set<LocalDate> holidays, Holidays config) {
		for (ChristianHoliday ch : config.getChristianHoliday()) {
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
			case EASTER_MONDAY:
				easterSunday = easterSunday.plusDays(1);
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
			default:
				throw new IllegalArgumentException("Unknown christian holiday type " + ch.getType());
			}
			holidays.add(CalendarUtil.convertToGregorianDate(easterSunday));
		}
	}

}
