package de.jollyday.spi;

import java.time.DayOfWeek;
import java.time.LocalDate;

/**
 * @author sdiedrichsen
 * @version $
 * @since 03.11.19
 */
public interface FixedWeekdayBetweenFixed extends Described, Typed {
    Fixed from();
    Fixed to();
    DayOfWeek weekday();
}
