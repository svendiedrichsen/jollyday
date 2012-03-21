/**
 * Copyright 2012 Sven Diedrichsen 
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

/**
 * Utility class to implement check functionality.
 * 
 * @author sven
 * 
 */
public final class Check {

	public static void notNull(Object object, String objectName) {
		if (object == null) {
			throw new NullPointerException(objectName + " is null.");
		}
	}

	public static void greaterThan(int value, int minValue, String valueName) {
		if (value <= minValue) {
			throw new IllegalArgumentException("Value " + valueName + " of value " + value + " is less than "
					+ minValue);
		}
	}

	public static void equals(int value, int compareValue, String valueName) {
		if (value != compareValue) {
			throw new IllegalArgumentException("Value " + valueName + " of value " + value + " does not equal "
					+ compareValue);
		}
	}

	public static void notEquals(int value, int compareValue, String valueName) {
		if (value == compareValue) {
			throw new IllegalArgumentException("Value " + valueName + " of value " + value + " equals " + compareValue);
		}
	}

}
