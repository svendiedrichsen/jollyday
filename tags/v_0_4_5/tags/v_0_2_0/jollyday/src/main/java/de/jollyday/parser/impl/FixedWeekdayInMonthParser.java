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

import de.jollyday.config.FixedWeekdayInMonth;
import de.jollyday.config.Holidays;
import de.jollyday.config.Which;
import de.jollyday.parser.AbstractHolidayParser;
import de.jollyday.util.CalendarUtil;
import de.jollyday.util.XMLUtil;

public class FixedWeekdayInMonthParser extends AbstractHolidayParser{

	public void parse(int year, Set<LocalDate> holidays, Holidays config) {
		for(FixedWeekdayInMonth fwm : config.getFixedWeekday()){
			if(!isValid(fwm, year)) continue;
			LocalDate date = parse(year, fwm);
			holidays.add(date);
		}
	}

	protected LocalDate parse(int year, FixedWeekdayInMonth fwm) {
		LocalDate date = CalendarUtil.create(year, XMLUtil.getMonth(fwm.getMonth()), 1);
		int direction = 1;
		if(fwm.getWhich() == Which.LAST){
			date = date.withDayOfMonth(date.dayOfMonth().getMaximumValue());
			direction = -1;
		}
		int weekDay = XMLUtil.getWeekday(fwm.getWeekday());
		while(date.getDayOfWeek() != weekDay){
			date = date.plusDays(direction);
		}
		switch(fwm.getWhich()){
			case SECOND:
				date = date.plusDays(7);
				break;
			case THIRD:
				date = date.plusDays(14);
				break;
			case FOURTH:
				date = date.plusDays(21);
		}
		return date;
	}

}