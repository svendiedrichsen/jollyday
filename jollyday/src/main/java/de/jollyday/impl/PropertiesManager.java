package de.jollyday.impl;

import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.jollyday.Manager;
import de.jollyday.util.CalendarUtil;

public class PropertiesManager extends Manager {

	private static final Logger LOG = Logger.getLogger(PropertiesManager.class.getName());
	private static final String CONFIG_PARAM_RELATIVE = "holidays.relative.eastern";
	private static final String CONFIG_PARAM_RELATIVE_TO_FIXED = "holidays.relative.to.fixed";
	private static final String CONFIG_PARAM_FIXED = "holidays.fixed";
	private static final String LIST_SEPARATOR = ",";
	private static final String FILE_PREFIX = "holidays";
	private static final String FILE_SUFFIX = ".properties";
	private static final Map<String, Integer> WEEKDAYS = new HashMap<String, Integer>();
	
	private Set<String> fixed = new HashSet<String>();
	private Set<String> variable = new HashSet<String>();
	private Set<String> variableToFixed = new HashSet<String>();

	
	static{
		WEEKDAYS.put("SUNDAY", Calendar.SUNDAY);
		WEEKDAYS.put("MONDAY", Calendar.MONDAY);
		WEEKDAYS.put("TUESDAY", Calendar.TUESDAY);
		WEEKDAYS.put("WEDNESDAY", Calendar.WEDNESDAY);
		WEEKDAYS.put("THURSDAY", Calendar.THURSDAY);
		WEEKDAYS.put("FRIDAY", Calendar.FRIDAY);
		WEEKDAYS.put("SATURDAY", Calendar.SATURDAY);
	}
	
	@Override
	public Set<Calendar> getHolidays(int year) {
		Set<Calendar> holidays = new HashSet<Calendar>();
		for (String md : fixed) {
			Calendar holiday = parseFixed(year, md);
			holidays.add(holiday);
		}
		for (String v2f : variableToFixed){
			Calendar holiday = parseRelativeToFixed(year, v2f);
			holidays.add(holiday);
		}
		for (String rel : variable) {
			Calendar holiday = parseRelative(year, rel);
			holidays.add(holiday);
		}
		return holidays;
	}

	private Calendar parseRelative(int year, String rel) {
		Calendar holiday = getEasterSunday(year);
		holiday.add(Calendar.DAY_OF_YEAR, Integer.valueOf(rel));
		return holiday;
	}

	private Calendar parseRelativeToFixed(int year, String v2f) {
		String[] v2fArray = v2f.split("-");
		Calendar holiday = createFixedCalendar(year, v2fArray[2].split("/"));
		int direction = (v2fArray[1].equalsIgnoreCase("before") ? -1 : 1);
		do{
			holiday.add(Calendar.DAY_OF_YEAR, direction);
		}while(holiday.get(Calendar.DAY_OF_WEEK) != WEEKDAYS.get(v2fArray[0]));
		return holiday;
	}

	private Calendar parseFixed(int year, String md) {
		String[] mdArray = md.split("/");
		Calendar holiday = createFixedCalendar(year, mdArray);
		return holiday;
	}

	private static Calendar createFixedCalendar(int year, String[] mdArray) {
		Calendar holiday = CalendarUtil.create();
		holiday.set(year, Integer.valueOf(mdArray[0]) - 1, Integer
				.valueOf(mdArray[1]));
		return holiday;
	}

	@Override
	public void init(String country, String... args) throws IOException {
		loadProperties(country);
		String sub = country;
		for (String arg : args) {
			sub += "_" + arg;
			try {
				loadProperties(sub);
			} catch (Exception ioe) {
				if (LOG.isLoggable(Level.WARNING)) {
					LOG.log(Level.WARNING, "Could not load properties for '"
							+ sub + "'.");
				}
			}
		}
	}

	private void loadProperties(String subName) throws IOException {
		Properties p = new Properties();
		p.load(ClassLoader.getSystemResourceAsStream(FILE_PREFIX + "_"
				+ subName + FILE_SUFFIX));
		readConfiguration(p, CONFIG_PARAM_FIXED, fixed);
		readConfiguration(p, CONFIG_PARAM_RELATIVE_TO_FIXED, variableToFixed);
		readConfiguration(p, CONFIG_PARAM_RELATIVE, variable);
	}

	private static void readConfiguration(Properties p, String configParam, Set<String> values) {
		String list = p.getProperty(configParam);
		if (list != null) {
			String[] fixedArray = list.split(LIST_SEPARATOR);
			values.addAll(Arrays.asList(fixedArray));
		}
	}

}
