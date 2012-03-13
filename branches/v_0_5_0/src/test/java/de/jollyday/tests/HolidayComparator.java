/**
 * Copyright 2011 Sven Diedrichsen 
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
package de.jollyday.tests;

import java.io.Serializable;
import java.util.Comparator;

import de.jollyday.Holiday;

/**
 * @author Sven
 * 
 */
public class HolidayComparator implements Comparator<Holiday>, Serializable {

	private static final long serialVersionUID = 7346124638993088797L;

	public int compare(Holiday o1, Holiday o2) {
		return o1.getDate().compareTo(o2.getDate());
	}

}
