package de.jollyday.util;

import org.joda.time.DateTimeConstants;
import org.joda.time.LocalDate;
import org.joda.time.chrono.GregorianChronology;

import de.jollyday.config.Fixed;

public abstract class CalendarUtil {

	public static LocalDate create() {
		return new LocalDate(GregorianChronology.getInstance());
	}
	
	public static LocalDate create(int year, int month, int day){
		return new LocalDate(year, month, day, GregorianChronology.getInstance());
	}
	
	public static LocalDate create(int year, Fixed fixed){
		return create(year, XMLUtil.getMonth(fixed.getMonth()), fixed.getDay());
	}

	public static LocalDate getEasterSunday(int year){
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
		return create(year, (month == 3 ? DateTimeConstants.MARCH : DateTimeConstants.APRIL), day);
	}

}
