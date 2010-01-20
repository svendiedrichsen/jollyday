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
	
	abstract public void init(String country, String...args) throws IOException;

	protected Calendar getEasterSunday(int year){
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
		Calendar easterSunday = CalendarUtil.create();
		easterSunday.set(year, (month == 3 ? Calendar.MARCH : Calendar.APRIL), day);
		return easterSunday;
	}
	
}
