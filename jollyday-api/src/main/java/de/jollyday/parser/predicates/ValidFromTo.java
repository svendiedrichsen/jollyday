package de.jollyday.parser.predicates;

import de.jollyday.spi.Limited;

import java.util.function.Predicate;

/**
 * @author sdiedrichsen
 * @version $
 * @since 12.03.20
 */
public class ValidFromTo implements Predicate<Limited> {

    private final int year;

    public ValidFromTo(final int year) {
        this.year = year;
    }

    @Override
    public boolean test(Limited limited) {
        return (limited.validFrom() == null || limited.validFrom().getValue() <= year)
        				&& (limited.validTo() == null || limited.validTo().getValue() >= year);

    }

}
