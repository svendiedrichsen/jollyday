/*
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

import de.jollyday.Holiday;
import de.jollyday.HolidayType;
import de.jollyday.config.ChineseHoliday;
import de.jollyday.config.Holidays;
import de.jollyday.parser.AbstractHolidayParser;
import net.time4j.PlainDate;
import net.time4j.calendar.ChineseCalendar;
import net.time4j.calendar.EastAsianMonth;
import net.time4j.calendar.EastAsianYear;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Set;

/**
 * This parser calculates gregorian dates for the different Chinese holidays.
 *
 * @author Meno Hochschild
 * @version $Id: $
 */
public class ChineseHolidayParser extends AbstractHolidayParser {

	/**
	 * Properties prefix for Chinese holidays.
	 */
	private static final String PREFIX_PROPERTY_CHINESE = "chinese.";

	/*
	 * (non-Javadoc)
	 *
	 * @see de.jollyday.parser.HolidayParser#parse(int, java.util.Set,
	 * de.jollyday.config.Holidays)
	 */
	/** {@inheritDoc} */
	@Override
	public void parse(int year, Set<Holiday> holidays, final Holidays config) {
		for (ChineseHoliday i : config.getChineseHoliday()) {
			if (!isValid(i, year)) {
				continue;
			}
			ChineseCalendar chineseHoliday;
			switch (i.getType()) {
			case NEW_YEAR:
				chineseHoliday = ChineseCalendar.ofNewYear(year);
				break;
			case QING_MING:
				chineseHoliday = ChineseCalendar.ofQingMing(year);
				break;
			case DRAGON_BOAT:
				chineseHoliday = ChineseCalendar.of(EastAsianYear.forGregorian(year), EastAsianMonth.valueOf(5), 5);
				break;
			case MID_AUTUMN:
				chineseHoliday = ChineseCalendar.of(EastAsianYear.forGregorian(year), EastAsianMonth.valueOf(8), 15);
				break;
			case CHUNG_YEUNG:
				chineseHoliday = ChineseCalendar.of(EastAsianYear.forGregorian(year), EastAsianMonth.valueOf(9), 9);
				break;
			default:
				throw new IllegalArgumentException("Unknown islamic holiday " + i.getType());
			}
			Set<LocalDate> chineseHolidays =
				Collections.singleton(chineseHoliday.transform(PlainDate.class).toTemporalAccessor());
			String propertiesKey = PREFIX_PROPERTY_CHINESE + i.getType().name();
			HolidayType type = xmlUtil.getType(i.getLocalizedType());
			for (LocalDate d : chineseHolidays) {
				holidays.add(new Holiday(d, propertiesKey, type));
			}
		}
	}

}
