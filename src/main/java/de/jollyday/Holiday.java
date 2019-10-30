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

import java.time.LocalDate;
import java.util.Locale;

/**
 * Represents the holiday and contains the actual date and a localized
 * description.
 *
 * @author Sven Diedrichsen
 * @version $Id: $
 */
public final class Holiday extends AbstractI18nObject implements Comparable<Holiday> {
	/**
	 * The date the holiday occurs.
	 */
	private final LocalDate date;
	/**
	 * The type of holiday. e.g. official holiday or not.
	 */
	private final HolidayType type;

	/**
	 * Constructs a holiday for a date using the provided properties key to
	 * retrieve the description with.
	 *
	 * @param date
	 *            a {@link LocalDate} object.
	 * @param propertiesKey
	 *            a {@link java.lang.String} object.
	 * @param type
	 *            a {@link de.jollyday.HolidayType} object.
	 */
	public Holiday(LocalDate date, String propertiesKey, HolidayType type) {
		super(propertiesKey == null ? "" : propertiesKey);
		this.date = date;
		this.type = type;
	}

	/**
	 * <p>
	 * Getter for the field <code>date</code>.
	 * </p>
	 *
	 * @return the holiday date
	 */
	public LocalDate getDate() {
		return date;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj instanceof Holiday) {
			Holiday other = (Holiday) obj;
			return date.equals(other.date) && propertiesKey.equals(other.propertiesKey) && type.equals(other.type);
		}
		return false;
	}

	@Override
	public String getDescription(Locale locale) {
		return resourceUtil.getHolidayDescription(locale, propertiesKey);
	}

	@Override
	public int hashCode() {
		if (hashCode == 0) {
			int hash = 1;
			hash = hash * 31 + date.hashCode();
			hash = hash * 31 + propertiesKey.hashCode();
			hash = hash * 31 + type.hashCode();
			hashCode = hash;
		}
		return hashCode;
	}

	@Override
	public String toString() {
		return date.toString() + " (" + getDescription() + ")";
	}

	/**
	 * Gets the type holiday.
	 *
	 * @return the type holiday
	 */
	public HolidayType getType() {
		return type;
	}

	/**
	 * Compares this holiday to another holiday.
	 *
	 * The comparison is primarily based on the date, from earliest to latest by
	 * using the LocalDate comparator.
	 *
	 * @param other
	 *            the other holiday to compare to, not null
	 * @return the comparator value, negative if less, positive if greater
	 */
	@Override
	public int compareTo(Holiday other) {
		return date.compareTo(other.date);
	}
}
