package de.jollyday.tests;

import de.jollyday.Holiday;
import de.jollyday.HolidayCalendar;
import de.jollyday.HolidayManager;
import de.jollyday.ManagerParameters;
import de.jollyday.tests.base.AbstractCountryTestBase;
import de.jollyday.util.CalendarUtil;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class HolidayTRTest extends AbstractCountryTestBase {

    private static final String ISO_CODE = "tr";
    private static final int YEAR = 2019;

    private CalendarUtil calendarUtil = new CalendarUtil();

    @Test
    public void testManagerTRStructure() throws Exception {
        validateCalendarData(ISO_CODE, YEAR);
    }

    @Test
    public void testNumberOfHolidays() throws Exception {
        HolidayManager holidayManager = HolidayManager.getInstance(ManagerParameters.create(HolidayCalendar.TURKEY));
        Set<Holiday> holidays = holidayManager.getHolidays(YEAR);
        assertEquals(9, holidays.size());
    }

    @Test
    public void testRamazan2019() throws  Exception {
        // Actually, in Turkey, Ramadan is one day after Eid Mubarak, for keep the Eid al Fitr for now
        LocalDate expected = calendarUtil.create(YEAR, 6, 4);
        HolidayManager holidayManager = HolidayManager.getInstance(ManagerParameters.create(HolidayCalendar.TURKEY));
        Set<Holiday> holidays = holidayManager.getHolidays(YEAR);
        assertEquals(9, holidays.size());
        boolean found = false;
        for (Holiday holiday: holidays) {
            if (holiday.getPropertiesKey().equals("islamic.ID_AL_FITR")) {
                if (holiday.getDate().equals(expected)) {
                    found = true;
                }
            }
        }
        assertTrue(found, "Wrong / missing holiday for Ramazan");
    }

    @Test
    public void testKurban2019() throws  Exception {
        LocalDate expected = calendarUtil.create(YEAR, 8, 11);
        HolidayManager holidayManager = HolidayManager.getInstance(ManagerParameters.create(HolidayCalendar.TURKEY));
        Set<Holiday> holidays = holidayManager.getHolidays(YEAR);
        assertEquals(9, holidays.size());
        boolean found = false;
        for (Holiday holiday: holidays) {
            if (holiday.getPropertiesKey().equals("islamic.ID_UL_ADHA")) {
                if (holiday.getDate().equals(expected)) {
                    found = true;
                }
            }
        }
        assertTrue(found, "Wrong / missing holiday for Kurban");
    }
}
