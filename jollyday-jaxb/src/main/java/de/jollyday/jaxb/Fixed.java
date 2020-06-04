package de.jollyday.jaxb;

import de.jollyday.HolidayType;
import de.jollyday.spi.YearCycle;

import java.time.MonthDay;
import java.time.Year;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author sdiedrichsen
 * @version $
 * @since 15.03.20
 */
public class Fixed implements de.jollyday.spi.Fixed {

    private XMLUtil xmlUtil = new XMLUtil();

    private de.jollyday.config.Fixed jaxbFixed;

    public Fixed(de.jollyday.config.Fixed jaxbFixed) {
        this.jaxbFixed = jaxbFixed;
    }

    @Override
    public MonthDay day() {
        return MonthDay.of(xmlUtil.getMonth(jaxbFixed.getMonth()), jaxbFixed.getDay());
    }

    @Override
    public String descriptionPropertiesKey() {
        return jaxbFixed.getDescriptionPropertiesKey();
    }

    @Override
    public HolidayType officiality() {
        return jaxbFixed.getLocalizedType() == null
                ? HolidayType.OFFICIAL_HOLIDAY
                : HolidayType.valueOf(jaxbFixed.getLocalizedType().name());
    }

    @Override
    public Year validFrom() {
        return Year.of(jaxbFixed.getValidFrom());
    }

    @Override
    public Year validTo() {
        return Year.of(jaxbFixed.getValidTo());
    }

    @Override
    public YearCycle cycle() {
        return jaxbFixed.getEvery() == null
                ? YearCycle.EVERY_YEAR
                : YearCycle.valueOf(jaxbFixed.getEvery());
    }

    @Override
    public List<de.jollyday.spi.MovingCondition> conditions() {
        return jaxbFixed.getMovingCondition().stream().map(MovingCondition::new).collect(Collectors.toList());
    }
}
