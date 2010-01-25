package de.jollyday.parser.impl;

import java.util.Set;

import org.joda.time.LocalDate;

import de.jollyday.config.FixedMoving;
import de.jollyday.config.Holidays;
import de.jollyday.parser.HolidayParser;
import de.jollyday.util.CalendarUtil;
import de.jollyday.util.XMLUtil;

public class FixedMovingParser implements HolidayParser {

	public void parse(int year, Set<LocalDate> holidays, Holidays config) {
		for(FixedMoving fm : config.getFixedMoving()){
			LocalDate fixed = CalendarUtil.create(year, fm.getDate());
			if(CalendarUtil.isWeekend(fixed)){
				int weekday = XMLUtil.getWeekday(fm.getNextWeekday());
				while(fixed.getDayOfWeek() != weekday){
					fixed = fixed.plusDays(1);
				}
			}
			holidays.add(fixed);
		}
	}

}
