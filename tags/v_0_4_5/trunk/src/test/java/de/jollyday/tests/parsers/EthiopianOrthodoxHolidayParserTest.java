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
import de.jollyday.config.EthiopianOrthodoxHoliday;
import de.jollyday.config.EthiopianOrthodoxHolidayType;
import de.jollyday.config.Holidays;
import de.jollyday.parser.impl.EthiopianOrthodoxHolidayParser;
import de.jollyday.util.CalendarUtil;

/**
 * @author Sven
 *
 */
public class EthiopianOrthodoxHolidayParserTest {

	private EthiopianOrthodoxHolidayParser parser = new EthiopianOrthodoxHolidayParser();
	
	@Test
	public void testEmpty(){
		Set<Holiday> holidays = new HashSet<Holiday>();
		Holidays config = new Holidays();
		parser.parse(2010, holidays, config);
		Assert.assertTrue("Expected to be empty.", holidays.isEmpty());
	}
	
	@Test
	public void testAllHolidays(){
		Set<Holiday> holidays = new HashSet<Holiday>();
		Holidays config = new Holidays();
		config.getEthiopianOrthodoxHoliday().add(createHoliday(EthiopianOrthodoxHolidayType.ENKUTATASH));
		config.getEthiopianOrthodoxHoliday().add(createHoliday(EthiopianOrthodoxHolidayType.MESKEL));
		config.getEthiopianOrthodoxHoliday().add(createHoliday(EthiopianOrthodoxHolidayType.TIMKAT));
		parser.parse(2010, holidays, config);
		Assert.assertEquals("Wrong number of holidays.", 3, holidays.size());
		assertContains(CalendarUtil.create(2010, 1, 18), holidays);
		assertContains(CalendarUtil.create(2010, 9, 11), holidays);
		assertContains(CalendarUtil.create(2010, 9, 27), holidays);
	}
	
	private static final void assertContains(LocalDate date, Set<Holiday> holidays){
		Assert.assertTrue("Missing holiday "+date, CalendarUtil.contains(holidays, date));
	}

	/**
	 * @return an EthiopianOrthodoxHoliday instance
	 */
	private EthiopianOrthodoxHoliday createHoliday(EthiopianOrthodoxHolidayType type) {
		EthiopianOrthodoxHoliday h = new EthiopianOrthodoxHoliday();
		h.setType(type);
		return h;
	}
	
	
}
