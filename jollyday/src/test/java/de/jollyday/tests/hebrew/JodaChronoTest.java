/*
 *  Copyright 2001-2005 Stephen Colebourne
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package de.jollyday.tests.hebrew;

import java.util.Locale;
import java.util.TimeZone;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.joda.time.Chronology;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeUtils;
import org.joda.time.DateTimeZone;
import org.joda.time.DurationFieldType;
import org.joda.time.DateTime.Property;
import org.joda.time.chrono.HebrewChronology;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.chrono.JulianChronology;

/**
 * This class is a Junit unit test for HebrewChronology.
 *
 * @author Stephen Colebourne
 */
public class JodaChronoTest extends TestCase {

	private static final long PARTS_OF_AN_HOUR = 1080L;

	private static final long LEAP_CYCLE_YEARS = 19L;

    private static long SKIP = 1 * DateTimeConstants.MILLIS_PER_DAY;

    private static final DateTimeZone PARIS = DateTimeZone.forID("Europe/Paris");
    private static final DateTimeZone LONDON = DateTimeZone.forID("Europe/London");
    private static final DateTimeZone TOKYO = DateTimeZone.forID("Asia/Tokyo");
    private static final Chronology HEBREW_UTC = HebrewChronology.getInstanceUTC();
    private static final Chronology JULIAN_UTC = JulianChronology.getInstanceUTC();
    private static final Chronology ISO_UTC = ISOChronology.getInstanceUTC();

    long y2002days = 365 + 365 + 366 + 365 + 365 + 365 + 366 + 365 + 365 + 365 + 
                     366 + 365 + 365 + 365 + 366 + 365 + 365 + 365 + 366 + 365 + 
                     365 + 365 + 366 + 365 + 365 + 365 + 366 + 365 + 365 + 365 +
                     366 + 365;
    // 2002-06-09
    private long TEST_TIME_NOW =
            (y2002days + 31L + 28L + 31L + 30L + 31L + 9L -1L) * DateTimeConstants.MILLIS_PER_DAY;

    private DateTimeZone originalDateTimeZone = null;
    private TimeZone originalTimeZone = null;
    private Locale originalLocale = null;

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static TestSuite suite() {
        SKIP = 1 * DateTimeConstants.MILLIS_PER_DAY;
        return new TestSuite(JodaChronoTest.class);
    }

    public JodaChronoTest(String name) {
        super(name);
    }

    protected void setUp() throws Exception {
        DateTimeUtils.setCurrentMillisFixed(TEST_TIME_NOW);
        originalDateTimeZone = DateTimeZone.getDefault();
        originalTimeZone = TimeZone.getDefault();
        originalLocale = Locale.getDefault();
        DateTimeZone.setDefault(LONDON);
        TimeZone.setDefault(TimeZone.getTimeZone("Europe/London"));
        Locale.setDefault(Locale.UK);
    }

    protected void tearDown() throws Exception {
        DateTimeUtils.setCurrentMillisSystem();
        DateTimeZone.setDefault(originalDateTimeZone);
        TimeZone.setDefault(originalTimeZone);
        Locale.setDefault(originalLocale);
        originalDateTimeZone = null;
        originalTimeZone = null;
        originalLocale = null;
    }

    //-----------------------------------------------------------------------
    public void testFactoryUTC() {
        assertEquals(DateTimeZone.UTC, HebrewChronology.getInstanceUTC().getZone());
        assertSame(HebrewChronology.class, HebrewChronology.getInstanceUTC().getClass());
    }

    public void testFactory() {
        assertEquals(LONDON, HebrewChronology.getInstance().getZone());
        assertSame(HebrewChronology.class, HebrewChronology.getInstance().getClass());
    }

    public void testFactory_Zone() {
        assertEquals(TOKYO, HebrewChronology.getInstance(TOKYO).getZone());
        assertEquals(PARIS, HebrewChronology.getInstance(PARIS).getZone());
        assertEquals(LONDON, HebrewChronology.getInstance(null).getZone());
        assertSame(HebrewChronology.class, HebrewChronology.getInstance(TOKYO).getClass());
    }

