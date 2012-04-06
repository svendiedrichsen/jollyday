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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.joda.time.LocalDate;

import de.jollyday.Holiday;
import de.jollyday.config.ChristianHoliday;
import de.jollyday.config.ChristianHolidayType;
import de.jollyday.config.ChronologyType;
import de.jollyday.config.HolidayType;
import de.jollyday.processor.HolidayProcessor;
import de.jollyday.util.CalendarUtil;
import de.jollyday.util.XMLUtil;

/**
 * Parses {@link ChristianHoliday} configurations.
 * 
 * @author sven
 * 
 */
public class ChristianHolidayProcessor implements HolidayProcessor {

	/**
	 * the {@link ChristianHoliday} configuration
	 */
	private final ChristianHoliday christianHoliday;
	/**
	 * {@link Map} of {@link ChristianHolidayType} to {@link Integer} as days to
	 * easter sunday
	 */
	private static Map<ChristianHolidayType, Integer> daysToEasterByChristianHolidayType = new HashMap<ChristianHolidayType, Integer>();

	static {
		daysToEasterByChristianHolidayType.put(ChristianHolidayType.EASTER, Integer.valueOf(0));
		daysToEasterByChristianHolidayType.put(ChristianHolidayType.CLEAN_MONDAY, Integer.valueOf(-48));
		daysToEasterByChristianHolidayType.put(ChristianHolidayType.SHROVE_MONDAY, Integer.valueOf(-48));
		daysToEasterByChristianHolidayType.put(ChristianHolidayType.MARDI_GRAS, Integer.valueOf(-47));
		daysToEasterByChristianHolidayType.put(ChristianHolidayType.CARNIVAL, Integer.valueOf(-47));
		daysToEasterByChristianHolidayType.put(ChristianHolidayType.ASH_WEDNESDAY, Integer.valueOf(-46));
		daysToEasterByChristianHolidayType.put(ChristianHolidayType.MAUNDY_THURSDAY, Integer.valueOf(-3));
		daysToEasterByChristianHolidayType.put(ChristianHolidayType.GOOD_FRIDAY, Integer.valueOf(-2));
		daysToEasterByChristianHolidayType.put(ChristianHolidayType.EASTER_SATURDAY, Integer.valueOf(-1));
		daysToEasterByChristianHolidayType.put(ChristianHolidayType.EASTER_MONDAY, Integer.valueOf(1));
		daysToEasterByChristianHolidayType.put(ChristianHolidayType.EASTER_TUESDAY, Integer.valueOf(2));
		daysToEasterByChristianHolidayType.put(ChristianHolidayType.GENERAL_PRAYER_DAY, Integer.valueOf(26));
		daysToEasterByChristianHolidayType.put(ChristianHolidayType.ASCENSION_DAY, Integer.valueOf(39));
		daysToEasterByChristianHolidayType.put(ChristianHolidayType.PENTECOST, Integer.valueOf(49));
		daysToEasterByChristianHolidayType.put(ChristianHolidayType.WHIT_SUNDAY, Integer.valueOf(49));
		daysToEasterByChristianHolidayType.put(ChristianHolidayType.WHIT_MONDAY, Integer.valueOf(50));
		daysToEasterByChristianHolidayType.put(ChristianHolidayType.PENTECOST_MONDAY, Integer.valueOf(50));
		daysToEasterByChristianHolidayType.put(ChristianHolidayType.CORPUS_CHRISTI, Integer.valueOf(60));
		daysToEasterByChristianHolidayType.put(ChristianHolidayType.SACRED_HEART, Integer.valueOf(68));
	}

	/**
	 * creates processor with a {@link ChristianHoliday} configuration. Checks
	 * it for not null.
	 * 
	 * @param christianHoliday
	 *            the configuration
	 */
	public ChristianHolidayProcessor(ChristianHoliday christianHoliday) {
		notNull(christianHoliday, "christianHoliday");
		this.christianHoliday = christianHoliday;
	}

	/**
	 * initializes the processor
	 */
	public void init() {
	}

	/**
	 * processes the configuration and returns the holidays.
	 */
	public Set<Holiday> process(int year, String... args) {

		if (!daysToEasterByChristianHolidayType.containsKey(christianHoliday.getType())) {
			throw new IllegalArgumentException("Unknown christian holiday type " + christianHoliday.getType());
		}

		LocalDate easterSunday = getEasterSunday(year, christianHoliday.getChronology());

		Integer daysToEaster = daysToEasterByChristianHolidayType.get(christianHoliday.getType());

		LocalDate resultingDate = easterSunday.plusDays(daysToEaster);

		String propertiesKey = "christian." + christianHoliday.getType().name();

		return new HashSet<Holiday>(Arrays.asList(createChrstianHoliday(resultingDate, propertiesKey,
				christianHoliday.getLocalizedType())));
	}

	/**
	 * returns the easter sunday for the year and {@link ChronologyType}
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
