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
import org.joda.time.DateTimeZone;

/**
 * @author svdi1de
 *
 */
public class HebrewChronology extends BasicChronology {

	private static final long serialVersionUID = -4095519300332478503L;
	
    /** The lowest year that can be fully supported. */
    private static final int MIN_YEAR = -292269054;

    /** The highest year that can be fully supported. */
    private static final int MAX_YEAR = 292272992;
	
    private static final long PARTS_OF_AN_HOUR = 1080L;
    private static final long SYNODIC_MONTH_MILLIS = 
    	29L * DateTimeConstants.MILLIS_PER_DAY + 12L * DateTimeConstants.MILLIS_PER_HOUR + DateTimeConstants.MILLIS_PER_HOUR * 793L / PARTS_OF_AN_HOUR;

	private static final long LEAP_CYCLE_YEARS = 19L;
	private static final long LEAP_CYCLE_MILLIS = 12L * 12L * SYNODIC_MONTH_MILLIS  + 7L * 13L * SYNODIC_MONTH_MILLIS ;
	
	private static final DateTime BEGIN; 

	private static Map<DateTimeZone, HebrewChronology> cCache = new HashMap<DateTimeZone, HebrewChronology>();
	
    /** Singleton instance of a UTC HinduChronology */
    private static final HebrewChronology INSTANCE_UTC;
    static {
        // init after static fields
        INSTANCE_UTC = getInstance(DateTimeZone.UTC);
        BEGIN = new DateTime(-3760,DateTimeConstants.FEBRUARY, 2, 0,0,0,0, JulianChronology.getInstanceUTC());
    }

    public static HebrewChronology getInstance(){
    	return getInstance(DateTimeZone.getDefault());
    }

    public static HebrewChronology getInstanceUTC(){
    	return INSTANCE_UTC;
    }

    public static HebrewChronology getInstance(DateTimeZone zone){
    	if(zone == null){
    		zone = DateTimeZone.getDefault();
    	}
    	if(!cCache.containsKey(zone)){
    		HebrewChronology chrono = new HebrewChronology(null, null);
            cCache.put(zone, chrono);
    	}
    	return cCache.get(zone);
    }

    /**
     * @param base
     * @param param
     * @param minDaysInFirstWeek
     */
    HebrewChronology(Chronology base, Object param) {
    	super(base, param, 4);
    }
    
	@Override
	int getYear(long instant) {
		long hebrewMillis = instant - BEGIN.getMillis();
		return (int)((hebrewMillis / SYNODIC_MONTH_MILLIS - hebrewMillis / LEAP_CYCLE_MILLIS * 7L) / 12L) + 1;
	}
	
	/* (non-Javadoc)
	 * @see org.joda.time.chrono.BasicChronology#calculateFirstDayOfYearMillis(int)
	 */
	@Override
	long calculateFirstDayOfYearMillis(int year) {
		year--;
		return year * 12L * SYNODIC_MONTH_MILLIS + year / LEAP_CYCLE_YEARS * 7L * SYNODIC_MONTH_MILLIS;
	}

	/* (non-Javadoc)
	 * @see org.joda.time.chrono.BasicChronology#getApproxMillisAtEpochDividedByTwo()
	 */
	@Override
	long getApproxMillisAtEpochDividedByTwo() {
		return BEGIN.getMillis() / 2;
	}

	/* (non-Javadoc)
	 * @see org.joda.time.chrono.BasicChronology#getAverageMillisPerMonth()
	 */
	@Override
	long getAverageMillisPerMonth() {
		return getAverageMillisPerYear() / 12L;
	}

	/* (non-Javadoc)
	 * @see org.joda.time.chrono.BasicChronology#getAverageMillisPerYear()
	 */
	@Override
	long getAverageMillisPerYear() {
		return (12L * 12L * SYNODIC_MONTH_MILLIS + 7L * 13L * SYNODIC_MONTH_MILLIS) / 19L;
	}

	/* (non-Javadoc)
	 * @see org.joda.time.chrono.BasicChronology#getAverageMillisPerYearDividedByTwo()
	 */
	@Override
	long getAverageMillisPerYearDividedByTwo() {
		return getAverageMillisPerYear() / 2;
	}

	/* (non-Javadoc)
	 * @see org.joda.time.chrono.BasicChronology#getDaysInMonthMax(int)
	 */
	@Override
	int getDaysInMonthMax(int month) {
		return month % 2 == 0 ? 29 : 30;
	}

	/* (non-Javadoc)
	 * @see org.joda.time.chrono.BasicChronology#getDaysInYearMonth(int, int)
	 */
	@Override
	int getDaysInYearMonth(int year, int month) {
		if(isLeapYear(year) && month > 5){
			return getDaysInMonthMax(month - 1);
		}
		return getDaysInMonthMax(month);
	}

