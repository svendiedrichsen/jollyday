package de.jollyday.parser.predicates;

import de.jollyday.spi.Limited;

import java.util.function.Predicate;

/**
 * @author sdiedrichsen
 * @version $
 * @since 12.03.20
 */
public class ValidCycle implements Predicate<Limited> {

    private final int year;

    public ValidCycle(final int year) {
        this.year = year;
    }

    @Override
    public boolean test(Limited limited) {
        switch (limited.cycle()) {
            case EVERY_YEAR:
                return true;
            case ODD_YEARS:
                return year % 2 != 0;
            case EVEN_YEARS:
                return year % 2 == 0;
            default:
                if (limited.validFrom() != null) {
                    int cycleYears;
                    switch (limited.cycle()) {
                        case TWO_YEARS:
                            cycleYears = 2;
                            break;
                        case THREE_YEARS:
                            cycleYears = 3;
                            break;
                        case FOUR_YEARS:
                            cycleYears = 4;
                            break;
                        case FIVE_YEARS:
                            cycleYears = 5;
                            break;
                        case SIX_YEARS:
                            cycleYears = 6;
                            break;
                        default:
                            throw new IllegalArgumentException("Cannot handle unknown cycle type '" + limited.cycle() + "'.");
                    }
                    return (year - limited.validFrom().getValue()) % cycleYears == 0;
                }
        }
        return true;
    }

}
