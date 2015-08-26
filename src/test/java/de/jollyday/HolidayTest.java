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
}
