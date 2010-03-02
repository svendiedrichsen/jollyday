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

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import org.joda.time.LocalDate;

/**
 * Represents the holiday and contains the actual date and an
 * localized desription.
 * 
 * @author Sven Diedrichsen
 *
 */
public final class Holiday {
	/**
	 * The prefix of the descriptions file.
	 */
	private static final String DESCRITOPNS_FILE_PREFIX = "descriptions.holiday_descriptions";
	/**
	 * Unknown constant will be returned when there is no description configured.
	 */
	private static final String UNKNOWN = "UNKNOWN";
	/**
	 * Cache for the holiday descriptions.
	 */
	private static final Map<Locale, ResourceBundle> DESCRIPTION_CACHE= new HashMap<Locale, ResourceBundle>();
	/**
	 * The date the holiday occurrs. 
	 */
	private final LocalDate date;
	/**
	 * The properties key to retrieve the description with. 
	 */
	private final String propertiesKey;
	/**
	 * Constructs a holiday for a date using the provided properties
	 * key to retrieve the description with.
	 */
	public Holiday(LocalDate date, String propertiesKey){
		this.date = date;
		this.propertiesKey = propertiesKey == null ? "" : propertiesKey;
	}
	
	/**
	 * Returns the eventually cached ResourceBundle for the holiday descriptions.
	 * @param l Locale to retrieve the descriptions for.
	 * @return ResourceBundle containing the descriptions for the locale.
	 */
	private static synchronized ResourceBundle getDescriptions(Locale l){
		if(!DESCRIPTION_CACHE.containsKey(l)){
			ResourceBundle bundle = ResourceBundle.getBundle(DESCRITOPNS_FILE_PREFIX, l);
			DESCRIPTION_CACHE.put(l, bundle);
		}
		return DESCRIPTION_CACHE.get(l);
	}

	/**
	 * @return the holiday date
	 */
	public LocalDate getDate() {
		return date;
	}

	/**
	 * @return the holidays properties key
	 */
	public String getPropertiesKey() {
		return propertiesKey;
	}
	
	/**
	 * The description read with the default locale.
	 * @return
	 */
	public String getDescription(){
		return getDescription(Locale.getDefault());
	}
	
	/**
	 * The description read with the provided locale.
	 * @param locale
	 * @return
	 */
	public String getDescription(Locale locale){
		ResourceBundle bundle = getDescriptions(locale);
		if(bundle.containsKey(getPropertiesKey())){
			return bundle.getString(getPropertiesKey());
		}
		return UNKNOWN;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if(obj == this) return true;
		if(obj instanceof Holiday){
			Holiday other = (Holiday) obj;
			return other.date.equals(this.date) && other.propertiesKey == this.propertiesKey;
		}
		return false;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
	    int hash = 1;
	    hash = hash * 31 + date.hashCode();
	    hash = hash * 31 + propertiesKey.hashCode();
	    return hash;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return date.toString() + " (" + getDescription() + ")";
	}
}