    //-----------------------------------------------------------------------
    public void testEquality() {
        assertSame(HebrewChronology.getInstance(TOKYO), HebrewChronology.getInstance(TOKYO));
        assertSame(HebrewChronology.getInstance(LONDON), HebrewChronology.getInstance(LONDON));
        assertSame(HebrewChronology.getInstance(PARIS), HebrewChronology.getInstance(PARIS));
        assertSame(HebrewChronology.getInstanceUTC(), HebrewChronology.getInstanceUTC());
        assertSame(HebrewChronology.getInstance(), HebrewChronology.getInstance(LONDON));
    }

    public void testWithUTC() {
        assertSame(HebrewChronology.getInstanceUTC(), HebrewChronology.getInstance(LONDON).withUTC());
        assertSame(HebrewChronology.getInstanceUTC(), HebrewChronology.getInstance(TOKYO).withUTC());
        assertSame(HebrewChronology.getInstanceUTC(), HebrewChronology.getInstanceUTC().withUTC());
        assertSame(HebrewChronology.getInstanceUTC(), HebrewChronology.getInstance().withUTC());
    }

    public void testWithZone() {
        assertSame(HebrewChronology.getInstance(TOKYO), HebrewChronology.getInstance(TOKYO).withZone(TOKYO));
        assertSame(HebrewChronology.getInstance(LONDON), HebrewChronology.getInstance(TOKYO).withZone(LONDON));
        assertSame(HebrewChronology.getInstance(PARIS), HebrewChronology.getInstance(TOKYO).withZone(PARIS));
        assertSame(HebrewChronology.getInstance(LONDON), HebrewChronology.getInstance(TOKYO).withZone(null));
        assertSame(HebrewChronology.getInstance(PARIS), HebrewChronology.getInstance().withZone(PARIS));
        assertSame(HebrewChronology.getInstance(PARIS), HebrewChronology.getInstanceUTC().withZone(PARIS));
    }

    public void testToString() {
        assertEquals("HebrewChronology[Europe/London]", HebrewChronology.getInstance(LONDON).toString());
        assertEquals("HebrewChronology[Asia/Tokyo]", HebrewChronology.getInstance(TOKYO).toString());
        assertEquals("HebrewChronology[Europe/London]", HebrewChronology.getInstance().toString());
        assertEquals("HebrewChronology[UTC]", HebrewChronology.getInstanceUTC().toString());
    }

