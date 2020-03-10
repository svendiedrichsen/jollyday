package de.jollyday.spi;

import java.time.Year;

/**
 * @author sdiedrichsen
 * @version $
 * @since 10.03.20
 */
public interface Limited {
    Year validFrom();
    Year validTo();
    YearCycle cycle();
}
