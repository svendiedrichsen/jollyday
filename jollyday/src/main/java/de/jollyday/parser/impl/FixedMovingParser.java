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

import de.jollyday.config.FixedMoving;
import de.jollyday.config.Holidays;
import de.jollyday.config.MovingCondition;
import de.jollyday.config.With;
import de.jollyday.parser.AbstractHolidayParser;
import de.jollyday.util.CalendarUtil;
import de.jollyday.util.XMLUtil;

public class FixedMovingParser extends AbstractHolidayParser{

	public void parse(int year, Set<LocalDate> holidays, Holidays config) {
		for(FixedMoving fm : config.getFixedMoving()){
			if(!isValid(fm, year)) continue;
			LocalDate fixed = CalendarUtil.create(year, fm);
			for(MovingCondition mc : fm.getCondition()){
				if(shallBeSubstituted(fixed, mc)){
					fixed = moveDate(mc, fixed);
					break;
				}
			}
			holidays.add(fixed);
		}
	}

	/**
	 * Moves the date using the FixedMoving information
	 * @param mc
	 * @param fixed
	 * @return
	 */
	protected LocalDate moveDate(MovingCondition mc, LocalDate fixed) {
		int weekday = XMLUtil.getWeekday(mc.getWeekday());
		int direction = (mc.getWith() == With.NEXT ? 1 : -1 );
		while(fixed.getDayOfWeek() != weekday){
			fixed = fixed.plusDays(direction);
		}
		return fixed;
	}

	/**
	 * Determines if the provided date shall be substituted.
	 * @param fixed
	 * @param mc
	 */
	protected boolean shallBeSubstituted(LocalDate fixed, MovingCondition mc) {
		return fixed.getDayOfWeek() == XMLUtil.getWeekday(mc.getSubstitute());
	}
	
	

}
