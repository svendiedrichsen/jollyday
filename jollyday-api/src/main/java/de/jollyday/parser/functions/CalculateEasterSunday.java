package de.jollyday.parser.functions;

import de.jollyday.util.CalendarUtil;
import org.threeten.extra.chrono.JulianChronology;

import java.time.LocalDate;
import java.time.chrono.Chronology;
import java.time.chrono.IsoChronology;
import java.util.function.Function;

/**
 * @author sdiedrichsen
 * @version $
 * @since 13.03.20
 */
public class CalculateEasterSunday implements Function<Chronology, LocalDate> {

    private Integer year;

    public CalculateEasterSunday(Integer year) {
        this.year = year;
    }

    @Override
    public LocalDate apply(Chronology chronology) {
        if (chronology == JulianChronology.INSTANCE) {
            return new CalculateJulianEasterSunday().apply(year);
        } else if (chronology == IsoChronology.INSTANCE) {
            return new CalculateGregorianEasterSunday().apply(year);
        } else if (year > 1583) {
            return new CalculateGregorianEasterSunday().apply(year);
        } else {
            return new CalculateJulianEasterSunday().apply(year);
        }
    }

}
