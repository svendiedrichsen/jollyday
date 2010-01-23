package de.jollyday.util;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public abstract class CalendarUtil {

	private static final Map<String, Integer> WEEKDAYS = new HashMap<String, Integer>();
	
	static{
		WEEKDAYS.put("SUNDAY", Calendar.SUNDAY);
		WEEKDAYS.put("MONDAY", Calendar.MONDAY);
		WEEKDAYS.put("TUESDAY", Calendar.TUESDAY);
		WEEKDAYS.put("WEDNESDAY", Calendar.WEDNESDAY);
		WEEKDAYS.put("THURSDAY", Calendar.THURSDAY);
		WEEKDAYS.put("FRIDAY", Calendar.FRIDAY);
		WEEKDAYS.put("SATURDAY", Calendar.SATURDAY);
	}
	
	public static int getWeekday(String name){
		return WEEKDAYS.get(name);
	}

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

	public static Calendar getEasterSunday(int year){
		int a,b,c,d,e,f,g,h,i,j,k,l;
		int x,month,day;
		
		if (year <= 1583) 
		{   
			a = year%4;
			b = year%7;
			c = year%19;
			d = (19*c+15)%30;
			e = (2*a+4*b-d+34)%7;
			x = d+e+114;
			month = x/31;
			day = (x%31)+1;
		}	
		else
		{   
	 		a = year%19;
			b = year/100;
			c = year%100;
			d = b/4;
			e = b%4;
			f = (b+8)/25;
			g = (b-f+1)/3;
			h = (19*a+b-d-g+15)%30;
			i = c/4;
			j = c%4;
			k = (32+2*e+2*i-h-j)%7;
			l = (a+11*h+22*k)/451;
			x = h+k-7*l+114;
			month = x/31;
			day = (x%31)+1;	
		}
		Calendar easterSunday = create();
		easterSunday.set(year, (month == 3 ? Calendar.MARCH : Calendar.APRIL), day);
		return easterSunday;
	}

}
