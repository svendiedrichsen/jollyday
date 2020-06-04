package de.jollyday.parser.functions;

import de.jollyday.Holiday;
import de.jollyday.spi.Described;

import java.time.LocalDate;
import java.util.function.Function;

/**
 * @author sdiedrichsen
 * @version $
 * @since 12.03.20
 */
public class CreateHoliday implements Function<Described, Holiday> {

    private LocalDate localDate;

    public CreateHoliday(LocalDate localDate) {
        this.localDate = localDate;
    }

    @Override
    public Holiday apply(Described described) {
        return new Holiday(localDate, described.descriptionPropertiesKey(), described.officiality());
    }

}
