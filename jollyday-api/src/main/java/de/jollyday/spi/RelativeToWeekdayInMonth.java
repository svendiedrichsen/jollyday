package de.jollyday.spi;

import java.time.DayOfWeek;

public interface RelativeToWeekdayInMonth extends Described, Typed {
    FixedWeekdayInMonth weekdayInMonth();
    DayOfWeek weekday();
    Relation when();
}
