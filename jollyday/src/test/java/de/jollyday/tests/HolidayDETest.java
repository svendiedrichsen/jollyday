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

import de.jollyday.Hierarchy;
import de.jollyday.Manager;
import de.jollyday.util.CalendarUtil;

public class HolidayDETest extends TestCase {

	private static final String ISO_CODE = "de";
	private static final int YEAR = 2010;
	private static Set<LocalDate> de = new HashSet<LocalDate>();
	private static Set<LocalDate> de_1990 = new HashSet<LocalDate>();
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
		
		de_1990.add(CalendarUtil.create(1990, DateTimeConstants.JANUARY, 1));
		de_1990.add(CalendarUtil.create(1990, DateTimeConstants.MAY, 1));
		de_1990.add(CalendarUtil.create(1990, DateTimeConstants.OCTOBER, 3));
		de_1990.add(CalendarUtil.create(1990, DateTimeConstants.DECEMBER, 25));
		de_1990.add(CalendarUtil.create(1990, DateTimeConstants.DECEMBER, 26));
		de_1990.add(CalendarUtil.create(1990, DateTimeConstants.APRIL, 13));
		de_1990.add(CalendarUtil.create(1990, DateTimeConstants.APRIL, 16));
		c = CalendarUtil.getEasterSunday(1990);
		c = c.plusDays(39);
		de_1990.add(c);
		c = CalendarUtil.getEasterSunday(1990);
		c = c.plusDays(50);
		de_1990.add(c);
		de_1990.add(CalendarUtil.create(1990, DateTimeConstants.JUNE, 17));
	}

	@Test
	public void testManagerDEStructure() throws Exception{
		Manager m = Manager.getInstance(ISO_CODE);
		Hierarchy h = m.getHierarchy();
		Assert.assertEquals("Wrong id.", ISO_CODE, h.getId());
		Assert.assertEquals("Missing children.", 11, h.getChildren().size());
	}
	
	@Test
	public void testManagerDEDates() throws Exception{
		Manager m = Manager.getInstance(ISO_CODE);
		Set<LocalDate> holidays = m.getHolidays(YEAR);
		Assert.assertEquals("Wrong number of holidays.", 9, holidays.size());
		Assert.assertEquals("Wrong dates.", de, holidays);
		for(LocalDate holiday : de){
			Assert.assertTrue("Holiday missing "+holiday, m.isHoliday(holiday));
		}
	}

	@Test
	public void testManagerDEDates1990() throws Exception{
		Manager m = Manager.getInstance(ISO_CODE);
		Set<LocalDate> holidays = m.getHolidays(1990);
		Assert.assertEquals("Wrong number of holidays.", de_1990.size(), holidays.size());
		Assert.assertEquals("Wrong dates.", de_1990, holidays);
		for(LocalDate holiday : de_1990){
			Assert.assertTrue("Holiday missing "+holiday, m.isHoliday(holiday));
		}
	}
	
	@Test
	public void testManagerDE_BY() throws Exception{
		Manager m = Manager.getInstance(ISO_CODE);
		Set<LocalDate> holidays = m.getHolidays(2010, "by");
		Assert.assertEquals("Wrong number of holidays.", 12, holidays.size());
		Assert.assertEquals("Wrong dates.", de_by, holidays);
		for(LocalDate holiday : de_by){
			Assert.assertTrue("Holiday missing "+holiday, m.isHoliday(holiday, "by"));
		}
	}

	@Test
	public void testManagerDE_SN() throws Exception{
		Manager m = Manager.getInstance(ISO_CODE);
		Set<LocalDate> holidays = m.getHolidays(2010, "sn");
		Assert.assertEquals("Wrong number of holidays.", 11, holidays.size());
		Assert.assertEquals("Wrong dates.", de_sn, holidays);
		for(LocalDate holiday : de_sn){
			Assert.assertTrue("Holiday missing "+holiday, m.isHoliday(holiday, "sn"));
		}
	}

}
