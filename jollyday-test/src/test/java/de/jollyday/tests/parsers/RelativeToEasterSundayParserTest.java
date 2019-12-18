package de.jollyday.tests.parsers;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import de.jollyday.Holiday;
import de.jollyday.config.Holidays;
import de.jollyday.config.RelativeToEasterSunday;
import de.jollyday.parser.impl.RelativeToEasterSundayParser;
import de.jollyday.util.CalendarUtil;

public class RelativeToEasterSundayParserTest {

	RelativeToEasterSundayParser parser = new RelativeToEasterSundayParser();
	Set<Holiday> holidays = new HashSet<>();	
	CalendarUtil calendarUtil = new CalendarUtil();
	
	@Test
	public void testForEasterMonday() {
		doTest(2013, 1);
	}

	@Test
	public void testForEasterSaturday() {
		doTest(2013, -1);
	}

	private void doTest(int year, int days) {
		Holidays holidaysConfig = new Holidays();
		addRelativeToEasterHoliday(holidaysConfig, days);
		parser.parse(year, holidays, holidaysConfig);
		assertEquals("Missing holiday.", 1, holidays.size());
		Holiday h = holidays.iterator().next();
		LocalDate targetDate = calendarUtil.getEasterSunday(year).plusDays(days);	
		assertEquals("Wrong date found.", targetDate, h.getDate());
	}

	private void addRelativeToEasterHoliday(Holidays holidaysConfig, int days) {
		RelativeToEasterSunday r = new RelativeToEasterSunday();
		r.setDays(days);
		holidaysConfig.getRelativeToEasterSunday().add(r);
	}

}
