package de.jollyday.parser.functions;

import de.jollyday.parser.predicates.ValidMovingCondition;
import de.jollyday.spi.Movable;
import de.jollyday.spi.With;

import java.time.LocalDate;
import java.util.function.Function;

import static java.time.temporal.TemporalAdjusters.nextOrSame;
import static java.time.temporal.TemporalAdjusters.previousOrSame;

/**
 * @author sdiedrichsen
 * @version $
 * @since 12.03.20
 */
public class MoveDateRelative implements Function<Movable, LocalDate> {

    private LocalDate date;

    public MoveDateRelative(LocalDate date) {
        this.date = date;
    }

    @Override
    public LocalDate apply(Movable movable) {
        return movable.conditions().stream()
                .filter(new ValidMovingCondition(date))
                .map(mc -> date.with(mc.with() == With.NEXT
                        ? nextOrSame(mc.weekday())
                        : previousOrSame(mc.weekday())))
                .findFirst()
                .orElse(date);
    }

}
