package de.jollyday.parameter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import de.jollyday.HolidayManager;
import de.jollyday.ManagerParameter;
import de.jollyday.ManagerParameters;
import java.util.Calendar;
import java.util.Locale;
import org.junit.Test;

public class ManagerParametersTest {
    @Test
    public void testCreateParameterFromLocale() {
        ManagerParameter params = ManagerParameters.create(Locale.GERMANY);
        HolidayManager manager = HolidayManager.getInstance(params);
        assertEquals(Locale.GERMANY.getCountry().toLowerCase(), manager.getCalendarHierarchy().getId());
        Calendar thirdOfOctober = Calendar.getInstance();
        thirdOfOctober.set(Calendar.MONTH, Calendar.OCTOBER);
        thirdOfOctober.set(Calendar.DAY_OF_MONTH, 3);
        assertTrue("Oct 3rd should be a holiday in " + Locale.GERMANY, manager.isHoliday(thirdOfOctober));
    }
}