    //-----------------------------------------------------------------------
    public void testDurationFields() {
        assertEquals("eras", HebrewChronology.getInstance().eras().getName());
        assertEquals("centuries", HebrewChronology.getInstance().centuries().getName());
        assertEquals("years", HebrewChronology.getInstance().years().getName());
        assertEquals("weekyears", HebrewChronology.getInstance().weekyears().getName());
        assertEquals("months", HebrewChronology.getInstance().months().getName());
        assertEquals("weeks", HebrewChronology.getInstance().weeks().getName());
        assertEquals("days", HebrewChronology.getInstance().days().getName());
        assertEquals("halfdays", HebrewChronology.getInstance().halfdays().getName());
        assertEquals("hours", HebrewChronology.getInstance().hours().getName());
        assertEquals("minutes", HebrewChronology.getInstance().minutes().getName());
        assertEquals("seconds", HebrewChronology.getInstance().seconds().getName());
        assertEquals("millis", HebrewChronology.getInstance().millis().getName());
        
        assertEquals(false, HebrewChronology.getInstance().eras().isSupported());
        assertEquals(true, HebrewChronology.getInstance().centuries().isSupported());
        assertEquals(true, HebrewChronology.getInstance().years().isSupported());
        assertEquals(true, HebrewChronology.getInstance().weekyears().isSupported());
        assertEquals(true, HebrewChronology.getInstance().months().isSupported());
        assertEquals(true, HebrewChronology.getInstance().weeks().isSupported());
        assertEquals(true, HebrewChronology.getInstance().days().isSupported());
        assertEquals(true, HebrewChronology.getInstance().halfdays().isSupported());
        assertEquals(true, HebrewChronology.getInstance().hours().isSupported());
        assertEquals(true, HebrewChronology.getInstance().minutes().isSupported());
        assertEquals(true, HebrewChronology.getInstance().seconds().isSupported());
        assertEquals(true, HebrewChronology.getInstance().millis().isSupported());
        
        assertEquals(false, HebrewChronology.getInstance().centuries().isPrecise());
        assertEquals(false, HebrewChronology.getInstance().years().isPrecise());
        assertEquals(false, HebrewChronology.getInstance().weekyears().isPrecise());
        assertEquals(false, HebrewChronology.getInstance().months().isPrecise());
        assertEquals(false, HebrewChronology.getInstance().weeks().isPrecise());
        assertEquals(false, HebrewChronology.getInstance().days().isPrecise());
        assertEquals(false, HebrewChronology.getInstance().halfdays().isPrecise());
        assertEquals(true, HebrewChronology.getInstance().hours().isPrecise());
        assertEquals(true, HebrewChronology.getInstance().minutes().isPrecise());
        assertEquals(true, HebrewChronology.getInstance().seconds().isPrecise());
        assertEquals(true, HebrewChronology.getInstance().millis().isPrecise());
        
        assertEquals(false, HebrewChronology.getInstanceUTC().centuries().isPrecise());
        assertEquals(false, HebrewChronology.getInstanceUTC().years().isPrecise());
        assertEquals(false, HebrewChronology.getInstanceUTC().weekyears().isPrecise());
        assertEquals(false, HebrewChronology.getInstanceUTC().months().isPrecise());
        assertEquals(true, HebrewChronology.getInstanceUTC().weeks().isPrecise());
        assertEquals(true, HebrewChronology.getInstanceUTC().days().isPrecise());
        assertEquals(true, HebrewChronology.getInstanceUTC().halfdays().isPrecise());
        assertEquals(true, HebrewChronology.getInstanceUTC().hours().isPrecise());
        assertEquals(true, HebrewChronology.getInstanceUTC().minutes().isPrecise());
        assertEquals(true, HebrewChronology.getInstanceUTC().seconds().isPrecise());
        assertEquals(true, HebrewChronology.getInstanceUTC().millis().isPrecise());
        
        DateTimeZone gmt = DateTimeZone.forID("Etc/GMT");
        assertEquals(false, HebrewChronology.getInstance(gmt).centuries().isPrecise());
        assertEquals(false, HebrewChronology.getInstance(gmt).years().isPrecise());
        assertEquals(false, HebrewChronology.getInstance(gmt).weekyears().isPrecise());
        assertEquals(false, HebrewChronology.getInstance(gmt).months().isPrecise());
        assertEquals(true, HebrewChronology.getInstance(gmt).weeks().isPrecise());
        assertEquals(true, HebrewChronology.getInstance(gmt).days().isPrecise());
        assertEquals(true, HebrewChronology.getInstance(gmt).halfdays().isPrecise());
        assertEquals(true, HebrewChronology.getInstance(gmt).hours().isPrecise());
        assertEquals(true, HebrewChronology.getInstance(gmt).minutes().isPrecise());
        assertEquals(true, HebrewChronology.getInstance(gmt).seconds().isPrecise());
        assertEquals(true, HebrewChronology.getInstance(gmt).millis().isPrecise());
    }

    public void testDateFields() {
        assertEquals("era", HebrewChronology.getInstance().era().getName());
        assertEquals("centuryOfEra", HebrewChronology.getInstance().centuryOfEra().getName());
        assertEquals("yearOfCentury", HebrewChronology.getInstance().yearOfCentury().getName());
        assertEquals("yearOfEra", HebrewChronology.getInstance().yearOfEra().getName());
        assertEquals("year", HebrewChronology.getInstance().year().getName());
        assertEquals("monthOfYear", HebrewChronology.getInstance().monthOfYear().getName());
        assertEquals("weekyearOfCentury", HebrewChronology.getInstance().weekyearOfCentury().getName());
        assertEquals("weekyear", HebrewChronology.getInstance().weekyear().getName());
        assertEquals("weekOfWeekyear", HebrewChronology.getInstance().weekOfWeekyear().getName());
        assertEquals("dayOfYear", HebrewChronology.getInstance().dayOfYear().getName());
        assertEquals("dayOfMonth", HebrewChronology.getInstance().dayOfMonth().getName());
        assertEquals("dayOfWeek", HebrewChronology.getInstance().dayOfWeek().getName());
        
        assertEquals(true, HebrewChronology.getInstance().era().isSupported());
        assertEquals(true, HebrewChronology.getInstance().centuryOfEra().isSupported());
        assertEquals(true, HebrewChronology.getInstance().yearOfCentury().isSupported());
        assertEquals(true, HebrewChronology.getInstance().yearOfEra().isSupported());
        assertEquals(true, HebrewChronology.getInstance().year().isSupported());
        assertEquals(true, HebrewChronology.getInstance().monthOfYear().isSupported());
        assertEquals(true, HebrewChronology.getInstance().weekyearOfCentury().isSupported());
        assertEquals(true, HebrewChronology.getInstance().weekyear().isSupported());
        assertEquals(true, HebrewChronology.getInstance().weekOfWeekyear().isSupported());
        assertEquals(true, HebrewChronology.getInstance().dayOfYear().isSupported());
        assertEquals(true, HebrewChronology.getInstance().dayOfMonth().isSupported());
        assertEquals(true, HebrewChronology.getInstance().dayOfWeek().isSupported());
    }

