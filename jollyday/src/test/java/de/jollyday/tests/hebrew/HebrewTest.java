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
package de.jollyday.tests.hebrew;

import org.joda.time.DateTime;
import org.joda.time.chrono.GregorianChronology;
import org.joda.time.chrono.HebrewChronology;
import org.junit.Test;

/**
 * @author svdi1de
 *
 */
public class HebrewTest {

	@Test
	public void testBase(){
		DateTime dt = new DateTime(5769,1,1,0,0,0,0, HebrewChronology.getInstance());
		System.out.println("Hebrew: "+dt);
		DateTime greg = dt.toDateTime(GregorianChronology.getInstance());
		System.out.println("Gregorian: "+greg);
	}
	
}