	/* (non-Javadoc)
	 * @see org.joda.time.chrono.BasicChronology#getMaxYear()
	 */
	@Override
	int getMaxYear() {
		return MAX_YEAR;
	}

	/* (non-Javadoc)
	 * @see org.joda.time.chrono.BasicChronology#getMinYear()
	 */
	@Override
	int getMinYear() {
		return MIN_YEAR;
	}

	/* (non-Javadoc)
	 * @see org.joda.time.chrono.BasicChronology#getMaxMonth(int)
	 */
	@Override
	int getMaxMonth(int year) {
		return isLeapYear(year) ? 13 : 12;
	}
	
	/* (non-Javadoc)
	 * @see org.joda.time.chrono.BasicChronology#getMaxMonth()
	 */
	@Override
	int getMaxMonth() {
		return 13;
	}
	
	/* (non-Javadoc)
	 * @see org.joda.time.chrono.BasicChronology#getDaysInYear(int)
	 */
	@Override
	int getDaysInYear(int year) {
		return (int)(((long)getMaxMonth(year)) * SYNODIC_MONTH_MILLIS / DateTimeConstants.MILLIS_PER_DAY);
	}
	
	/* (non-Javadoc)
	 * @see org.joda.time.chrono.BasicChronology#getDaysInMonthMax(long)
	 */
	@Override
	int getDaysInMonthMax(long instant) {
		int year = getYear(instant);
		int month = getMonthOfYear(instant, year);
		return getDaysInYearMonth(year, month);
	}
	
	/* (non-Javadoc)
	 * @see org.joda.time.chrono.BasicChronology#getDaysInYearMax()
	 */
	@Override
	int getDaysInYearMax() {
		return 385;
	}
	
	/* (non-Javadoc)
	 * @see org.joda.time.chrono.BasicChronology#getDaysInMonthMax()
	 */
	@Override
	int getDaysInMonthMax() {
		return 30;
	}
	
	/* (non-Javadoc)
	 * @see org.joda.time.chrono.BasicChronology#getMonthOfYear(long, int)
	 */
	@Override
	int getMonthOfYear(long millis, int year) {
		long millisInYear = millis - getYearMillis(year);
		return (int)(millisInYear / SYNODIC_MONTH_MILLIS) + 1;
	}
	
	/* (non-Javadoc)
	 * @see org.joda.time.chrono.BasicChronology#getYearMillis(int)
	 */
	@Override
	long getYearMillis(int year) {
		year--;
		return BEGIN.getMillis() + ((long)year) * 12L * SYNODIC_MONTH_MILLIS + ((long)year) / 19L * 7L * SYNODIC_MONTH_MILLIS;
	}

	/* (non-Javadoc)
	 * @see org.joda.time.chrono.BasicChronology#getTotalMillisByYearMonth(int, int)
	 */
	@Override
	long getTotalMillisByYearMonth(int year, int month) {
		long millis = 0;
		for(int i = 1; i < month; i++){
			millis += ((long)getDaysInYearMonth(year, i)) * DateTimeConstants.MILLIS_PER_DAY;
		}
		return millis;
	}

	/* (non-Javadoc)
	 * @see org.joda.time.chrono.BasicChronology#getYearDifference(long, long)
	 */
	@Override
	long getYearDifference(long minuendInstant, long subtrahendInstant) {
		return (minuendInstant - subtrahendInstant) / SYNODIC_MONTH_MILLIS / 12L;
	}

	/* (non-Javadoc)
	 * @see org.joda.time.chrono.BasicChronology#isLeapYear(int)
	 */
	@Override
	boolean isLeapYear(int year) {
		int mod = year % 19;
		return mod == 0 || mod == 3 || mod == 6 || mod == 8 || mod == 11
				|| mod == 14 || mod == 17;
	}

	/* (non-Javadoc)
	 * @see org.joda.time.chrono.BasicChronology#setYear(long, int)
	 */
	@Override
	long setYear(long instant, int year) {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see org.joda.time.chrono.BaseChronology#withUTC()
	 */
	@Override
	public Chronology withUTC() {
		return INSTANCE_UTC;
	}

	/* (non-Javadoc)
	 * @see org.joda.time.chrono.BaseChronology#withZone(org.joda.time.DateTimeZone)
	 */
	@Override
	public Chronology withZone(DateTimeZone zone) {
        if (zone == null) {
            zone = DateTimeZone.getDefault();
        }
        if (zone == getZone()) {
            return this;
        }
        return getInstance(zone);
	}

}
