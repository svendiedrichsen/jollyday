package de.jollyday.tests;

import de.jollyday.Holiday;
import de.jollyday.HolidayCalendar;
import de.jollyday.HolidayManager;
import de.jollyday.ManagerParameters;
import de.jollyday.tests.base.AbstractCountryTestBase;
import de.jollyday.util.CalendarUtil;
import org.junit.Test;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class HolidayMATest extends AbstractCountryTestBase {

    private static final HolidayCalendar MOROCCAN_CALENDAR = HolidayCalendar.MOROCCO;
    private static final int YEAR = 2020;
    private CalendarUtil calendarUtil = new CalendarUtil();

    @Test
    public void testNumberOfHolidays() throws Exception {
        final HolidayManager holidayManager = HolidayManager
                .getInstance(ManagerParameters.create(MOROCCAN_CALENDAR));
        Set<Holiday> holidays = holidayManager.getHolidays(YEAR);
        assertEquals(16, holidays.size());
    }

    @Test
    public void test11ofJanuary() {
        testHolidayForKey("INDEPENDENCE_MANIFESTO", calendarUtil.create(YEAR, 1, 11));
    }

    @Test
    public void testGreenMarch() {
        testHolidayForKey("GREEN_MARCH_DAY", calendarUtil.create(YEAR, 11, 6));
    }

    @Test
    public void testAidElFitr() {
        Set<LocalDate> dates = calendarUtil.getIslamicHolidaysInGregorianYear(YEAR, 10, 1);
        final HolidayManager holidayManager = HolidayManager
                .getInstance(ManagerParameters.create(MOROCCAN_CALENDAR));
        Set<Holiday> holidays = holidayManager.getHolidays(YEAR);
        Set<LocalDate> holidayTests = holidays
                .stream()
                .map(Holiday::getDate)
                .collect(Collectors.toSet());
        assertTrue(holidayTests.containsAll(dates));

    }

    @Test
    public void testInvalidAidElFitr() {
        Set<LocalDate> dates = calendarUtil.getIslamicHolidaysInGregorianYear(2002, 10, 1);
        final HolidayManager holidayManager = HolidayManager
                .getInstance(ManagerParameters.create(MOROCCAN_CALENDAR));
        Set<Holiday> holidays = holidayManager.getHolidays(2002);
        Set<LocalDate> holidayTests = holidays
                .stream()
                .map(Holiday::getDate)
                .collect(Collectors.toSet());
        assertFalse(holidayTests.containsAll(dates));

    }

    @Test
    public void testYouthDay() {
        testHolidayForKey("MOROCCO_YOUTH_DAY", calendarUtil.create(YEAR, 8, 21));
    }

    @Test
    public void testMawlidNabiDay2() {
        testHolidayForKey("islamic.MAWLID_AN_NABI_2", calendarUtil.create(YEAR, 10, 30));
    }

    @Test
    public void testThroneDay() {
        testHolidayForKey("THRONE_DAY", calendarUtil.create(YEAR, 7, 30));
    }

    private void testHolidayForKey(String key, LocalDate date) {
        final HolidayManager holidayManager = HolidayManager
                .getInstance(ManagerParameters.create(MOROCCAN_CALENDAR));
        final Set<Holiday> holidays = holidayManager.getHolidays(YEAR);
        final Holiday holiday = holidays.stream()
                .filter(optHoliday ->
                        key.equals(optHoliday.getPropertiesKey()))
                .findFirst().orElse(null);
        assertNotNull(holiday);
        assertEquals(holiday.getDate(), date);
    }

}
