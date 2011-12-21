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
import java.util.Map;

/**
 * Bean class for describing the configuration hierarchy.
 * @author Sven Diedrichsen
 */
public class Hierarchy {
	private String id;
	private String description;
	private Map<String, Hierarchy> children = new HashMap<String, Hierarchy>();
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	/**
	 * Compares Hierarchies by id.
	 */
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Hierarchy){
			return ((Hierarchy)obj).getId().equals(this.getId());
		}
		return super.equals(obj);
	}
	/**
	 * @param children the children to set
	 */
	public void setChildren(Map<String, Hierarchy> children) {
		this.children = children;
	}
	/**
	 * @return the children
	 */
	public Map<String, Hierarchy> getChildren() {
		return children;
	}
	
}