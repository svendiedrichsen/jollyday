package de.jollyday.util;

import java.util.Calendar;

public abstract class CalendarUtil {

	public static Calendar truncate(Calendar c) {
		c.clear(Calendar.HOUR);
		c.clear(Calendar.MINUTE);
		c.clear(Calendar.SECOND);
		c.clear(Calendar.MILLISECOND);
		return c;
	}

	public static Calendar create() {
		return truncate(Calendar.getInstance());
	}
	
	public static Calendar create(int year, int month, int day){
		Calendar c = create();
		c.set(year, month, day);
		return c;
	}

}
