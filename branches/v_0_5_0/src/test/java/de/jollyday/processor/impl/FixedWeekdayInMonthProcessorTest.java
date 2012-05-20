/**
 * Copyright 2012 Sven Diedrichsen 
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
package de.jollyday.processor.impl;

import static org.junit.Assert.assertEquals;

import java.util.Set;

import org.joda.time.DateTimeConstants;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;

import de.jollyday.Holiday;
import de.jollyday.config.FixedWeekdayInMonth;
import de.jollyday.config.Month;
import de.jollyday.config.Weekday;
import de.jollyday.config.Which;

/**
 * @author sven
 * 
 */
public class FixedWeekdayInMonthProcessorTest {

	private FixedWeekdayInMonthProcessor fixedWeekdayInMonthProcessor;
	private FixedWeekdayInMonth fixedWeekdayInMonth;

	@Before
	public void setup() {
		fixedWeekdayInMonth = new FixedWeekdayInMonth();
		fixedWeekdayInMonthProcessor = new FixedWeekdayInMonthProcessor(fixedWeekdayInMonth);
	}

	@Test(expected = NullPointerException.class)
	public void testConstructorNullCheck() throws Exception {
		new FixedWeekdayInMonthProcessor(null);
	}

	@Test
	public void testProcessorResult() throws Exception {
		fixedWeekdayInMonth.setMonth(Month.JANUARY);
		fixedWeekdayInMonth.setWeekday(Weekday.MONDAY);
		fixedWeekdayInMonth.setWhich(Which.SECOND);
		Set<Holiday> holidays = fixedWeekdayInMonthProcessor.process(2012);
		assertEquals(1, holidays.size());
		assertEquals(new LocalDate(2012, DateTimeConstants.JANUARY, 9), holidays.iterator().next().getDate());
	}

}
