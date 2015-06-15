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
package de.jollyday.impl;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import de.jollyday.Holiday;
import de.jollyday.HolidayType;

/**
 * <p>
 * JapaneseHolidayManager class.
 * </p>
 *
 * @author Sven
 * @version $Id: $
 */
public class JapaneseHolidayManager extends DefaultHolidayManager {

	/**
	 * The properties key for japanese bridging holidays.
	 */
	private static final String BRIDGING_HOLIDAY_PROPERTIES_KEY = "BRIDGING_HOLIDAY";

	/**
	 * {@inheritDoc}
	 *
	 * Implements the rule which requests if two holidays have one non holiday
	 * between each other than this day is also a holiday.
	 */
	@Override
	public Set<Holiday> getHolidays(int year, final String... args) {
		Set<Holiday> holidays = super.getHolidays(year, args);
		Set<Holiday> additionalHolidays = new HashSet<>();
		for (Holiday d : holidays) {
			LocalDate twoDaysLater = d.getDate().plusDays(2);
			if (calendarUtil.contains(holidays, twoDaysLater)) {
				LocalDate bridgingDate = twoDaysLater.minusDays(1);
				additionalHolidays.add(new Holiday(bridgingDate, BRIDGING_HOLIDAY_PROPERTIES_KEY,
						HolidayType.OFFICIAL_HOLIDAY));
			}
		}
		holidays.addAll(additionalHolidays);
		return holidays;
	}

}
