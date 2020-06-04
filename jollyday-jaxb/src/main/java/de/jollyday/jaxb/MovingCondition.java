package de.jollyday.jaxb;

import de.jollyday.spi.With;

import java.time.DayOfWeek;

/**
 * @author sdiedrichsen
 * @version $
 * @since 15.03.20
 */
public class MovingCondition implements de.jollyday.spi.MovingCondition {

    private de.jollyday.config.MovingCondition jaxbMovingCondition;

    public MovingCondition(de.jollyday.config.MovingCondition jaxbMovingCondition) {
        this.jaxbMovingCondition = jaxbMovingCondition;
    }

    @Override
    public DayOfWeek substitute() {
        return DayOfWeek.valueOf(jaxbMovingCondition.getSubstitute().name());
    }

    @Override
    public With with() {
        return With.valueOf(jaxbMovingCondition.getWith().name());
    }

    @Override
    public DayOfWeek weekday() {
        return DayOfWeek.valueOf(jaxbMovingCondition.getWeekday().name());
    }
}
