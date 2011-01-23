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

import org.junit.Test;

/**
 * @author Sven
 *
 */
public class HebrewChronologyTest {

	@Test
	public void test(){
		// this is intentionally left blank 
	}
	
//	@Test
//	public void testRoshHashanah5761(){
//		LocalDate hebrew = new LocalDate(5761,1,1,HebrewChronology.getInstanceUTC());
//		LocalDate greg = new LocalDate(2000, 9, 30, GregorianChronology.getInstanceUTC());
//		baseTestHebrew("Rosh Hashanah",hebrew, greg);
//	}

//	@Test
//	public void testRoshHashanah5765(){
//		LocalDate hebrew = new LocalDate(5765,1,1,HebrewChronology.getInstanceUTC());
//		LocalDate greg = new LocalDate(2004,9,16, GregorianChronology.getInstanceUTC());
//		baseTestHebrew("Rosh Hashanah",hebrew, greg);
//	}

//	@Test
//	public void testRoshHashanah5769(){
//		LocalDate hebrew = new LocalDate(5769,1,1,HebrewChronology.getInstanceUTC());
//		LocalDate greg = new LocalDate(2008,9,30, GregorianChronology.getInstanceUTC());
//		baseTestHebrew("Rosh Hashanah",hebrew, greg);
//	}
	
//	@Test
//	public void testRoshHashanah5770(){
//		LocalDate hebrew = new LocalDate(5770,1,1,HebrewChronology.getInstanceUTC());
//		LocalDate greg = new LocalDate(2009,9,19, GregorianChronology.getInstanceUTC());
//		baseTestHebrew("Rosh Hashanah",hebrew, greg);
//	}

//	@Test
//	public void testSukkot5770(){
//		LocalDate hebrew = new LocalDate(5770,1,15,HebrewChronology.getInstanceUTC());
//		LocalDate greg = new LocalDate(2009,10,3, GregorianChronology.getInstanceUTC());
//		baseTestHebrew("Sukkot",hebrew, greg);
//	}

//	@Test
//	public void testSheminiAtzeret5770(){
//		LocalDate hebrew = new LocalDate(5770,1,22,HebrewChronology.getInstanceUTC());
//		LocalDate greg = new LocalDate(2009,10,10, GregorianChronology.getInstanceUTC());
//		baseTestHebrew("Shemini Atzeret",hebrew, greg);
//	}

//	@Test
//	public void testSimchatTorah5770(){
//		LocalDate hebrew = new LocalDate(5770,1,23,HebrewChronology.getInstanceUTC());
//		LocalDate greg = new LocalDate(2009,10,11, GregorianChronology.getInstanceUTC());
//		baseTestHebrew("Simchat Torah",hebrew, greg);
//	}
	
//	@Test
//	public void testHanukkah5770(){
//		LocalDate hebrew = new LocalDate(5770,3,25,HebrewChronology.getInstanceUTC());
//		LocalDate greg = new LocalDate(2009,12,11, GregorianChronology.getInstanceUTC());
//		baseTestHebrew("Hanukkah",hebrew, greg);
//	}

//	@Test
//	public void testPurim5770(){
//		LocalDate hebrew = new LocalDate(5770,6,14,HebrewChronology.getInstanceUTC());
//		LocalDate greg = new LocalDate(2010,2,27, GregorianChronology.getInstanceUTC());
//		baseTestHebrew("Purim",hebrew, greg);
//	}

//	@Test
//	public void testTuBishvat5770(){
//		LocalDate hebrew = new LocalDate(5770,5,15,HebrewChronology.getInstanceUTC());
//		LocalDate greg = new LocalDate(2010,1,29, GregorianChronology.getInstanceUTC());
//		baseTestHebrew("Tu Bishvat",hebrew, greg);
//	}

//	@Test
//	public void testRoshHaShanah5768(){
//		LocalDate hebrew = new LocalDate(5768,1,1,HebrewChronology.getInstanceUTC());
//		LocalDate greg = new LocalDate(2007,9,13, GregorianChronology.getInstanceUTC());
//		baseTestHebrew("Rosh HaShanah",hebrew, greg);
//	}

//	@Test
//	public void testYomKippur5768(){
//		LocalDate hebrew = new LocalDate(5768,1,10,HebrewChronology.getInstanceUTC());
//		LocalDate greg = new LocalDate(2007,9,22, GregorianChronology.getInstanceUTC());
//		baseTestHebrew("Rosh HaShanah",hebrew, greg);
//	}

//	@Test
//	public void testYomHaatzmaut5762(){
//		LocalDate hebrew = new LocalDate(5762,8,5,HebrewChronology.getInstanceUTC());
//		LocalDate greg = new LocalDate(2002,4,17, GregorianChronology.getInstanceUTC());
//		baseTestHebrew("Yom Ha'atzmaut",hebrew, greg);
//	}

//	@Test
//	public void testLagBaomer5765(){
//		LocalDate hebrew = new LocalDate(5765,9,18,HebrewChronology.getInstanceUTC());
//		LocalDate greg = new LocalDate(2005,5,27, GregorianChronology.getInstanceUTC());
//		baseTestHebrew("Lag Ba'omer",hebrew, greg);
//	}

//	public void baseTestHebrew(String name, LocalDate hebrew, LocalDate correctGregorianDate){
//		System.out.println("Test: "+name);
//		System.out.println("Hebrew date: "+hebrew);
//		LocalDate calculatedGregorianDate = new LocalDate(hebrew.toDateTimeAtStartOfDay(), GregorianChronology.getInstanceUTC());
//		System.out.println("Greg. date: calculated="+calculatedGregorianDate+", expexted="+correctGregorianDate);
//		Assert.assertEquals("Wrong date.", correctGregorianDate, calculatedGregorianDate);
//	}

	
}
