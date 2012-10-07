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

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import junit.framework.Assert;

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
import de.jollyday.holidaytype.LocalizedHolidayType;
import de.jollyday.util.CalendarUtil;

/**
 * @author Sven
 * 
 */
public class HolidayTest {

	private final static Logger LOG = Logger.getLogger(HolidayTest.class.getName());

	private static final Set<LocalDate> test_days = new HashSet<LocalDate>();
	private static final Set<LocalDate> test_days_l1 = new HashSet<LocalDate>();
	private static final Set<LocalDate> test_days_l2 = new HashSet<LocalDate>();
	private static final Set<LocalDate> test_days_l11 = new HashSet<LocalDate>();

	private static CalendarUtil calendarUtil = new CalendarUtil();

	static {
		test_days.add(calendarUtil.create(2010, DateTimeConstants.FEBRUARY, 17));
		test_days.add(calendarUtil.create(2010, DateTimeConstants.AUGUST, 30));
		test_days.add(calendarUtil.create(2010, DateTimeConstants.APRIL, 2));
		test_days.add(calendarUtil.create(2010, DateTimeConstants.APRIL, 5));
		test_days.add(calendarUtil.create(2010, DateTimeConstants.NOVEMBER, 17));
		test_days.add(calendarUtil.create(2010, DateTimeConstants.NOVEMBER, 28));
		test_days.add(calendarUtil.create(2010, DateTimeConstants.JANUARY, 1));
		test_days.add(calendarUtil.create(2010, DateTimeConstants.JANUARY, 18));
		test_days.add(calendarUtil.create(2010, DateTimeConstants.NOVEMBER, 26));
		test_days_l1.addAll(test_days);
		test_days_l1.add(calendarUtil.create(2010, DateTimeConstants.JANUARY, 2));
		test_days_l2.addAll(test_days_l1);
		test_days_l2.add(calendarUtil.create(2010, DateTimeConstants.JANUARY, 3));

		test_days_l11.addAll(test_days);
		test_days_l11.add(calendarUtil.create(2010, DateTimeConstants.JULY, 27));
		test_days_l11.add(calendarUtil.create(2010, DateTimeConstants.JULY, 9));
		test_days_l11.add(calendarUtil.create(2010, DateTimeConstants.FEBRUARY, 26));
		test_days_l11.add(calendarUtil.create(2010, DateTimeConstants.AUGUST, 11));
		test_days_l11.add(calendarUtil.create(2010, DateTimeConstants.SEPTEMBER, 6));
		test_days_l11.add(calendarUtil.create(2010, DateTimeConstants.SEPTEMBER, 10));
		test_days_l11.add(calendarUtil.create(2010, DateTimeConstants.NOVEMBER, 17));
		test_days_l11.add(calendarUtil.create(2010, DateTimeConstants.DECEMBER, 8));
		test_days_l11.add(calendarUtil.create(2010, DateTimeConstants.DECEMBER, 17));
	}

	private Locale defaultLocale;

	@Before
	public void init() {
		System.setProperty("de.jollyday.config", "./src/test/resources/test.app.properties");
		defaultLocale = Locale.getDefault();
		Locale.setDefault(Locale.GERMAN);
	}

	@After
	public void destroy() {
		Locale.setDefault(defaultLocale);
		System.clearProperty("de.jollyday.config");
	}

	@Test(expected = IllegalStateException.class)
	public void testMissingCountry() throws Exception {
		HolidayManager.getInstance("XXX");
	}

	@Test
	public void testMissingCountryWithAbsoluteFilePath() {
		URL filePathURL = getClass().getResource("/holidays/Holidays_test_al_2010.xml");
		HolidayManager.getInstance(filePathURL);
	}

	@Test
	public void testMissingCountryWithRelativeFilePath() throws MalformedURLException {
		String filePathURL = getClass().getResource("/holidays/Holidays_test_al_2010.xml").toString();
		String filePath = filePathURL.substring("file:".length());
		String currentRunningLocation = System.getProperty("user.dir");
		URL relativeFilePath = new URL("file:./" + filePath.substring(currentRunningLocation.length()));
		HolidayManager.getInstance(relativeFilePath);
	}

	@Test
	public void testMissingCountryWithURL() {
		URL filePathURL = getClass().getResource("/holidays/Holidays_test_al_2010.xml");
		HolidayManager.getInstance(filePathURL);
	}

	@Test(expected = IllegalStateException.class)
	public void testMissingCountryWithNonExistingURL() throws Exception {
		URL filePathURL = new URL("file:///non-existing-file");
		HolidayManager.getInstance(filePathURL);
	}

	@Test
	public void testBaseStructure() throws Exception {
		HolidayManager m = HolidayManager.getInstance("test");
		CalendarHierarchy h = m.getCalendarHierarchy();
		Assert.assertEquals("Wrong id.", "test", h.getId());
		Assert.assertEquals("Wrong number of children on first level.", 2, h.getChildren().size());
		for (CalendarHierarchy hi : h.getChildren().values()) {
			if (hi.getId().equalsIgnoreCase("level1")) {
				Assert.assertEquals("Wrong number of children on second level of level 1.", 1, hi.getChildren().size());
			} else if (hi.getId().equalsIgnoreCase("level11")) {
				Assert.assertEquals("Wrong number of children on second level of level 11.", 0, hi.getChildren().size());
			}
		}
	}

