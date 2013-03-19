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

import static org.junit.Assert.assertFalse;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Level;
import java.util.logging.Logger;

import junit.framework.Assert;

import org.joda.time.DateTimeConstants;
import org.joda.time.LocalDate;
import org.joda.time.chrono.ISOChronology;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.jollyday.CalendarHierarchy;
import de.jollyday.Holiday;
import de.jollyday.HolidayCalendar;
import de.jollyday.HolidayManager;
import de.jollyday.holidaytype.LocalizedHolidayType;
import de.jollyday.util.CalendarUtil;

/**
 * @author Sven
 * 
 */
public class HolidayTest {

	private final static Logger LOG = Logger.getLogger(HolidayTest.class
			.getName());

	private static final Set<LocalDate> test_days = new HashSet<LocalDate>();
	private static final Set<LocalDate> test_days_l1 = new HashSet<LocalDate>();
	private static final Set<LocalDate> test_days_l2 = new HashSet<LocalDate>();
	private static final Set<LocalDate> test_days_l11 = new HashSet<LocalDate>();

	private static CalendarUtil calendarUtil = new CalendarUtil();

	static {
		test_days
				.add(calendarUtil.create(2010, DateTimeConstants.FEBRUARY, 17));
		test_days.add(calendarUtil.create(2010, DateTimeConstants.AUGUST, 30));
		test_days.add(calendarUtil.create(2010, DateTimeConstants.APRIL, 2));
		test_days.add(calendarUtil.create(2010, DateTimeConstants.APRIL, 5));
		test_days
				.add(calendarUtil.create(2010, DateTimeConstants.NOVEMBER, 17));
		test_days
				.add(calendarUtil.create(2010, DateTimeConstants.NOVEMBER, 28));
		test_days.add(calendarUtil.create(2010, DateTimeConstants.JANUARY, 1));
		test_days.add(calendarUtil.create(2010, DateTimeConstants.JANUARY, 18));
		test_days
				.add(calendarUtil.create(2010, DateTimeConstants.NOVEMBER, 26));
		test_days_l1.addAll(test_days);
		test_days_l1.add(calendarUtil
				.create(2010, DateTimeConstants.JANUARY, 2));
		test_days_l2.addAll(test_days_l1);
		test_days_l2.add(calendarUtil
				.create(2010, DateTimeConstants.JANUARY, 3));

		test_days_l11.addAll(test_days);
		test_days_l11
				.add(calendarUtil.create(2010, DateTimeConstants.JULY, 27));
		test_days_l11.add(calendarUtil.create(2010, DateTimeConstants.JULY, 9));
		test_days_l11.add(calendarUtil.create(2010, DateTimeConstants.FEBRUARY,
				26));
		test_days_l11.add(calendarUtil.create(2010, DateTimeConstants.AUGUST,
				11));
		test_days_l11.add(calendarUtil.create(2010,
				DateTimeConstants.SEPTEMBER, 6));
		test_days_l11.add(calendarUtil.create(2010,
				DateTimeConstants.SEPTEMBER, 10));
		test_days_l11.add(calendarUtil.create(2010, DateTimeConstants.NOVEMBER,
				17));
		test_days_l11.add(calendarUtil.create(2010, DateTimeConstants.DECEMBER,
				8));
		test_days_l11.add(calendarUtil.create(2010, DateTimeConstants.DECEMBER,
				17));
	}

	private Locale defaultLocale;

	@Before
	public void init() {
		System.setProperty("de.jollyday.config.urls",
				"file:./src/test/resources/test.app.properties");
		defaultLocale = Locale.getDefault();
		Locale.setDefault(Locale.GERMAN);
	}

	@After
	public void destroy() {
		Locale.setDefault(defaultLocale);
		System.clearProperty("de.jollyday.config.urls");
	}

	@Test(expected = IllegalStateException.class)
	public void testMissingCountry() throws Exception {
		HolidayManager.getInstance("XXX");
	}

	@Test
	public void testBaseStructure() throws Exception {
		HolidayManager m = HolidayManager.getInstance("test");
		CalendarHierarchy h = m.getCalendarHierarchy();
		Assert.assertEquals("Wrong id.", "test", h.getId());
		Assert.assertEquals("Wrong number of children on first level.", 2, h
				.getChildren().size());
		for (CalendarHierarchy hi : h.getChildren().values()) {
			if (hi.getId().equalsIgnoreCase("level1")) {
				Assert.assertEquals(
						"Wrong number of children on second level of level 1.",
						1, hi.getChildren().size());
			} else if (hi.getId().equalsIgnoreCase("level11")) {
				Assert.assertEquals(
						"Wrong number of children on second level of level 11.",
						0, hi.getChildren().size());
			}
		}
	}

