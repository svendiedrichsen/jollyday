package de.jollyday.parser.functions;

import java.time.LocalDate;
import java.time.chrono.ChronoLocalDate;
import java.time.chrono.Chronology;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.time.Month.DECEMBER;
import static java.time.Month.JANUARY;

/**
 * @author sdiedrichsen
 * @version $
 * @since 15.03.20
 */
public class CalculateRelativeDatesFromChronologyWithinGregorianYear implements Function<Integer, Stream<LocalDate>> {

    private final int targetMonth;
    private final int targetDay;
    private final Chronology targetChrono;
    private final int relativeShift;

    public CalculateRelativeDatesFromChronologyWithinGregorianYear(int targetMonth, int targetDay, Chronology targetChrono, int relativeShift) {
        this.targetMonth = targetMonth;
        this.targetDay = targetDay;
        this.targetChrono = targetChrono;
        this.relativeShift = relativeShift;
    }

    @Override
    public Stream<LocalDate> apply(final Integer gregorianYear) {
        final int absoluteShift = Math.abs(relativeShift);

        LocalDate firstGregorianDate = LocalDate.of(gregorianYear, JANUARY, 1);
        LocalDate lastGregorianDate = LocalDate.of(gregorianYear, DECEMBER, 31);

        ChronoLocalDate firstTargetDate = targetChrono.date(firstGregorianDate.minusDays(absoluteShift));
        ChronoLocalDate lastTargetDate = targetChrono.date(lastGregorianDate.plusDays(absoluteShift));

        int targetYear = firstTargetDate.get(ChronoField.YEAR);
        final int lastYear = lastTargetDate.get(ChronoField.YEAR);

        Stream.Builder<LocalDate> builder = Stream.builder();
        while (targetYear <= lastYear) {
            ChronoLocalDate d = targetChrono.date(targetYear, targetMonth, targetDay).plus(relativeShift,
                    ChronoUnit.DAYS);
            if (!firstGregorianDate.isAfter(d) && !lastGregorianDate.isBefore(d)) {
                builder.accept(LocalDate.from(d));
            }
            targetYear++;
        }
        return builder.build();
    }

}
