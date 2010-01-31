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

import de.jollyday.config.FixedMovingOnWeekend;
import de.jollyday.config.Holidays;
import de.jollyday.parser.HolidayParser;
import de.jollyday.util.CalendarUtil;
import de.jollyday.util.XMLUtil;

public class FixedMovingOnWeekendParser implements HolidayParser {

	public void parse(int year, Set<LocalDate> holidays, Holidays config) {
		for(FixedMovingOnWeekend fm : config.getFixedMovingOnWeekend()){
			LocalDate fixed = CalendarUtil.create(year, fm);
			if(CalendarUtil.isWeekend(fixed)){
				int weekday = XMLUtil.getWeekday(fm.getNextWeekday());
				while(fixed.getDayOfWeek() != weekday){
					fixed = fixed.plusDays(1);
				}
			}
			holidays.add(fixed);
		}
	}

}
