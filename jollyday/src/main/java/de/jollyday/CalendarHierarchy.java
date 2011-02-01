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

import de.jollyday.util.ResourceUtil;

/**
 * Bean class for describing the configuration hierarchy.
 * 
 * @author Sven Diedrichsen
 */
public class CalendarHierarchy {
	private String id;
	private Map<String, CalendarHierarchy> children = new HashMap<String, CalendarHierarchy>();
	private final CalendarHierarchy parent;

	/**
	 * Constructor which takes a eventually existing parent hierarchy node and
	 * the ID of this hierarchy.
	 * 
	 * @param parent
	 * @param id
	 */
	public CalendarHierarchy(CalendarHierarchy parent, String id) {
		this.parent = parent;
		this.id = id;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return ResourceUtil.getCountryDescription(Locale.getDefault(),
				getPropertiesKey());
	}

	/**
	 * Returns the hierarchys description text from the resource bundle.
	 * 
	 * @param l
	 *            Locale to return the description text for.
	 * @return Description text
	 */
	public String getDescription(Locale l) {
		return ResourceUtil.getCountryDescription(l, getPropertiesKey());
	}

	/**
	 * Recursively returns the properties key to retrieve the description from
	 * the localized resource bundle.
	 * 
	 * @return
	 */
	private String getPropertiesKey() {
		if (parent != null) {
			return parent.getPropertiesKey() + "." + getId();
		}
		return getId();
	}

	/**
	 * Compares Hierarchies by id.
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof CalendarHierarchy) {
			return ((CalendarHierarchy) obj).getId().equals(this.getId());
		}
		return super.equals(obj);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return getId().hashCode();
	}

	/**
	 * @param children
	 *            the children to set
	 */
	public void setChildren(final Map<String, CalendarHierarchy> children) {
		this.children = children;
	}

	/**
	 * @return the children
	 */
	public Map<String, CalendarHierarchy> getChildren() {
		return children;
	}

}
