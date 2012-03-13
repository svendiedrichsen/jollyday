/**
 * Copyright 2012 Sven Diedrichsen 
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
package de.jollyday.processor.impl;

import java.util.HashSet;
import java.util.Set;

import org.joda.time.LocalDate;

import de.jollyday.Holiday;
import de.jollyday.HolidayType;
import de.jollyday.config.FixedWeekdayInMonth;
import de.jollyday.config.Which;
import de.jollyday.processor.HolidayProcessor;
import de.jollyday.util.CalendarUtil;
import de.jollyday.util.XMLUtil;

import static de.jollyday.util.Check.*;

/**
 * @author sven
 *
 */
public class FixedWeekdayInMonthProcessor implements HolidayProcessor {

	private final FixedWeekdayInMonth fixedWeekdayInMonth;

	/**
	 * @param fixedWeekdayInMonth
	 */
	public FixedWeekdayInMonthProcessor(FixedWeekdayInMonth fixedWeekdayInMonth) {
		notNull(fixedWeekdayInMonth, "fixedWeekdayInMonth");
		this.fixedWeekdayInMonth = fixedWeekdayInMonth;
	}
	
	public void init() {
	}

	/* (non-Javadoc)
	 * @see de.jollyday.processor.HolidayProcessor#process(int)
	 */
	public Set<Holiday> process(int year, String... args) {
		Set<Holiday> holidays = new HashSet<Holiday>();
		LocalDate date = parse(year, fixedWeekdayInMonth);
			HolidayType type = XMLUtil.getType(fixedWeekdayInMonth.getLocalizedType());
			holidays.add(new Holiday(date, fixedWeekdayInMonth.getDescriptionPropertiesKey(),
					type));
		return holidays;
	}
	
	/**
	 * Parses the.
	 *
	 * @param year
	 *            the year
	 * @param fwm
	 *            the fwm
	 * @return the local date
	 */
	protected LocalDate parse(int year, FixedWeekdayInMonth fwm) {
		LocalDate date = CalendarUtil.create(year,
				XMLUtil.getMonth(fwm.getMonth()), 1);
		int direction = 1;
		if (fwm.getWhich() == Which.LAST) {
			date = date.withDayOfMonth(date.dayOfMonth().getMaximumValue());
			direction = -1;
		}
		int weekDay = XMLUtil.getWeekday(fwm.getWeekday());
		while (date.getDayOfWeek() != weekDay) {
			date = date.plusDays(direction);
		}
		switch (fwm.getWhich()) {
		case SECOND:
			date = date.plusDays(7);
			break;
		case THIRD:
			date = date.plusDays(14);
			break;
		case FOURTH:
			date = date.plusDays(21);
		}
		return date;
	}


}
