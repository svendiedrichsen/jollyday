package de.jollyday.parser.impl;

import java.util.Set;

import org.joda.time.LocalDate;

import de.jollyday.config.FixedWeekdayInMonth;
import de.jollyday.config.Holidays;
import de.jollyday.config.Which;
import de.jollyday.parser.HolidayParser;
import de.jollyday.util.CalendarUtil;
import de.jollyday.util.XMLUtil;

public class FixedWeekdayInMonthParser implements HolidayParser {

	public void parse(int year, Set<LocalDate> holidays, Holidays config) {
		for(FixedWeekdayInMonth fwm : config.getFixedWeekday()){
			LocalDate date = CalendarUtil.create(year, XMLUtil.getMonth(fwm.getMonth()), 1);
			int direction = 1;
			if(fwm.getWhich() == Which.LAST){
				date = date.withDayOfMonth(date.dayOfMonth().getMaximumValue());
				direction = -1;
			}
			int weekDay = XMLUtil.getWeekday(fwm.getWeekday());
			while(date.getDayOfWeek() != weekDay){
				date = date.plusDays(direction);
			}
			switch(fwm.getWhich()){
				case SECOND:
					date = date.plusDays(7);
					break;
				case THIRD:
					date = date.plusDays(14);
					break;
			}
			holidays.add(date);
		}
	}

}
