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

import de.jollyday.util.CalendarUtil;

/**
 * @author svdi1de
 *
 */
public class UtilTest {
	
	@Test
	public void testWeekend(){
		LocalDate dateFriday = CalendarUtil.create(2010, DateTimeConstants.MARCH, 12);
		LocalDate dateSaturday = CalendarUtil.create(2010, DateTimeConstants.MARCH, 13);
		LocalDate dateSunday = CalendarUtil.create(2010, DateTimeConstants.MARCH, 14);
		LocalDate dateMonday = CalendarUtil.create(2010, DateTimeConstants.MARCH, 15);
		Assert.assertFalse(CalendarUtil.isWeekend(dateFriday));
		Assert.assertTrue(CalendarUtil.isWeekend(dateSaturday));
		Assert.assertTrue(CalendarUtil.isWeekend(dateSunday));
		Assert.assertFalse(CalendarUtil.isWeekend(dateMonday));
	}

	@Test
	public void testCalendarIslamicNewYear(){
		Set<LocalDate> expected = new HashSet<LocalDate>();
		expected.add(CalendarUtil.create(2008, DateTimeConstants.JANUARY, 10));
		expected.add(CalendarUtil.create(2008, DateTimeConstants.DECEMBER, 29));
		Set<LocalDate> holidays = CalendarUtil.getIslamicHolidaysInGregorianYear(2008, 1, 1);
		Assert.assertNotNull(holidays);
		Assert.assertEquals("Wrong number of islamic new years in 2008.", expected.size(), holidays.size());
		Assert.assertEquals("Wrong islamic New Year holidays in 2008.", expected, holidays);
	}

	@Test
	public void testCalendarIslamicAschura2008(){
		Set<LocalDate> expected = new HashSet<LocalDate>();
		expected.add(CalendarUtil.create(2008, DateTimeConstants.JANUARY, 19));
		Set<LocalDate> holidays = CalendarUtil.getIslamicHolidaysInGregorianYear(2008, 1, 10);
		Assert.assertNotNull(holidays);
		Assert.assertEquals("Wrong number of islamic Aschura holidays in 2008.", expected.size(), holidays.size());
		Assert.assertEquals("Wrong islamic Aschura holidays in 2008.", expected, holidays);
	}

	@Test
	public void testCalendarIslamicAschura2009(){
		Set<LocalDate> expected = new HashSet<LocalDate>();
		expected.add(CalendarUtil.create(2009, DateTimeConstants.JANUARY, 7));
		expected.add(CalendarUtil.create(2009, DateTimeConstants.DECEMBER, 27));
		Set<LocalDate> holidays = CalendarUtil.getIslamicHolidaysInGregorianYear(2009, 1, 10);
		Assert.assertNotNull(holidays);
		Assert.assertEquals("Wrong number of islamic Aschura holidays in 2009.", expected.size(), holidays.size());
		Assert.assertEquals("Wrong islamic Aschura holidays in 2009.", expected, holidays);
	}

	@Test
	public void testCalendarIslamicIdAlFitr2008(){
		Set<LocalDate> expected = new HashSet<LocalDate>();
		expected.add(CalendarUtil.create(2008, DateTimeConstants.OCTOBER, 2));
		Set<LocalDate> holidays = CalendarUtil.getIslamicHolidaysInGregorianYear(2008, 10, 1);
		Assert.assertNotNull(holidays);
		Assert.assertEquals("Wrong number of islamic IdAlFitr holidays in 2008.", expected.size(), holidays.size());
		Assert.assertEquals("Wrong islamic IdAlFitr holidays in 2008.", expected, holidays);
	}

	@Test
	public void testCalendarIslamicIdAlFitr2009(){
		Set<LocalDate> expected = new HashSet<LocalDate>();
		expected.add(CalendarUtil.create(2009, DateTimeConstants.SEPTEMBER, 21));
		Set<LocalDate> holidays = CalendarUtil.getIslamicHolidaysInGregorianYear(2009, 10, 1);
		Assert.assertNotNull(holidays);
		Assert.assertEquals("Wrong number of islamic IdAlFitr holidays in 2009.", expected.size(), holidays.size());
		Assert.assertEquals("Wrong islamic IdAlFitr holidays in 2009.", expected, holidays);
	}

}
