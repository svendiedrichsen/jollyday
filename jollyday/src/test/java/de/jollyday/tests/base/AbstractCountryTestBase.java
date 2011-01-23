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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.junit.Assert;

import de.jollyday.CalendarHierarchy;
import de.jollyday.Holiday;
import de.jollyday.HolidayManager;
import de.jollyday.util.CalendarUtil;

/**
 * @author Sven
 * 
 */
public abstract class AbstractCountryTestBase {

	/**
	 * Compares two hierarchy structure by traversing down.
	 * 
	 * @param expected
	 *            This is the test structure which is how it should be.
	 * @param found
	 *            This is the real live data structure.
	 */
	protected void compareHierarchies(CalendarHierarchy expected,
			CalendarHierarchy found) {
		Assert.assertNotNull("Null description", found.getDescription());
		Assert.assertEquals("Wrong hierarchy id.", expected.getId(),
				found.getId());
		Assert.assertEquals("Number of children wrong.", expected.getChildren()
				.size(), found.getChildren().size());
		for (String id : expected.getChildren().keySet()) {
			Assert.assertTrue("Missing " + id + " within " + found.getId(),
					found.getChildren().containsKey(id));
			compareHierarchies(expected.getChildren().get(id), found
					.getChildren().get(id));
		}
	}

	/**
	 * @param testManager
	 * @param m
	 */
	protected void compareData(HolidayManager expected, HolidayManager found,
			int year) {
		CalendarHierarchy expectedHierarchy = expected.getCalendarHierarchy();
		ArrayList<String> args = new ArrayList<String>();
		compareDates(expected, found, expectedHierarchy, args, year);
	}

	private void compareDates(HolidayManager expected, HolidayManager found,
			CalendarHierarchy h, List<String> args, int year) {
		Set<Holiday> expectedHolidays = expected.getHolidays(year,
				args.toArray(new String[] {}));
		Set<Holiday> foundHolidays = found.getHolidays(year,
				args.toArray(new String[] {}));
		for (Holiday expectedHoliday : expectedHolidays) {
			Assert.assertNotNull("Description is null.",
					expectedHoliday.getDescription());
			if (!CalendarUtil
					.contains(foundHolidays, expectedHoliday.getDate())) {
				Assert.fail("Could not find " + expectedHoliday + " in "
						+ h.getDescription() + " - " + foundHolidays);
			}
		}
		for (String id : h.getChildren().keySet()) {
			ArrayList<String> newArgs = new ArrayList<String>(args);
			newArgs.add(id);
			compareDates(expected, found, h.getChildren().get(id), newArgs,
					year);
		}
	}

	protected void validateCalendarData(String countryCode, int year)
			throws Exception {
		HolidayManager dataManager = HolidayManager.getInstance(countryCode);
		HolidayManager testManager = HolidayManager.getInstance("test_"
				+ countryCode + "_" + Integer.toString(year));

		CalendarHierarchy dataHierarchy = dataManager.getCalendarHierarchy();
		CalendarHierarchy testHierarchy = testManager.getCalendarHierarchy();

		compareHierarchies(testHierarchy, dataHierarchy);
		compareData(testManager, dataManager, year);
	}

}
