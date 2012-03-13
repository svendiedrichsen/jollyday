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
 * Class that contains general utility methods.
 * 
 * @author sven
 * 
 */
public class GeneralUtils {
	/**
	 * Copies the specified range from the original array to a new array. This
	 * is a replacement for Java 1.6 Arrays.copyOfRange() specialized in String.
	 * 
	 * @param original
	 *            the original array to copy range from
	 * @param from
	 *            the start of the range to copy from the original array
	 * @param to
	 *            the inclusive end of the range to copy from the original array
	 * @return the copied range
	 */
	public static String[] copyOfRange(final String[] original, int from, int to) {
		int newLength = to - from;
		if (newLength < 0) {
			throw new IllegalArgumentException(from + " > " + to);
		}
		String[] copy = new String[newLength];
		System.arraycopy(original, from, copy, 0, Math.min(original.length - from, newLength));
		return copy;
	}

}
