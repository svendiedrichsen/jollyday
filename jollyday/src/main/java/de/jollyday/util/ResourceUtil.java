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

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * @author svdi1de
 *
 */
public class ResourceUtil {

	/**
	 * 
	 */
	private static final String COUNTRY_DESCRIPTIONS_FILE_PREFIX = "descriptions.country_descriptions";
	/**
	 * The prefix of the descriptions file.
	 */
	private static final String HOLIDAY_DESCRIPTIONS_FILE_PREFIX = "descriptions.holiday_descriptions";
	/**
	 * Unknown constant will be returned when there is no description configured.
	 */
	private static final String UNDEFINED = "UNDEFINED";
	/**
	 * Cache for the holiday descriptions.
	 */
	private static final Map<Locale, ResourceBundle> HOLIDAY_DESCRIPTION_CACHE= new HashMap<Locale, ResourceBundle>();
	private final static Map<Locale, ResourceBundle> COUNTRY_DESCRIPTIONS_CACHE = 
		new HashMap<Locale, ResourceBundle>();

	/**
	 * The description read with the default locale.
	 * @return
	 */
	public static String getHolidayDescription(String key){
		return getHolidayDescription(Locale.getDefault(), key);
	}
	
	/**
	 * The description read with the provided locale.
	 * @param locale
	 * @return
	 */
	public static String getHolidayDescription(Locale locale, String key){
		ResourceBundle bundle = getHolidayDescriptions(locale);
		if(bundle.containsKey(key)){
			return bundle.getString(key);
		}
		return UNDEFINED;
	}

	/**
	 * Returns the eventually cached ResourceBundle for the holiday descriptions.
	 * @param l Locale to retrieve the descriptions for.
	 * @return ResourceBundle containing the descriptions for the locale.
	 */
	private static synchronized ResourceBundle getHolidayDescriptions(Locale l){
		if(!HOLIDAY_DESCRIPTION_CACHE.containsKey(l)){
			ResourceBundle bundle = ResourceBundle.getBundle(HOLIDAY_DESCRIPTIONS_FILE_PREFIX, l);
			HOLIDAY_DESCRIPTION_CACHE.put(l, bundle);
		}
		return HOLIDAY_DESCRIPTION_CACHE.get(l);
	}

	/**
	 * @return the description
	 */
	public static String getCountryDescription(String key) {
		return getCountryDescription(Locale.getDefault(), key);
	}
	
	/**
	 * Returns the hierarchys description text from the resource bundle.
	 * @param l Locale to return the description text for.
	 * @return Description text
	 */
	public static String getCountryDescription(Locale l, String key){
		if(!COUNTRY_DESCRIPTIONS_CACHE.containsKey(l)){
			ResourceBundle bundle = ResourceBundle.getBundle(COUNTRY_DESCRIPTIONS_FILE_PREFIX, l);
			COUNTRY_DESCRIPTIONS_CACHE.put(l, bundle);
		}
		ResourceBundle resourceBundle = COUNTRY_DESCRIPTIONS_CACHE.get(l);
		if(!resourceBundle.containsKey(key)) {
			return UNDEFINED;
		}
		return resourceBundle.getString(key);
	}

}
