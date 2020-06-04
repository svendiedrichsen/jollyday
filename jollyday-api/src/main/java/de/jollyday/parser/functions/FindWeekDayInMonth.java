package de.jollyday.parser.functions;

import de.jollyday.spi.FixedWeekdayInMonth;
import de.jollyday.spi.Occurrance;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.function.Function;

import static java.time.temporal.TemporalAdjusters.dayOfWeekInMonth;
import static java.time.temporal.TemporalAdjusters.lastInMonth;

/**
 * @author sdiedrichsen
 * @version $
 * @since 12.03.20
 */
public class FindWeekDayInMonth implements Function<FixedWeekdayInMonth, LocalDate> {

    private int year;

    public FindWeekDayInMonth(int year) {
        this.year = year;
    }

    @Override
    public LocalDate apply(FixedWeekdayInMonth fixedWeekdayInMonth) {
        final LocalDate date = LocalDate.of(year, fixedWeekdayInMonth.month(), 1);
        final DayOfWeek weekday = fixedWeekdayInMonth.weekday();
        if (Occurrance.LAST == fixedWeekdayInMonth.which()) {
            return date.with(lastInMonth(weekday));
        }
        return date.with(dayOfWeekInMonth(fixedWeekdayInMonth.which().ordinal() + 1, weekday));
    }
}
