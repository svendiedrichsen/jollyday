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

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.joda.time.LocalDate;

import de.jollyday.Holiday;
import de.jollyday.HolidayType;
import de.jollyday.config.Fixed;
import de.jollyday.processor.HolidayProcessor;
import de.jollyday.util.CalendarUtil;
import de.jollyday.util.XMLUtil;

import static de.jollyday.util.Check.*;

/**
 * @author sven
 * 
 */
public class FixedProcessor  implements HolidayProcessor {

	private final Fixed fixed;

	/**
	 * @param fixed
	 */
	public FixedProcessor(Fixed fixed) {
		notNull(fixed, "fixed");
		this.fixed = fixed;
	}

	public void init() {
	}
	
	/**
	 * Handles fixed configuration (non-Javadoc)
	 * 
	 * @see de.jollyday.processor.HolidayProcessor#process(java.lang.Object, String...)
	 */
	public Set<Holiday> process(int year, String... args) {
		LocalDate date = CalendarUtil.create(year, fixed);
		HolidayType type = XMLUtil.getType(fixed.getLocalizedType());
		Holiday holiday = new Holiday(date, fixed.getDescriptionPropertiesKey(), type);
		return new HashSet<Holiday>(Arrays.asList(holiday));
	}

}
