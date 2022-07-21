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

/**
 * Represents a city and contains the city code and a localized description.
 *
 * @author Christoph Weitkamp
 * @version $Id: $
 */
public final class City extends AbstractI18nObject implements Comparable<City> {
	/**
	 * The ISO code to retrieve the description with.
	 */
	private final String isoCode;
	/**
	 * The region code to retrieve the description with.
	 */
	private final String regionCode;
	/**
	 * The city code to retrieve the description with.
	 */
	private final String code;

	/**
	 * Constructs a city using the provided code to retrieve the description
	 * with.
	 *
	 * @param isoCode
	 *            a {@link java.lang.String} object.
	 * @param regionCode
	 *            a {@link java.lang.String} object.
	 * @param code
	 *            a {@link java.lang.String} object.
	 */
	public City(String isoCode, String regionCode, String code) {
		super(isoCode + "." + regionCode + "." + code);
		this.isoCode = isoCode;
		this.regionCode = regionCode;
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
	 * Getter for the field <code>regionCode</code>.
	 * </p>
	 *
	 * @return the region code
	 */
	public String getRegionCode() {
		return regionCode;
	}

	/**
	 * <p>
	 * Getter for the field <code>code</code>.
	 * </p>
	 *
	 * @return the city code
	 */
	public String getCode() {
		return code;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj instanceof City) {
			City other = (City) obj;
			return isoCode.equals(other.isoCode) && regionCode.equals(other.regionCode) && code.equals(other.code);
		}
		return false;
	}

	@Override
	public int hashCode() {
		if (hashCode == 0) {
			int hash = 1;
			hash = hash * 31 + isoCode.hashCode();
			hash = hash * 31 + regionCode.hashCode();
			hash = hash * 31 + code.hashCode();
			hashCode = hash;
		}
		return hashCode;
	}

	/**
	 * Compares this city to another city.
	 *
	 * The comparison is primarily based on the city code.
	 *
	 * @param other
	 *            the other city to compare to, not null
	 * @return the comparator value, negative if less, positive if greater
	 */
	@Override
	public int compareTo(City other) {
		return code.compareTo(other.code);
	}
}
