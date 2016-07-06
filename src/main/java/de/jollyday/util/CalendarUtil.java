/**
 * Copyright 2010 Sven Diedrichsen
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package de.jollyday.util;

import de.jollyday.Holiday;
import de.jollyday.HolidayType;
import de.jollyday.config.Fixed;
import org.threeten.extra.chrono.CopticChronology;
import org.threeten.extra.chrono.JulianChronology;

import java.time.LocalDate;
import java.time.chrono.ChronoLocalDate;
import java.time.chrono.Chronology;
import java.time.chrono.HijrahChronology;
import java.time.temporal.ChronoField;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import static java.time.DayOfWeek.SATURDAY;
import static java.time.DayOfWeek.SUNDAY;
import static java.time.Month.*;

/**
 * Utility class for date operations.
 *
 * @author Sven Diedrichsen
 * @version $Id: $
 */
public class CalendarUtil {

    private final XMLUtil xmlUtil = new XMLUtil();

    /**
     * Creates the current date within the gregorian calendar.
     *
     * @return today
     */
    public LocalDate create() {
        return LocalDate.now();
    }

    /**
     * Creates the date within the ISO chronology.
     *
     * @param year  a int.
     * @param month a int.
     * @param day   a int.
     * @return date
     */
    public LocalDate create(int year, int month, int day) {
        return LocalDate.of(year, month, day);
    }

    /**
     * Creates the date within the provided chronology.
     *
     * @param year
     *            a int.
     * @param month
     *            a int.
     * @param day
     *            a int.
     * @param chronology
     *            the chronology to use
     * @return date the {@link LocalDate}
     */
    public ChronoLocalDate create(int year, int month, int day, Chronology chronology) {
        return chronology.date(year, month, day);
    }

    /**
     * Creates the date from the month/day within the specified year.
     *
     * @param year
     *            a int.
     * @param fixed
     *            a {@link de.jollyday.config.Fixed} object.
     * @return A local date instance.
     */
    public LocalDate create(int year, Fixed fixed) {
        return create(year, xmlUtil.getMonth(fixed.getMonth()), fixed.getDay());
    }

    /**
     * Creates a LocalDate. Does not use the Chronology of the Calendar.
     *
     * @param c
     *            a {@link java.util.Calendar} object.
     * @return The local date representing the provided date.
     */
    public LocalDate create(final Calendar c) {
        //TODO: javadoc needs updating
        return LocalDate.of(c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1, c
                .get(Calendar.DAY_OF_MONTH));
    }

    /**
     * Returns the easter sunday for a given year.
     *
     * @param year
     *            a int.
     * @return Easter sunday.
     */
    public LocalDate getEasterSunday(int year) {
        if (year <= 1583) {
            return getJulianEasterSunday(year);
        } else {
            return getGregorianEasterSunday(year);
        }
    }

    /**
     * Returns the easter sunday within the julian chronology.
     *
     * @param year
     *            a int.
     * @return julian easter sunday
     */
    public LocalDate getJulianEasterSunday(int year) {
        int a, b, c, d, e;
        int x, month, day;
        a = year % 4;
        b = year % 7;
        c = year % 19;
        d = (19 * c + 15) % 30;
        e = (2 * a + 4 * b - d + 34) % 7;
        x = d + e + 114;
        month = x / 31;
        day = (x % 31) + 1;
        return LocalDate.from(JulianChronology.INSTANCE.date(year, (month == 3 ? 3 : 4), day));
    }

    /**
     * Returns the easter sunday within the gregorian chronology.
     *
     * @param year
     *            a int.
     * @return gregorian easter sunday.
     */
    public LocalDate getGregorianEasterSunday(int year) {
        int a, b, c, d, e, f, g, h, i, j, k, l;
        int x, month, day;
        a = year % 19;
        b = year / 100;
        c = year % 100;
        d = b / 4;
        e = b % 4;
        f = (b + 8) / 25;
        g = (b - f + 1) / 3;
        h = (19 * a + b - d - g + 15) % 30;
        i = c / 4;
        j = c % 4;
        k = (32 + 2 * e + 2 * i - h - j) % 7;
        l = (a + 11 * h + 22 * k) / 451;
        x = h + k - 7 * l + 114;
        month = x / 31;
        day = (x % 31) + 1;
        return LocalDate.of(year, (month == 3 ? MARCH : APRIL), day);
    }

