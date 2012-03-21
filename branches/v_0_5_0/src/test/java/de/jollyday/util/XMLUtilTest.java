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
package de.jollyday.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.StringBufferInputStream;

import org.joda.time.DateTimeConstants;
import org.junit.Test;

import de.jollyday.config.CycleType;
import de.jollyday.config.HolidayRule;
import de.jollyday.config.HolidayType;
import de.jollyday.config.Month;
import de.jollyday.config.Weekday;

/**
 * Tests for {@link de.jollyday.util.XMLUtil}.
 * @author sven
 *
 */
@SuppressWarnings("deprecation")
public class XMLUtilTest {

	/**
	 * Test method for {@link de.jollyday.util.XMLUtil#unmarshallConfiguration(java.io.InputStream)} with NULL parameter.
	 * @throws IOException 
	 */
	@Test(expected=NullPointerException.class)
	public void testUnmarshallConfigurationCalledWithNull() throws IOException {
		XMLUtil.unmarshallConfiguration(null);
	}
	
	/**
	 * Test method for {@link de.jollyday.util.XMLUtil#unmarshallConfiguration(java.io.InputStream)} with empty
	 * input stream.
	 * @throws IOException
	 */
	@Test(expected=IllegalStateException.class)
	public void testUnmarshallConfigurationFromAnyInputStream() throws IOException {
		XMLUtil.unmarshallConfiguration(new StringBufferInputStream(""));
	}

	/**
	 * Test method for {@link de.jollyday.util.XMLUtil#getWeekday(de.jollyday.config.Weekday)}.
	 */
	@Test
	public void testGetWeekday() {
		assertEquals("Wrong weekday.", DateTimeConstants.MONDAY, XMLUtil.getWeekday(Weekday.MONDAY));
		assertEquals("Wrong weekday.", DateTimeConstants.TUESDAY, XMLUtil.getWeekday(Weekday.TUESDAY));
		assertEquals("Wrong weekday.", DateTimeConstants.WEDNESDAY, XMLUtil.getWeekday(Weekday.WEDNESDAY));
		assertEquals("Wrong weekday.", DateTimeConstants.THURSDAY, XMLUtil.getWeekday(Weekday.THURSDAY));
		assertEquals("Wrong weekday.", DateTimeConstants.FRIDAY, XMLUtil.getWeekday(Weekday.FRIDAY));
		assertEquals("Wrong weekday.", DateTimeConstants.SATURDAY, XMLUtil.getWeekday(Weekday.SATURDAY));
		assertEquals("Wrong weekday.", DateTimeConstants.SUNDAY, XMLUtil.getWeekday(Weekday.SUNDAY));
	}

	/**
	 * Test method for {@link de.jollyday.util.XMLUtil#getMonth(de.jollyday.config.Month)}.
	 */
	@Test
	public void testGetMonth() {
		assertEquals("Wrong month.", DateTimeConstants.JANUARY, XMLUtil.getMonth(Month.JANUARY));
		assertEquals("Wrong month.", DateTimeConstants.FEBRUARY, XMLUtil.getMonth(Month.FEBRUARY));
		assertEquals("Wrong month.", DateTimeConstants.MARCH, XMLUtil.getMonth(Month.MARCH));
		assertEquals("Wrong month.", DateTimeConstants.APRIL, XMLUtil.getMonth(Month.APRIL));
		assertEquals("Wrong month.", DateTimeConstants.MAY, XMLUtil.getMonth(Month.MAY));
		assertEquals("Wrong month.", DateTimeConstants.JUNE, XMLUtil.getMonth(Month.JUNE));
		assertEquals("Wrong month.", DateTimeConstants.JULY, XMLUtil.getMonth(Month.JULY));
		assertEquals("Wrong month.", DateTimeConstants.AUGUST, XMLUtil.getMonth(Month.AUGUST));
		assertEquals("Wrong month.", DateTimeConstants.SEPTEMBER, XMLUtil.getMonth(Month.SEPTEMBER));
		assertEquals("Wrong month.", DateTimeConstants.OCTOBER, XMLUtil.getMonth(Month.OCTOBER));
		assertEquals("Wrong month.", DateTimeConstants.NOVEMBER, XMLUtil.getMonth(Month.NOVEMBER));
		assertEquals("Wrong month.", DateTimeConstants.DECEMBER, XMLUtil.getMonth(Month.DECEMBER));
	}

