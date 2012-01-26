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
package de.jollyday.util;

import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.Set;

/**
 * <p>ResourceUtil class.</p>
 *
 * @author Sven
 * @version $Id: $
 */
public class ResourceUtil {

	/**
	 * Property prefix for country descriptions.
	 */
	private static final String COUNTRY_PROPERTY_PREFIX = "country.description";
	/**
	 * Property prefix for holiday descriptions.
	 */
	private static final String HOLIDAY_PROPERTY_PREFIX = "holiday.description";
	/**
	 * The prefix of the country description file.
	 */
	private static final String COUNTRY_DESCRIPTIONS_FILE_PREFIX = "descriptions.country_descriptions";
	/**
	 * The prefix of the holiday descriptions file.
	 */
	private static final String HOLIDAY_DESCRIPTIONS_FILE_PREFIX = "descriptions.holiday_descriptions";
	/**
	 * Unknown constant will be returned when there is no description
	 * configured.
	 */
	private static final String UNDEFINED = "undefined";
	/**
	 * Cache for the holiday descriptions resource bundles.
	 */
	private static final Map<Locale, ResourceBundle> HOLIDAY_DESCRIPTION_CACHE = new HashMap<Locale, ResourceBundle>();
	/**
	 * Cache for the country descriptions resource bundles.
	 */
	private final static Map<Locale, ResourceBundle> COUNTRY_DESCRIPTIONS_CACHE = new HashMap<Locale, ResourceBundle>();

	/**
	 * The description read with the default locale.
	 *
	 * @return holiday description using default locale.
	 * @param key a {@link java.lang.String} object.
	 */
	public static String getHolidayDescription(String key) {
		return getHolidayDescription(Locale.getDefault(), key);
	}

	/**
	 * The description read with the provided locale.
	 *
	 * @param locale a {@link java.util.Locale} object.
	 * @return holiday description using the provided locale.
	 * @param key a {@link java.lang.String} object.
	 */
	public static String getHolidayDescription(Locale locale, String key) {
		return getDescription(HOLIDAY_PROPERTY_PREFIX + "." + key,
				getHolidayDescriptions(locale));
	}

	/**
	 * <p>getCountryDescription.</p>
	 *
	 * @return the description
	 * @param key a {@link java.lang.String} object.
	 */
	public static String getCountryDescription(String key) {
		return getCountryDescription(Locale.getDefault(), key);
	}

	/**
	 * Returns the hierarchys description text from the resource bundle.
	 *
	 * @param l
	 *            Locale to return the description text for.
	 * @return Description text
	 * @param key a {@link java.lang.String} object.
	 */
	public static String getCountryDescription(Locale l, String key) {
		return getDescription(COUNTRY_PROPERTY_PREFIX + "." + key,
				getCountryDescriptions(l));
	}

	/**
	 * Returns a list of ISO codes.
	 *
	 * @return 2-digit ISO codes.
	 */
	public static final Set<String> getISOCodes() {
		Set<String> codes = new HashSet<String>();
		ResourceBundle countryDescriptions = getCountryDescriptions(Locale
				.getDefault());
		for (String property : Collections.list(countryDescriptions.getKeys())) {
			String[] split = property.split("\\.");
			if (split != null && split.length > 2) {
				codes.add(split[2].toLowerCase());
			}
		}
		return codes;
	}

	/**
	 * Returns the description from the resource bundle if the key is contained.
	 * It will return 'undefined' otherwise.
	 * 
	 * @param key
	 * @param bundle
	 * @return description
	 */
	private static String getDescription(String key, final ResourceBundle bundle) {
		if (!Collections.list(bundle.getKeys()).contains(key)) {
			return UNDEFINED;
		}
		return bundle.getString(key);
	}

	/**
	 * Returns the eventually cached ResourceBundle for the holiday
	 * descriptions.
	 * 
	 * @param l
	 *            Locale to retrieve the descriptions for.
	 * @return ResourceBundle containing the descriptions for the locale.
	 */
	private static ResourceBundle getHolidayDescriptions(Locale l) {
		return getResourceBundle(l, HOLIDAY_DESCRIPTION_CACHE,
				HOLIDAY_DESCRIPTIONS_FILE_PREFIX);
	}

	/**
	 * Returns the eventually cached ResourceBundle for the holiday
	 * descriptions.
	 * 
	 * @param l
	 *            Locale to retrieve the descriptions for.
	 * @return ResourceBundle containing the descriptions for the locale.
	 */
	private static ResourceBundle getCountryDescriptions(Locale l) {
		return getResourceBundle(l, COUNTRY_DESCRIPTIONS_CACHE,
				COUNTRY_DESCRIPTIONS_FILE_PREFIX);
	}

	/**
	 * Returns the eventually cached ResourceBundle for the descriptions.
	 * 
	 * @param l
	 *            Locale to retrieve the descriptions for.
	 * @return ResourceBundle containing the descriptions for the locale.
	 */
	private static ResourceBundle getResourceBundle(Locale l,
			Map<Locale, ResourceBundle> resourceCache, String filePrefix) {
		synchronized (resourceCache) {
			if (!resourceCache.containsKey(l)) {
				ResourceBundle bundle = ResourceBundle.getBundle(filePrefix, l,
						ResourceUtil.class.getClassLoader());
				if (bundle instanceof PropertyResourceBundle) {
					bundle = new Utf8PropertyResourceBundle(
							(PropertyResourceBundle) bundle);
				}
				resourceCache.put(l, bundle);
			}
			return resourceCache.get(l);
		}
	}

	/**
	 * Own ResourceBundle implementation to overcome missing UTF-8 support of
	 * PropertyResourceBundle in a backward compatible way.
	 * 
	 * @author svdi1de
	 * 
	 */
	private static class Utf8PropertyResourceBundle extends ResourceBundle {
		PropertyResourceBundle bundle;

		private Utf8PropertyResourceBundle(PropertyResourceBundle bundle) {
			this.bundle = bundle;
		}

		public Enumeration<String> getKeys() {
			return bundle.getKeys();
		}

		protected Object handleGetObject(String key) {
			String value = (String) bundle.handleGetObject(key);
			if (value == null) {
				return null;
			}
			try {
				return new String(value.getBytes("ISO-8859-1"), "UTF-8");
			} catch (UnsupportedEncodingException e) {
				// should not fail - logging omitted
				return null;
			}
		}

	}

}
