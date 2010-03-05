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

import org.joda.time.LocalDate;

import de.jollyday.util.ResourceUtil;

/**
 * Represents the holiday and contains the actual date and an
 * localized desription.
 * 
 * @author Sven Diedrichsen
 *
 */
public final class Holiday {
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
		return ResourceUtil.getHolidayDescription(Locale.getDefault(), getPropertiesKey());
	}
	
	/**
	 * The description read with the provided locale.
	 * @param locale
	 * @return
	 */
	public String getDescription(Locale locale){
		return ResourceUtil.getHolidayDescription(locale, getPropertiesKey());
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
