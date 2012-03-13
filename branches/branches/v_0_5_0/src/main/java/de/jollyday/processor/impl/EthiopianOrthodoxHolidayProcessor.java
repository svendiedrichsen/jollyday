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
import de.jollyday.config.EthiopianOrthodoxHoliday;
import de.jollyday.processor.HolidayProcessor;
import de.jollyday.util.CalendarUtil;
import de.jollyday.util.XMLUtil;

import static de.jollyday.util.Check.*;

/**
 * @author sven
 * 
 */
public class EthiopianOrthodoxHolidayProcessor  implements HolidayProcessor {

	private final EthiopianOrthodoxHoliday ethiopianOrthodoxHoliday;

	/**
	 * @param ethiopianOrthodoxHoliday
	 */
	public EthiopianOrthodoxHolidayProcessor(EthiopianOrthodoxHoliday ethiopianOrthodoxHoliday) {
		notNull(ethiopianOrthodoxHoliday, "ethiopianOrthodoxHoliday");
		this.ethiopianOrthodoxHoliday = ethiopianOrthodoxHoliday;
	}
	
	public void init(){
	}

	public Set<Holiday> process(int year, String... args) {
		Set<LocalDate> ethiopianHolidays = null;
		switch (ethiopianOrthodoxHoliday.getType()) {
		case TIMKAT:
			ethiopianHolidays = CalendarUtil.getEthiopianOrthodoxHolidaysInGregorianYear(year, 5, 10);
			break;
		case ENKUTATASH:
			ethiopianHolidays = CalendarUtil.getEthiopianOrthodoxHolidaysInGregorianYear(year, 1, 1);
			break;
		case MESKEL:
			ethiopianHolidays = CalendarUtil.getEthiopianOrthodoxHolidaysInGregorianYear(year, 1, 17);
			break;
		default:
			throw new IllegalArgumentException("Unknown ethiopian orthodox holiday type " + ethiopianOrthodoxHoliday.getType());
		}
		String propertiesKey = "ethiopian.orthodox." + ethiopianOrthodoxHoliday.getType().name();
		HolidayType type = XMLUtil.getType(ethiopianOrthodoxHoliday.getLocalizedType());
		Set<Holiday> holidays = new HashSet<Holiday>();
		for (LocalDate d : ethiopianHolidays) {
			holidays.add(new Holiday(d, propertiesKey, type));
		}
		return holidays;
	}

}
