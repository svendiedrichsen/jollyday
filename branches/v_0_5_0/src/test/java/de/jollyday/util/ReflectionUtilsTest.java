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

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import de.jollyday.config.ObjectFactory;

/**
 * @author sven
 * 
 */
public class ReflectionUtilsTest {

	/**
	 * Test method for
	 * {@link de.jollyday.util.ReflectionUtils#loadClass(java.lang.String)}
	 * using NULL class name.
	 * 
	 * @throws ClassNotFoundException
	 */
	@Test(expected = NullPointerException.class)
	public void testLoadClassNullCheck() throws ClassNotFoundException {
		ReflectionUtils.loadClass(null);
	}

	/**
	 * Test method for
	 * {@link de.jollyday.util.ReflectionUtils#loadClass(java.lang.String)}
	 * loading an undefined class.
	 * 
	 * @throws ClassNotFoundException
	 */
	@Test(expected = ClassNotFoundException.class)
	public void testLoadClassNotFound() throws ClassNotFoundException {
		ReflectionUtils.loadClass("anypackage.AnyUnknownClass");
	}

	/**
	 * Test method for
	 * {@link de.jollyday.util.ReflectionUtils#loadClass(java.lang.String)} with
	 * loading the correct class.
	 * 
	 * @throws ClassNotFoundException
	 */
	@Test
	public void testLoadClass() throws Exception {
		assertEquals("Wrong class loaded.", ObjectFactory.class,
				ReflectionUtils.loadClass("de.jollyday.config.ObjectFactory"));
	}

}
