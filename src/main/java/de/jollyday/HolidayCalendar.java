/**
 * Copyright 2010 Sven Diedrichsen
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package de.jollyday;

import java.util.Locale;

/**
 * This enum provides a list of all supported holiday calendars.
 *
 * @author Sven
 * @version $Id: $
 */
public enum HolidayCalendar {

	ALBANIA("AL"), ARGENTINA("AR"), AUSTRIA("AT"), AUSTRALIA("AU"), BOSNIA_HERZIGOWINA("BA"), BELGIUM("BE"), BULGARIA(
			"BG"), BOLIVIA("BO"), BRAZIL("BR"), BELARUS("BY"), CANADA(Locale.CANADA.getCountry()), CHILE("CL"), COLOMBIA(
			"CO"), COSTA_RICA("CR"), CROATIA("HR"), CZECH_REPUBLIC("CZ"), DENMARK("DK"), ECUADOR("EC"), ESTONIA("EE"), ETHIOPIA(
			"ET"), FINLAND("FI"), FRANCE(Locale.FRANCE.getCountry()), GERMANY(Locale.GERMANY.getCountry()), GREECE("GR"), HUNGARY(
			"HU"), ICELAND("IS"), IRELAND("IE"), ITALY(Locale.ITALY.getCountry()), JAPAN("JP"), KAZAKHSTAN("KZ"), LATVIA(
			"LV"), LIECHTENSTEIN("LI"), LITHUANIA("LT"), LUXEMBOURG("LU"), MACEDONIA("MK"), MALTA("MT"), MEXICO("MX"), MOLDOVA(
			"MD"), MONTENEGRO("ME"), NETHERLANDS("NL"), NICARAGUA("NI"), NIGERIA("NG"), NORWAY("NO"), PANAMA("PA"), PARAGUAY(
			"PY"), PERU("PE"), POLAND("PL"), PORTUGAL("PT"), ROMANIA("RO"), RUSSIA("RU"), SERBIA("RS"), SLOWAKIA("SK"), SLOWENIA(
			"SI"), SOUTH_AFRICA("ZA"), SPAIN("ES"), SWEDEN("SE"), SWITZERLAND("CH"), TARGET("TARGET"), UKRAINE("UA"), UNITED_STATES(
			Locale.US.getCountry()), UNITED_KINGDOM("GB"), URUGUAY("UY"), VENEZUELA("VE"), NYSE("NYSE");

	private final String id;

	HolidayCalendar(String id) {
		this.id = id;
	}

	/**
	 * <p>
	 * Getter for the field <code>id</code>.
	 * </p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getId() {
		return id;
	}

}