    public void testTimeFields() {
        assertEquals("halfdayOfDay", HebrewChronology.getInstance().halfdayOfDay().getName());
        assertEquals("clockhourOfHalfday", HebrewChronology.getInstance().clockhourOfHalfday().getName());
        assertEquals("hourOfHalfday", HebrewChronology.getInstance().hourOfHalfday().getName());
        assertEquals("clockhourOfDay", HebrewChronology.getInstance().clockhourOfDay().getName());
        assertEquals("hourOfDay", HebrewChronology.getInstance().hourOfDay().getName());
        assertEquals("minuteOfDay", HebrewChronology.getInstance().minuteOfDay().getName());
        assertEquals("minuteOfHour", HebrewChronology.getInstance().minuteOfHour().getName());
        assertEquals("secondOfDay", HebrewChronology.getInstance().secondOfDay().getName());
        assertEquals("secondOfMinute", HebrewChronology.getInstance().secondOfMinute().getName());
        assertEquals("millisOfDay", HebrewChronology.getInstance().millisOfDay().getName());
        assertEquals("millisOfSecond", HebrewChronology.getInstance().millisOfSecond().getName());
        
        assertEquals(true, HebrewChronology.getInstance().halfdayOfDay().isSupported());
        assertEquals(true, HebrewChronology.getInstance().clockhourOfHalfday().isSupported());
        assertEquals(true, HebrewChronology.getInstance().hourOfHalfday().isSupported());
        assertEquals(true, HebrewChronology.getInstance().clockhourOfDay().isSupported());
        assertEquals(true, HebrewChronology.getInstance().hourOfDay().isSupported());
        assertEquals(true, HebrewChronology.getInstance().minuteOfDay().isSupported());
        assertEquals(true, HebrewChronology.getInstance().minuteOfHour().isSupported());
        assertEquals(true, HebrewChronology.getInstance().secondOfDay().isSupported());
        assertEquals(true, HebrewChronology.getInstance().secondOfMinute().isSupported());
        assertEquals(true, HebrewChronology.getInstance().millisOfDay().isSupported());
        assertEquals(true, HebrewChronology.getInstance().millisOfSecond().isSupported());
    }

    //-----------------------------------------------------------------------
    public void testEpoch() {
        DateTime epoch = new DateTime(1, 1, 1, 0, 0, 0, 0, HEBREW_UTC);
        DateTime expectedEpoch = new DateTime(-3761, 10, 7, 0,0,0,0, JULIAN_UTC);
        assertEquals(expectedEpoch.getMillis(), epoch.getMillis());
    }

//    public void testEra() {
//        assertEquals(1, HebrewChronology.AH);
//        try {
//            new DateTime(-1, 13, 5, 0, 0, 0, 0, HEBREW_UTC);
//            fail();
//        } catch (IllegalArgumentException ex) {}
//    }

    //-----------------------------------------------------------------------
    public void testFieldConstructor() {
        DateTime date = new DateTime(1364, 12, 6, 0, 0, 0, 0, HEBREW_UTC);
        DateTime expectedDate = new DateTime(-2396, 8, 4, 0, 0, 0, 0, ISO_UTC);
        assertEquals(expectedDate.getMillis(), date.getMillis());
    }

