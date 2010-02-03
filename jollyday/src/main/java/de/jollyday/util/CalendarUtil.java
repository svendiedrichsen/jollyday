/**
 * Copyright 2010 Sven Diedrichsen 
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 * 
 * http://www.apache.org/licenses/LICENSE-2.0 
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an 
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either 
 * express or implied. See the License for the specific language 
 * governing permissions and limitations under the License. 
 */
package de.jollyday.util;

import java.util.HashSet;
import java.util.Set;

import org.joda.time.Chronology;
import org.joda.time.DateTimeConstants;
import org.joda.time.Interval;
import org.joda.time.LocalDate;
import org.joda.time.chrono.GregorianChronology;
import org.joda.time.chrono.IslamicChronology;
import org.joda.time.chrono.JulianChronology;

import de.jollyday.config.Fixed;

/**
 * Utility class for date operations.
 * @author Sven Diedrichsen
 *
 */
public abstract class CalendarUtil {

	/**
	 * Creates the current date within the gregorian calendar.
	 * @return today
	 */
	public static LocalDate create() {
		return new LocalDate(GregorianChronology.getInstance());
	}
	
	/**
	 * Creates the given date within the julian/gregorian chronology.
	 * @param year
	 * @param month
	 * @param day
	 * @return Gregorian/julian date.
	 */
	public static LocalDate create(int year, int month, int day){
		Chronology c = ( year <= 1583 ? JulianChronology.getInstance() : GregorianChronology.getInstance()); 
		return create(year, month, day, c);
	}

	/**
	 * Creates the date within the provided chronology.
	 * @param year
	 * @param month
	 * @param day
	 * @param c
	 * @return date
	 */
	public static LocalDate create(int year, int month, int day, Chronology c){
		return new LocalDate(year, month, day, c);
	}
	
	/**
	 * Creates the date from the month/day within the specified year.
	 * @param year
	 * @param fixed
	 * @return
	 */
	public static LocalDate create(int year, Fixed fixed){
		return create(year, XMLUtil.getMonth(fixed.getMonth()), fixed.getDay());
	}

	/**
	 * Returns the easter sunday for a given year.
	 * @param year 
	 * @return Easter sunday.
	 */
	public static LocalDate getEasterSunday(int year){
		if (year <= 1583) 
		{   
			return getJulianEasterSunday(year);
		}	
		else
		{   
			return getGregorianEasterSunday(year);
		}
	}
	
	/**
	 * Returns the easter sunday within the julian chronology.
	 * @param year
	 * @return julian easter sunday
	 */
	public static LocalDate getJulianEasterSunday(int year){
		int a,b,c,d,e;
		int x,month,day;
		a = year%4;
		b = year%7;
		c = year%19;
		d = (19*c+15)%30;
		e = (2*a+4*b-d+34)%7;
		x = d+e+114;
		month = x/31;
		day = (x%31)+1;
		return create(year, (month == 3 ? DateTimeConstants.MARCH : DateTimeConstants.APRIL), day, JulianChronology.getInstance());
	}
	
	/**
	 * Returns the easter sunday within the gregorian chronology.
	 * @param year
	 * @return gregorian easter sunday.
	 */
	public static LocalDate getGregorianEasterSunday(int year){
		int a,b,c,d,e,f,g,h,i,j,k,l;
		int x,month,day;
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
		return create(year, (month == 3 ? DateTimeConstants.MARCH : DateTimeConstants.APRIL), day, GregorianChronology.getInstance());
	}

	/**
	 * Returns if this date is on a wekkend.
	 * @param date
	 * @return is weekend
	 */
	public static boolean isWeekend(LocalDate date) {
		return date.getDayOfWeek() == DateTimeConstants.SATURDAY
			|| date.getDayOfWeek() == DateTimeConstants.SUNDAY; 
	}
	
	/**
	 * Returns a set of gregorian dates within a gregorian year which equal the islamic
	 * month and day. Because the islamic year is about 11 days shorter than the gregorian
	 * there may be more than one occurrence of an islamic 1/1 in an gregorian year.
	 * i.e.: In the gregorian year 2008 there where two 1/1. They occurred on 1/10 and 12/29.  
	 * @param gregorianYear
	 * @param islamicMonth
	 * @param islamicDay
	 * @return List of gregorian dates for the islamic month/day.
	 */
	public static Set<LocalDate> getIslamicHolidaysInGregorianYear(int gregorianYear, int islamicMonth, int islamicDay){
		Set<LocalDate> holidays = new HashSet<LocalDate>();
		
		LocalDate firstDayG = new LocalDate(gregorianYear, DateTimeConstants.JANUARY, 1, GregorianChronology.getInstance());
		LocalDate lastDayG = new LocalDate(gregorianYear, DateTimeConstants.DECEMBER, 31, GregorianChronology.getInstance());
		
		LocalDate firstDayI = new LocalDate(firstDayG.toDateTimeAtStartOfDay().getMillis(), IslamicChronology.getInstance());
		LocalDate lastDayI = new LocalDate(lastDayG.toDateTimeAtStartOfDay().getMillis(), IslamicChronology.getInstance());
		
		Interval interv = new Interval(firstDayI.toDateTimeAtStartOfDay(), lastDayI.plusDays(1).toDateTimeAtStartOfDay());
		
		int islamicYear = firstDayI.getYear();
		
		for(;islamicYear <= lastDayI.getYear();){
			LocalDate d = new LocalDate(islamicYear, islamicMonth, islamicDay, IslamicChronology.getInstance());
			if(interv.contains(d.toDateTimeAtStartOfDay())){
				holidays.add(convertToGregorianDate(d));
			}
			islamicYear++;
		}
		
		return holidays;
	}

	/**
	 * Takes converts the provided date into a date within the gregorian chronology.
	 * @param date
	 */
	public static LocalDate convertToGregorianDate(LocalDate date) {
		return new LocalDate(date.toDateTimeAtStartOfDay().getMillis(), GregorianChronology.getInstance());
	}

}
