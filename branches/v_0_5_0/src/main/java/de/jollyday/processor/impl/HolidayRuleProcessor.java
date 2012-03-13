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

import static de.jollyday.util.Check.notNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.joda.time.LocalDate;

import de.jollyday.Holiday;
import de.jollyday.config.HolidayRule;
import de.jollyday.config.MovingCondition;
import de.jollyday.processor.HolidayProcessor;

/**
 * @author sven
 *
 */
public class HolidayRuleProcessor implements HolidayProcessor {

	private final HolidayRule holidayRule;
	private HolidayProcessor holidayProcessor;
	private List<MovingConditionProcessor> movingProcessors = new ArrayList<MovingConditionProcessor>();

	/**
	 * 
	 */
	public HolidayRuleProcessor(HolidayRule holidayRule) {
		notNull(holidayRule, "holidayRule");
		this.holidayRule = holidayRule;
	}

	/**
	 * Initializes the {@link HolidayRuleProcessor} by creating the {@link HolidayProcessor}
	 * instance to use.
	 */
	public void init() {
		if(holidayRule.getChristianHoliday() != null){
			holidayProcessor = new ChristianHolidayProcessor(holidayRule.getChristianHoliday());
		}else if(holidayRule.getEthiopianOrthodoxHoliday() != null){
			holidayProcessor = new EthiopianOrthodoxHolidayProcessor(holidayRule.getEthiopianOrthodoxHoliday());
		}else if(holidayRule.getFixed() != null){
			holidayProcessor = new FixedProcessor(holidayRule.getFixed());
		}else if(holidayRule.getFixedWeekdayInMonth() != null){
			holidayProcessor = new FixedWeekdayInMonthProcessor(holidayRule.getFixedWeekdayInMonth());
		}else if(holidayRule.getHinduHoliday() != null){
			holidayProcessor = new HinduHolidayProcessor(holidayRule.getHinduHoliday());
		}else if(holidayRule.getIslamicHoliday() != null){
			holidayProcessor = new IslamicHolidayProcessor(holidayRule.getIslamicHoliday());
		}else if(holidayRule.getRelativeToEasterSunday() != null){
			holidayProcessor = new RelativeToEasterSundayProcessor(holidayRule.getRelativeToEasterSunday());
		}else{
			throw new IllegalArgumentException("Unhandled holiday rule found.");
		}
		holidayProcessor.init();
		for(MovingCondition movingCondition : holidayRule.getMovingCondition()){
			movingProcessors.add(new MovingConditionProcessor(movingCondition));
		}
		
	}

	/* (non-Javadoc)
	 * @see de.jollyday.processor.HolidayProcessor#process(int, java.lang.String[])
	 */
	public Set<Holiday> process(int year, String... args) {
		if(!isValid(year)){
			return Collections.emptySet();
		}
		Set<Holiday> holidays = holidayProcessor.process(year, args);
		if(!movingProcessors.isEmpty()){
			Set<Holiday> movedHolidays = new HashSet<Holiday>();
			for(Holiday holiday : holidays){
				for(MovingConditionProcessor movingProcessor : movingProcessors){
					LocalDate movedDate = movingProcessor.process(holiday.getDate());
					if(!movedDate.equals(holiday.getDate())){
						movedHolidays.add(holiday.copyWithDate(movedDate));
					}else{
						movedHolidays.add(holiday);
					}
				}
			}
			holidays = movedHolidays;
		}
		return holidays;
	}

	/**
	 * @param year
	 * @return
	 */
	private boolean isValid(int year) {
		return (holidayRule.getValidFrom() == null || holidayRule.getValidFrom().intValue() <= year)
				&& (holidayRule.getValidTo() == null || holidayRule.getValidTo().intValue() >= year)
				&& holidayRule.getEvery();
	}
	
	

}
