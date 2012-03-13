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

import org.joda.time.LocalDate;

import de.jollyday.config.MovingCondition;
import de.jollyday.config.To;
import de.jollyday.config.Weekday;
import de.jollyday.util.CalendarUtil;
import de.jollyday.util.Check;

/**
 * Moves the provided date until the target weekday is reached. If there are
 * when conditions provided there must at least match one to move the date.
 * @author sven
 *
 */
public class MovingConditionProcessor {
	private final MovingCondition movingCondition;

	/**
	 * 
	 */
	public MovingConditionProcessor(MovingCondition movingCondition) {
		Check.notNull(movingCondition, "movingCondition");
		this.movingCondition = movingCondition;
	}

	public LocalDate process(LocalDate date){
		
		boolean shallMoveDate = movingCondition.getWhen().isEmpty();
		
		for(Weekday weekday : movingCondition.getWhen()){
			if(CalendarUtil.isWeekday(date, weekday)){
				shallMoveDate = true;
				break;
			}
		}
		if(shallMoveDate){
			int daysToAdd = movingCondition.getTo() == To.NEXT ? 1 : -1;
			while(!CalendarUtil.isWeekday(date, movingCondition.getWeekday())){
				date = date.plusDays(daysToAdd);
			}
		}
		return date;
	}
	
}