    /**
     * Returns if this date is on a wekkend.
     *
     * @param date
     *            a {@link LocalDate} object.
     * @return is weekend
     */
    public boolean isWeekend(final LocalDate date) {
        return date.getDayOfWeek() == SATURDAY || date.getDayOfWeek() == SUNDAY;
    }

    /**
     * Returns a set of gregorian dates within a gregorian year which equal the
     * islamic month and day. Because the islamic year is about 11 days shorter
     * than the gregorian there may be more than one occurrence of an islamic
     * date in an gregorian year. i.e.: In the gregorian year 2008 there were
     * two 1/1. They occurred on 1/10 and 12/29.
     *
     * @param gregorianYear
     *            a int.
     * @param islamicMonth
     *            a int.
     * @param islamicDay
     *            a int.
     * @return List of gregorian dates for the islamic month/day.
     */
    public Set<LocalDate> getIslamicHolidaysInGregorianYear(int gregorianYear, int islamicMonth, int islamicDay) {
        return getDatesFromChronologyWithinGregorianYear(islamicMonth, islamicDay, gregorianYear,
                HijrahChronology.INSTANCE);
    }

    /**
     * Returns a set of gregorian dates within a gregorian year which equal the
     * ethiopian orthodox month and day. Because the ethiopian orthodox year
     * different from the gregorian there may be more than one occurrence of an
     * ethiopian orthodox date in an gregorian year.
     *
     * @param gregorianYear
     *            a int.
     * @return List of gregorian dates for the ethiopian orthodox month/day.
     * @param eoMonth
     *            a int.
     * @param eoDay
     *            a int.
     */
    public Set<LocalDate> getEthiopianOrthodoxHolidaysInGregorianYear(int gregorianYear, int eoMonth, int eoDay) {
        return getDatesFromChronologyWithinGregorianYear(eoMonth, eoDay, gregorianYear, CopticChronology.INSTANCE);
    }

    /**
     * Searches for the occurrences of a month/day in one chronology within one
     * gregorian year.
     *
     * @param targetMonth
     * @param targetDay
     * @param gregorianYear
     * @param targetChrono
     * @return the list of gregorian dates.
     */
    private Set<LocalDate> getDatesFromChronologyWithinGregorianYear(int targetMonth, int targetDay, int gregorianYear,
                                                                     Chronology targetChrono) {
        Set<LocalDate> holidays = new HashSet<>();
        LocalDate firstGregorianDate = LocalDate.of(gregorianYear, JANUARY, 1);
        LocalDate lastGregorianDate = LocalDate.of(gregorianYear, DECEMBER, 31);

        ChronoLocalDate firstTargetDate = targetChrono.date(firstGregorianDate);
        ChronoLocalDate lastTargetDate = targetChrono.date(lastGregorianDate);

        int targetYear = firstTargetDate.get(ChronoField.YEAR);
        final int lastYear = lastTargetDate.get(ChronoField.YEAR);

        for (; targetYear <= lastYear; ) {
            ChronoLocalDate d = targetChrono.date(targetYear, targetMonth, targetDay);
            if (!firstGregorianDate.isAfter(d) && !lastGregorianDate.isBefore(d)) {
                holidays.add(LocalDate.from(d));
            }
            targetYear++;
        }
        return holidays;
    }

    /**
     * Shows if the requested date is contained in the Set of holidays.
     *
     * @param holidays a {@link java.util.Set} object.
     * @param date a {@link LocalDate} object.
     * @param holidayType a {@link HolidayType} object
     * @return contains this date
     */
    public boolean contains(final Set<Holiday> holidays, final LocalDate date, final HolidayType holidayType) {
        return holidays.stream().anyMatch(h -> h.getDate().equals(date) && (holidayType == null || h.getType() == holidayType));
    }

    /**
     * Calls #contains(holidays, date, null)
     * @param holidays the holidays to search through
     * @param date the date to look for
     * @return the date is contained in the set of holidays
     */
    public boolean contains(final Set<Holiday> holidays, final LocalDate date) {
        return contains(holidays, date, null);
    }

}
