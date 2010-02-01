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
package de.jollyday.tests;

import java.util.HashSet;
import java.util.Set;

import junit.framework.Assert;

import org.joda.time.DateTimeConstants;
import org.joda.time.LocalDate;
import org.junit.Test;

import de.jollyday.Hierarchy;
import de.jollyday.Manager;
import de.jollyday.util.CalendarUtil;

/**
 * @author svdi1de
 *
 */
public class HolidayTest {
	
	private static final Set<LocalDate> test_days = new HashSet<LocalDate>();
	private static final Set<LocalDate> test_days_l1 = new HashSet<LocalDate>();
	private static final Set<LocalDate> test_days_l2 = new HashSet<LocalDate>();

	static{
		test_days.add(CalendarUtil.create(2010, DateTimeConstants.AUGUST, 30));
		test_days.add(CalendarUtil.create(2010, DateTimeConstants.APRIL, 2));
		test_days.add(CalendarUtil.create(2010, DateTimeConstants.NOVEMBER, 17));
		test_days.add(CalendarUtil.create(2010, DateTimeConstants.NOVEMBER, 28));
		test_days.add(CalendarUtil.create(2010, DateTimeConstants.JANUARY, 1));
		test_days.add(CalendarUtil.create(2010, DateTimeConstants.JANUARY, 18));
		test_days.add(CalendarUtil.create(2010, DateTimeConstants.NOVEMBER, 26));
		test_days_l1.addAll(test_days);
		test_days_l1.add(CalendarUtil.create(2010, DateTimeConstants.JANUARY, 2));
		test_days_l2.addAll(test_days_l1);
		test_days_l2.add(CalendarUtil.create(2010, DateTimeConstants.JANUARY, 3));
	}
	
	@Test
	public void testBaseStructure() throws Exception{
		Manager m = Manager.getInstance("test");
		Hierarchy h = m.getHierarchy();
		Assert.assertEquals("Wrong id.", "test", h.getId());
		Assert.assertEquals("Wrong number of children on first level.", 1, h.getChildren().size());
		Assert.assertEquals("Wrong number of children on second level.", 1, h.getChildren().iterator().next().getChildren().size());
	}

	@Test
	public void testBaseDates() throws Exception{
		Manager m = Manager.getInstance("test");
		Set<LocalDate> holidays = m.getHolidays(2010);
		Assert.assertNotNull(holidays);
		Assert.assertEquals("Wrong number of dates.", test_days.size(), holidays.size());
		Assert.assertEquals("Wrong dates.", test_days, holidays);
	}

	@Test
	public void testLevel1() throws Exception{
		Manager m = Manager.getInstance("test");
		Set<LocalDate> holidays = m.getHolidays(2010, "level1");
		Assert.assertNotNull(holidays);
		Assert.assertEquals("Wrong number of dates.", test_days_l1.size(), holidays.size());
		Assert.assertEquals("Wrong dates.", test_days_l1, holidays);
	}

	@Test
	public void testLevel2() throws Exception{
		Manager m = Manager.getInstance("test");
		Set<LocalDate> holidays = m.getHolidays(2010, "level1", "level2");
		Assert.assertNotNull(holidays);
		Assert.assertEquals("Wrong number of dates.", test_days_l2.size(), holidays.size());
		Assert.assertEquals("Wrong dates.", test_days_l2, holidays);
	}

	
}
