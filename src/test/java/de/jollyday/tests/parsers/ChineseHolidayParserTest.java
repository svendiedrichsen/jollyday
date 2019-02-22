/*
 * Copyright 2011 Sven Diedrichsen
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
package de.jollyday.tests.parsers;

import de.jollyday.Holiday;
import de.jollyday.config.ChineseHoliday;
import de.jollyday.config.ChineseHolidayType;
import de.jollyday.config.Holidays;
import de.jollyday.parser.impl.ChineseHolidayParser;
import de.jollyday.util.CalendarUtil;
import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Meno Hochschild
 */
public class ChineseHolidayParserTest {

	private ChineseHolidayParser parser = new ChineseHolidayParser();
	private CalendarUtil calendarUtil = new CalendarUtil();

	@Test
	public void testEmpty() {
		Set<Holiday> holidays = new HashSet<>();
		Holidays config = new Holidays();
		parser.parse(2010, holidays, config);
		Assert.assertTrue("Expected to be empty.", holidays.isEmpty());
	}

	@Test
	public void testAllHolidays() {
		Set<Holiday> holidays = new HashSet<>();
		Holidays config = new Holidays();
		config.getChineseHoliday().add(createHoliday(ChineseHolidayType.NEW_YEAR));
		config.getChineseHoliday().add(createHoliday(ChineseHolidayType.QING_MING));
		config.getChineseHoliday().add(createHoliday(ChineseHolidayType.DRAGON_BOAT));
		config.getChineseHoliday().add(createHoliday(ChineseHolidayType.MID_AUTUMN));
		config.getChineseHoliday().add(createHoliday(ChineseHolidayType.CHUNG_YEUNG));
		parser.parse(2019, holidays, config);
		Assert.assertEquals("Wrong number of holidays.", 5, holidays.size());
		assertContains(LocalDate.of(2019, 2, 5), holidays);
		assertContains(LocalDate.of(2019, 4, 5), holidays);
		assertContains(LocalDate.of(2019, 6, 7), holidays);
		assertContains(LocalDate.of(2019, 9, 13), holidays);
		assertContains(LocalDate.of(2019, 10, 7), holidays);
	}

	private void assertContains(LocalDate date, Set<Holiday> holidays) {
		Assert.assertTrue("Missing holiday " + date, calendarUtil.contains(holidays, date));
	}

	/**
	 * @return an ChineseHoliday instance
	 */
	private ChineseHoliday createHoliday(ChineseHolidayType type) {
		ChineseHoliday h = new ChineseHoliday();
		h.setType(type);
		return h;
	}

}
