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

import junit.framework.TestCase;

import org.joda.time.DateTimeConstants;
import org.joda.time.LocalDate;
import org.junit.Assert;
import org.junit.Test;

import de.jollyday.Manager;
import de.jollyday.util.CalendarUtil;

public class HolidayDETest extends TestCase {

	private static final int YEAR = 2010;
	private static Set<LocalDate> de = new HashSet<LocalDate>();
	private static Set<LocalDate> de_by = new HashSet<LocalDate>();
	private static Set<LocalDate> de_sn = new HashSet<LocalDate>();
	
	static{
		de.add(CalendarUtil.create(YEAR, DateTimeConstants.JANUARY, 1));
		de.add(CalendarUtil.create(YEAR, DateTimeConstants.MAY, 1));
		de.add(CalendarUtil.create(YEAR, DateTimeConstants.OCTOBER, 3));
		de.add(CalendarUtil.create(YEAR, DateTimeConstants.DECEMBER, 25));
		de.add(CalendarUtil.create(YEAR, DateTimeConstants.DECEMBER, 26));
		de.add(CalendarUtil.create(YEAR, DateTimeConstants.APRIL, 2));
		de.add(CalendarUtil.create(YEAR, DateTimeConstants.APRIL, 5));
		LocalDate c = CalendarUtil.getEasterSunday(YEAR);
		c = c.plusDays(39);
		de.add(c);
		c = CalendarUtil.getEasterSunday(YEAR);
		c = c.plusDays(50);
		de.add(c);
		
		de_by.addAll(de);
		de_by.add(CalendarUtil.create(YEAR, DateTimeConstants.JANUARY, 6));
		de_by.add(CalendarUtil.create(YEAR, DateTimeConstants.NOVEMBER, 1));
		c = CalendarUtil.getEasterSunday(YEAR);
		c = c.plusDays(60);
		de_by.add(c);
		
		de_sn.addAll(de);
		de_sn.add(CalendarUtil.create(YEAR, DateTimeConstants.OCTOBER, 31));
		de_sn.add(CalendarUtil.create(YEAR, DateTimeConstants.NOVEMBER, 17));

	}
	
	@Test
	public void testManagerDE() throws Exception{
		Manager m = Manager.getInstance("de");
		Set<LocalDate> holidays = m.getHolidays(2010);
		Assert.assertEquals("Wrong number of holidays.", 9, holidays.size());
		Assert.assertEquals("Wrong dates.", de, holidays);
	}
	
	@Test
	public void testManagerDE_BY() throws Exception{
		Manager m = Manager.getInstance("de");
		Set<LocalDate> holidays = m.getHolidays(2010, "by");
		Assert.assertEquals("Wrong number of holidays.", 12, holidays.size());
		Assert.assertEquals("Wrong dates.", de_by, holidays);
	}

	@Test
	public void testManagerDE_SN() throws Exception{
		Manager m = Manager.getInstance("de");
		Set<LocalDate> holidays = m.getHolidays(2010, "sn");
		Assert.assertEquals("Wrong number of holidays.", 11, holidays.size());
		Assert.assertEquals("Wrong dates.", de_sn, holidays);
	}

}
