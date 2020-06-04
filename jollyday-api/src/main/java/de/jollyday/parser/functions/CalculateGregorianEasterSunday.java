package de.jollyday.parser.functions;

import java.time.LocalDate;
import java.util.function.Function;

import static java.time.Month.APRIL;
import static java.time.Month.MARCH;

/**
 * @author sdiedrichsen
 * @version $
 * @since 15.03.20
 */
public class CalculateGregorianEasterSunday implements Function<Integer, LocalDate> {

    @Override
    public LocalDate apply(Integer year) {
        int a, b, c, d, e, f, g, h, i, j, k, l;
        int x, month, day;
        a = year % 19;
        b = year / 100;
        c = year % 100;
        d = b / 4;
        e = b % 4;
        f = (b + 8) / 25;
        g = (b - f + 1) / 3;
        h = (19 * a + b - d - g + 15) % 30;
        i = c / 4;
        j = c % 4;
        k = (32 + 2 * e + 2 * i - h - j) % 7;
        l = (a + 11 * h + 22 * k) / 451;
        x = h + k - 7 * l + 114;
        month = x / 31;
        day = (x % 31) + 1;
        return LocalDate.of(year, (month == 3 ? MARCH : APRIL), day);
    }

}
