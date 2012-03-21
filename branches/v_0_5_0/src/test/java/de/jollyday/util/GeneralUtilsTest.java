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

import java.util.Arrays;

import junit.framework.Assert;

import org.junit.Test;

/**
 * @author sven
 * 
 */
public class GeneralUtilsTest {

	@Test(expected = IllegalArgumentException.class)
	public void testCopyOfRange() throws Exception {
		GeneralUtils.copyOfRange(new String[] {}, 1, 0);
	}

	@Test
	public void testCopyOfRangeFromString() throws Exception {
		String[] stringArray = new String[] { "0", "1", "2", "3" };
		Assert.assertEquals("Unexpected array copy.", Arrays.toString(Arrays.copyOfRange(stringArray, 1, 2)),
				Arrays.toString(GeneralUtils.copyOfRange(stringArray, 1, 2)));
	}

}
