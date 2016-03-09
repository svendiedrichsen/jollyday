/**
 * Copyright 2015 Bernd Hoffmann
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
package de.jollyday;

import junit.framework.Assert;
import org.junit.Test;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.jollyday.util.ResourceUtil;

public class HolidayTest {

    @Test
    public void testComparable() {
        Holiday holiday = new Holiday(LocalDate.of(2015, 1, 1), null, HolidayType.OFFICIAL_HOLIDAY);
        Assert.assertTrue("Holiday does not implement the Comparable interface.", holiday instanceof Comparable);
    }

    @Test
    public void testCompareToLess() {
        Holiday newYear = new Holiday(LocalDate.of(2015, 1, 1), null, HolidayType.OFFICIAL_HOLIDAY);
        Holiday christmas = new Holiday(LocalDate.of(2015, 12, 25), null, HolidayType.OFFICIAL_HOLIDAY);
        int actual = newYear.compareTo(christmas);
        Assert.assertTrue("Wrong holiday comparator value for less.", actual < 0);
    }

    @Test
    public void testCompareToGreater() {
        Holiday christmas = new Holiday(LocalDate.of(2015, 12, 25), null, HolidayType.OFFICIAL_HOLIDAY);
        Holiday newYear = new Holiday(LocalDate.of(2015, 1, 1), null, HolidayType.OFFICIAL_HOLIDAY);
        int actual = christmas.compareTo(newYear);
        Assert.assertTrue("Wrong holiday comparator value for greater.", actual > 0);
    }

    @Test
    public void testCompareToEqual() {
        Holiday firstDayOfYear = new Holiday(LocalDate.of(2015, 1, 1), null, HolidayType.OFFICIAL_HOLIDAY);
        Holiday newYear = new Holiday(LocalDate.of(2015, 1, 1), null, HolidayType.OFFICIAL_HOLIDAY);
        int actual = firstDayOfYear.compareTo(newYear);
        Assert.assertTrue("Wrong holiday comparator for equal.", actual == 0);
    }

    @Test
    public void print2016Holiday() {
        final ResourceUtil resourceUtil = new ResourceUtil();

        final Map<String,List<Holiday>> map = new HashMap<>();
        final List<String> countries = new LinkedList<>();

        for (Map.Entry<String, HolidayCalendar> current : CountryToHolidayCalendar.map.entrySet()) {
            final String key = current.getKey();
            final HolidayCalendar value = current.getValue();
            final String countryDescription = resourceUtil.getCountryDescription(key);
            countries.add(countryDescription);

            HolidayManager m = HolidayManager.getInstance(ManagerParameters.create(value));
            final Set<Holiday> holidays = m.getHolidays(2016);
            List<Holiday> holidayList = new LinkedList<>();
            for (Holiday h : holidays) {
                holidayList.add(h);
            }

            Collections.sort(holidayList, new Comparator<Holiday>() {
                @Override
                public int compare(Holiday o1, Holiday o2) {
                    return o1.getDate().compareTo(o2.getDate());
                }
            });
            map.put(countryDescription,holidayList);
        }

        Collections.sort(countries);
        System.out.println("# 2016 Holidays");
        System.out.println();
        for (String country : countries) {
            final List<Holiday> holidays = map.get(country);
            System.out.println("## " + country);
            System.out.println();
            for (Holiday holiday : holidays) {
                System.out.println(holiday.toString());
            }
            System.out.println();
        }
    }
}
