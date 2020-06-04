/**
 * Copyright 2010-2019 Sven Diedrichsen
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

import java.time.LocalDate;
import java.time.chrono.ChronoLocalDate;
import java.time.chrono.Chronology;
import java.util.Set;

import static java.time.DayOfWeek.SATURDAY;
import static java.time.DayOfWeek.SUNDAY;

/**
 * Utility class for date operations.
 *
 * @author Sven Diedrichsen
 * @version $Id: $
 */
public class CalendarUtil {

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
     * @param year       a int.
     * @param month      a int.
     * @param day        a int.
     * @param chronology the chronology to use
     * @return date the {@link LocalDate}
     */
    public ChronoLocalDate create(int year, int month, int day, Chronology chronology) {
        return chronology.date(year, month, day);
    }

    /**
     * Returns if this date is on a wekkend.
     *
     * @param date a {@link LocalDate} object.
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
     * @param gregorianYear a int.
     * @param islamicMonth  a int.
     * @param islamicDay    a int.
     * @return List of gregorian dates for the islamic month/day.
     */
    /*
    public Set<LocalDate> getIslamicHolidaysInGregorianYear(int gregorianYear, int islamicMonth, int islamicDay) {
        return getDatesFromChronologyWithinGregorianYear(islamicMonth, islamicDay, gregorianYear,
                HijrahChronology.INSTANCE);
    }
    */
    /**
     * Returns a set of gregorian dates within a gregorian year which equal the
     * islamic month and day with a relative shift. Because the islamic year is
     * about 11 days shorter than the gregorian there may be more than one occurrence
     * of an islamic date in an gregorian year. i.e.: In the gregorian year 2008
     * there were two 1/1. They occurred on 1/10 and 12/29.
     *
     * @param gregorianYear a int.
     * @param islamicMonth  a int.
     * @param islamicDay    a int.
     * @param relativeShift a int.
     * @return List of gregorian dates for the islamic month/day shifted by relative shift days.
     */
    /*
    public Set<LocalDate> getRelativeIslamicHolidaysInGregorianYear(int gregorianYear, int islamicMonth, int islamicDay, int relativeShift) {
        return getRelativeDatesFromChronologyWithinGregorianYear(islamicMonth, islamicDay, gregorianYear,
                HijrahChronology.INSTANCE, relativeShift);
    }
    */
    /**
     * Returns a set of gregorian dates within a gregorian year which equal the
     * ethiopian orthodox month and day. Because the ethiopian orthodox year
     * different from the gregorian there may be more than one occurrence of an
     * ethiopian orthodox date in an gregorian year.
     *
     * @param gregorianYear a int.
     * @param eoMonth       a int.
     * @param eoDay         a int.
     * @return List of gregorian dates for the ethiopian orthodox month/day.
     */
    /*
    public Stream<LocalDate> getEthiopianOrthodoxHolidaysInGregorianYear(int gregorianYear, int eoMonth, int eoDay) {

        return getDatesFromChronologyWithinGregorianYear(eoMonth, eoDay, gregorianYear, CopticChronology.INSTANCE);
    }
    */
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
    /*
    private Stream<LocalDate> getDatesFromChronologyWithinGregorianYear(int targetMonth, int targetDay, int gregorianYear,
                                                                        Chronology targetChrono) {
        return new CalculateRelativeDatesFromChronologyWithinGregorianYear(targetMonth, targetDay, targetChrono, 0).apply(gregorianYear);
    }
    */
    /**
     * Searches for the occurrences of a month/day +- relative shift in one
     * chronology within one gregorian year.
     *
     * @param targetMonth
     * @param targetDay
     * @param gregorianYear
     * @param targetChrono
     * @param relativeShift
     * @return the list of gregorian dates.
     */
    /*
    private Set<LocalDate> getRelativeDatesFromChronologyWithinGregorianYear(int targetMonth, int targetDay,
            int gregorianYear, Chronology targetChrono, int relativeShift) {
        int absoluteShift = Math.abs(relativeShift);
        Set<LocalDate> holidays = new HashSet<>();
        LocalDate firstGregorianDate = LocalDate.of(gregorianYear, JANUARY, 1);
        LocalDate lastGregorianDate = LocalDate.of(gregorianYear, DECEMBER, 31);

        ChronoLocalDate firstTargetDate = targetChrono.date(firstGregorianDate.minusDays(absoluteShift));
        ChronoLocalDate lastTargetDate = targetChrono.date(lastGregorianDate.plusDays(absoluteShift));

        int targetYear = firstTargetDate.get(ChronoField.YEAR);
        final int lastYear = lastTargetDate.get(ChronoField.YEAR);

        while (targetYear <= lastYear) {
            ChronoLocalDate d = targetChrono.date(targetYear, targetMonth, targetDay).plus(relativeShift,
                    ChronoUnit.DAYS);
            if (!firstGregorianDate.isAfter(d) && !lastGregorianDate.isBefore(d)) {
                holidays.add(LocalDate.from(d));
            }
            targetYear++;
        }
        return holidays;
    }
    */

    /**
     * Shows if the requested date is contained in the Set of holidays.
     *
     * @param holidays    a {@link java.util.Set} object.
     * @param date        a {@link LocalDate} object.
     * @param holidayType a {@link HolidayType} object
     * @return contains this date
     */
    public boolean contains(final Set<Holiday> holidays, final LocalDate date, final HolidayType holidayType) {
        return holidays.stream().anyMatch(h -> h.getDate().equals(date) && (holidayType == null || h.getType() == holidayType));
    }

    /**
     * Calls #contains(holidays, date, null)
     *
     * @param holidays the holidays to search through
     * @param date     the date to look for
     * @return the date is contained in the set of holidays
     */
    public boolean contains(final Set<Holiday> holidays, final LocalDate date) {
        return contains(holidays, date, null);
    }

}
