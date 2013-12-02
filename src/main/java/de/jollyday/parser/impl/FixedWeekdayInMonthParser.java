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
package de.jollyday.parser.impl;

import java.util.Set;

import org.joda.time.LocalDate;

import de.jollyday.Holiday;
import de.jollyday.HolidayType;
import de.jollyday.config.FixedWeekdayInMonth;
import de.jollyday.config.Holidays;
import de.jollyday.config.Which;
import de.jollyday.parser.AbstractHolidayParser;

/**
 * The Class FixedWeekdayInMonthParser.
 * 
 * @author tboven
 * @version $Id: $
 */
public class FixedWeekdayInMonthParser extends AbstractHolidayParser {

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.jollyday.parser.HolidayParser#parse(int, java.util.Set,
	 * de.jollyday.config.Holidays)
	 */
	/** {@inheritDoc} */
	public void parse(int year, Set<Holiday> holidays, final Holidays config) {
		for (FixedWeekdayInMonth fwm : config.getFixedWeekday()) {
			if (!isValid(fwm, year)) {
				continue;
			}
			LocalDate date = parse(year, fwm);
			HolidayType type = xmlUtil.getType(fwm.getLocalizedType());
			holidays.add(new Holiday(date, fwm.getDescriptionPropertiesKey(), type));
		}
	}

	/**
	 * Parses the {@link FixedWeekdayInMonth}.
	 * 
	 * @param year
	 *            the year
	 * @param fwm
	 *            the fwm
	 * @return the local date
	 */
	protected LocalDate parse(int year, FixedWeekdayInMonth fwm) {
		LocalDate date = calendarUtil.create(year, xmlUtil.getMonth(fwm.getMonth()), 1);
		int direction = 1;
		if (Which.LAST.equals(fwm.getWhich())) {
			date = date.withDayOfMonth(date.dayOfMonth().getMaximumValue());
			direction = -1;
		}
		date = moveToNextRequestedWeekdayByDirection(fwm, date, direction);
		date = moveNumberOfRequestedWeeks(fwm, date);
		return date;
	}

	private LocalDate moveNumberOfRequestedWeeks(FixedWeekdayInMonth fwm, LocalDate date) {
		switch (fwm.getWhich()) {
		case SECOND:
			return date.plusDays(7);
		case THIRD:
			return date.plusDays(14);
		case FOURTH:
			return date.plusDays(21);
		default:
			return date;
		}
	}

	private LocalDate moveToNextRequestedWeekdayByDirection(FixedWeekdayInMonth fwm, LocalDate date, int direction) {
		int weekDay = xmlUtil.getWeekday(fwm.getWeekday());
		while (date.getDayOfWeek() != weekDay) {
			date = date.plusDays(direction);
		}
		return date;
	}

}
