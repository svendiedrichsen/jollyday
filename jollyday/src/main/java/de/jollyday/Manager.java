package de.jollyday;

import java.util.Calendar;
import java.util.Properties;
import java.util.Set;

import org.joda.time.LocalDate;

public abstract class Manager {
	
	private static final String MANAGER_IMPL_CLASS_PREFIX = "manager.impl.";
	private static final String CONFIG_FILE = "application.properties";

	public static final Manager getInstance(String country) throws Exception{
		Properties props = new Properties();
		props.load(ClassLoader.getSystemResourceAsStream(CONFIG_FILE));
		Manager m = (Manager)Class.forName(props.getProperty(MANAGER_IMPL_CLASS_PREFIX+country)).newInstance();
		m.init(country);
		return m;
	}
	
	public boolean isHoliday(Calendar c, String...args){
		return isHoliday(new LocalDate(c), args);
	}
	
	public boolean isHoliday(LocalDate c, String...args){
		return getHolidays(c.getYear(), args).contains(c);
	}
	
	abstract public Set<LocalDate> getHolidays(int year, String...args);
	
	abstract public void init(String country) throws Exception;
	
}