	@Test
	public void testIsHolidayPerformance() throws Exception {
		LocalDate date = calendarUtil.create(2010, 1, 1);
		int count = 0;
		long sumDuration = 0;
		while (date.getYear() < 2011) {
			long start = System.currentTimeMillis();
			HolidayManager m = HolidayManager.getInstance("test");
			m.isHoliday(date);
			long duration = System.currentTimeMillis() - start;
			date = date.plusDays(1);
			duration = System.currentTimeMillis() - start;
			count++;
			sumDuration += duration;
		}
		LOG.log(Level.INFO, "isHoliday took " + sumDuration / count + " millis average.");
	}

	@Test
	public void testCalendarChronology() throws Exception {
		HolidayManager m = HolidayManager.getInstance("test");
		Calendar c = Calendar.getInstance();
		c.set(Calendar.YEAR, 2010);
		c.set(Calendar.MONTH, Calendar.FEBRUARY);
		c.set(Calendar.DAY_OF_MONTH, 16);
		Assert.assertFalse("This date should NOT be a holiday.", m.isHoliday(c));
		c.add(Calendar.DAY_OF_YEAR, 1);
		Assert.assertTrue("This date should be a holiday.", m.isHoliday(c));
	}

	@Test
	public void testChronology() throws Exception {
		GregorianChronology gregorianChronoUTC = GregorianChronology.getInstance(DateTimeZone.UTC);
		HolidayManager m = HolidayManager.getInstance("test");
		Set<Holiday> holidays = m.getHolidays(2010);
		for (Holiday d : holidays) {
			Assert.assertEquals("Wrong chronology.", gregorianChronoUTC, d.getDate().getChronology());
		}
	}

	@Test
	public void testBaseDates() throws Exception {
		HolidayManager m = HolidayManager.getInstance("test");
		Set<Holiday> holidays = m.getHolidays(2010);
		Assert.assertNotNull(holidays);
		assertDates(test_days, holidays);
	}

	private void assertDates(Set<LocalDate> expected, Set<Holiday> holidays) {
		Assert.assertEquals("Wrong number of dates.", expected.size(), holidays.size());
		for (LocalDate d : expected) {
			if (!calendarUtil.contains(holidays, d)) {
				Assert.fail("Missing " + d + " in " + holidays);
			}
		}
	}

	@Test
	public void testLevel1() throws Exception {
		HolidayManager m = HolidayManager.getInstance("test");
		Set<Holiday> holidays = m.getHolidays(2010, "level1");
		Assert.assertNotNull(holidays);
		assertDates(test_days_l1, holidays);
	}

	@Test
	public void testLevel2() throws Exception {
		HolidayManager m = HolidayManager.getInstance("test");
		Set<Holiday> holidays = m.getHolidays(2010, "level1", "level2");
		Assert.assertNotNull(holidays);
		assertDates(test_days_l2, holidays);
	}

	@Test
	public void testLevel11() throws Exception {
		HolidayManager m = HolidayManager.getInstance("test");
		Set<Holiday> holidays = m.getHolidays(2010, "level11");
		Assert.assertNotNull(holidays);
		assertDates(test_days_l11, holidays);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testFail() throws Exception {
		HolidayManager.getInstance("test_fail");
	}

	@Test
	public void testAllAvailableManagers() throws Exception {
		Set<String> supportedCalendarCodes = HolidayManager.getSupportedCalendarCodes();
		Assert.assertNotNull(supportedCalendarCodes);
		Assert.assertFalse(supportedCalendarCodes.isEmpty());
		for (String calendar : supportedCalendarCodes) {
			HolidayManager manager = HolidayManager.getInstance(calendar);
			Assert.assertNotNull("Manager for calendar " + calendar + " is NULL.", manager);
		}
	}

	@Test
	public void testHolidayDescription() {
		Holiday h = new Holiday(calendarUtil.create(2011, 2, 2), "CHRISTMAS", LocalizedHolidayType.OFFICIAL_HOLIDAY);
		Assert.assertEquals("Wrong description", "Weihnachten", h.getDescription());
		Assert.assertEquals("Wrong description", "Christmas", h.getDescription(Locale.ENGLISH));
		Assert.assertEquals("Wrong description", "Kerstmis", h.getDescription(new Locale("nl")));
	}

	@Test
	public void testHolidayEquals() {
		Holiday h1 = new Holiday(calendarUtil.create(2011, 2, 2), "CHRISTMAS", LocalizedHolidayType.OFFICIAL_HOLIDAY);
		Assert.assertTrue("Wrong equals implementation", h1.equals(h1));
		Holiday h2b = new Holiday(calendarUtil.create(2011, 2, 2), new String("CHRISTMAS"),
				LocalizedHolidayType.OFFICIAL_HOLIDAY);
		Assert.assertTrue("Wrong equals implementation", h1.equals(h2b));
		Holiday h2 = new Holiday(calendarUtil.create(2011, 2, 1), "CHRISTMAS", LocalizedHolidayType.OFFICIAL_HOLIDAY);
		Assert.assertFalse("Wrong equals implementation", h1.equals(h2));
		Holiday h3 = new Holiday(calendarUtil.create(2011, 2, 2), "NEW_YEAR", LocalizedHolidayType.OFFICIAL_HOLIDAY);
		Assert.assertFalse("Wrong equals implementation", h1.equals(h3));
		Holiday h4 = new Holiday(calendarUtil.create(2011, 2, 2), "CHRISTMAS", LocalizedHolidayType.UNOFFICIAL_HOLIDAY);
		Assert.assertFalse("Wrong equals implementation", h1.equals(h4));
	}

}
