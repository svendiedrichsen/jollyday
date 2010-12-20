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

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.joda.time.chrono.GregorianChronology;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.jollyday.CalendarHierarchy;
import de.jollyday.Holiday;
import de.jollyday.HolidayManager;
import de.jollyday.util.CalendarUtil;

/**
 * @author svdi1de
 *
 */
public class HolidayTest extends TestCase {

	private final static Logger LOG = Logger.getLogger(HolidayTest.class.getName());
	
	private static final Set<LocalDate> test_days = new HashSet<LocalDate>();
	private static final Set<LocalDate> test_days_l1 = new HashSet<LocalDate>();
	private static final Set<LocalDate> test_days_l2 = new HashSet<LocalDate>();
	private static final Set<LocalDate> test_days_l11 = new HashSet<LocalDate>();

	static{
		test_days.add(CalendarUtil.create(2010, DateTimeConstants.FEBRUARY, 17));
		test_days.add(CalendarUtil.create(2010, DateTimeConstants.AUGUST, 30));
		test_days.add(CalendarUtil.create(2010, DateTimeConstants.APRIL, 2));
		test_days.add(CalendarUtil.create(2010, DateTimeConstants.APRIL, 5));
		test_days.add(CalendarUtil.create(2010, DateTimeConstants.NOVEMBER, 17));
		test_days.add(CalendarUtil.create(2010, DateTimeConstants.NOVEMBER, 28));
		test_days.add(CalendarUtil.create(2010, DateTimeConstants.JANUARY, 1));
		test_days.add(CalendarUtil.create(2010, DateTimeConstants.JANUARY, 18));
		test_days.add(CalendarUtil.create(2010, DateTimeConstants.NOVEMBER, 26));
		test_days_l1.addAll(test_days);
		test_days_l1.add(CalendarUtil.create(2010, DateTimeConstants.JANUARY, 2));
		test_days_l2.addAll(test_days_l1);
		test_days_l2.add(CalendarUtil.create(2010, DateTimeConstants.JANUARY, 3));
		
		test_days_l11.addAll(test_days);
		test_days_l11.add(CalendarUtil.create(2010, DateTimeConstants.JULY, 27));
		test_days_l11.add(CalendarUtil.create(2010, DateTimeConstants.JULY, 9));
		test_days_l11.add(CalendarUtil.create(2010, DateTimeConstants.FEBRUARY, 26));
		test_days_l11.add(CalendarUtil.create(2010, DateTimeConstants.AUGUST, 11));
		test_days_l11.add(CalendarUtil.create(2010, DateTimeConstants.SEPTEMBER, 6));
		test_days_l11.add(CalendarUtil.create(2010, DateTimeConstants.SEPTEMBER, 10));
		test_days_l11.add(CalendarUtil.create(2010, DateTimeConstants.NOVEMBER, 17));
		test_days_l11.add(CalendarUtil.create(2010, DateTimeConstants.DECEMBER, 8));
		test_days_l11.add(CalendarUtil.create(2010, DateTimeConstants.DECEMBER, 17));
	}
	
	@Before
	public void init(){
		System.setProperty("de.jollyday.config", "./src/test/resources/test.app.properties");
	}
	
	@After
	public void destroy(){
		System.clearProperty("de.jollyday.config");
	}

	@Test(expected=IllegalArgumentException.class)
	public void testMissingCountry() throws Exception{
		try{
			HolidayManager.getInstance("XXX");
			fail("Expected some IllegalArgumentException for this missing country.");
		}catch(IllegalArgumentException e){}
	}
	
	@Test
	public void testBaseStructure() throws Exception{
		HolidayManager m = HolidayManager.getInstance("test");
		CalendarHierarchy h = m.getCalendarHierarchy();
		Assert.assertEquals("Wrong id.", "test", h.getId());
		Assert.assertEquals("Wrong number of children on first level.", 2, h.getChildren().size());
		for(CalendarHierarchy hi : h.getChildren().values()){
			if(hi.getId().equalsIgnoreCase("level1")){
				Assert.assertEquals("Wrong number of children on second level of level 1.", 1, hi.getChildren().size());
			}else if(hi.getId().equalsIgnoreCase("level11")){
				Assert.assertEquals("Wrong number of children on second level of level 11.", 0, hi.getChildren().size());
			}
		}
	}
	