    //-----------------------------------------------------------------------
    /**
     * Tests era, year, monthOfYear, dayOfMonth and dayOfWeek.
     */
    public void testCalendar() {
        System.out.println("\nTestHebrewChronology.testCalendar");
        DateTime epoch = new DateTime(1, 1, 1, 0, 0, 0, 0, HEBREW_UTC);
        long millis = epoch.getMillis();
        long end = new DateTime(3000, 1, 1, 0, 0, 0, 0, ISO_UTC).getMillis();
        DateTimeField dayOfWeek = HEBREW_UTC.dayOfWeek();
        DateTimeField dayOfYear = HEBREW_UTC.dayOfYear();
        DateTimeField dayOfMonth = HEBREW_UTC.dayOfMonth();
        DateTimeField monthOfYear = HEBREW_UTC.monthOfYear();
        DateTimeField year = HEBREW_UTC.year();
        DateTimeField yearOfEra = HEBREW_UTC.yearOfEra();
        DateTimeField era = HEBREW_UTC.era();
        int expectedDOW = new DateTime(-3761, 10, 7, 0,0,0,0, JULIAN_UTC).getDayOfWeek();
        int expectedDOY = 1;
        int expectedDay = 1;
        int expectedMonth = 1;
        int expectedYear = 1;
        while (millis < end) {
            int dowValue = dayOfWeek.get(millis);
            int doyValue = dayOfYear.get(millis);
            int dayValue = dayOfMonth.get(millis);
            int monthValue = monthOfYear.get(millis);
            int yearValue = year.get(millis);
            int yearOfEraValue = yearOfEra.get(millis);
            int dayOfYearLen = dayOfYear.getMaximumValue(millis);
            int monthLen = dayOfMonth.getMaximumValue(millis);
            if (monthValue < 1 || monthValue > 12) {
                fail("Bad month: " + millis);
            }
            
            // test era
            assertEquals(1, era.get(millis));
//            assertEquals("AH", era.getAsText(millis));
//            assertEquals("AH", era.getAsShortText(millis));
            
            // test date
            assertEquals(expectedDOY, doyValue);
            assertEquals(expectedMonth, monthValue);
            assertEquals(expectedDay, dayValue);
            assertEquals(expectedDOW, dowValue);
            assertEquals(expectedYear, yearValue);
            assertEquals(expectedYear, yearOfEraValue);
            
            // test leap year
    		boolean leap = isLeapYear(yearValue);
            assertEquals(leap, year.isLeap(millis));
            
            assertEquals(getDays(yearValue, monthValue), monthLen);
            
            // test year length
            assertEquals((leap ? 355 : 354), dayOfYearLen);
            
            // recalculate date
            expectedDOW = (((expectedDOW + 1) - 1) % 7) + 1;
            expectedDay++;
            expectedDOY++;
            if (expectedDay > monthLen) {
                expectedDay = 1;
                expectedMonth++;
                if (expectedMonth == 13) {
                    expectedMonth = 1;
                    expectedDOY = 1;
                    expectedYear++;
                }
            }
            millis += SKIP;
        }
    }

	private boolean isLeapYear(int yearValue) {
		int mod = yearValue % 19;
		boolean leap = mod == 0 || mod == 3 || mod == 6 || mod == 8 || mod == 11
		|| mod == 14 || mod == 17;
		return leap;
	}

    private int getDays(int year, int month){
		if ((month == 2) || (month == 4) || (month == 6)
				|| ((month == 8) && !(isCheshvanLong(year)))
				|| ((month == 9) && isKislevShort(year)) || (month == 10)
				|| ((month == 12) && !(isLeapYear(year))) || (month == 13))
			return 29;
		else
			return 30;
    }
    
	// ND+ER //
	// True if Heshvan is long in Hebrew year. //
	boolean isCheshvanLong(int hYear) {
		if ((getDaysInHebrewYear(hYear) % 10) == 5)
			return true;
		else
			return false;
	}

	// ND+ER //
	// True if Kislev is short in Hebrew year.//
	boolean isKislevShort(int hYear) {
		if ((getDaysInHebrewYear(hYear) % 10) == 3)
			return true;
		else
			return false;
	}

