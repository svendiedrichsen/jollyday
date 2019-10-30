/**
 * Copyright 2019 Sven Diedrichsen
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

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents a region. It contains the region code, a localized description and
 * a list of cities.
 *
 * @author Christoph Weitkamp
 * @version $Id: $
 */
public final class Region extends AbstractI18nObject implements Comparable<Region> {
	/**
	 * The ISO code to retrieve the description with.
	 */
	private final String isoCode;
	/**
	 * The code to retrieve the description with.
	 */
	private final String code;
	/**
	 * A map of cities inside this region
	 */
	private final Map<String, City> cities = new HashMap<>(0);

	/**
	 * Constructs a region using the provided code to retrieve the description
	 * with.
	 *
	 * @param isoCode
	 *            a {@link java.lang.String} object.
	 * @param code
	 *            a {@link java.lang.String} object.
	 */
	public Region(String isoCode, String code) {
		super(isoCode + "." + code);
		this.isoCode = isoCode;
		this.code = code;
	}

	/**
	 * <p>
	 * Getter for the field <code>isoCode</code>.
	 * </p>
	 *
	 * @return the ISO code
	 */
	public String getISOCode() {
		return isoCode;
	}

	/**
	 * <p>
	 * Getter for the field <code>code</code>.
	 * </p>
	 *
	 * @return the region code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * Adds a {@link City} to this region.
	 *
	 * @param city
	 *            a {@link City} object.
	 */
	public void addCity(City city) {
		cities.put(city.getCode(), city);
	}

	/**
	 * The {@link City Cities} of this region.
	 *
	 * @return an unmodifiable list of all {@link City Cities} of this region
	 */
	public Collection<City> getCities() {
		return Collections.unmodifiableCollection(cities.values());
	}

	/**
	 * Returns a specific {@link City} of this region.
	 *
	 * @param cityCode
	 *            {@link java.lang.String} the city code.
	 * @return a {@link City} (could be <code>null</code>)
	 */
	public City getCity(String cityCode) {
		return cities.get(cityCode);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj instanceof Region) {
			Region other = (Region) obj;
			return isoCode.equals(other.isoCode) && code.equals(other.code) && cities.equals(other.cities);
		}
		return false;
	}

	@Override
	public int hashCode() {
		if (hashCode == 0) {
			int hash = 1;
			hash = hash * 31 + isoCode.hashCode();
			hash = hash * 31 + code.hashCode();
			hashCode = hash;
		}
		return hashCode;
	}

	/**
	 * Compares this region to another region.
	 *
	 * The comparison is primarily based on the region code.
	 *
	 * @param other
	 *            the other region to compare to, not null
	 * @return the comparator value, negative if less, positive if greater
	 */
	@Override
	public int compareTo(Region other) {
		return code.compareTo(other.code);
	}
}
