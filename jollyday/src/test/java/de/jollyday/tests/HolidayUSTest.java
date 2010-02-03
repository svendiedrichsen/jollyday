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

public class HolidayUSTest extends TestCase {

	private static final String US = "us";
	private static final int YEAR = 2010;
	private static Set<LocalDate> us = new HashSet<LocalDate>();
	private static Set<LocalDate> us_de = new HashSet<LocalDate>();
	private static Set<LocalDate> us_ct = new HashSet<LocalDate>();
	
	static{
		us.add(CalendarUtil.create(YEAR, DateTimeConstants.JANUARY, 1));
		us.add(CalendarUtil.create(YEAR, DateTimeConstants.MAY, 31));
		us.add(CalendarUtil.create(YEAR, DateTimeConstants.JULY, 4));
		us.add(CalendarUtil.create(YEAR, DateTimeConstants.SEPTEMBER, 6));
		us.add(CalendarUtil.create(YEAR, DateTimeConstants.NOVEMBER, 11));
		us.add(CalendarUtil.create(YEAR, DateTimeConstants.NOVEMBER, 25));
		us.add(CalendarUtil.create(YEAR, DateTimeConstants.DECEMBER, 25));
		
		us_de.addAll(us);
		us_de.add(CalendarUtil.create(YEAR, DateTimeConstants.APRIL, 2));
		us_de.add(CalendarUtil.create(YEAR, DateTimeConstants.NOVEMBER, 26));
		us_de.add(CalendarUtil.create(YEAR, DateTimeConstants.JANUARY, 18));
		us_de.add(CalendarUtil.create(YEAR, DateTimeConstants.FEBRUARY, 15));

		us_ct.addAll(us);
		us_ct.add(CalendarUtil.create(YEAR, DateTimeConstants.FEBRUARY, 12));
		us_ct.add(CalendarUtil.create(YEAR, DateTimeConstants.APRIL, 2));
		us_ct.add(CalendarUtil.create(YEAR, DateTimeConstants.JANUARY, 18));
		us_ct.add(CalendarUtil.create(YEAR, DateTimeConstants.FEBRUARY, 15));
		us_ct.add(CalendarUtil.create(YEAR, DateTimeConstants.OCTOBER, 11));
	}

	@Test
	public void testManagerUSStructure() throws Exception{
		Manager m = Manager.getInstance(US);
		Hierarchy h = m.getHierarchy();
		Assert.assertEquals("Wrong id.", US, h.getId());
		Assert.assertEquals("Wrong number of children.", 51, h.getChildren().size());
	}
	
	@Test
	public void testManagerUSDates() throws Exception{
		Manager m = Manager.getInstance(US);
		Set<LocalDate> holidays = m.getHolidays(2010);
		Assert.assertEquals("Wrong number of holidays.", 7, holidays.size());
		Assert.assertEquals("Wrong dates.", us, holidays);
	}

	@Test
	public void testManagerUS_DEDates() throws Exception{
		Manager m = Manager.getInstance(US);
		Set<LocalDate> holidays = m.getHolidays(2010, "de");
		Assert.assertEquals("Wrong number of holidays.", us_de.size(), holidays.size());
		Assert.assertEquals("Wrong dates.", us_de, holidays);
	}

	@Test
	public void testManagerUS_CTDates() throws Exception{
		Manager m = Manager.getInstance(US);
		Set<LocalDate> holidays = m.getHolidays(2010, "ct");
		Assert.assertEquals("Wrong number of holidays.", us_ct.size(), holidays.size());
		Assert.assertEquals("Wrong dates.", us_ct, holidays);
	}

}
