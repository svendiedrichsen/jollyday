package de.jollyday.parser;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import de.jollyday.util.CalendarUtil;

public class RelativeToFixedParser extends FixedParser {

	private static final String CONFIG_PARAM_RELATIVE_TO_FIXED = "holidays.relative.to.fixed";

	private Set<String> relativeToFixed = new HashSet<String>();

	@Override
	public void init(Properties p) {
		readConfiguration(p, CONFIG_PARAM_RELATIVE_TO_FIXED, relativeToFixed);
	}
	
	/**
	 * Parses strings like [weekday]-[before|after]-[month]/[day]
	 * @param year
	 * @param v2f
	 * @return
	 */
	@Override
	protected Calendar parse(int year, String date) {
		String[] v2fArray = date.split("-");
		Calendar holiday = createFixedCalendar(year, v2fArray[2].split("/"));
		int direction = (v2fArray[1].equalsIgnoreCase("before") ? -1 : 1);
		do{
			holiday.add(Calendar.DAY_OF_YEAR, direction);
		}while(holiday.get(Calendar.DAY_OF_WEEK) != CalendarUtil.getWeekday(v2fArray[0]));
		return holiday;
	}
	
	@Override
	protected Set<String> getDates() {
		return relativeToFixed;
	}
	
}
