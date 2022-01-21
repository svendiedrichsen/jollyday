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

import de.jollyday.*;
import de.jollyday.util.CalendarUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.time.Month.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Sven
 *
 */
public class HolidayTest {

	private final static Logger LOG = Logger.getLogger(HolidayTest.class
			.getName());

	private static final Set<LocalDate> test_days = new HashSet<>();
	private static final Set<LocalDate> test_days_l1 = new HashSet<>();
	private static final Set<LocalDate> test_days_l2 = new HashSet<>();
	private static final Set<LocalDate> test_days_l11 = new HashSet<>();

	private static CalendarUtil calendarUtil = new CalendarUtil();

	static {
		test_days
				.add(LocalDate.of(2010, FEBRUARY, 17));
		test_days.add(LocalDate.of(2010, AUGUST, 30));
		test_days.add(LocalDate.of(2010, APRIL, 2));
		test_days.add(LocalDate.of(2010, APRIL, 5));
		test_days
				.add(LocalDate.of(2010, NOVEMBER, 17));
		test_days
				.add(LocalDate.of(2010, NOVEMBER, 28));
		test_days.add(LocalDate.of(2010, JANUARY, 1));
		test_days.add(LocalDate.of(2010, JANUARY, 18));
		test_days
				.add(LocalDate.of(2010, NOVEMBER, 26));
		test_days_l1.addAll(test_days);
		test_days_l1.add(LocalDate.of(2010, JANUARY, 2));
		test_days_l2.addAll(test_days_l1);
		test_days_l2.add(LocalDate.of(2010, JANUARY, 3));

		test_days_l11.addAll(test_days);
		test_days_l11
				.add(LocalDate.of(2010, JULY, 27));
		test_days_l11.add(LocalDate.of(2010, JULY, 9));
		test_days_l11.add(LocalDate.of(2010, FEBRUARY,
				26));
		test_days_l11.add(LocalDate.of(2010, AUGUST,
				11));
		test_days_l11.add(LocalDate.of(2010,
				SEPTEMBER, 6));
		test_days_l11.add(LocalDate.of(2010,
				SEPTEMBER, 10));
		test_days_l11.add(LocalDate.of(2010, NOVEMBER,
				17));
		test_days_l11.add(LocalDate.of(2010, DECEMBER,
				7));
		test_days_l11.add(LocalDate.of(2010, DECEMBER,
				16));
	}

	private Locale defaultLocale;

	@BeforeEach
	public void init() {
		System.setProperty("de.jollyday.config.urls",
				"file:./src/test/resources/test.app.properties");
		defaultLocale = Locale.getDefault();
		Locale.setDefault(Locale.GERMAN);
	}

	@AfterEach
	public void destroy() {
		Locale.setDefault(defaultLocale);
		System.clearProperty("de.jollyday.config.urls");
	}

	@Test
	public void testMissingCountry() throws Exception {
		assertThrows(IllegalStateException.class, () -> HolidayManager.getInstance("XXX"));
	}

