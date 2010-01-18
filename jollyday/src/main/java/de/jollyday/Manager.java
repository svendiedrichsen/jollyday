package de.jollyday;

import java.util.Calendar;
import java.util.Set;

import de.jollyday.util.CalendarUtil;

public abstract class Manager {

	private static Manager MANAGER = null;

	public static final Manager getInstance() {
		if (null == MANAGER) {
		}
		return MANAGER;
	}
	
	public boolean isHoliday(Calendar c){
		Calendar cal = CalendarUtil.truncate((Calendar)c.clone());
		int year = cal.get(Calendar.YEAR);
		return getHolidays(year).contains(cal);
	}
	
	public Set<Calendar> getHolidays(int year){
		return null;
	}

}
