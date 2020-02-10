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
package de.jollyday.tests.base;

import de.jollyday.CalendarHierarchy;
import de.jollyday.Holiday;
import de.jollyday.HolidayCalendar;
import de.jollyday.HolidayManager;
import de.jollyday.util.CalendarUtil;
import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.junit.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public abstract class AbstractCountryTestBase {

	private CalendarUtil calendarUtil = new CalendarUtil();

	/**
	 * Compares two hierarchy structure by traversing down.
	 *
	 * @param expected
	 *            This is the test structure which is how it should be.
	 * @param found
	 *            This is the real live data structure.
	 */
	protected void compareHierarchies(CalendarHierarchy expected, CalendarHierarchy found) {
		Assert.assertNotNull("Null description", found.getDescription());
		Assert.assertEquals("Wrong hierarchy id.", expected.getId(), found.getId());
		Assert.assertEquals("Number of children wrong.", expected.getChildren().size(), found.getChildren().size());
		for (String id : expected.getChildren().keySet()) {
			Assert.assertTrue("Missing " + id + " within " + found.getId(), found.getChildren().containsKey(id));
			compareHierarchies(expected.getChildren().get(id), found.getChildren().get(id));
		}
	}

	protected void compareData(HolidayManager expected, HolidayManager found, int year,
			boolean assertAllHolidaysChecked) {
		CalendarHierarchy expectedHierarchy = expected.getCalendarHierarchy();
		List<String> args = new ArrayList<>();
		compareDates(expected, found, expectedHierarchy, args, year, assertAllHolidaysChecked);
	}

	private void compareDates(HolidayManager expected, HolidayManager found, CalendarHierarchy h,
			final List<String> args, int year, boolean assertAllHolidaysChecked) {
		Set<Holiday> expectedHolidays = expected.getHolidays(year, args.toArray(new String[] {}));
		Set<Holiday> foundHolidays = found.getHolidays(year, args.toArray(new String[] {}));
		for (Holiday expectedHoliday : expectedHolidays) {
			Assert.assertNotNull("Description is null.", expectedHoliday.getDescription());
			if (!calendarUtil.contains(foundHolidays, expectedHoliday.getDate())) {
				Assert.fail("Could not find " + expectedHoliday + " in " + h.getDescription() + " - " + foundHolidays);
			}
		}

		if (assertAllHolidaysChecked) {
			foundHolidays.removeAll(expectedHolidays);
			MatcherAssert.assertThat(
					"Not all found holidays were expected. Leftover in " + found.getCalendarHierarchy().getDescription()
							+ args.toString() + " : " + foundHolidays, foundHolidays.isEmpty(), CoreMatchers.is(true));
		}

		for (String id : h.getChildren().keySet()) {
			ArrayList<String> newArgs = new ArrayList<>(args);
			newArgs.add(id);
			compareDates(expected, found, h.getChildren().get(id), newArgs, year, assertAllHolidaysChecked);
		}
	}

	protected void validateCalendarData(final String countryCode, int year) {
		validateCalendarData(countryCode, year, false);
	}

	protected void validateCalendarData(final String countryCode, int year, boolean assertAllHolidaysChecked) {
		HolidayManager dataManager = HolidayManager.getInstance(countryCode);
		HolidayManager testManager = HolidayManager.getInstance("test_" + countryCode + "_" + Integer.toString(year));

		CalendarHierarchy dataHierarchy = dataManager.getCalendarHierarchy();
		CalendarHierarchy testHierarchy = testManager.getCalendarHierarchy();

		compareHierarchies(testHierarchy, dataHierarchy);
		compareData(testManager, dataManager, year, assertAllHolidaysChecked);
	}

	/**
	 * Validate Country calendar and Default calendar is same if local default
	 * is set to country local
	 *
	 * @param countryLocale
	 * @param countryCalendar
	 *
	 */
	protected void validateManagerSameInstance(Locale countryLocale, HolidayCalendar countryCalendar) {
		Locale defaultLocale = Locale.getDefault();
		Locale.setDefault(countryLocale);
		try {
			HolidayManager defaultManager = HolidayManager.getInstance();
			HolidayManager countryManager = HolidayManager.getInstance(countryCalendar);
			Assert.assertEquals("Unexpected manager found", defaultManager, countryManager);
		} catch (Exception e) {
			Assert.fail("Unexpected error occurred: " + e.getClass().getName() + " - " + e.getMessage());
		} finally {
			Locale.setDefault(defaultLocale);
		}
	}

	protected void validateManagerDifferentInstance(HolidayCalendar countryCalendar) {
		Locale defaultLocale = Locale.getDefault();
		if (countryCalendar == HolidayCalendar.UNITED_STATES) {
			Locale.setDefault(Locale.FRANCE);
		} else {
			Locale.setDefault(Locale.US);
		}
		try {
			HolidayManager defaultManager = HolidayManager.getInstance();
			HolidayManager countryManager = HolidayManager.getInstance(countryCalendar);
			Assert.assertNotSame("Unexpected manager found", defaultManager, countryManager);
		} catch (Exception e) {
			Assert.fail("Unexpected error occurred: " + e.getClass().getName() + " - " + e.getMessage());
		} finally {
			Locale.setDefault(defaultLocale);
		}
	}

}
