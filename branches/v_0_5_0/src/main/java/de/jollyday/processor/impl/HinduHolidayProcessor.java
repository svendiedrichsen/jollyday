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

import de.jollyday.Holiday;
import de.jollyday.config.HinduHoliday;
import de.jollyday.processor.HolidayProcessor;

import static de.jollyday.util.Check.*;
/**
 * @author sven
 *
 */
public class HinduHolidayProcessor implements HolidayProcessor {

	private final HinduHoliday hinduHoliday;

	/**
	 * @param hinduHoliday
	 */
	public HinduHolidayProcessor(HinduHoliday hinduHoliday) {
		notNull(hinduHoliday, "hinduHoliday");
		this.hinduHoliday = hinduHoliday;
	}

	public void init() {
	}
	
	public Set<Holiday> process(int year, String... args) {
		switch (hinduHoliday.getType()) {
		case HOLI:
			// 20 February and ending on 21 March (20th march in leap years)
			// TODO: Calculate with hindu calendar.
			break;
		default:
			throw new IllegalArgumentException("Unknown hindu holiday "
					+ hinduHoliday.getType());
		}
		return new HashSet<Holiday>();
	}

}
