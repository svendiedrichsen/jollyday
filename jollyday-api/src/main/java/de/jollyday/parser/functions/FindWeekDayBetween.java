package de.jollyday.parser.functions;

import de.jollyday.spi.FixedWeekdayBetweenFixed;

import java.time.LocalDate;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Function;
import java.util.stream.StreamSupport;

/**
 * @author sdiedrichsen
 * @version $
 * @since 12.03.20
 */
public class FindWeekDayBetween implements Function<FixedWeekdayBetweenFixed, LocalDate> {

    private final LocalDate from;
    private final LocalDate to;

    public FindWeekDayBetween(LocalDate from, LocalDate to) {
        this.from = from;
        this.to = to;
    }

    @Override
    public LocalDate apply(FixedWeekdayBetweenFixed fwm) {
        LocalDate current = from;
        final Iterator<LocalDate> iterator = new Iterator<LocalDate>() {
             @Override
             public boolean hasNext() {
                 return !current.isAfter(to);
             }
             @Override
             public LocalDate next() {
                 return current.plusDays(1);
             }
         };
         return StreamSupport.stream(Spliterators.spliteratorUnknownSize(iterator,Spliterator.ORDERED | Spliterator.IMMUTABLE), false)
                .filter(date -> date.getDayOfWeek() == fwm.weekday())
                .findFirst().orElse(null);
    }

}
