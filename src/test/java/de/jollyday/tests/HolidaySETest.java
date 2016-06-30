package de.jollyday.tests;

import org.junit.Test;

import de.jollyday.tests.base.AbstractCountryTestBase;

public class HolidaySETest extends AbstractCountryTestBase {

    private static final String ISO_CODE = "se";
    private static final int YEAR = 2016;

    @Test
    public void testManagerSEStructure() throws Exception {
        validateCalendarData(ISO_CODE, YEAR);
    }

}
