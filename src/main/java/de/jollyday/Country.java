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
 * Represents a country. It contains the ISO code, a localized description and a
 * list of regions.
 *
 * @author Christoph Weitkamp
 * @version $Id: $
 */
public final class Country extends AbstractI18nObject implements Comparable<Country> {
	/**
	 * The ISO code to retrieve the description with.
	 */
	private final String isoCode;
	/**
	 * A map of regions inside this country
	 */
	private final Map<String, Region> regions = new HashMap<>(0);

	/**
	 * Constructs a country using the provided ISO code to retrieve the
	 * description with.
	 *
	 * @param isoCode
	 *            a {@link java.lang.String} object.
	 */
	public Country(String isoCode) {
		super(isoCode);
		this.isoCode = isoCode;
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
	 * Adds a {@link Region} to this country.
	 *
	 * @param region
	 *            a {@link Region} object.
	 */
	public void addRegion(Region region) {
		regions.put(region.getCode(), region);
	}

	/**
	 * The {@link Region}s of this country.
	 *
	 * @return an unmodifiable list of all {@link Region}s of this country
	 */
	public Collection<Region> getRegions() {
		return Collections.unmodifiableCollection(regions.values());
	}

	/**
	 * Returns a specific {@link Region}s of this country.
	 *
	 * @param regionCode
	 *            {@link java.lang.String}the region code.
	 * @return a {@link Region} (could be <code>null</code>)
	 */
	public Region getRegion(String regionCode) {
		return regions.get(regionCode);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj instanceof Country) {
			Country other = (Country) obj;
			return isoCode.equals(other.isoCode) && regions.equals(other.regions);
		}
		return false;
	}

	@Override
	public int hashCode() {
		if (hashCode == 0) {
			int hash = 1;
			hash = hash * 31 + isoCode.hashCode();
			hashCode = hash;
		}
		return hashCode;
	}

	/**
	 * Compares this country to another country.
	 *
	 * The comparison is primarily based on the ISO code.
	 *
	 * @param other
	 *            the other country to compare to, not null
	 * @return the comparator value, negative if less, positive if greater
	 */
	@Override
	public int compareTo(Country other) {
		return isoCode.compareTo(other.isoCode);
	}
}
