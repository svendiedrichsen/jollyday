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

import org.junit.Test;

import de.jollyday.tests.base.AbstractCountryTestBase;

public class HolidayFITest extends AbstractCountryTestBase {

	private static final String ISO_CODE = "fi";
	private static final int YEAR = 2010;

	@Test
	public void testManagerFIStructure() throws Exception{
		validateCountryData(ISO_CODE, YEAR);
	}
	
}
