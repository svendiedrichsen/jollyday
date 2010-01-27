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

import de.jollyday.config.Holidays;
import de.jollyday.config.RelativeToFixed;
import de.jollyday.config.When;
import de.jollyday.parser.HolidayParser;
import de.jollyday.util.CalendarUtil;
import de.jollyday.util.XMLUtil;

public class RelativeToFixedParser implements HolidayParser {

	public void parse(int year, Set<LocalDate> holidays, Holidays config) {
		for(RelativeToFixed rf : config.getRelativeToFixed()){
			LocalDate fixed = CalendarUtil.create(year, rf.getDate());
			if(rf.getWeekday() != null){
				int day = XMLUtil.getWeekday(rf.getWeekday());
				int direction = (rf.getWhen() == When.BEFORE ? -1 : 1);
				do{
					fixed = fixed.plusDays(direction);
				}while(fixed.getDayOfWeek() != day);
			}else if(rf.getDays() != null){
				fixed = fixed.plusDays( rf.getWhen() == When.BEFORE ? -rf.getDays() : rf.getDays());
			}
			holidays.add(fixed);
		}
	}

}
