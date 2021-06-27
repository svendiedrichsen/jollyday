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

import java.util.Locale;

import de.jollyday.util.ResourceUtil;

/**
 * Represents a localizable object.
 *
 * @author Christoph Weitkamp
 * @version $Id: $
 */
public abstract class AbstractI18nObject {
	/**
	 * The calculated hashcode cached for performance.
	 */
	protected int hashCode = 0;
	/**
	 * The properties key to retrieve the description with.
	 */
	protected final String propertiesKey;

	/**
	 * Utility for accessing resources.
	 */
	protected final ResourceUtil resourceUtil = new ResourceUtil();

	/**
	 * Constructs a country using the provided ISO code to retrieve the
	 * description with.
	 *
	 * @param propertiesKey
	 *            a {@link java.lang.String} object.
	 */
	public AbstractI18nObject(String propertiesKey) {
		super();
		this.propertiesKey = propertiesKey;
	}

	/**
	 * <p>
	 * Getter for the <code>propertiesKey</code>.
	 * </p>
	 *
	 * @return the country properties key
	 */
	public String getPropertiesKey() {
		return propertiesKey;
	}

	/**
	 * The description read with the default locale.
	 *
	 * @return Description for this object
	 */
	public String getDescription() {
		return getDescription(Locale.getDefault());
	}

	/**
	 * The description read with the provided locale.
	 *
	 * @param locale
	 *            a {@link java.util.Locale} object.
	 * @return Description for this object
	 */
	public String getDescription(Locale locale) {
		return resourceUtil.getCountryDescription(locale, propertiesKey);
	}

	@Override
	public String toString() {
		return propertiesKey + " (" + getDescription() + ")";
	}

	@Override
	public abstract int hashCode();
}
