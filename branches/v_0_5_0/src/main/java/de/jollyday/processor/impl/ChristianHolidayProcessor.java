/**
 * Copyright 2012 Sven Diedrichsen 
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
package de.jollyday.processor.impl;

import static de.jollyday.util.Check.notNull;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.joda.time.LocalDate;

import de.jollyday.Holiday;
import de.jollyday.config.ChristianHoliday;
import de.jollyday.config.ChronologyType;
import de.jollyday.config.HolidayType;
import de.jollyday.processor.HolidayProcessor;
import de.jollyday.util.CalendarUtil;
import de.jollyday.util.XMLUtil;

/**
 * Parses christian holiday configurations.
 * 
 * @author sven
 * 
 */
public class ChristianHolidayProcessor implements HolidayProcessor {

	private final ChristianHoliday christianHoliday;

	/**
	 * @param christianHoliday
	 */
	public ChristianHolidayProcessor(ChristianHoliday christianHoliday) {
		notNull(christianHoliday, "christianHoliday");
		this.christianHoliday = christianHoliday;
	}

	public void init() {
	}

	public Set<Holiday> process(int year, String... args) {
		LocalDate easterSunday = getEasterSunday(year, christianHoliday.getChronology());
		switch (christianHoliday.getType()) {
		case EASTER:
			break;
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
		case WHIT_SUNDAY:
			easterSunday = easterSunday.plusDays(49);
			break;
		case WHIT_MONDAY:
		case PENTECOST_MONDAY:
			easterSunday = easterSunday.plusDays(50);
			break;
		case CORPUS_CHRISTI:
			easterSunday = easterSunday.plusDays(60);
			break;
		case SACRED_HEART:
			easterSunday = easterSunday.plusDays(68);
			break;
		default:
			throw new IllegalArgumentException("Unknown christian holiday type " + christianHoliday.getType());
		}
		String propertiesKey = "christian." + christianHoliday.getType().name();
		return new HashSet<Holiday>(Arrays.asList(createChrstianHoliday(easterSunday, propertiesKey,
				christianHoliday.getLocalizedType())));
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
	 * @return a {@link org.joda.time.LocalDate} object.
	 */
	protected LocalDate getEasterSunday(int year, ChronologyType ct) {
		LocalDate easterSunday;
		if (ct == ChronologyType.JULIAN) {
			easterSunday = CalendarUtil.getJulianEasterSunday(year);
		} else if (ct == ChronologyType.GREGORIAN) {
			easterSunday = CalendarUtil.getISOEasterSunday(year);
		} else {
			easterSunday = CalendarUtil.getEasterSunday(year);
		}
		return easterSunday;
	}

	/**
	 * Adds the given day to the list of holidays.
	 * 
	 * @param day
	 *            a {@link org.joda.time.LocalDate} object.
	 * @param propertiesKey
	 *            a {@link java.lang.String} object.
	 * @param holidayType
	 *            a {@link de.jollyday.config.HolidayType} object.
	 * @param holidays
	 *            a {@link java.util.Set} object.
	 */
	protected Holiday createChrstianHoliday(LocalDate day, String propertiesKey, HolidayType holidayType) {
		LocalDate convertedDate = CalendarUtil.convertToISODate(day);
		de.jollyday.HolidayType type = XMLUtil.getType(holidayType);
		Holiday h = new Holiday(convertedDate, propertiesKey, type);
		return h;
	}

}
