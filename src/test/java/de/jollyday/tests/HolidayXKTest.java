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
package de.jollyday.tests;

import de.jollyday.*;
import de.jollyday.tests.base.AbstractCountryTestBase;
import de.jollyday.util.CalendarUtil;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class HolidayXKTest extends AbstractCountryTestBase {

    private static final String ISO_CODE = "xk";

    private CalendarUtil calendarUtil = new CalendarUtil();

    @Test
    public void testManagerXKStructure2021() {
        validateCalendarData(ISO_CODE, 2021);
    }
    @Test
    public void testManagerXKStructure2022() {
        validateCalendarData(ISO_CODE, 2022);
    }
    @Test
    public void testManagerXKStructure2023() {
        validateCalendarData(ISO_CODE, 2023);
    }

    @Test
    public void testManagerXKInterval() {
        HolidayManager instance = HolidayManager.getInstance(HolidayCalendar.KOSOVO);
        Set<Holiday> holidays = instance.getHolidays(calendarUtil.create(2010, 6, 1),
                calendarUtil.create(2011, 5, 31));
        
        List<LocalDate> expected = Arrays.asList(calendarUtil.create(2010, 6, 12),
                calendarUtil.create(2010, 6, 15),
                calendarUtil.create(2010, 9, 28),
                calendarUtil.create(2010, 11, 28),
                calendarUtil.create(2010, 12, 25),
                calendarUtil.create(2011, 1, 1),
                calendarUtil.create(2011, 2, 15),
                calendarUtil.create(2011, 2, 17),
                calendarUtil.create(2011, 3, 6),
                calendarUtil.create(2011, 3, 8),
                calendarUtil.create(2011, 4, 23),
                calendarUtil.create(2011, 5, 1),
                calendarUtil.create(2011, 5, 6),
                calendarUtil.create(2011, 5, 9)
                );
        
        assertEquals(expected.size(), holidays.size(), "Wrong number of holidays");
        
        holidays.stream().forEach(holiday -> assertTrue(expected.contains(holiday.getDate()), "Holiday " + holiday + " is not expected."));
        
        expected.stream().forEach(d -> assertTrue(calendarUtil.contains(holidays, d), "Expected date " + d + " missing."));
    }

    @Test
    public void testManagerDifferentInstance() {
        Locale defaultLocale = Locale.getDefault();
        Locale.setDefault(Locale.US);
        try {
            HolidayManager defaultManager = HolidayManager.getInstance();
            HolidayManager germanManager = HolidayManager.getInstance(HolidayCalendar.KOSOVO);
            assertNotSame(defaultManager, germanManager, "Unexpected manager found");
        } catch (Exception e) {
            fail("Unexpected error occurred: " + e.getClass().getName() + " - " + e.getMessage());
        } finally {
            Locale.setDefault(defaultLocale);
        }
    }
}