	long getDaysInHebrewYear(int hYear) {
		return ((getHebrewCalendarElapsedDays(hYear + 1)) - (getHebrewCalendarElapsedDays(hYear)));
	}			   

    
	private long getHebrewCalendarElapsedDays(int year) {
			long monthsElapsed = (235L * ((year - 1) / LEAP_CYCLE_YEARS)) // Months in complete
			// cycles so far.//
			+ (12 * ((year - 1) % LEAP_CYCLE_YEARS)) // Regular months in this cycle.//
			+ (7 * ((year - 1) % LEAP_CYCLE_YEARS) + 1) / LEAP_CYCLE_YEARS; // Leap months this cycle//
			long partsElapsed = 204L + 793L * (monthsElapsed % PARTS_OF_AN_HOUR);
			long hoursElapsed = 5L + 12L * monthsElapsed + 793L
			* (monthsElapsed / PARTS_OF_AN_HOUR) + partsElapsed / PARTS_OF_AN_HOUR;
			long conjunctionDay = 1 + 29 * monthsElapsed + hoursElapsed / 24;
			long conjunctionParts = PARTS_OF_AN_HOUR * (hoursElapsed % 24) + partsElapsed % PARTS_OF_AN_HOUR;
			long alternativeDay;
			if ((conjunctionParts >= 19440) // If new moon is at or after midday,//
					|| (((conjunctionDay % 7) == 2) // ...or is on a Tuesday...//
							&& (conjunctionParts >= 9924) // at 9 hours, 204 parts
							// or later...//
							&& !isLeapYear(year)) // ...of a common year,//
							|| (((conjunctionDay % 7) == 1) // ...or is on a Monday at...//
									&& (conjunctionParts >= 16789) // 15 hours, 589 parts or
									// later...//
									&& (isLeapYear(year - 1))))// at the end of a leap year//
				// Then postpone Rosh HaShanah one day//
				alternativeDay = conjunctionDay + 1;
			else
				alternativeDay = conjunctionDay;
			if (((alternativeDay % 7) == 0)// If Rosh HaShanah would occur on
					// Sunday,//
					|| ((alternativeDay % 7) == 3) // or Wednesday,//
					|| ((alternativeDay % 7) == 5)) // or Friday//
				// Then postpone it one (more) day//
				alternativeDay = (1 + alternativeDay);
			return alternativeDay;
	}

    
    public void testSampleDate1() {
        DateTime dt = new DateTime(1945, 11, 12, 0, 0, 0, 0, ISO_UTC);
        dt = dt.withChronology(HEBREW_UTC);
//        assertEquals(HebrewChronology.AH, dt.getEra());
        assertEquals(58, dt.getCenturyOfEra());  // TODO confirm
        assertEquals(6, dt.getYearOfCentury());
        assertEquals(5706, dt.getYearOfEra());
        
        assertEquals(5706, dt.getYear());
        Property fld = dt.year();
        assertEquals(true, fld.isLeap());
        assertEquals(1, fld.getLeapAmount());
        assertEquals(DurationFieldType.days(), fld.getLeapDurationField().getType());
        assertEquals(new DateTime(1365, 12, 6, 0, 0, 0, 0, HEBREW_UTC), fld.addToCopy(1));
        
        assertEquals(12, dt.getMonthOfYear());
        fld = dt.monthOfYear();
        assertEquals(false, fld.isLeap());
        assertEquals(0, fld.getLeapAmount());
        assertEquals(DurationFieldType.days(), fld.getLeapDurationField().getType());
        assertEquals(1, fld.getMinimumValue());
        assertEquals(1, fld.getMinimumValueOverall());
        assertEquals(12, fld.getMaximumValue());
        assertEquals(12, fld.getMaximumValueOverall());
        assertEquals(new DateTime(1365, 1, 6, 0, 0, 0, 0, HEBREW_UTC), fld.addToCopy(1));
        assertEquals(new DateTime(1364, 1, 6, 0, 0, 0, 0, HEBREW_UTC), fld.addWrapFieldToCopy(1));
        
        assertEquals(6, dt.getDayOfMonth());
        fld = dt.dayOfMonth();
        assertEquals(false, fld.isLeap());
        assertEquals(0, fld.getLeapAmount());
        assertEquals(null, fld.getLeapDurationField());
        assertEquals(1, fld.getMinimumValue());
        assertEquals(1, fld.getMinimumValueOverall());
        assertEquals(29, fld.getMaximumValue());
        assertEquals(30, fld.getMaximumValueOverall());
        assertEquals(new DateTime(1364, 12, 7, 0, 0, 0, 0, HEBREW_UTC), fld.addToCopy(1));
        
        assertEquals(DateTimeConstants.MONDAY, dt.getDayOfWeek());
        fld = dt.dayOfWeek();
        assertEquals(false, fld.isLeap());
        assertEquals(0, fld.getLeapAmount());
        assertEquals(null, fld.getLeapDurationField());
        assertEquals(1, fld.getMinimumValue());
        assertEquals(1, fld.getMinimumValueOverall());
        assertEquals(7, fld.getMaximumValue());
        assertEquals(7, fld.getMaximumValueOverall());
        assertEquals(new DateTime(1364, 12, 7, 0, 0, 0, 0, HEBREW_UTC), fld.addToCopy(1));
        
        assertEquals(6 * 30 + 5 * 29 + 6, dt.getDayOfYear());
        fld = dt.dayOfYear();
        assertEquals(false, fld.isLeap());
        assertEquals(0, fld.getLeapAmount());
        assertEquals(null, fld.getLeapDurationField());
        assertEquals(1, fld.getMinimumValue());
        assertEquals(1, fld.getMinimumValueOverall());
        assertEquals(354, fld.getMaximumValue());
        assertEquals(355, fld.getMaximumValueOverall());
        assertEquals(new DateTime(1364, 12, 7, 0, 0, 0, 0, HEBREW_UTC), fld.addToCopy(1));
        
        assertEquals(0, dt.getHourOfDay());
        assertEquals(0, dt.getMinuteOfHour());
        assertEquals(0, dt.getSecondOfMinute());
        assertEquals(0, dt.getMillisOfSecond());
    }