	/**
	 * Test method for {@link de.jollyday.util.XMLUtil#getType(de.jollyday.config.HolidayType)}.
	 */
	@Test
	public void testGetType() {
		assertTrue("Wrong holiday type.", XMLUtil.getType(HolidayType.OFFICIAL_HOLIDAY).isOfficialHoliday());
		assertFalse("Wrong holiday type.", XMLUtil.getType(HolidayType.UNOFFICIAL_HOLIDAY).isOfficialHoliday());
	}

	/**
	 * Test method for {@link de.jollyday.util.XMLUtil#isValidInYear(int, de.jollyday.config.HolidayRule)}.
	 */
	@Test
	public void testIsValidInYearFromAndTo() {
		HolidayRule rule = new HolidayRule();
		assertTrue(XMLUtil.isValidInYear(2012, rule));
		
		rule.setValidFrom(2012);
		assertTrue(XMLUtil.isValidInYear(2012, rule));

		rule.setValidFrom(2013);
		assertFalse(XMLUtil.isValidInYear(2012, rule));

		rule.setValidFrom(null);
		
		rule.setValidTo(2012);
		assertTrue(XMLUtil.isValidInYear(2012, rule));

		rule.setValidTo(2011);
		assertFalse(XMLUtil.isValidInYear(2012, rule));
	}
	
	/**
	 * Test method for {@link de.jollyday.util.XMLUtil#isValidInYear(int, de.jollyday.config.HolidayRule)}.
	 */
	@Test
	public void testIsValidInYearCycle() {
		HolidayRule rule = new HolidayRule();
		rule.setValidFrom(2000);
		rule.setValidTo(2100);
		
		rule.setEvery(CycleType.EVERY_YEAR);
		for(int i = 2000; i <= 2100; i++){
			assertTrue(XMLUtil.isValidInYear(i, rule));
		}

		rule.setEvery(CycleType.TWO_YEARS);
		for(int i = 2000; i <= 2100; i++){
			assertEquals("Validation for year "+i+" failed.", (i-2000) % 2 == 0,XMLUtil.isValidInYear(i, rule));
		}
		
		rule.setEvery(CycleType.THREE_YEARS);
		for(int i = 2000; i <= 2100; i++){
			assertEquals("Validation for year "+i+" failed.", (i-2000) % 3 == 0,XMLUtil.isValidInYear(i, rule));
		}

		rule.setEvery(CycleType.FOUR_YEARS);
		for(int i = 2000; i <= 2100; i++){
			assertEquals("Validation for year "+i+" failed.", (i-2000) % 4 == 0,XMLUtil.isValidInYear(i, rule));
		}

		rule.setEvery(CycleType.FIVE_YEARS);
		for(int i = 2000; i <= 2100; i++){
			assertEquals("Validation for year "+i+" failed.", (i-2000) % 5 == 0,XMLUtil.isValidInYear(i, rule));
		}

		rule.setEvery(CycleType.SIX_YEARS);
		for(int i = 2000; i <= 2100; i++){
			assertEquals("Validation for year "+i+" failed.", (i-2000) % 6 == 0,XMLUtil.isValidInYear(i, rule));
		}

		rule.setEvery(CycleType.SEVEN_YEARS);
		for(int i = 2000; i <= 2100; i++){
			assertEquals("Validation for year "+i+" failed.", (i-2000) % 7 == 0,XMLUtil.isValidInYear(i, rule));
		}

		rule.setEvery(CycleType.EVEN_YEARS);
		for(int i = 2000; i <= 2100; i++){
			assertEquals("Validation for year "+i+" failed.", i % 2 == 0,XMLUtil.isValidInYear(i, rule));
		}

		rule.setEvery(CycleType.ODD_YEARS);
		for(int i = 2000; i <= 2100; i++){
			assertEquals("Validation for year "+i+" failed.", i % 2 == 1,XMLUtil.isValidInYear(i, rule));
		}

	}


}
