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
package de.jollyday.tests.hindu;

import junit.framework.Assert;

import org.joda.time.LocalDate;
import org.joda.time.chrono.GregorianChronology;
import org.joda.time.chrono.HinduChronology;
import org.junit.Test;

/**
 * @author svdi1de
 *
 */
public class TestHinduChronology {
	
	@Test
	public void testHinduBase2078(){
		LocalDate hinduDate = new LocalDate(2000,1,1,HinduChronology.getInstance());
		LocalDate correctGregorianDate = new LocalDate(2078,3,22, GregorianChronology.getInstance());
		baseTestHindu("2078", hinduDate, correctGregorianDate);
	}

	@Test
	public void testHinduBase1970(){
		LocalDate hinduDate = new LocalDate(1892,1,1, HinduChronology.getInstance());
		LocalDate correctGregorianDate = new LocalDate(1970,3,22, GregorianChronology.getInstance());
		baseTestHindu("1970", hinduDate, correctGregorianDate);
	}

	@Test
	public void testHinduHoli(){
		LocalDate hinduDate = new LocalDate(1929,12,15, HinduChronology.getInstance());
		LocalDate correctGregorianDate = new LocalDate(2008,3,5, GregorianChronology.getInstance());
		baseTestHindu("Holi", hinduDate, correctGregorianDate);
	}

	@Test
	public void testHinduGudiPadwa(){
		LocalDate hinduDate = new LocalDate(1930,1,1, HinduChronology.getInstance());
		LocalDate correctGregorianDate = new LocalDate(2008,3,21, GregorianChronology.getInstance());
		baseTestHindu("GudiPadwa", hinduDate, correctGregorianDate);
	}

	@Test
	public void testHinduVatPurnima(){
		LocalDate hinduDate = new LocalDate(1930,3,15, HinduChronology.getInstance());
		LocalDate correctGregorianDate = new LocalDate(2008,6,5,GregorianChronology.getInstance());
		baseTestHindu("VatPurnima", hinduDate, correctGregorianDate);
	}

	@Test
	public void testHinduGuruPurnima(){
		LocalDate hinduDate = new LocalDate(1930,4,22,HinduChronology.getInstance());
		LocalDate correctGregorianDate = new LocalDate(2008,7,13, GregorianChronology.getInstance());
		baseTestHindu("GuruPurnima", hinduDate, correctGregorianDate);
	}

	public void baseTestHindu(String name, LocalDate hinduDate, LocalDate correctGregorianDate){
		System.out.println("Test: "+name);
		System.out.println("Hindu date: "+hinduDate);
		LocalDate calculatedGregorianDate = new LocalDate(hinduDate.plusDays(1).toDateTimeAtStartOfDay(), GregorianChronology.getInstance());
		System.out.println("Greg. date: calculated="+calculatedGregorianDate+", expexted="+correctGregorianDate);
		Assert.assertEquals("Wrong date.", correctGregorianDate, calculatedGregorianDate);
	}

}