    public void testSampleDate2() {
        DateTime dt = new DateTime(2005, 11, 26, 0, 0, 0, 0, ISO_UTC);
        dt = dt.withChronology(HEBREW_UTC);
//        assertEquals(HebrewChronology.AH, dt.getEra());
        assertEquals(15, dt.getCenturyOfEra());  // TODO confirm
        assertEquals(26, dt.getYearOfCentury());
        assertEquals(1426, dt.getYearOfEra());
        
        assertEquals(1426, dt.getYear());
        Property fld = dt.year();
        assertEquals(true, fld.isLeap());
        assertEquals(1, fld.getLeapAmount());
        assertEquals(DurationFieldType.days(), fld.getLeapDurationField().getType());
        
        assertEquals(10, dt.getMonthOfYear());
        fld = dt.monthOfYear();
        assertEquals(false, fld.isLeap());
        assertEquals(0, fld.getLeapAmount());
        assertEquals(DurationFieldType.days(), fld.getLeapDurationField().getType());
        assertEquals(1, fld.getMinimumValue());
        assertEquals(1, fld.getMinimumValueOverall());
        assertEquals(12, fld.getMaximumValue());
        assertEquals(12, fld.getMaximumValueOverall());
        
        assertEquals(24, dt.getDayOfMonth());
        fld = dt.dayOfMonth();
        assertEquals(false, fld.isLeap());
        assertEquals(0, fld.getLeapAmount());
        assertEquals(null, fld.getLeapDurationField());
        assertEquals(1, fld.getMinimumValue());
        assertEquals(1, fld.getMinimumValueOverall());
        assertEquals(29, fld.getMaximumValue());
        assertEquals(30, fld.getMaximumValueOverall());
        
        assertEquals(DateTimeConstants.SATURDAY, dt.getDayOfWeek());
        fld = dt.dayOfWeek();
        assertEquals(false, fld.isLeap());
        assertEquals(0, fld.getLeapAmount());
        assertEquals(null, fld.getLeapDurationField());
        assertEquals(1, fld.getMinimumValue());
        assertEquals(1, fld.getMinimumValueOverall());
        assertEquals(7, fld.getMaximumValue());
        assertEquals(7, fld.getMaximumValueOverall());
        
        assertEquals(5 * 30 + 4 * 29 + 24, dt.getDayOfYear());
        fld = dt.dayOfYear();
        assertEquals(false, fld.isLeap());
        assertEquals(0, fld.getLeapAmount());
        assertEquals(null, fld.getLeapDurationField());
        assertEquals(1, fld.getMinimumValue());
        assertEquals(1, fld.getMinimumValueOverall());
        assertEquals(355, fld.getMaximumValue());
        assertEquals(355, fld.getMaximumValueOverall());
        
        assertEquals(0, dt.getHourOfDay());
        assertEquals(0, dt.getMinuteOfHour());
        assertEquals(0, dt.getSecondOfMinute());
        assertEquals(0, dt.getMillisOfSecond());
    }

