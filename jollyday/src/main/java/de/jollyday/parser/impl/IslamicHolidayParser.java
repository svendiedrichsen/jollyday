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

import org.joda.time.DateTimeConstants;
import org.joda.time.LocalDate;

import de.jollyday.config.Holidays;
import de.jollyday.config.IslamicHoliday;
import de.jollyday.parser.HolidayParser;
import de.jollyday.util.CalendarUtil;

/**
 * This parser calculates gregorian dates for the different islamic holidays.
 * @author Sven Diedrichsen
 */
public class IslamicHolidayParser implements HolidayParser {

	/* (non-Javadoc)
	 * @see de.jollyday.parser.HolidayParser#parse(int, java.util.Set, de.jollyday.config.Holidays)
	 */
	public void parse(int year, Set<LocalDate> holidays, Holidays config) {
		for(IslamicHoliday i : config.getIslamicHoliday()){
			switch(i.getType()){
			case NEUJAHR:
				holidays.addAll(CalendarUtil.getIslamicHolidaysInGregorianYear(year, DateTimeConstants.JANUARY, 1));
				break;
			case ASCHURA:
				holidays.addAll(CalendarUtil.getIslamicHolidaysInGregorianYear(year, DateTimeConstants.JANUARY, 10));
				break;
			case ID_AL_FITR:
				holidays.addAll(CalendarUtil.getIslamicHolidaysInGregorianYear(year, DateTimeConstants.OCTOBER, 1));
				break;
			case ID_UL_ADHA:
				holidays.addAll(CalendarUtil.getIslamicHolidaysInGregorianYear(year, DateTimeConstants.DECEMBER, 10));
				break;
			case LAILAT_AL_BARAT:
				holidays.addAll(CalendarUtil.getIslamicHolidaysInGregorianYear(year, DateTimeConstants.AUGUST, 15));
				break;
			case LAILAT_AL_MIRAJ:
				holidays.addAll(CalendarUtil.getIslamicHolidaysInGregorianYear(year, DateTimeConstants.JULY, 27));
				break;
			case LAILAT_AL_QADR:
				holidays.addAll(CalendarUtil.getIslamicHolidaysInGregorianYear(year, DateTimeConstants.SEPTEMBER, 27));
				break;
			case MAWLID_AN_NABI:
				holidays.addAll(CalendarUtil.getIslamicHolidaysInGregorianYear(year, DateTimeConstants.MARCH, 12));
				break;
			case RAMADAN:
				holidays.addAll(CalendarUtil.getIslamicHolidaysInGregorianYear(year, DateTimeConstants.SEPTEMBER, 1));
				break;
			default:
				throw new IllegalArgumentException("Unknown islamic holiday "+i.getType());
			}
		}
	}

}
