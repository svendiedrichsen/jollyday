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
 * @version $Id: $
 */
public class CalendarHierarchy {
	private final String id;
	private Map<String, CalendarHierarchy> children = new HashMap<>();
	private final CalendarHierarchy parent;
	private final ResourceUtil resourceUtil = new ResourceUtil();
	private String fallbackDescription;

	/**
	 * Constructor which takes a eventually existing parent hierarchy node and
	 * the ID of this hierarchy.
	 * 
	 * @param parent
	 *            a {@link de.jollyday.CalendarHierarchy} object.
	 * @param id
	 *            a {@link java.lang.String} object.
	 */
	public CalendarHierarchy(CalendarHierarchy parent, String id) {
		this.parent = parent;
		this.id = id;
	}

	/**
	 * <p>
	 * Getter for the field <code>id</code>.
	 * </p>
	 * 
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * Returns the country description for the default locale.
	 * 
	 * @return the description
	 */
	public String getDescription() {
		return getDescription(Locale.getDefault());
	}

	/**
	 * Returns the hierarchies description text from the resource bundle.
	 * 
	 * @param l
	 *            Locale to return the description text for.
	 * @return Description text
	 */
	public String getDescription(Locale l) {
		String descr = resourceUtil.getCountryDescription(l, getPropertiesKey());
		if (ResourceUtil.UNDEFINED.equals(descr) && fallbackDescription != null) {
			descr = fallbackDescription;
		}
		return descr;
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
	 * {@inheritDoc}
	 * 
	 * Compares Hierarchies by id.
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof CalendarHierarchy) {
			return ((CalendarHierarchy) obj).getId().equals(this.getId());
		}
		return super.equals(obj);
	}

	@Override
	public int hashCode() {
		return getId().hashCode();
	}

	/**
	 * <p>
	 * Setter for the field <code>children</code>.
	 * </p>
	 * 
	 * @param children
	 *            the children to set
	 */
	public void setChildren(final Map<String, CalendarHierarchy> children) {
		this.children = children;
	}

	/**
	 * <p>
	 * Getter for the field <code>children</code>.
	 * </p>
	 * 
	 * @return the children
	 */
	public Map<String, CalendarHierarchy> getChildren() {
		return children;
	}

	/**
	 * @param description the fallback description
	 */
	public void setFallbackDescription(String description) {
		this.fallbackDescription = description;
	}

}
