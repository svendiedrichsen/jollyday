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

import org.joda.time.Chronology;
import org.joda.time.DateTimeConstants;
import org.joda.time.LocalDate;
import org.joda.time.chrono.GregorianChronology;
import org.joda.time.chrono.IslamicChronology;

import de.jollyday.config.Fixed;
import de.jollyday.config.Holidays;
import de.jollyday.config.IslamicHoliday;
import de.jollyday.parser.HolidayParser;
import de.jollyday.util.CalendarUtil;

/**
 * @author svdi1de
 *
 */
public class IslamicHolidayParser implements HolidayParser {

	/* (non-Javadoc)
	 * @see de.jollyday.parser.HolidayParser#parse(int, java.util.Set, de.jollyday.config.Holidays)
	 */
	public void parse(int year, Set<LocalDate> holidays, Holidays config) {
		for(IslamicHoliday i : config.getIslamicHoliday()){
			LocalDate d = null;
			switch(i.getType()){
			case NEUJAHR:
				d = new LocalDate(year, DateTimeConstants.JANUARY, 1, IslamicChronology.getInstance());
				d = new LocalDate(d, GregorianChronology.getInstance());
				break;
			case ASCHURA:
				d = new LocalDate(year, DateTimeConstants.JANUARY, 15, IslamicChronology.getInstance());
				d = new LocalDate(d, GregorianChronology.getInstance());
				break;
			case ID_AL_FITR:
				d = new LocalDate(year, DateTimeConstants.OCTOBER, 1, IslamicChronology.getInstance());
				d = new LocalDate(d, GregorianChronology.getInstance());
				break;
			case ID_UL_ADHA:
				break;
			case LAILAT_AL_BARAT:
				break;
			case LAILAT_AL_MIRAJ:
				break;
			case LAILAT_AL_QADR:
				break;
			case MAWLID_AN_NABI:
				break;
			case RAMADAN:
				break;
			default:
				throw new IllegalArgumentException("Unknown islamic holiday "+i.getType());
			}
			holidays.add(d);
		}
	}

}
