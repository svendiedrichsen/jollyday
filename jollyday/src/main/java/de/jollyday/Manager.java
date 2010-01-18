package de.jollyday;

import java.util.Calendar;
import java.util.Set;

public class Manager {

	private static Manager MANAGER = null;

	private Manager() {
	}

	public static final Manager getInstance() {
		if (null == MANAGER) {
			MANAGER = new Manager();
		}
		return MANAGER;
	}
	
	public boolean isHoliday(Calendar c){
		Calendar cal = (Calendar)c.clone();
		cal.clear(Calendar.HOUR);
		cal.clear(Calendar.MINUTE);
		cal.clear(Calendar.SECOND);
		cal.clear(Calendar.MILLISECOND);
		int year = cal.get(Calendar.YEAR);
		return getHolidays(year).contains(cal);
	}
	
	public Set<Calendar> getHolidays(int year){
		return null;
	}

}
