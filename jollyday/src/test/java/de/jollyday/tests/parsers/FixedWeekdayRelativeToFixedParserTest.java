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

import java.util.HashSet;
import java.util.Set;

import junit.framework.Assert;

import org.junit.Test;

import de.jollyday.Holiday;
import de.jollyday.config.Fixed;
import de.jollyday.config.FixedWeekdayRelativeToFixed;
import de.jollyday.config.Holidays;
import de.jollyday.config.Month;
import de.jollyday.config.Weekday;
import de.jollyday.config.When;
import de.jollyday.config.Which;
import de.jollyday.parser.impl.FixedWeekdayRelativeToFixedParser;
import de.jollyday.util.CalendarUtil;

/**
 * @author Sven
 *
 */
public class FixedWeekdayRelativeToFixedParserTest {

	private FixedWeekdayRelativeToFixedParser fwrtf = new FixedWeekdayRelativeToFixedParser();
	
	@Test
	public void testEmpty(){
		Set<Holiday> result =new HashSet<Holiday>();
		Holidays config = new Holidays();
		fwrtf.parse(2011, result, config);
		Assert.assertTrue("Result is not empty.", result.isEmpty());
	}

	@Test
	public void testParserFirstBefore(){
		Set<Holiday> result =new HashSet<Holiday>();
		Holidays config = new Holidays();
		FixedWeekdayRelativeToFixed rule = new FixedWeekdayRelativeToFixed();
		rule.setWhich(Which.FIRST);
		rule.setWeekday(Weekday.MONDAY);
		rule.setWhen(When.BEFORE);
		Fixed fixed = new Fixed();
		fixed.setDay(29);
		fixed.setMonth(Month.JANUARY);
		rule.setDay(fixed);
		config.getFixedWeekdayRelativeToFixed().add(rule);
		fwrtf.parse(2011, result, config);
		Assert.assertEquals("Wrong number of dates.", 1, result.size());
		Assert.assertEquals("Wrong date.", CalendarUtil.create(2011, 1, 24), result.iterator().next().getDate());
	}

	@Test
	public void testParserSecondBefore(){
		Set<Holiday> result =new HashSet<Holiday>();
		Holidays config = new Holidays();
		FixedWeekdayRelativeToFixed rule = new FixedWeekdayRelativeToFixed();
		rule.setWhich(Which.SECOND);
		rule.setWeekday(Weekday.MONDAY);
		rule.setWhen(When.BEFORE);
		Fixed fixed = new Fixed();
		fixed.setDay(29);
		fixed.setMonth(Month.JANUARY);
		rule.setDay(fixed);
		config.getFixedWeekdayRelativeToFixed().add(rule);
		fwrtf.parse(2011, result, config);
		Assert.assertEquals("Wrong number of dates.", 1, result.size());
		Assert.assertEquals("Wrong date.", CalendarUtil.create(2011, 1, 17), result.iterator().next().getDate());
	}

}
