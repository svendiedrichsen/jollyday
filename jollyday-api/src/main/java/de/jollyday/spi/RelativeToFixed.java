package de.jollyday.spi;

import java.time.DayOfWeek;

public interface RelativeToFixed extends Described, Typed {
    Fixed date();
    DayOfWeek weekday();
    Relation when();
    Integer days();
}
