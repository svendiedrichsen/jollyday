package de.jollyday.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.util.Set;

import org.junit.Test;

import de.jollyday.Holiday;
import de.jollyday.HolidayCalendar;
import de.jollyday.HolidayManager;
import de.jollyday.ManagerParameters;
import de.jollyday.tests.base.AbstractCountryTestBase;
import de.jollyday.util.CalendarUtil;

public class HolidayEGTest extends AbstractCountryTestBase {

    private static final int YEAR = 2019;

    private CalendarUtil calendarUtil = new CalendarUtil();

    @Test
    public void testNumberOfHolidays() throws Exception {
        HolidayManager holidayManager = HolidayManager.getInstance(ManagerParameters.create(HolidayCalendar.EGYPT));
        Set<Holiday> holidays = holidayManager.getHolidays(YEAR);
        assertEquals(17, holidays.size());
    }

    @Test
    public void testEasterMonday2019() throws Exception {
        LocalDate expected = calendarUtil.create(YEAR, 4, 29);
        HolidayManager holidayManager = HolidayManager.getInstance(ManagerParameters.create(HolidayCalendar.EGYPT));
        Set<Holiday> holidays = holidayManager.getHolidays(YEAR);
        assertEquals(17, holidays.size());
        assertTrue("Wrong / missing holiday for Easter Monday",
                holidays.stream().filter(holiday -> holiday.getPropertiesKey().equals("christian.EASTER_MONDAY")
                        && holiday.getDate().equals(expected)).count() == 1);
    }

    @Test
    public void testEidFitr2019() throws Exception {
        LocalDate expected = calendarUtil.create(YEAR, 6, 4);
        HolidayManager holidayManager = HolidayManager.getInstance(ManagerParameters.create(HolidayCalendar.EGYPT));
        Set<Holiday> holidays = holidayManager.getHolidays(YEAR);
        assertEquals(17, holidays.size());
        assertTrue("Wrong / missing holiday for Eid Fitr",
                holidays.stream().filter(holiday -> holiday.getPropertiesKey().equals("islamic.ID_AL_FITR")
                        && holiday.getDate().equals(expected)).count() == 1);
    }

    @Test
    public void testArafaat2019() throws Exception {
        LocalDate expected = calendarUtil.create(YEAR, 8, 10);
        HolidayManager holidayManager = HolidayManager.getInstance(ManagerParameters.create(HolidayCalendar.EGYPT));
        Set<Holiday> holidays = holidayManager.getHolidays(YEAR);
        assertEquals(17, holidays.size());
        assertTrue("Wrong / missing holiday for Arafaat", holidays.stream().filter(
                holiday -> holiday.getPropertiesKey().equals("islamic.ARAFAAT") && holiday.getDate().equals(expected))
                .count() == 1);
    }

}
