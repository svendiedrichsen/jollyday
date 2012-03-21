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

import org.junit.Test;

/**
 * @author sven
 * 
 */
public class CheckTest {

	@Test(expected = NullPointerException.class)
	public void testCheckNull() throws Exception {
		Check.notNull(null, "");
	}

	@Test
	public void testCheckNotNull() throws Exception {
		Check.notNull(new Object(), "");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCheckGreaterThanEqual() throws Exception {
		Check.greaterThan(1, 1, "");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCheckGreaterThanSmaller() throws Exception {
		Check.greaterThan(0, 1, "");
	}

	@Test
	public void testCheckGreaterThanGreater() throws Exception {
		Check.greaterThan(2, 1, "");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testEqualsDoesNot() throws Exception {
		Check.equals(0, 1, "");
	}

	public void testEqualsDoes() throws Exception {
		Check.equals(0, 0, "");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNotEqualsDoesNot() throws Exception {
		Check.notEquals(1, 1, "");
	}

	public void testNotEqualsDoes() throws Exception {
		Check.notEquals(0, 1, "");
	}

}
