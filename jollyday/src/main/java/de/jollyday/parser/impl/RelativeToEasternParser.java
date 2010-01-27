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
import de.jollyday.config.RelativeToEastern;
import de.jollyday.parser.HolidayParser;
import de.jollyday.util.CalendarUtil;

public class RelativeToEasternParser implements HolidayParser {

	public void parse(int year, Set<LocalDate> holidays, Holidays config) {
		for(RelativeToEastern re : config.getRelativeToEastern()){
			LocalDate easterSunday = CalendarUtil.getEasterSunday(year);
			holidays.add(easterSunday.plusDays(re.getDays()));
		}
	}

}
