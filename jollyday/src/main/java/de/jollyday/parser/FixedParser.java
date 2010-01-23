package de.jollyday.parser;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import de.jollyday.util.CalendarUtil;

public class FixedParser extends BaseParser {

	private static final String CONFIG_PARAM_FIXED = "holidays.fixed";

	private Set<String> fixed = new HashSet<String>();

	public void init(Properties p) {
		readConfiguration(p, CONFIG_PARAM_FIXED, fixed);
	}
	
	/**
	 * Parses strings like [month]/[day]
	 * @param year
	 * @param rel
	 * @return
	 */
	protected Calendar parse(int year, String date) {
		String[] mdArray = date.split("/");
		Calendar holiday = createFixedCalendar(year, mdArray);
		return holiday;
	}


	protected static Calendar createFixedCalendar(int year, String[] mdArray) {
		Calendar holiday = CalendarUtil.create();
		holiday.set(year, Integer.valueOf(mdArray[0]) - 1, Integer
				.valueOf(mdArray[1]));
		return holiday;
	}

	@Override
	protected Set<String> getDates() {
		return fixed;
	}

}
