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

import org.junit.Assert;
import org.junit.Test;

import de.jollyday.Holiday;
import de.jollyday.config.FixedWeekdayInMonth;
import de.jollyday.config.Holidays;
import de.jollyday.parser.impl.FixedWeekdayInMonthParser;

/**
 * @author svdi1de
 *
 */
public class FixedWeekdayInMonthParserTest {
	
	private FixedWeekdayInMonthParser parser = new FixedWeekdayInMonthParser();
	
	@Test
	public void testEmpty(){
		Set<Holiday> holidays = new HashSet<Holiday>();
		Holidays config = new Holidays();
		parser.parse(2010, holidays, config);
		Assert.assertTrue("Expected to be empty.", holidays.isEmpty());
	}

	@Test
	public void testInvalid(){
		Set<Holiday> holidays = new HashSet<Holiday>();
		Holidays config = new Holidays();
		FixedWeekdayInMonth e = new FixedWeekdayInMonth();
		e.setValidFrom(2011);
		config.getFixedWeekday().add(e);
		parser.parse(2010, holidays, config);
		Assert.assertEquals("Expected to be empty.", 0, holidays.size());
	}

}
