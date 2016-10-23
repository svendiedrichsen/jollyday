package de.jollyday;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Riccardo Casatta @RCasatta on 09/03/16.
 */
public class CountryToHolidayCalendar {
    public static Map<String,HolidayCalendar> map=new HashMap<>();
    static {
        map.put("AL", HolidayCalendar.ALBANIA);
        map.put("AR", HolidayCalendar.ARGENTINA);
        map.put("AT", HolidayCalendar.AUSTRIA);
        map.put("AU", HolidayCalendar.AUSTRALIA);
        map.put("BA", HolidayCalendar.BOSNIA_HERZIGOWINA);
        map.put("BE", HolidayCalendar.BELGIUM);
        map.put("BG", HolidayCalendar.BULGARIA);
        map.put("BO", HolidayCalendar.BOLIVIA);
        map.put("BR", HolidayCalendar.BRAZIL);
        map.put("BY", HolidayCalendar.BELARUS);
        map.put(Locale.CANADA.getCountry(), HolidayCalendar.CANADA);
        map.put("CL", HolidayCalendar.CHILE);
        map.put("CO", HolidayCalendar.COLOMBIA);
        map.put("CR", HolidayCalendar.COSTA_RICA);
        map.put("HR", HolidayCalendar.CROATIA);
        map.put("CZ", HolidayCalendar.CZECH_REPUBLIC);
        map.put("DK", HolidayCalendar.DENMARK);
        map.put("EC", HolidayCalendar.ECUADOR);
        map.put("EE", HolidayCalendar.ESTONIA);
        map.put("ET", HolidayCalendar.ETHIOPIA);
        map.put("FI", HolidayCalendar.FINLAND);
        map.put(Locale.FRANCE.getCountry(), HolidayCalendar.FRANCE);
        map.put(Locale.GERMANY.getCountry(), HolidayCalendar.GERMANY);
        map.put("GR", HolidayCalendar.GREECE);
        map.put("HU", HolidayCalendar.HUNGARY);
        map.put("IS", HolidayCalendar.ICELAND);
        map.put("IE", HolidayCalendar.IRELAND);
        map.put(Locale.ITALY.getCountry(), HolidayCalendar.ITALY);
        map.put("JP", HolidayCalendar.JAPAN);
        map.put("KZ", HolidayCalendar.KAZAKHSTAN);
        map.put("LV", HolidayCalendar.LATVIA);
        map.put("LI", HolidayCalendar.LIECHTENSTEIN);
        map.put("LT", HolidayCalendar.LITHUANIA);
        map.put("LU", HolidayCalendar.LUXEMBOURG);
        map.put("MK", HolidayCalendar.MACEDONIA);
        map.put("MT", HolidayCalendar.MALTA);
        map.put("MX", HolidayCalendar.MEXICO);
        map.put("MD", HolidayCalendar.MOLDOVA);
        map.put("ME", HolidayCalendar.MONTENEGRO);
        map.put("NL", HolidayCalendar.NETHERLANDS);
        map.put("NI", HolidayCalendar.NICARAGUA);
        map.put("NG", HolidayCalendar.NIGERIA);
        map.put("NO", HolidayCalendar.NORWAY);
        map.put("PA", HolidayCalendar.PANAMA);
        map.put("PY", HolidayCalendar.PARAGUAY);
        map.put("PE", HolidayCalendar.PERU);
        map.put("PL", HolidayCalendar.POLAND);
        map.put("PT", HolidayCalendar.PORTUGAL);
        map.put("RO", HolidayCalendar.ROMANIA);
        map.put("RU", HolidayCalendar.RUSSIA);
        map.put("RS", HolidayCalendar.SERBIA);
        map.put("SK", HolidayCalendar.SLOWAKIA);
        map.put("SI", HolidayCalendar.SLOWENIA);
        map.put("ZA", HolidayCalendar.SOUTH_AFRICA);
        map.put("ES", HolidayCalendar.SPAIN);
        map.put("SE", HolidayCalendar.SWEDEN);
        map.put("CH", HolidayCalendar.SWITZERLAND);
        map.put("UA", HolidayCalendar.UKRAINE);
        map.put(Locale.US.getCountry(), HolidayCalendar.UNITED_STATES);
        map.put("GB", HolidayCalendar.UNITED_KINGDOM);
        map.put("UY", HolidayCalendar.URUGUAY);
        map.put("VE", HolidayCalendar.VENEZUELA);

    }
}
