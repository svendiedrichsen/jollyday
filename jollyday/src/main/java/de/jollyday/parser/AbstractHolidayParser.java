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
package de.jollyday.parser;

import de.jollyday.config.Holiday;

/**
 * The abstract base class for all HolidayParser implementations.
 * @author Sven Diedrichsen
 * 
 */
public abstract class AbstractHolidayParser implements HolidayParser {

	/**
	 * Evaluates if the provided <code>Holiday</code> instance is valid
	 * for the provided year.
	 * @param <T> <code>Holiday</code> is the base class for all holiday configuration entries.
	 * @param h The holiday configuration entry to validate
	 * @param year The year to validate against.
	 * @return is valid for the year.
	 */
	protected <T extends Holiday> boolean isValid(T h, int year) {
		boolean valid = checkValidDates(h, year);
		if(valid){
			valid = valid || checkCyclicHolidays(h, year);  
		}
		return valid;
	}

	/**
	 * Checks cyclic holidays and checks if the requested year is
	 * hit within the cycles.
	 * @param <T> Holiday
	 * @param h Holiday
	 * @param year the year to check against
	 * @return is valid
	 */
	private <T extends Holiday> boolean checkCyclicHolidays(T h, int year) {
		if(h.getValidFrom() != null && h.getEvery() != null){
			int cycleYears = 0;
			if("2_YEARS".equalsIgnoreCase(h.getEvery())){
				cycleYears = 2;
			}else if("4_YEARS".equalsIgnoreCase(h.getEvery())){
				cycleYears = 4;
			}else if("6_YEARS".equalsIgnoreCase(h.getEvery())){
				cycleYears = 6;
			}else{
				throw new IllegalArgumentException("Cannot handle unknown cycle type '"+h.getEvery()+"'.");
			}
			return (year - h.getValidFrom().intValue()) % cycleYears == 0;
		}
		return true;
	}

	/**
	 * Checks whether the holiday is within the valid date range.
	 * @param <T>
	 * @param h
	 * @param year
	 * @return is valid.
	 */
	private <T extends Holiday> boolean checkValidDates(T h, int year) {
		return (h.getValidFrom() == null || h.getValidFrom().intValue() <= year)
			&& (h.getValidTo() == null || h.getValidTo().intValue() >= year);
	}

}
