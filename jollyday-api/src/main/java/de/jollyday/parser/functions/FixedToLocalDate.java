package de.jollyday.parser.functions;

import de.jollyday.spi.Fixed;

import java.time.LocalDate;
import java.util.function.Function;

/**
 * @author sdiedrichsen
 * @version $
 * @since 12.03.20
 */
public class FixedToLocalDate implements Function<Fixed, LocalDate> {

    private int year;

    public FixedToLocalDate(int year) {
        this.year = year;
    }

    @Override
    public LocalDate apply(Fixed fixed) {
        return fixed.day().atYear(year);
    }
}