	@Test
	public void testBaseStructure() throws Exception {
		HolidayManager m = HolidayManager.getInstance("test");
		CalendarHierarchy h = m.getCalendarHierarchy();
		assertEquals("test", h.getId(), "Wrong id.");
		assertEquals( 2, h.getChildren().size(), "Wrong number of children on first level.");
		for (CalendarHierarchy hi : h.getChildren().values()) {
			if (hi.getId().equalsIgnoreCase("level1")) {
				assertEquals(1, hi.getChildren().size(), "Wrong number of children on second level of level 1.");
			} else if (hi.getId().equalsIgnoreCase("level11")) {
				assertEquals(0, hi.getChildren().size(), "Wrong number of children on second level of level 11.");
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
		assertFalse("undefined".equals(c.getDescription()),
				"Calendar " + calendar + " Hierarchy " + c.getId() + " is lacking a description.");
		for (Map.Entry<String, CalendarHierarchy> element : c.getChildren()
				.entrySet()) {
			assertNotUndefined(calendar, element.getValue());
		}
	}

	@Test
	public void testIsHolidayPerformanceMultithreaded() throws Exception {
		LocalDate date = LocalDate.of(2010, 1, 1);
		final AtomicLong count = new AtomicLong(0);
		final AtomicLong sumDuration = new AtomicLong(0);
		ExecutorService executorService = Executors.newCachedThreadPool();
		while (date.getYear() < 2013) {
			final LocalDate localDate = date;
			executorService.submit(() -> {
        long start = System.currentTimeMillis();
        HolidayManager m = HolidayManager.getInstance("test");
        m.isHoliday(localDate);
        long duration = System.currentTimeMillis() - start;
        count.incrementAndGet();
        sumDuration.addAndGet(duration);
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
		assertFalse(m.isHoliday(c), "This date should NOT be a holiday.");
		c.add(Calendar.DAY_OF_YEAR, 1);
		assertTrue(m.isHoliday(c), "This date should be a holiday.");
	}

	@Test
	public void testBaseDates() throws Exception {
		HolidayManager m = HolidayManager.getInstance("test");
		Set<Holiday> holidays = m.getHolidays(2010);
		assertNotNull(holidays);
		assertDates(test_days, holidays);
	}

	private void assertDates(Set<LocalDate> expected, Set<Holiday> holidays) {
		assertEquals(expected.size(),
				holidays.size(),
				"Wrong number of dates.");
		for (LocalDate d : expected) {
			if (!calendarUtil.contains(holidays, d)) {
				fail("Missing " + d + " in " + holidays);
			}
		}
	}

	@Test
	public void testLevel1() throws Exception {
		HolidayManager m = HolidayManager.getInstance("test");
		Set<Holiday> holidays = m.getHolidays(2010, "level1");
		assertNotNull(holidays);
		assertDates(test_days_l1, holidays);
	}

	@Test
	public void testLevel2() throws Exception {
		HolidayManager m = HolidayManager.getInstance("test");
		Set<Holiday> holidays = m.getHolidays(2010, "level1", "level2");
		assertNotNull(holidays);
		assertDates(test_days_l2, holidays);
	}

	@Test
	public void testLevel11() throws Exception {
		HolidayManager m = HolidayManager.getInstance("test");
		Set<Holiday> holidays = m.getHolidays(2010, "level11");
		assertNotNull(holidays);
		assertDates(test_days_l11, holidays);
	}

	@Test
	public void testFail() throws Exception {
		assertThrows(IllegalArgumentException.class, () -> HolidayManager.getInstance("test_fail"));
	}

	@Test
	public void testAllAvailableManagers() throws Exception {
		Set<String> supportedCalendarCodes = HolidayManager
				.getSupportedCalendarCodes();
		assertNotNull(supportedCalendarCodes);
		assertFalse(supportedCalendarCodes.isEmpty());
		for (String calendar : supportedCalendarCodes) {
			HolidayManager manager = HolidayManager.getInstance(calendar);
			assertNotNull(manager, "Manager for calendar " + calendar + " is NULL.");
		}
	}

	@Test
	public void testHolidayDescription() {
		Holiday h = new Holiday(LocalDate.of(2011, 2, 2), "CHRISTMAS",
				HolidayType.OFFICIAL_HOLIDAY);
		assertEquals("Weihnachten",
				h.getDescription(),
				"Wrong description");
		assertEquals("Christmas",
				h.getDescription(Locale.ENGLISH),
				"Wrong description");
		assertEquals("Kerstmis",
				h.getDescription(new Locale("nl")),
				"Wrong description");
	}

	@Test
	public void testHolidayEquals() {
		Holiday h1 = new Holiday(LocalDate.of(2011, 2, 2), "CHRISTMAS",
				HolidayType.OFFICIAL_HOLIDAY);
		assertTrue(h1.equals(h1), "Wrong equals implementation");
		Holiday h2b = new Holiday(LocalDate.of(2011, 2, 2), "CHRISTMAS",
				HolidayType.OFFICIAL_HOLIDAY);
		assertTrue(h1.equals(h2b), "Wrong equals implementation");
		Holiday h2 = new Holiday(LocalDate.of(2011, 2, 1), "CHRISTMAS",
				HolidayType.OFFICIAL_HOLIDAY);
		assertFalse(h1.equals(h2), "Wrong equals implementation");
		Holiday h3 = new Holiday(LocalDate.of(2011, 2, 2), "NEW_YEAR",
				HolidayType.OFFICIAL_HOLIDAY);
		assertFalse(h1.equals(h3), "Wrong equals implementation");
		Holiday h4 = new Holiday(LocalDate.of(2011, 2, 2), "CHRISTMAS",
				HolidayType.UNOFFICIAL_HOLIDAY);
		assertFalse(h1.equals(h4), "Wrong equals implementation");
	}

}
