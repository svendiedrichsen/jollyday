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

import de.jollyday.config.FixedWeekdayBetweenFixed;
import de.jollyday.config.Holidays;
import de.jollyday.parser.HolidayParser;
import de.jollyday.util.CalendarUtil;
import de.jollyday.util.XMLUtil;

/**
 * Parses the configuration for fixed weekdays between two fixed
 * dates.
 * @author Sven Diedrichsen
 */
public class FixedWeekdayBetweenFixedParser implements HolidayParser {

	/**
	 * Parses the provided configuration and creates holidays for the provided year.
	 */
	public void parse(int year, Set<LocalDate> holidays, Holidays config) {
		for(FixedWeekdayBetweenFixed fwm : config.getFixedWeekdayBetweenFixed()){
			LocalDate from = CalendarUtil.create(year, fwm.getFrom());
			LocalDate to = CalendarUtil.create(year, fwm.getTo());
			LocalDate result = null;
			for(;!from.isAfter(to);){
				if(from.getDayOfWeek() == XMLUtil.getWeekday(fwm.getWeekday())){
					result = from;
					break;
				}
				from = from.plusDays(1);
			}
			if(result != null) holidays.add(result);
		}
	}

}
