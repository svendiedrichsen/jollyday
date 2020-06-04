package de.jollyday.spi;

import java.util.stream.Stream;

/**
 * @author sdiedrichsen
 * @version $
 * @since 11.03.20
 */
public interface Holidays {

    Stream<Fixed> fixed();
    Stream<RelativeToFixed> relativeToFixed();
    Stream<RelativeToWeekdayInMonth> relativeToWeekdayInMonth();
    Stream<FixedWeekdayInMonth> fixedWeekdays();
    Stream<ChristianHoliday> christianHolidays();
    Stream<IslamicHoliday> islamicHolidays();
    Stream<FixedWeekdayBetweenFixed> fixedWeekdayBetweenFixed();
    Stream<FixedWeekdayRelativeToFixed> fixedWeekdayRelativeToFixed();
    Stream<HinduHoliday> hinduHolidays();
    // TODO: ? is this used anywhere?
    // Stream<HebrewHoliday> hebrewHolidays();
    Stream<EthiopianOrthodoxHoliday> ethiopianOrthodoxHolidays();
    Stream<RelativeToEasterSunday> relativeToEasterSunday();

}
