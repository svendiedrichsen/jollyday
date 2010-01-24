package de.jollyday.util;

import java.util.Calendar;

import de.jollyday.config.Weekday;


public class XMLUtil {

	public static final int getWeekday(Weekday w){
		switch(w){
			case MONDAY: return Calendar.MONDAY;
			case TUESDAY: return Calendar.TUESDAY;
			case WEDNESDAY: return Calendar.WEDNESDAY;
			case THURSDAY: return Calendar.THURSDAY;
			case FRIDAY: return Calendar.FRIDAY;
			case SATURDAY: return Calendar.SATURDAY;
			case SUNDAY: return Calendar.SUNDAY;
			default:
				throw new IllegalArgumentException("Unknown weekday "+w);
		}
	}
	
}
