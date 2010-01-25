package de.jollyday.parser;

import java.util.Set;

import org.joda.time.LocalDate;

import de.jollyday.config.Holidays;

public interface HolidayParser {
	void parse(int year, Set<LocalDate> holidays, Holidays config);
}
