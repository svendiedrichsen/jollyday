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

public class HolidayFITest extends TestCase {

	private static final String ISO_CODE = "fi";
	private static final int YEAR = 2010;
	private static Set<LocalDate> fi = new HashSet<LocalDate>();
	
	static{
		fi.add(CalendarUtil.create(YEAR, DateTimeConstants.JANUARY, 1));
		fi.add(CalendarUtil.create(YEAR, DateTimeConstants.JANUARY, 6));
		fi.add(CalendarUtil.create(YEAR, DateTimeConstants.MAY, 1));
		fi.add(CalendarUtil.create(YEAR, DateTimeConstants.DECEMBER, 6));
		fi.add(CalendarUtil.create(YEAR, DateTimeConstants.DECEMBER, 24));
		fi.add(CalendarUtil.create(YEAR, DateTimeConstants.DECEMBER, 25));
		fi.add(CalendarUtil.create(YEAR, DateTimeConstants.DECEMBER, 26));
		fi.add(CalendarUtil.create(YEAR, DateTimeConstants.APRIL, 2));
		fi.add(CalendarUtil.create(YEAR, DateTimeConstants.APRIL, 5));
		fi.add(CalendarUtil.create(YEAR, DateTimeConstants.MAY, 13));
		fi.add(CalendarUtil.create(YEAR, DateTimeConstants.JUNE, 25));
	}

	@Test
	public void testManagerFIStructure() throws Exception{
		Manager m = Manager.getInstance(ISO_CODE);
		Hierarchy h = m.getHierarchy();
		Assert.assertEquals("Wrong id.", ISO_CODE, h.getId());
		Assert.assertEquals("Missing children.", 0, h.getChildren().size());
	}
	
	@Test
	public void testManagerFIDates() throws Exception{
		Manager m = Manager.getInstance(ISO_CODE);
		Set<LocalDate> holidays = m.getHolidays(2010);
		Assert.assertEquals("Wrong number of holidays.", fi.size(), holidays.size());
		Assert.assertEquals("Wrong dates.", fi, holidays);
		for(LocalDate holiday : fi){
			Assert.assertTrue("Holiday missing "+holiday, m.isHoliday(holiday));
		}
	}
	
}