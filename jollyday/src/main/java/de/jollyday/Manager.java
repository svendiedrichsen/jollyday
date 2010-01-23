package de.jollyday;

import java.io.IOException;
import java.util.Calendar;
import java.util.Properties;
import java.util.Set;

import de.jollyday.util.CalendarUtil;

public abstract class Manager {
	
	private static final String MANAGER_IMPL_CLASS_PREFIX = "manager.impl.";
	private static final String CONFIG_FILE = "application.properties";

	public static final Manager getInstance(String country, String...args) throws IOException, InstantiationException, IllegalAccessException, ClassNotFoundException{
		Properties props = new Properties();
		props.load(ClassLoader.getSystemResourceAsStream(CONFIG_FILE));
		Manager m = (Manager)Class.forName(props.getProperty(MANAGER_IMPL_CLASS_PREFIX+country)).newInstance();
		m.init(country, args);
		return m;
	}
	
	public boolean isHoliday(Calendar c){
		Calendar cal = CalendarUtil.truncate((Calendar)c.clone());
		int year = cal.get(Calendar.YEAR);
		return getHolidays(year).contains(cal);
	}
	
	abstract public Set<Calendar> getHolidays(int year);
	
	abstract public void init(String country, String...args) throws IOException, InstantiationException, IllegalAccessException;
	
}
