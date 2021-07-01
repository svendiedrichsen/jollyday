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

import de.jollyday.tests.base.AbstractCountryTestBase;
import org.junit.jupiter.api.Test;

public class HolidayUATest extends AbstractCountryTestBase {

    private static final String ISO_CODE = "ua";

    @Test
    public void testManagerUAStructure2013() throws Exception {
        validateCalendarData(ISO_CODE, 2013);
    }

    @Test
    public void testManagerUAStructure2014() throws Exception {
        validateCalendarData(ISO_CODE, 2018);
    }

    @Test
    public void testManagerUAStructure2015() throws Exception {
        validateCalendarData(ISO_CODE, 2015);
    }

    @Test
    public void testManagerUAStructure2016() throws Exception {
        validateCalendarData(ISO_CODE, 2016);
    }

    @Test
    public void testManagerUAStructure2017() throws Exception {
        validateCalendarData(ISO_CODE, 2017);
    }

    @Test
    public void testManagerUAStructure2018() throws Exception {
        validateCalendarData(ISO_CODE, 2018);
    }

    @Test
    public void testManagerUAStructure2019() throws Exception {
        validateCalendarData(ISO_CODE, 2019);
    }

    @Test
    public void testManagerUAStructure2020() throws Exception {
        validateCalendarData(ISO_CODE, 2020);
    }

}
