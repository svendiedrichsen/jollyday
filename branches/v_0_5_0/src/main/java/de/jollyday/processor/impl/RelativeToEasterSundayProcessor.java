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
import de.jollyday.config.ChronologyType;
import de.jollyday.config.HolidayType;
import de.jollyday.config.RelativeToEasterSunday;
import de.jollyday.processor.HolidayProcessor;
import de.jollyday.util.CalendarUtil;
import de.jollyday.util.XMLUtil;

import static de.jollyday.util.Check.*;
/**
 * @author sven
 *
 */
public class RelativeToEasterSundayProcessor  implements HolidayProcessor{

	private final RelativeToEasterSunday relativeToEasterSunday;

	/**
	 * @param relativeToEasterSunday
	 */
	public RelativeToEasterSundayProcessor(RelativeToEasterSunday relativeToEasterSunday) {
		notNull(relativeToEasterSunday, "relativeToEasterSunday");
		this.relativeToEasterSunday = relativeToEasterSunday;
	}

	public void init() {
	}

	public Set<Holiday> process(int year, String... args) {
		LocalDate easterSunday = getEasterSunday(year, relativeToEasterSunday.getChronology());
		easterSunday.plusDays(relativeToEasterSunday.getDays());
		String propertiesKey = "christian." + relativeToEasterSunday.getDescriptionPropertiesKey();
		Set<Holiday> holidays = new HashSet<Holiday>();
		addChrstianHoliday(easterSunday, propertiesKey, relativeToEasterSunday.getLocalizedType(), holidays);
		return holidays;																		
	}

	/**
	 * <p>getEasterSunday.</p>
	 *
	 * @param year a int.
	 * @param ct a {@link de.jollyday.config.ChronologyType} object.
	 * @return a {@link org.joda.time.LocalDate} object.
	 */
	protected LocalDate getEasterSunday(int year, ChronologyType ct) {
		LocalDate easterSunday;
		if (ct == ChronologyType.JULIAN) {
			easterSunday = CalendarUtil.getJulianEasterSunday(year);
		} else if (ct == ChronologyType.GREGORIAN) {
			easterSunday = CalendarUtil.getISOEasterSunday(year);
		} else {
			easterSunday = CalendarUtil.getEasterSunday(year);
		}
		return easterSunday;
	}
	
	/**
	 * Adds the given day to the list of holidays.
	 *
	 * @param day a {@link org.joda.time.LocalDate} object.
	 * @param propertiesKey a {@link java.lang.String} object.
	 * @param holidayType a {@link de.jollyday.config.HolidayType} object.
	 * @param holidays a {@link java.util.Set} object.
	 */
	protected void addChrstianHoliday(LocalDate day, String propertiesKey, HolidayType holidayType, Set<Holiday> holidays){
		LocalDate convertedDate = CalendarUtil
				.convertToISODate(day);
		de.jollyday.HolidayType type = XMLUtil.getType(holidayType);
		Holiday h = new Holiday(convertedDate, propertiesKey, type);
		holidays.add(h);
	}


}
