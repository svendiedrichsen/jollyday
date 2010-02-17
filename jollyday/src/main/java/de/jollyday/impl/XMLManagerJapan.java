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
package de.jollyday.impl;

import java.util.HashSet;
import java.util.Set;

import org.joda.time.LocalDate;

/**
 * @author svdi1de
 *
 */
public class XMLManagerJapan extends XMLManager {

	/**
	 * Implements the rule which requests if two holidays
	 * have one non holiday between each other than this day
	 * is also a holiday.
	 */
	@Override
	public Set<LocalDate> getHolidays(int year, String... args) {
		Set<LocalDate> holidays = super.getHolidays(year, args);
		Set<LocalDate> additionalHolidays = new HashSet<LocalDate>();
		for(LocalDate d : holidays){
			LocalDate twoDaysLater = d.plusDays(2);
			if(holidays.contains(twoDaysLater)){
				additionalHolidays.add(twoDaysLater.minusDays(1));
			}
		}
		holidays.addAll(additionalHolidays);
		return holidays;
	}
	
}
