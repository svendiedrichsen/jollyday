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
package org.joda.time.chrono;

import java.util.HashMap;
import java.util.Map;

import org.joda.time.Chronology;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeZone;
import org.joda.time.MutableDateTime;

/**
 * The indian public chronology represents the official indian calendar
 * used by the indian government.
 * It is implemented like described in 
 * http://en.wikipedia.org/wiki/Indian_national_calendar.
 * 
 * @author Sven Diedrichsen
 *
 */
public class IndianPublicChronology extends BasicChronology {
	
	private static final long serialVersionUID = 6685772155652627821L;

	private static GregorianChronology GREGORIAN = GregorianChronology.getInstance();

	private static final long MILLIS_PER_YEAR =
        (long) (365.25 * DateTimeConstants.MILLIS_PER_DAY);

    private static final long MILLIS_PER_MONTH =
        (long) (365.25 * DateTimeConstants.MILLIS_PER_DAY / 12);

    /** The lowest year that can be fully supported. */
    private static final int MIN_YEAR = -292269054;

    /** The highest year that can be fully supported. */
    private static final int MAX_YEAR = 292272992;

    /** The typical millis per year. */
    private static final long MILLIS_PER_SHORT_YEAR = 365L * DateTimeConstants.MILLIS_PER_DAY;

    /** The typical millis per year. */
    private static final long MILLIS_PER_LONG_YEAR = 366L * DateTimeConstants.MILLIS_PER_DAY;

    /** The length of the cycle of leap years. */
    private static final int CYCLE = 4;

    /** The millis of a 30 year cycle. */
    private static final long MILLIS_PER_CYCLE = ((3L * 365L + 1L * 366L) * DateTimeConstants.MILLIS_PER_DAY);

    /** The millis of 0001-01-01. */
    private static final long MILLIS_YEAR_1 = new DateTime(79, 1, 1, 0 ,0 ,0 ,0 ,GREGORIAN).getMillis();

    private static final long MILLIS_IN_YEAR = 80L * DateTimeConstants.MILLIS_PER_DAY;
    
    /** A singleton era field. */
    private static final DateTimeField ERA_FIELD = new BasicSingleEraDateTimeField("SAKA");

    private static Map<DateTimeZone, IndianPublicChronology> cCache = new HashMap<DateTimeZone, IndianPublicChronology>();
    
    /** Singleton instance of a UTC HinduChronology */
    private static final IndianPublicChronology INSTANCE_UTC;
    static {
        // init after static fields
        INSTANCE_UTC = getInstance(DateTimeZone.UTC);
    }

    public static IndianPublicChronology getInstance(){
    	return getInstance(DateTimeZone.getDefault());
    }
    
    public static IndianPublicChronology getInstance(DateTimeZone zone){
    	if(zone == null){
    		zone = DateTimeZone.getDefault();
    	}
    	if(!cCache.containsKey(zone)){
            IndianPublicChronology chrono = new IndianPublicChronology(null, null);
            cCache.put(zone, chrono);
    	}
    	return cCache.get(zone);
    }
    
    /**
     * @param base
     * @param param
     * @param minDaysInFirstWeek
     */
    IndianPublicChronology(Chronology base, Object param) {
    	super(base, param, 4);
    }

    /**
     * Serialization singleton.
     */
    private Object readResolve() {
        Chronology base = getBase();
        return base == null ? withUTC() : getInstance(base.getZone());
    }

    /**
     * Gets the Chronology in the UTC time zone.
     * 
     * @return the chronology in UTC
     */
    public Chronology withUTC() {
        return INSTANCE_UTC;
    }

    /**
     * Gets the Chronology in a specific time zone.
     * 
     * @param zone  the zone to get the chronology in, null is default
     * @return the chronology
     */
    public Chronology withZone(DateTimeZone zone) {
        if (zone == null) {
            zone = DateTimeZone.getDefault();
        }
        if (zone == getZone()) {
            return this;
        }
        return getInstance(zone);
    }

