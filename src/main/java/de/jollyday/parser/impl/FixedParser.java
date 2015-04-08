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

import java.time.LocalDate;
import java.util.Set;

import de.jollyday.Holiday;
import de.jollyday.HolidayType;
import de.jollyday.config.Fixed;
import de.jollyday.config.Holidays;
import de.jollyday.parser.AbstractHolidayParser;

/**
 * The Class FixedParser. Parses a fixed date
 * 
 * @author tboven
 * @version $Id: $
 */
public class FixedParser extends AbstractHolidayParser {

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.jollyday.parser.HolidayParser#parse(int, java.util.Set,
	 * de.jollyday.config.Holidays)
	 */
	/** {@inheritDoc} */
	@Override
	public void parse(int year, Set<Holiday> holidays, final Holidays config) {
		for (Fixed f : config.getFixed()) {
			if (!isValid(f, year)) {
				continue;
			}
			LocalDate date = calendarUtil.create(year, f);
			LocalDate movedDate = moveDate(f, date);
			HolidayType type = xmlUtil.getType(f.getLocalizedType());
			Holiday h = new Holiday(movedDate, f.getDescriptionPropertiesKey(), type);
			holidays.add(h);
		}
	}

}
