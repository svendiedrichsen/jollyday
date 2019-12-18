package de.jollyday.spi;

import java.time.DayOfWeek;
import java.time.Month;

public interface FixedWeekdayInMonth extends Described, Typed {
    DayOfWeek weekday();
    Month month();
    Occurrance which();
}
