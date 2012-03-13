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

import org.joda.time.DateTimeConstants;
import org.joda.time.LocalDate;

import de.jollyday.Holiday;
import de.jollyday.HolidayType;
import de.jollyday.config.IslamicHoliday;
import de.jollyday.processor.HolidayProcessor;
import de.jollyday.util.CalendarUtil;
import de.jollyday.util.XMLUtil;

import static de.jollyday.util.Check.*;
/**
 * @author sven
 * 
 */
public class IslamicHolidayProcessor implements HolidayProcessor {

	private final IslamicHoliday islamicHoliday;

	/**
	 * @param islamicHoliday
	 */
	public IslamicHolidayProcessor(IslamicHoliday islamicHoliday) {
		notNull(islamicHoliday, "islamicHoliday");
		this.islamicHoliday = islamicHoliday;
	}
	
	public void init() {
	}

	public Set<Holiday> process(int year, String... args) {
		Set<LocalDate> islamicHolidays = null;
		switch (islamicHoliday.getType()) {
		case NEWYEAR:
			islamicHolidays = CalendarUtil.getIslamicHolidaysInGregorianYear(year, DateTimeConstants.JANUARY, 1);
			break;
		case ASCHURA:
			islamicHolidays = CalendarUtil.getIslamicHolidaysInGregorianYear(year, DateTimeConstants.JANUARY, 10);
			break;
		case ID_AL_FITR:
			islamicHolidays = CalendarUtil.getIslamicHolidaysInGregorianYear(year, DateTimeConstants.OCTOBER, 1);
			break;
		case ID_UL_ADHA:
			islamicHolidays = CalendarUtil.getIslamicHolidaysInGregorianYear(year, DateTimeConstants.DECEMBER, 10);
			break;
		case LAILAT_AL_BARAT:
			islamicHolidays = CalendarUtil.getIslamicHolidaysInGregorianYear(year, DateTimeConstants.AUGUST, 15);
			break;
		case LAILAT_AL_MIRAJ:
			islamicHolidays = CalendarUtil.getIslamicHolidaysInGregorianYear(year, DateTimeConstants.JULY, 27);
			break;
		case LAILAT_AL_QADR:
			islamicHolidays = CalendarUtil.getIslamicHolidaysInGregorianYear(year, DateTimeConstants.SEPTEMBER, 27);
			break;
		case MAWLID_AN_NABI:
			islamicHolidays = CalendarUtil.getIslamicHolidaysInGregorianYear(year, DateTimeConstants.MARCH, 12);
			break;
		case RAMADAN:
			islamicHolidays = CalendarUtil.getIslamicHolidaysInGregorianYear(year, DateTimeConstants.SEPTEMBER, 1);
			break;
		default:
			throw new IllegalArgumentException("Unknown islamic holiday " + islamicHoliday.getType());
		}
		String propertiesKey = "islamic." + islamicHoliday.getType().name();
		HolidayType type = XMLUtil.getType(islamicHoliday.getLocalizedType());
		Set<Holiday> holidays = new HashSet<Holiday>();
		for (LocalDate d : islamicHolidays) {
			holidays.add(new Holiday(d, propertiesKey, type));
		}
		return holidays;
	}

}
