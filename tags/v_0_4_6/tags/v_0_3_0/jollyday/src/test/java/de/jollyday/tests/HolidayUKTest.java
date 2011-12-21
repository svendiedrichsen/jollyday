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

public class HolidayUKTest extends TestCase {

	private static final int YEAR = 2010;
	private static Set<LocalDate> uk = new HashSet<LocalDate>();

	
	static{
		uk.add(CalendarUtil.create(YEAR, DateTimeConstants.DECEMBER, 27));
		uk.add(CalendarUtil.create(YEAR, DateTimeConstants.DECEMBER, 28));
		uk.add(CalendarUtil.create(YEAR, DateTimeConstants.MAY, 3));
		uk.add(CalendarUtil.create(YEAR, DateTimeConstants.MAY, 31));
		LocalDate c = CalendarUtil.getEasterSunday(YEAR);
		c = c.plusDays(-2);
		uk.add(c);
	}
	
	@Test
	public void testManagerUKStructure() throws Exception{
		Manager m = Manager.getInstance("uk");
		Hierarchy h = m.getHierarchy();
		Assert.assertEquals("Wrong id.", "uk", h.getId());
		Assert.assertEquals("Missing children.", 8, h.getChildren().size());
	}

	@Test
	public void testManagerUKDates() throws Exception{
		Manager m = Manager.getInstance("uk");
		Set<LocalDate> holidays = m.getHolidays(2010);
		Assert.assertEquals("Wrong number of holidays.", 5, holidays.size());
		Assert.assertEquals("Wrong dates.", uk, holidays);
	}

}