    /**
     * A suitable hash code for the chronology.
     * 
     * @return the hash code
     * @since 1.6
     */
    public int hashCode() {
        return super.hashCode() * 13;
    }
	
	public String toString() {
        String str = "HinduChronology";
        DateTimeZone zone = getZone();
        if (zone != null) {
            str = str + '[' + zone.getID() + ']';
        }
        return str;
    }
	
	/* (non-Javadoc)
	 * @see org.joda.time.chrono.BasicChronology#getYear(long)
	 */
	@Override
	int getYear(long instant) {
        long millisHindu = instant - (MILLIS_YEAR_1 - MILLIS_IN_YEAR);
        long cycles = millisHindu / MILLIS_PER_CYCLE;
        long cycleRemainder = millisHindu % MILLIS_PER_CYCLE;
        
        int year = (int) (cycles * CYCLE);
        long yearMillis = (isLeapYear(year) ? MILLIS_PER_LONG_YEAR : MILLIS_PER_SHORT_YEAR);
        while (cycleRemainder >= yearMillis) {
            cycleRemainder -= yearMillis;
            yearMillis = (isLeapYear(++year) ? MILLIS_PER_LONG_YEAR : MILLIS_PER_SHORT_YEAR);
        }
        return year;
	}

	/* (non-Javadoc)
	 * @see org.joda.time.chrono.BasicChronology#calculateFirstDayOfYearMillis(int)
	 */
	@Override
	long calculateFirstDayOfYearMillis(int year) {
        if (year > MAX_YEAR) {
            throw new ArithmeticException("Year is too large: " + year + " > " + MAX_YEAR);
        }
        if (year < MIN_YEAR) {
            throw new ArithmeticException("Year is too small: " + year + " < " + MIN_YEAR);
        }

        // Java epoch is 1970-01-01 Gregorian which is 2048-03-22 Hindu.
        // 0001-01-01 Hindu is 0079-03-22 -> millis
        // would prefer to calculate against year zero, but leap year
        // can be in that year so it doesn't work
        year--;
        long cycles = year / CYCLE;
        long millis = MILLIS_YEAR_1 + cycles * MILLIS_PER_CYCLE;
        int cycleRemainder = (year % CYCLE) + 1;
        
        for (int i = 1; i < cycleRemainder; i++) {
            long yearMillis = isLeapYear(i) ? MILLIS_PER_LONG_YEAR : MILLIS_PER_SHORT_YEAR;
			millis += yearMillis;
        }

        MutableDateTime date = new MutableDateTime(millis, GREGORIAN);
        if(GREGORIAN.isLeapYear(date.getYear())){
        	date.setMonthOfYear(DateTimeConstants.MARCH);
        	date.setDayOfMonth(21);
        }else{
        	date.setMonthOfYear(DateTimeConstants.MARCH);
        	date.setDayOfMonth(22);
        }
        millis = date.getMillis();
       	return millis;
	}

	/* (non-Javadoc)
	 * @see org.joda.time.chrono.BasicChronology#getApproxMillisAtEpochDividedByTwo()
	 */
	@Override
	long getApproxMillisAtEpochDividedByTwo() {
		return (-MILLIS_YEAR_1) / 2;
	}

	/* (non-Javadoc)
	 * @see org.joda.time.chrono.BasicChronology#getAverageMillisPerMonth()
	 */
	@Override
	long getAverageMillisPerMonth() {
		return MILLIS_PER_MONTH;
	}

	/* (non-Javadoc)
	 * @see org.joda.time.chrono.BasicChronology#getAverageMillisPerYear()
	 */
	@Override
	long getAverageMillisPerYear() {
		return MILLIS_PER_YEAR;
	}

	/* (non-Javadoc)
	 * @see org.joda.time.chrono.BasicChronology#getAverageMillisPerYearDividedByTwo()
	 */
	@Override
	long getAverageMillisPerYearDividedByTwo() {
		return MILLIS_PER_YEAR / 2;
	}

