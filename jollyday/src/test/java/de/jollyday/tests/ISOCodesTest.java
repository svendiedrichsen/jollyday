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
package de.jollyday.tests;

import java.util.Set;

import junit.framework.Assert;

import org.junit.Test;

import de.jollyday.util.ResourceUtil;

/**
 * @author svdi1de
 *
 */
public class ISOCodesTest {

	@Test
	public void testISOCodes(){
		Set<String> isoCodes = ResourceUtil.getISOCodes();
		Assert.assertNotNull(isoCodes);
		Assert.assertEquals("Wrong number of ISO codes.", 246, isoCodes.size());
	}
	
}
