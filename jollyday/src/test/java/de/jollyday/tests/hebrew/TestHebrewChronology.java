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

import junit.framework.Assert;

import org.joda.time.LocalDate;
import org.joda.time.chrono.GregorianChronology;
import org.joda.time.chrono.HebrewChronology;
import org.junit.Test;

/**
 * @author svdi1de
 *
 */
public class TestHebrewChronology {

	@Test
	public void testRoshHashanah5770(){
		LocalDate hebrew = new LocalDate(5770,1,1,HebrewChronology.getInstanceUTC());
		LocalDate greg = new LocalDate(2009,9,19, GregorianChronology.getInstanceUTC());
		baseTestHebrew("Rosh Hashanah",hebrew, greg);
	}

	@Test
	public void testSukkot5770(){
		LocalDate hebrew = new LocalDate(5770,1,15,HebrewChronology.getInstanceUTC());
		LocalDate greg = new LocalDate(2009,10,3, GregorianChronology.getInstanceUTC());
		baseTestHebrew("Sukkot",hebrew, greg);
	}

	@Test
	public void testSheminiAtzeret5770(){
		LocalDate hebrew = new LocalDate(5770,1,22,HebrewChronology.getInstanceUTC());
		LocalDate greg = new LocalDate(2009,10,10, GregorianChronology.getInstanceUTC());
		baseTestHebrew("Shemini Atzeret",hebrew, greg);
	}

	@Test
	public void testSimchatTorah5770(){
		LocalDate hebrew = new LocalDate(5770,1,23,HebrewChronology.getInstanceUTC());
		LocalDate greg = new LocalDate(2009,10,11, GregorianChronology.getInstanceUTC());
		baseTestHebrew("Simchat Torah",hebrew, greg);
	}
	
//	@Test
//	public void testHanukkah5770(){
//		LocalDate hebrew = new LocalDate(5770,3,25,HebrewChronology.getInstanceUTC());
//		LocalDate greg = new LocalDate(2009,12,12, GregorianChronology.getInstanceUTC());
//		baseTestHebrew("Hanukkah",hebrew, greg);
//	}
	
	public void baseTestHebrew(String name, LocalDate hebrew, LocalDate correctGregorianDate){
		System.out.println("Test: "+name);
		System.out.println("Hebrew date: "+hebrew);
		LocalDate calculatedGregorianDate = new LocalDate(hebrew.toDateTimeAtStartOfDay(), GregorianChronology.getInstanceUTC());
		System.out.println("Greg. date: calculated="+calculatedGregorianDate+", expexted="+correctGregorianDate);
		Assert.assertEquals("Wrong date.", correctGregorianDate, calculatedGregorianDate);
	}

	
}
