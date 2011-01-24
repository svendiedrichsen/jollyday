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

import org.joda.time.LocalDate;
import org.junit.Assert;
import org.junit.Test;

import de.jollyday.Holiday;
import de.jollyday.config.ChristianHoliday;
import de.jollyday.config.ChristianHolidayType;
import de.jollyday.config.Holidays;
import de.jollyday.parser.impl.ChristianHolidayParser;
import de.jollyday.util.CalendarUtil;

/**
 * @author svdi1de
 *
 */
public class ChristianHolidayParserTest {
	
	private ChristianHolidayParser hp = new ChristianHolidayParser();
	
	@Test
	public void testEmpty(){
		Set<Holiday> holidays = new HashSet<Holiday>();
		Holidays config = new Holidays();
		hp.parse(2010, holidays, config);
		Assert.assertTrue("Expected to be empty.", holidays.isEmpty());
	}
	
	@Test
	public void testEaster(){
		Set<Holiday> holidays = new HashSet<Holiday>();
		Holidays config = new Holidays();
		ChristianHoliday easter = new ChristianHoliday();
		easter.setType(ChristianHolidayType.EASTER);
		config.getChristianHoliday().add(easter);
		hp.parse(2011, holidays, config);
		Assert.assertEquals("Wrong number of holidays.", 1, holidays.size());
		Holiday easterDate = holidays.iterator().next();
		LocalDate ed = CalendarUtil.create(2011, 4, 24);
		Assert.assertEquals("Wrong easter date.", ed, easterDate.getDate());
	}


	@Test
	public void testChristianInvalidDate(){
		Set<Holiday> holidays = new HashSet<Holiday>();
		Holidays config = new Holidays();
		ChristianHoliday easter = new ChristianHoliday();
		easter.setType(ChristianHolidayType.EASTER);
		easter.setValidTo(2010);
		config.getChristianHoliday().add(easter);
		hp.parse(2011, holidays, config);
		Assert.assertEquals("Wrong number of holidays.", 0, holidays.size());
	}

}