	@Test
	public void testHierarchyDescriptionsDefined() {
		for (HolidayCalendar c : HolidayCalendar.values()) {
			HolidayManager m = HolidayManager.getInstance(c);
			assertNotUndefined(c, m.getCalendarHierarchy());
		}
	}

	private void assertNotUndefined(HolidayCalendar calendar,
			CalendarHierarchy c) {
		assertFalse("Calendar " + calendar + " Hierarchy " + c.getId()
				+ " is lacking a description.",
				"undefined".equals(c.getDescription()));
		for (Map.Entry<String, CalendarHierarchy> element : c.getChildren()
				.entrySet()) {
			assertNotUndefined(calendar, element.getValue());
		}
	}

	@Test
	public void testIsHolidayPerformanceMultithreaded() throws Exception {
		LocalDate date = calendarUtil.create(2010, 1, 1);
		final AtomicLong count = new AtomicLong(0);
		final AtomicLong sumDuration = new AtomicLong(0);
		ExecutorService executorService = Executors.newCachedThreadPool();
		while (date.getYear() < 2013) {
			final LocalDate localDate = date;
			executorService.submit(new Runnable() {
				public void run() {
					long start = System.currentTimeMillis();
					HolidayManager m = HolidayManager.getInstance("test");
					m.isHoliday(localDate);
					long duration = System.currentTimeMillis() - start;
					duration = System.currentTimeMillis() - start;
					count.incrementAndGet();
					sumDuration.addAndGet(duration);
				}
			});
			date = date.plusDays(1);
		}
		executorService.shutdown();
		executorService.awaitTermination(5, TimeUnit.SECONDS);
		LOG.log(Level.INFO,
				"isHoliday took " + sumDuration.doubleValue()
						/ count.doubleValue() + " millis average tested with "
						+ count.longValue() + " calls.");
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
		ISOChronology isoChrono = ISOChronology.getInstanceUTC();
		HolidayManager m = HolidayManager.getInstance("test");
		Set<Holiday> holidays = m.getHolidays(2010);
		for (Holiday d : holidays) {
			Assert.assertEquals("Wrong chronology.", isoChrono, d.getDate()
					.getChronology());
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
		Assert.assertEquals("Wrong number of dates.", expected.size(),
				holidays.size());
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
		Set<String> supportedCalendarCodes = HolidayManager
				.getSupportedCalendarCodes();
		Assert.assertNotNull(supportedCalendarCodes);
		Assert.assertFalse(supportedCalendarCodes.isEmpty());
		for (String calendar : supportedCalendarCodes) {
			HolidayManager manager = HolidayManager.getInstance(calendar);
			Assert.assertNotNull("Manager for calendar " + calendar
					+ " is NULL.", manager);
		}
	}

	@Test
	public void testHolidayDescription() {
		Holiday h = new Holiday(calendarUtil.create(2011, 2, 2), "CHRISTMAS",
				LocalizedHolidayType.OFFICIAL_HOLIDAY);
		Assert.assertEquals("Wrong description", "Weihnachten",
				h.getDescription());
		Assert.assertEquals("Wrong description", "Christmas",
				h.getDescription(Locale.ENGLISH));
		Assert.assertEquals("Wrong description", "Kerstmis",
				h.getDescription(new Locale("nl")));
	}

	@Test
	public void testHolidayEquals() {
		Holiday h1 = new Holiday(calendarUtil.create(2011, 2, 2), "CHRISTMAS",
				LocalizedHolidayType.OFFICIAL_HOLIDAY);
		Assert.assertTrue("Wrong equals implementation", h1.equals(h1));
		Holiday h2b = new Holiday(calendarUtil.create(2011, 2, 2), new String(
				"CHRISTMAS"), LocalizedHolidayType.OFFICIAL_HOLIDAY);
		Assert.assertTrue("Wrong equals implementation", h1.equals(h2b));
		Holiday h2 = new Holiday(calendarUtil.create(2011, 2, 1), "CHRISTMAS",
				LocalizedHolidayType.OFFICIAL_HOLIDAY);
		Assert.assertFalse("Wrong equals implementation", h1.equals(h2));
		Holiday h3 = new Holiday(calendarUtil.create(2011, 2, 2), "NEW_YEAR",
				LocalizedHolidayType.OFFICIAL_HOLIDAY);
		Assert.assertFalse("Wrong equals implementation", h1.equals(h3));
		Holiday h4 = new Holiday(calendarUtil.create(2011, 2, 2), "CHRISTMAS",
				LocalizedHolidayType.UNOFFICIAL_HOLIDAY);
		Assert.assertFalse("Wrong equals implementation", h1.equals(h4));
	}

}
