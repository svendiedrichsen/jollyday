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
import de.jollyday.config.EthiopianOrthodoxHoliday;
import de.jollyday.config.Holidays;
import de.jollyday.parser.AbstractHolidayParser;
import de.jollyday.util.CalendarUtil;
import de.jollyday.util.XMLUtil;

/**
 * Calculates the ethiopian orthodox holidays.
 * 
 * @author Sven Diedrichsen
 *
 */
public class EthiopianOrthodoxHolidayParser extends AbstractHolidayParser {

	/* (non-Javadoc)
	 * @see de.jollyday.parser.HolidayParser#parse(int, java.util.Set, de.jollyday.config.Holidays)
	 */
	public void parse(int year, Set<Holiday> holidays, Holidays config) {
		for (EthiopianOrthodoxHoliday h : config.getEthiopianOrthodoxHoliday()) {
			if(!isValid(h, year)) {
				continue;
			}
			Set<LocalDate> ethiopianHolidays = null;
			switch(h.getType()){
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
					throw new IllegalArgumentException("Unknown ethiopian orthodox holiday type "+h.getType());
			}
			String propertiesKey = "ethiopian.orthodox."+h.getType().name();
			HolidayType type = XMLUtil.getType(h.getLocalizedType());
			for(LocalDate d : ethiopianHolidays){
				holidays.add(new Holiday(d, propertiesKey, type));
			}
		}
	}

}
