package de.jollyday.parser.functions;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.function.Function;

public class CalendarToLocalDate implements Function<Calendar, LocalDate> {

    @Override
    public LocalDate apply(Calendar calendar) {
        return LocalDate.of(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH));
    }

}
