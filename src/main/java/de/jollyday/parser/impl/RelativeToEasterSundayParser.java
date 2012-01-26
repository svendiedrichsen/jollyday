/**
 * Copyright 2011 Sven Diedrichsen 
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
import de.jollyday.config.HolidayType;
import de.jollyday.config.Holidays;
import de.jollyday.config.RelativeToEasterSunday;
import de.jollyday.parser.AbstractHolidayParser;
import de.jollyday.util.CalendarUtil;
import de.jollyday.util.XMLUtil;

/**
 * This parser creates holidays relative to easter sunday.
 *
 * @author Sven Diedrichsen
 * @version $Id: $
 */
public class RelativeToEasterSundayParser extends AbstractHolidayParser {

	/**
	 * {@inheritDoc}
	 *
	 * Parses relative to easter sunday holidays.
	 */
	public void parse(int year, Set<Holiday> holidays, Holidays config) {
		for (RelativeToEasterSunday ch : config.getRelativeToEasterSunday()) {
			if (!isValid(ch, year)) {
				continue;
			}
			LocalDate easterSunday = getEasterSunday(year, ch.getChronology());
			easterSunday.plusDays(ch.getDays());
			String propertiesKey = "christian." + ch.getDescriptionPropertiesKey();
			addChrstianHoliday(easterSunday, propertiesKey, ch.getLocalizedType(), holidays);
		}
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
				.convertToGregorianDate(day);
		de.jollyday.HolidayType type = XMLUtil.getType(holidayType);
		Holiday h = new Holiday(convertedDate, propertiesKey, type);
		holidays.add(h);
	}
	
}
