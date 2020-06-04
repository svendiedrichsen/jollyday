package de.jollyday.parser.predicates;

import de.jollyday.spi.Limited;

import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * Evaluates if the provided <code>Holiday</code> instance is valid for the
 * provided year.
 */
public class ValidLimitation implements Predicate<Limited> {

    private int year;

    public ValidLimitation(final int year) {
        this.year = year;
    }

    /**
     * Evaluates if the provided <code>Holiday</code> instance is valid for the
     * provided year.
     *
     * @return is valid for the year.
     */
    @Override
    public boolean test(Limited limited) {
        return new ValidFromTo(year).and(new ValidCycle(year)).test(limited);
    }

}
