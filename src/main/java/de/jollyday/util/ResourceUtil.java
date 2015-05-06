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

import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

/**
 * <p>
 * ResourceUtil class.
 * </p>
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
	public static final String UNDEFINED = "undefined";
	/**
	 * Cache for the holiday descriptions resource bundles.
	 */
	private static final Map<Locale, ResourceBundle> HOLIDAY_DESCRIPTION_CACHE = new HashMap<>();
	/**
	 * Cache for the country descriptions resource bundles.
	 */
	private final static Map<Locale, ResourceBundle> COUNTRY_DESCRIPTIONS_CACHE = new HashMap<>();
	/**
	 * Util class to provide the correct classloader.
	 */
	private final transient ClassLoadingUtil classLoadingUtil = new ClassLoadingUtil();

	/**
	 * The description read with the default locale.
	 * 
	 * @return holiday description using default locale.
	 * @param key
	 *            a {@link java.lang.String} object.
	 */
	public String getHolidayDescription(String key) {
		return getHolidayDescription(Locale.getDefault(), key);
	}

	/**
	 * The description read with the provided locale.
	 * 
	 * @param locale
	 *            a {@link java.util.Locale} object.
	 * @return holiday description using the provided locale.
	 * @param key
	 *            a {@link java.lang.String} object.
	 */
	public String getHolidayDescription(Locale locale, String key) {
		return getDescription(HOLIDAY_PROPERTY_PREFIX + "." + key, getHolidayDescriptions(locale));
	}

	/**
	 * <p>
	 * getCountryDescription.
	 * </p>
	 * 
	 * @return the description
	 * @param key
	 *            a {@link java.lang.String} object.
	 */
	public String getCountryDescription(String key) {
		return getCountryDescription(Locale.getDefault(), key);
	}

	/**
	 * Returns the hierarchys description text from the resource bundle.
	 * 
	 * @param l
	 *            Locale to return the description text for.
	 * @return Description text
	 * @param key
	 *            a {@link java.lang.String} object.
	 */
	public String getCountryDescription(Locale l, String key) {
		if (key != null) {
			return getDescription(COUNTRY_PROPERTY_PREFIX + "." + key.toLowerCase(), getCountryDescriptions(l));
		}
		return ResourceUtil.UNDEFINED;
	}

	/**
	 * Returns a list of ISO codes.
	 * 
	 * @return 2-digit ISO codes.
	 */
	public Set<String> getISOCodes() {
		Set<String> codes = new HashSet<>();
		ResourceBundle countryDescriptions = getCountryDescriptions(Locale.getDefault());
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
	private String getDescription(String key, final ResourceBundle bundle) {
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
	private ResourceBundle getHolidayDescriptions(Locale l) {
		return getResourceBundle(l, HOLIDAY_DESCRIPTION_CACHE, HOLIDAY_DESCRIPTIONS_FILE_PREFIX);
	}

	/**
	 * Returns the eventually cached ResourceBundle for the holiday
	 * descriptions.
	 * 
	 * @param l
	 *            Locale to retrieve the descriptions for.
	 * @return ResourceBundle containing the descriptions for the locale.
	 */
	private ResourceBundle getCountryDescriptions(Locale l) {
		return getResourceBundle(l, COUNTRY_DESCRIPTIONS_CACHE, COUNTRY_DESCRIPTIONS_FILE_PREFIX);
	}

	/**
	 * Returns the eventually cached ResourceBundle for the descriptions.
	 * 
	 * @param l
	 *            Locale to retrieve the descriptions for.
	 * @return ResourceBundle containing the descriptions for the locale.
	 */
	private ResourceBundle getResourceBundle(Locale l, Map<Locale, ResourceBundle> resourceCache, String filePrefix) {
		synchronized (resourceCache) {
			if (!resourceCache.containsKey(l)) {
				ResourceBundle bundle = ResourceBundle.getBundle(filePrefix, l, classLoadingUtil.getClassloader());
				resourceCache.put(l, bundle);
			}
			return resourceCache.get(l);
		}
	}

	/**
	 * Returns the resource by URL.
	 * 
	 * @param resourceName
	 *            the name/path of the resource to load
	 * @return the URL to the resource
	 */
	public URL getResource(String resourceName) {
		try {
			return classLoadingUtil.getClassloader().getResource(resourceName);
		} catch (Exception e) {
			throw new IllegalStateException("Cannot load resource: " + resourceName, e);
		}
	}

}
