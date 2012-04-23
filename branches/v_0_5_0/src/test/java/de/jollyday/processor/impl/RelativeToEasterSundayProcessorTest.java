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

import java.util.Set;

import junit.framework.Assert;

import org.joda.time.DateTimeConstants;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;

import de.jollyday.Holiday;
import de.jollyday.config.ChronologyType;
import de.jollyday.config.RelativeToEasterSunday;

/**
 * @author sven
 * 
 */
public class RelativeToEasterSundayProcessorTest {

	private RelativeToEasterSunday relativeToEasterSunday;
	private RelativeToEasterSundayProcessor relativeToEasterSundayProcessor;

	@Before
	public void setup() {
		relativeToEasterSunday = new RelativeToEasterSunday();
		relativeToEasterSunday.setDays(1);
		relativeToEasterSundayProcessor = new RelativeToEasterSundayProcessor(relativeToEasterSunday);
	}

	/**
	 * Test method for
	 * {@link de.jollyday.processor.impl.RelativeToEasterSundayProcessor#RelativeToEasterSundayProcessor(de.jollyday.config.RelativeToEasterSunday)}
	 * .
	 */
	@Test(expected = NullPointerException.class)
	public void testRelativeToEasterSundayProcessorNull() {
		new RelativeToEasterSundayProcessor(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testRelativeToEasterSundayProcessorZeroDays() {
		RelativeToEasterSunday relativeToEasterSunday = new RelativeToEasterSunday();
		new RelativeToEasterSundayProcessor(relativeToEasterSunday);
	}

	/**
	 * Test method for
	 * {@link de.jollyday.processor.impl.RelativeToEasterSundayProcessor#init()}
	 * .
	 */
	@Test
	public void testInit() {
		relativeToEasterSundayProcessor.init();
	}

	/**
	 * Test method for
	 * {@link de.jollyday.processor.impl.RelativeToEasterSundayProcessor#process(int, java.lang.String[])}
	 * .
	 */
	@Test
	public void testProcessISOChronology() {
		Set<Holiday> holidays = relativeToEasterSundayProcessor.process(2011);
		Assert.assertNotNull(holidays);
		Assert.assertEquals(1, holidays.size());
		LocalDate isoEaster = holidays.iterator().next().getDate();
		Assert.assertEquals(new LocalDate(2011, DateTimeConstants.APRIL, 24), isoEaster);
	}

	@Test
	public void testProcessGregorianChronology() throws Exception {
		relativeToEasterSunday.setChronology(ChronologyType.GREGORIAN);
		Set<Holiday> holidays = relativeToEasterSundayProcessor.process(2011);
		Assert.assertNotNull(holidays);
		Assert.assertEquals(1, holidays.size());
		LocalDate gregorianEaster = holidays.iterator().next().getDate();
		Assert.assertEquals(new LocalDate(2011, DateTimeConstants.APRIL, 24), gregorianEaster);
	}

	@Test
	public void testProcessJulianChronology() throws Exception {
		relativeToEasterSunday.setChronology(ChronologyType.JULIAN);
		Set<Holiday> holidays = relativeToEasterSundayProcessor.process(2011);
		Assert.assertNotNull(holidays);
		Assert.assertEquals(1, holidays.size());
		LocalDate julianEaster = holidays.iterator().next().getDate();
		Assert.assertEquals(new LocalDate(2011, DateTimeConstants.APRIL, 11), julianEaster);
	}

}
