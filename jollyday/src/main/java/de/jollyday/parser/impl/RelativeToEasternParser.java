package de.jollyday.parser.impl;

import java.util.Set;

import org.joda.time.LocalDate;

import de.jollyday.config.Holidays;
import de.jollyday.config.RelativeToEastern;
import de.jollyday.parser.HolidayParser;
import de.jollyday.util.CalendarUtil;

public class RelativeToEasternParser implements HolidayParser {

	public void parse(int year, Set<LocalDate> holidays, Holidays config) {
		for(RelativeToEastern re : config.getRelativeToEastern()){
			LocalDate easterSunday = CalendarUtil.getEasterSunday(year);
			holidays.add(easterSunday.plusDays(re.getDays()));
		}
	}

}
