package de.jollyday.parser.impl;

import java.util.Set;

import org.joda.time.LocalDate;

import de.jollyday.config.Fixed;
import de.jollyday.config.Holidays;
import de.jollyday.parser.HolidayParser;
import de.jollyday.util.CalendarUtil;

public class FixedParser implements HolidayParser {

	public void parse(int year, Set<LocalDate> holidays, Holidays config) {
		for(Fixed f : config.getFixed()){
			holidays.add(CalendarUtil.create(year, f));
		}
	}

}
