package de.jollyday.parser;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import de.jollyday.util.CalendarUtil;

public class RelativeToEasternParser extends BaseParser {

	private static final String CONFIG_PARAM_RELATIVE = "holidays.relative.eastern";
	private Set<String> relativeToEastern = new HashSet<String>();

	@Override
	protected Calendar parse(int year, String date) {
		Calendar holiday = CalendarUtil.getEasterSunday(year);
		holiday.add(Calendar.DAY_OF_YEAR, Integer.valueOf(date));
		return holiday;
	}
	
	

	public void init(Properties p) {
		readConfiguration(p, CONFIG_PARAM_RELATIVE, relativeToEastern);
	}

	@Override
	protected Set<String> getDates() {
		return relativeToEastern;
	}
}
