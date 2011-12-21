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
		return (h.getValidFrom() == null || h.getValidFrom().intValue() <= year)
				&& (h.getValidTo() == null || h.getValidTo().intValue() >= year);
	}

}
