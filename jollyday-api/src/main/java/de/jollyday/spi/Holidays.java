package de.jollyday.spi;

import java.util.List;

/**
 * @author sdiedrichsen
 * @version $
 * @since 11.03.20
 */
public interface Holidays {

    List<Fixed> fixed();

    List<RelativeToFixed> relativeToFixed();

    List<RelativeToWeekdayInMonth> relativeToWeekdayInMonth();

    List<FixedWeekdayInMonth> fixedWeekday();

    List<ChristianHoliday> christianHoliday();

    List<IslamicHoliday> islamicHoliday();

    List<FixedWeekdayBetweenFixed> fixedWeekdayBetweenFixed();

    List<FixedWeekdayRelativeToFixed> fixedWeekdayRelativeToFixed();

    List<HinduHoliday> hinduHoliday();

    // TODO: ? is this used anywhere?
    // List<HebrewHoliday> hebrewHoliday();

    List<EthiopianOrthodoxHoliday> ethiopianOrthodoxHoliday();

    List<RelativeToEasterSunday> relativeToEasterSunday();

}
