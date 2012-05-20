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
 *
 * @author sven
 * @version $Id: $
 */
package de.jollyday.util;

import org.joda.time.DateTimeConstants;

import de.jollyday.HolidayType;
import de.jollyday.config.HolidayRule;
import de.jollyday.config.Month;
import de.jollyday.config.Weekday;
import de.jollyday.holidaytype.LocalizedHolidayType;

public class XMLUtil {

	/**
	 * the package name to search for the generated java classes.
	 */
	public static final String PACKAGE = "de.jollyday.config";

	/**
	 * Returns the <code>DateTimeConstants</code> value for the given weekday.
	 * 
	 * @param weekday
	 *            a {@link de.jollyday.config.Weekday} object.
	 * @return DateTimeConstants value.
	 */
	public static final int getWeekday(Weekday weekday) {
		Check.notNull(weekday, "Weekday");
		switch (weekday) {
		case MONDAY:
			return DateTimeConstants.MONDAY;
		case TUESDAY:
			return DateTimeConstants.TUESDAY;
		case WEDNESDAY:
			return DateTimeConstants.WEDNESDAY;
		case THURSDAY:
			return DateTimeConstants.THURSDAY;
		case FRIDAY:
			return DateTimeConstants.FRIDAY;
		case SATURDAY:
			return DateTimeConstants.SATURDAY;
		case SUNDAY:
			return DateTimeConstants.SUNDAY;
		default:
			throw new IllegalArgumentException("Unknown weekday " + weekday);
		}
	}

	/**
	 * Returns the {@link DateTimeConstants} value for the given month.
	 * 
	 * @param month
	 *            a {@link de.jollyday.config.Month} object.
	 * @return DateTimeConstants value.
	 */
	public static int getMonth(Month month) {
		Check.notNull(month, "Month");
		switch (month) {
		case JANUARY:
			return DateTimeConstants.JANUARY;
		case FEBRUARY:
			return DateTimeConstants.FEBRUARY;
		case MARCH:
			return DateTimeConstants.MARCH;
		case APRIL:
			return DateTimeConstants.APRIL;
		case MAY:
			return DateTimeConstants.MAY;
		case JUNE:
			return DateTimeConstants.JUNE;
		case JULY:
			return DateTimeConstants.JULY;
		case AUGUST:
			return DateTimeConstants.AUGUST;
		case SEPTEMBER:
			return DateTimeConstants.SEPTEMBER;
		case OCTOBER:
			return DateTimeConstants.OCTOBER;
		case NOVEMBER:
			return DateTimeConstants.NOVEMBER;
		case DECEMBER:
			return DateTimeConstants.DECEMBER;
		default:
			throw new IllegalArgumentException("Unknown month " + month);
		}
	}

	/**
	 * Gets the {@link HolidayType}.
	 * 
	 * @param type
	 *            the type of holiday in the config
	 * @return the type of holiday
	 */
	public static HolidayType getType(de.jollyday.config.HolidayType type) {
		Check.notNull(type, "HolidayType");
		switch (type) {
		case OFFICIAL_HOLIDAY:
			return LocalizedHolidayType.OFFICIAL_HOLIDAY;
		case UNOFFICIAL_HOLIDAY:
			return LocalizedHolidayType.UNOFFICIAL_HOLIDAY;
		default:
			throw new IllegalArgumentException("Unknown holiday type " + type);
		}
	}

	/**
	 * Shows if the year is valid for the current {@link HolidayRule}.
	 * 
	 * @param year
	 *            the year to inspect
	 * @return year is valid
	 */
	public static boolean isValidInYear(int year, HolidayRule holidayRule) {
		Check.notNull(holidayRule, "HolidayRule");
		boolean isInRange = (holidayRule.getValidFrom() == null || holidayRule.getValidFrom().intValue() <= year)
				&& (holidayRule.getValidTo() == null || holidayRule.getValidTo().intValue() >= year);
		if (isInRange && holidayRule.getValidFrom() != null && holidayRule.getEvery() != null) {
			int yearDifference = year - holidayRule.getValidFrom().intValue();
			switch (holidayRule.getEvery()) {
			case EVERY_YEAR:
				return true;
			case TWO_YEARS:
				return yearDifference % 2 == 0;
			case THREE_YEARS:
				return yearDifference % 3 == 0;
			case FOUR_YEARS:
				return yearDifference % 4 == 0;
			case FIVE_YEARS:
				return yearDifference % 5 == 0;
			case SIX_YEARS:
				return yearDifference % 6 == 0;
			case SEVEN_YEARS:
				return yearDifference % 7 == 0;
			case EVEN_YEARS:
				return year % 2 == 0;
			case ODD_YEARS:
				return year % 2 == 1;
			default:
				throw new IllegalStateException("Unhandled CycleType " + holidayRule.getEvery());
			}
		}
		return isInRange;
	}

}
