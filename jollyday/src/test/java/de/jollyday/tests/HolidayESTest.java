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

public class HolidayESTest extends TestCase {

	private static final String ISO_CODE = "es";
	private static final int YEAR = 2010;
	private static Set<LocalDate> es = new HashSet<LocalDate>();
	private static Set<LocalDate> es_ce = new HashSet<LocalDate>();

	static {
		es.add(CalendarUtil.create(YEAR, DateTimeConstants.JANUARY, 1));
		es.add(CalendarUtil.create(YEAR, DateTimeConstants.JANUARY, 6));
		es.add(CalendarUtil.create(YEAR, DateTimeConstants.MAY, 1));
		es.add(CalendarUtil.create(YEAR, DateTimeConstants.AUGUST, 15));
		es.add(CalendarUtil.create(YEAR, DateTimeConstants.OCTOBER, 12));
		es.add(CalendarUtil.create(YEAR, DateTimeConstants.DECEMBER, 6));
		es.add(CalendarUtil.create(YEAR, DateTimeConstants.DECEMBER, 8));
		es.add(CalendarUtil.create(YEAR, DateTimeConstants.DECEMBER, 25));
		es.add(CalendarUtil.create(YEAR, DateTimeConstants.APRIL, 2));
		
		es_ce.addAll(es);
		es_ce.add(CalendarUtil.create(YEAR, DateTimeConstants.SEPTEMBER, 2));
		es_ce.add(CalendarUtil.create(YEAR, DateTimeConstants.APRIL, 1));
		es_ce.add(CalendarUtil.create(YEAR, DateTimeConstants.NOVEMBER, 17));
		
	}

	@Test
	public void testManagerESStructure() throws Exception {
		Manager m = Manager.getInstance(ISO_CODE);
		Hierarchy h = m.getHierarchy();
		Assert.assertEquals("Wrong id.", ISO_CODE, h.getId());
		Assert.assertEquals("Missing children.", 19, h.getChildren().size());
	}

	@Test
	public void testManagerESDates() throws Exception {
		Manager m = Manager.getInstance(ISO_CODE);
		Set<LocalDate> holidays = m.getHolidays(2010);
		Assert.assertEquals("Wrong number of holidays.", es.size(), holidays.size());
		Assert.assertEquals("Wrong dates.", es, holidays);
	}

	@Test
	public void testManagerES_CEDates() throws Exception {
		Manager m = Manager.getInstance(ISO_CODE);
		Set<LocalDate> holidays = m.getHolidays(2010, "ce");
		Assert.assertEquals("Wrong number of holidays.", es_ce.size(), holidays.size());
		Assert.assertEquals("Wrong dates.", es_ce, holidays);
	}

}
