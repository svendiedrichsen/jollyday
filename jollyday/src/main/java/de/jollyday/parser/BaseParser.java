package de.jollyday.parser;

import java.util.Arrays;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;


public abstract class BaseParser implements PropertiesParser{

	private static final String LIST_SEPARATOR = ",";

	public Set<Calendar> parse(int year) {
		Set<Calendar> holidays = new HashSet<Calendar>();
		for (String monthDay : getDates()) {
			Calendar holiday = parse(year, monthDay);
			holidays.add(holiday);
		}
		return holidays;
	}

	protected static void readConfiguration(Properties p, String configParam, Set<String> values) {
		String list = p.getProperty(configParam);
		if (list != null) {
			String[] fixedArray = list.split(LIST_SEPARATOR);
			values.addAll(Arrays.asList(fixedArray));
		}
	}

	protected abstract Calendar parse(int year, String date);
	
	protected abstract Set<String> getDates();
}