	@Test
	public void testIsHolidayPerformance() throws Exception{
		LocalDate date = CalendarUtil.create(2010, 1, 1);
		int count = 0;
		long sumDuration = 0;
		while(date.getYear() < 2011){
			long start = System.currentTimeMillis();
			HolidayManager m = HolidayManager.getInstance("test");
			m.isHoliday(date);
			long duration = System.currentTimeMillis() - start;
			LOG.log(Level.INFO, "isHoliday took "+duration+" millis.");
			date = date.plusDays(1);
			duration = System.currentTimeMillis() - start;
			count++;
			sumDuration += duration;
		}
		LOG.log(Level.INFO, "isHoliday took "+sumDuration/count+" millis average.");
	}

	@Test
	public void testCalendarChronology() throws Exception{
		HolidayManager m = HolidayManager.getInstance("test");
		Calendar c = Calendar.getInstance();
		c.set(Calendar.YEAR, 2010);
		c.set(Calendar.MONTH, Calendar.FEBRUARY);
		c.set(Calendar.DAY_OF_MONTH, 16);
		Assert.assertFalse("This date should NOT be a doliday.", m.isHoliday(c));
		c.add(Calendar.DAY_OF_YEAR, 1);
		Assert.assertTrue("This date should be a doliday.", m.isHoliday(c));
	}
	
	@Test
	public void testChronology() throws Exception{
		HolidayManager m = HolidayManager.getInstance("test");
		Set<Holiday> holidays = m.getHolidays(2010);
		for(Holiday d : holidays){
			Assert.assertEquals("Wrong chronology.", GregorianChronology.getInstance(DateTimeZone.UTC), d.getDate().getChronology());
		}
	}

	@Test
	public void testBaseDates() throws Exception{
		HolidayManager m = HolidayManager.getInstance("test");
		Set<Holiday> holidays = m.getHolidays(2010);
		Assert.assertNotNull(holidays);
		Assert.assertEquals("Wrong number of dates.", test_days.size(), holidays.size());
		assertDates(test_days, holidays);
	}

	private void assertDates(Set<LocalDate> dates, Set<Holiday> holidays) {
		for(LocalDate d : dates){
			if(!CalendarUtil.contains(holidays, d)){
				fail("Missing "+d+" in "+holidays);
			}
		}
	}

	@Test
	public void testLevel1() throws Exception{
		HolidayManager m = HolidayManager.getInstance("test");
		Set<Holiday> holidays = m.getHolidays(2010, "level1");
		Assert.assertNotNull(holidays);
		Assert.assertEquals("Wrong number of dates.", test_days_l1.size(), holidays.size());
		assertDates(test_days_l1, holidays);
	}

	@Test
	public void testLevel2() throws Exception{
		HolidayManager m = HolidayManager.getInstance("test");
		Set<Holiday> holidays = m.getHolidays(2010, "level1", "level2");
		Assert.assertNotNull(holidays);
		Assert.assertEquals("Wrong number of dates.", test_days_l2.size(), holidays.size());
		assertDates(test_days_l2, holidays);
	}

	@Test
	public void testLevel11() throws Exception{
		HolidayManager m = HolidayManager.getInstance("test");
		Set<Holiday> holidays = m.getHolidays(2010, "level11");
		Assert.assertNotNull(holidays);
		assertDates(test_days_l11, holidays);

	}

	@Test(expected=IllegalArgumentException.class)
	public void testFail() throws Exception{
		try{
			HolidayManager.getInstance("test_fail");
			fail("Should have thrown an IllegalArgumentException.");
		}catch(IllegalArgumentException e){}
	}
	
	@Test
	public void testAllAvailableManagers() throws Exception{
		Set<String> supportedCountryCodes = HolidayManager.getSupportedCountryCodes();
		Assert.assertNotNull(supportedCountryCodes);
		Assert.assertFalse(supportedCountryCodes.isEmpty());
		for(String country : supportedCountryCodes){
			HolidayManager manager = HolidayManager.getInstance(country);
			Assert.assertNotNull("Manager for country "+country+" is NULL.", manager);
		}
	}
	
	
}
