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

import de.jollyday.CalendarHierarchy;
import de.jollyday.HolidayManager;
import de.jollyday.ManagerParameters;
import de.jollyday.tests.base.AbstractCountryTestBase;
import org.junit.Test;

public class HolidayAUTest extends AbstractCountryTestBase {

	private static final String ISO_CODE = "au";

	@Test
	public void testManagerAUStructure2019BeforeUpdate() throws Exception {
		validateCalendarData(ISO_CODE, 2019);
	}

	@Test
	public void testManagerAUStructure2020() throws Exception {
		validateCalendarData(ISO_CODE, 2020, true);
	}

	@Test
	public void testManagerAUStructure2021() throws Exception {
		validateCalendarData(ISO_CODE, 2021, true);
	}

	@Test
	public void testManagerAUStructure2022() throws Exception {
		validateCalendarData(ISO_CODE, 2022, true);
	}

	@Test
	public void testManagerAULoadFromUrl() {
		HolidayManager calendarPartLoaded = HolidayManager.getInstance(ManagerParameters.create("test_au_2020"));
		HolidayManager urlLoaded = HolidayManager.getInstance(
				ManagerParameters.create(AbstractCountryTestBase.class.getClassLoader().getResource("holidays/Holidays_test_au_2020.xml"))
		);

		CalendarHierarchy dataHierarchy = calendarPartLoaded.getCalendarHierarchy();
		CalendarHierarchy testHierarchy = urlLoaded.getCalendarHierarchy();

		compareHierarchies(testHierarchy, dataHierarchy);
		compareData(urlLoaded, calendarPartLoaded, 2020, true);
	}
}