	/* (non-Javadoc)
	 * @see org.joda.time.chrono.BasicChronology#getDaysInMonthMax(int)
	 */
	@Override
	int getDaysInMonthMax(int month) {
		if(month == 1) return 30;
		if(month < 7) return 31;
		return 30;
	}

	/* (non-Javadoc)
	 * @see org.joda.time.chrono.BasicChronology#getDaysInYearMonth(int, int)
	 */
	@Override
	int getDaysInYearMonth(int year, int month) {
		if(isLeapYear(year) && month == 1) return 31;
		return getDaysInMonthMax(month);
	}

	/* (non-Javadoc)
	 * @see org.joda.time.chrono.BasicChronology#getMaxYear()
	 */
	@Override
	int getMaxYear() {
		// TODO Auto-generated method stub
		return MAX_YEAR;
	}

	/* (non-Javadoc)
	 * @see org.joda.time.chrono.BasicChronology#getMinYear()
	 */
	@Override
	int getMinYear() {
		// TODO Auto-generated method stub
		return MIN_YEAR;
	}

	/* (non-Javadoc)
	 * @see org.joda.time.chrono.BasicChronology#getMonthOfYear(long, int)
	 */
	@Override
	int getMonthOfYear(long millis, int year) {
		long millisInYear = millis - getYearMillis(year);
		int month = 0;
		do{
			month++;
			long value = ((long)getDaysInYearMonth(year, month)) * DateTimeConstants.MILLIS_PER_DAY;
			millisInYear -= value;
		}while(millisInYear > 0 && month < 13);
		return month;
	}

	/* (non-Javadoc)
	 * @see org.joda.time.chrono.BasicChronology#getTotalMillisByYearMonth(int, int)
	 */
	@Override
	long getTotalMillisByYearMonth(int year, int month) {
		long millis = 0;
		int tempMonth = 1;
		while(tempMonth < month){
			millis += ((long)getDaysInYearMonth(year, tempMonth)) * DateTimeConstants.MILLIS_PER_DAY;
			tempMonth++;
		}
		return millis;
	}

	/* (non-Javadoc)
	 * @see org.joda.time.chrono.BasicChronology#getYearDifference(long, long)
	 */
	@Override
	long getYearDifference(long minuendInstant, long subtrahendInstant) {
        // optimsed implementation of getDifference, due to fixed months
        int minuendYear = getYear(minuendInstant);
        int subtrahendYear = getYear(subtrahendInstant);

        // Inlined remainder method to avoid duplicate calls to get.
        long minuendRem = minuendInstant - getYearMillis(minuendYear);
        long subtrahendRem = subtrahendInstant - getYearMillis(subtrahendYear);

        int difference = minuendYear - subtrahendYear;
        if (minuendRem < subtrahendRem) {
            difference--;
        }
        return difference;
	}

	/* (non-Javadoc)
	 * @see org.joda.time.chrono.BasicChronology#isLeapYear(int)
	 */
	@Override
	boolean isLeapYear(int year) {
		return GREGORIAN.isLeapYear(year + 78);
	}

	/* (non-Javadoc)
	 * @see org.joda.time.chrono.BasicChronology#setYear(long, int)
	 */
	@Override
	long setYear(long instant, int year) {
        // optimsed implementation of set, due to fixed months
        int thisYear = getYear(instant);
        int dayOfYear = getDayOfYear(instant, thisYear);
        int millisOfDay = getMillisOfDay(instant);

        if (dayOfYear > 365) {
            // Current year is leap, and day is leap.
            if (!isLeapYear(year)) {
                // Moving to a non-leap year, leap day doesn't exist.
                dayOfYear--;
            }
        }

        instant = getYearMonthDayMillis(year, 1, dayOfYear);
        instant += millisOfDay;
        return instant;
	}

    //-----------------------------------------------------------------------
    protected void assemble(Fields fields) {
        if (getBase() == null) {
            super.assemble(fields);
            fields.era = ERA_FIELD;
            fields.monthOfYear = new BasicMonthOfYearDateTimeField(this, 1);
            fields.months = fields.monthOfYear.getDurationField();
        }
    }

}
