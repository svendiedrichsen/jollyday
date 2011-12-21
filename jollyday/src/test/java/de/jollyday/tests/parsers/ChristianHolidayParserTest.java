/**
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.joda.time.LocalDate;
import org.junit.Assert;
import org.junit.Test;

import de.jollyday.Holiday;
import de.jollyday.config.ChristianHoliday;
import de.jollyday.config.ChristianHolidayType;
import de.jollyday.config.ChronologyType;
import de.jollyday.config.Holidays;
import de.jollyday.config.RelativeToEasterSunday;
import de.jollyday.parser.AbstractHolidayParser;
import de.jollyday.parser.impl.ChristianHolidayParser;
import de.jollyday.parser.impl.RelativeToEasterSundayParser;
import de.jollyday.util.CalendarUtil;

/**
 * @author svdi1de
 * 
 */
public class ChristianHolidayParserTest {

	private AbstractHolidayParser hp = new ChristianHolidayParser();

	@Test
	public void testEmpty() {
		Set<Holiday> holidays = new HashSet<Holiday>();
		Holidays config = new Holidays();
		hp.parse(2010, holidays, config);
		Assert.assertTrue("Expected to be empty.", holidays.isEmpty());
	}

	@Test
	public void testEaster() {
		Set<Holiday> holidays = new HashSet<Holiday>();
		Holidays config = createConfig(ChristianHolidayType.EASTER);
		hp.parse(2011, holidays, config);
		Assert.assertEquals("Wrong number of holidays.", 1, holidays.size());
		Holiday easterDate = holidays.iterator().next();
		LocalDate ed = CalendarUtil.create(2011, 4, 24);
		Assert.assertEquals("Wrong easter date.", ed, easterDate.getDate());
	}

	@Test
	public void testChristianInvalidDate() {
		Set<Holiday> holidays = new HashSet<Holiday>();
		Holidays config = createConfig(ChristianHolidayType.EASTER);
		config.getChristianHoliday().get(0).setValidTo(2010);
		hp.parse(2011, holidays, config);
		Assert.assertEquals("Wrong number of holidays.", 0, holidays.size());
	}
	
	@Test
	public void testRelativeToEasterSunday(){
		Set<Holiday> holidays = new HashSet<Holiday>();
		Holidays config = createConfig(1);
		RelativeToEasterSundayParser p = new RelativeToEasterSundayParser();
		p.parse(2011, holidays, config);
		List<LocalDate> expected = new ArrayList<LocalDate>();
		expected.add(CalendarUtil.create(2011, 4, 24));
		Assert.assertEquals("Wrong number of holidays.", expected.size(),
				holidays.size());
		Assert.assertEquals("Wrong holiday.", expected.get(0), holidays.iterator().next().getDate());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testChristianDates() {
		Set<Holiday> holidays = new HashSet<Holiday>();
		Holidays config = createConfig(ChristianHolidayType.EASTER,
				ChristianHolidayType.CLEAN_MONDAY,
				ChristianHolidayType.EASTER_SATURDAY,
				ChristianHolidayType.EASTER_TUESDAY,
				ChristianHolidayType.GENERAL_PRAYER_DAY,
				ChristianHolidayType.PENTECOST,
				ChristianHolidayType.SACRED_HEART);
		hp.parse(2011, holidays, config);
		List<LocalDate> expected = new ArrayList<LocalDate>();
		expected.add(CalendarUtil.create(2011, 3, 7));
		expected.add(CalendarUtil.create(2011, 4, 23));
		expected.add(CalendarUtil.create(2011, 4, 24));
		expected.add(CalendarUtil.create(2011, 4, 26));
		expected.add(CalendarUtil.create(2011, 5, 20));
		expected.add(CalendarUtil.create(2011, 6, 12));
		expected.add(CalendarUtil.create(2011, 7, 1));

		Assert.assertEquals("Wrong number of holidays.", expected.size(),
				holidays.size());

		Collections.sort(expected);
		List<Holiday> found = new ArrayList<Holiday>(holidays);
		Collections.sort(found, new HolidayComparator());

		for (int i = 0; i < expected.size(); i++) {
			Assert.assertEquals("Wrong date.", expected.get(i), found.get(i)
					.getDate());
		}

	}
	
	private Holidays createConfig(int...days) {
		Holidays config = new Holidays();
		for (int day : days) {
			RelativeToEasterSunday d = new RelativeToEasterSunday();
			d.setDays(day);
			d.setChronology(ChronologyType.GREGORIAN);
			config.getRelativeToEasterSunday().add(d);
		}
		return config;
	}

	private Holidays createConfig(ChristianHolidayType... types) {
		Holidays config = new Holidays();
		for (ChristianHolidayType type : types) {
			ChristianHoliday h = new ChristianHoliday();
			h.setType(type);
			config.getChristianHoliday().add(h);
		}
		return config;
	}

}
