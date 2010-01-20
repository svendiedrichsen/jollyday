package de.jollyday.impl;

import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.jollyday.Manager;
import de.jollyday.util.CalendarUtil;

public class PropertiesManager extends Manager {

	private static final Logger LOG = Logger.getLogger(PropertiesManager.class.getName());
	private static final String CONFIG_PARAM_RELATIVE = "holidays.relative.eastern";
	private static final String CONFIG_PARAM_FIXED = "holidays.fixed";
	private static final String LIST_SEPARATOR = ",";
	private static final String FILE_PREFIX = "holidays";
	private static final String FILE_SUFFIX = ".properties";

	private Set<String> fixed = new HashSet<String>();
	private Set<String> variable = new HashSet<String>();

	@Override
	public Set<Calendar> getHolidays(int year) {
		Set<Calendar> holidays = new HashSet<Calendar>();
		for (String md : fixed) {
			String[] mdArray = md.split("/");
			Calendar holiday = CalendarUtil.create();
			holiday.set(year, Integer.valueOf(mdArray[0]) - 1, Integer
					.valueOf(mdArray[1]));
			holidays.add(holiday);
		}
		for (String rel : variable) {
			Calendar holiday = getEasterSunday(year);
			holiday.add(Calendar.DAY_OF_YEAR, Integer.valueOf(rel));
			holidays.add(holiday);
		}
		return holidays;
	}

	@Override
	public void init(String country, String... args) throws IOException {
		loadProperties(country);
		String sub = country;
		for (String arg : args) {
			sub += "_" + arg;
			try {
				loadProperties(sub);
			} catch (IOException ioe) {
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
		String fixedList = p.getProperty(CONFIG_PARAM_FIXED);
		if (fixedList != null) {
			String[] fixedArray = fixedList.split(LIST_SEPARATOR);
			fixed.addAll(Arrays.asList(fixedArray));
		}
		String relativeList = p.getProperty(CONFIG_PARAM_RELATIVE);
		if (relativeList != null) {
			String[] relativeArray = relativeList.split(LIST_SEPARATOR);
			variable.addAll(Arrays.asList(relativeArray));
		}
	}

}
