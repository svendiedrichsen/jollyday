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
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.joda.time.LocalDate;
import org.junit.Assert;
import org.junit.Test;

import de.jollyday.Holiday;
import de.jollyday.config.Fixed;
import de.jollyday.config.Holidays;
import de.jollyday.config.Month;
import de.jollyday.config.MovingCondition;
import de.jollyday.config.Weekday;
import de.jollyday.config.With;
import de.jollyday.parser.impl.FixedParser;
import de.jollyday.util.CalendarUtil;

/**
 * @author Sven
 * 
 */
public class FixedParserTest {

	private FixedParser fixedParser = new FixedParser();

	@Test
	public void testFixedWithValidity() {
		Holidays h = createHolidays(createFixed(1, Month.JANUARY),
				createFixed(3, Month.MARCH),
				createFixed(5, Month.MAY, 2011, null));
		Set<Holiday> set = new HashSet<Holiday>();
		fixedParser.parse(2010, set, h);
		contains(new ArrayList<Holiday>(set), CalendarUtil.create(2010, 1, 1),
				CalendarUtil.create(2010, 3, 3));
	}

	@Test
	public void testFixedWithMoving() {
		Holidays h = createHolidays(
				createFixed(
						8,
						Month.JANUARY,
						createMoving(Weekday.SATURDAY, With.PREVIOUS,
								Weekday.FRIDAY)),
				createFixed(23, Month.JANUARY,
						createMoving(Weekday.SUNDAY, With.NEXT, Weekday.MONDAY)));
		Set<Holiday> set = new HashSet<Holiday>();
		fixedParser.parse(2011, set, h);
		contains(new ArrayList<Holiday>(set), CalendarUtil.create(2011, 1, 7),
				CalendarUtil.create(2011, 1, 24));
	}

	@SuppressWarnings("unchecked")
	private void contains(List<Holiday> list, LocalDate... dates) {
		Assert.assertEquals("Number of holidays.", dates.length, list.size());
		List<LocalDate> expected = new ArrayList<LocalDate>(
				Arrays.asList(dates));
		Collections.sort(expected);
		Collections.sort(list, new HolidayComparator());
		for (int i = 0; i < expected.size(); i++) {
			Assert.assertEquals("Missing date.", expected.get(i), list.get(i)
					.getDate());
		}
	}

	public Holidays createHolidays(Fixed... fs) {
		Holidays h = new Holidays();
		h.getFixed().addAll(Arrays.asList(fs));
		return h;
	}

	/**
	 * @return
	 */
	public Fixed createFixed(int day, Month m, MovingCondition... mc) {
		Fixed f = new Fixed();
		f.setDay(day);
		f.setMonth(m);
		f.getMovingCondition().addAll(Arrays.asList(mc));
		return f;
	}

	public Fixed createFixed(int day, Month m, Integer validFrom,
			Integer validUntil, MovingCondition... mc) {
		Fixed f = createFixed(day, m, mc);
		f.setValidFrom(validFrom);
		f.setValidTo(validUntil);
		return f;
	}

	public MovingCondition createMoving(Weekday substitute, With with,
			Weekday weekday) {
		MovingCondition mc = new MovingCondition();
		mc.setSubstitute(substitute);
		mc.setWith(with);
		mc.setWeekday(weekday);
		return mc;
	}

}