    public void testSampleDate3() {
        DateTime dt = new DateTime(1426, 12, 24, 0, 0, 0, 0, HEBREW_UTC);
//        assertEquals(HebrewChronology.AH, dt.getEra());
        
        assertEquals(1426, dt.getYear());
        Property fld = dt.year();
        assertEquals(true, fld.isLeap());
        assertEquals(1, fld.getLeapAmount());
        assertEquals(DurationFieldType.days(), fld.getLeapDurationField().getType());
        
        assertEquals(12, dt.getMonthOfYear());
        fld = dt.monthOfYear();
        assertEquals(true, fld.isLeap());
        assertEquals(1, fld.getLeapAmount());
        assertEquals(DurationFieldType.days(), fld.getLeapDurationField().getType());
        assertEquals(1, fld.getMinimumValue());
        assertEquals(1, fld.getMinimumValueOverall());
        assertEquals(12, fld.getMaximumValue());
        assertEquals(12, fld.getMaximumValueOverall());
        
        assertEquals(24, dt.getDayOfMonth());
        fld = dt.dayOfMonth();
        assertEquals(false, fld.isLeap());
        assertEquals(0, fld.getLeapAmount());
        assertEquals(null, fld.getLeapDurationField());
        assertEquals(1, fld.getMinimumValue());
        assertEquals(1, fld.getMinimumValueOverall());
        assertEquals(30, fld.getMaximumValue());
        assertEquals(30, fld.getMaximumValueOverall());
        
        assertEquals(DateTimeConstants.TUESDAY, dt.getDayOfWeek());
        fld = dt.dayOfWeek();
        assertEquals(false, fld.isLeap());
        assertEquals(0, fld.getLeapAmount());
        assertEquals(null, fld.getLeapDurationField());
        assertEquals(1, fld.getMinimumValue());
        assertEquals(1, fld.getMinimumValueOverall());
        assertEquals(7, fld.getMaximumValue());
        assertEquals(7, fld.getMaximumValueOverall());
        
        assertEquals(6 * 30 + 5 * 29 + 24, dt.getDayOfYear());
        fld = dt.dayOfYear();
        assertEquals(false, fld.isLeap());
        assertEquals(0, fld.getLeapAmount());
        assertEquals(null, fld.getLeapDurationField());
        assertEquals(1, fld.getMinimumValue());
        assertEquals(1, fld.getMinimumValueOverall());
        assertEquals(355, fld.getMaximumValue());
        assertEquals(355, fld.getMaximumValueOverall());
        
        assertEquals(0, dt.getHourOfDay());
        assertEquals(0, dt.getMinuteOfHour());
        assertEquals(0, dt.getSecondOfMinute());
        assertEquals(0, dt.getMillisOfSecond());
    }

    public void testSampleDateWithZone() {
        DateTime dt = new DateTime(2005, 11, 26, 12, 0, 0, 0, PARIS).withChronology(HEBREW_UTC);
//        assertEquals(HebrewChronology.AH, dt.getEra());
        assertEquals(1426, dt.getYear());
        assertEquals(10, dt.getMonthOfYear());
        assertEquals(24, dt.getDayOfMonth());
        assertEquals(11, dt.getHourOfDay());  // PARIS is UTC+1 in summer (12-1=11)
        assertEquals(0, dt.getMinuteOfHour());
        assertEquals(0, dt.getSecondOfMinute());
        assertEquals(0, dt.getMillisOfSecond());
    }

}
