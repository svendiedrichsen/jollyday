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
package de.jollyday.tests.base;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.joda.time.LocalDate;
import org.junit.Assert;

import de.jollyday.Hierarchy;
import de.jollyday.Manager;

/**
 * @author svdi1de
 *
 */
public abstract class AbstractCountryTestBase {

	/**
	 * Compares two hierarchy structure by traversing down.
	 * @param expected This is the test structure which is how it should be.
	 * @param found This is the real live data structure.
	 */
	protected void compareHierarchies(Hierarchy expected, Hierarchy found) {
		Assert.assertEquals("Wrong hierarchy id.", expected.getId(), found.getId());
		Assert.assertEquals("Number of children wrong.", expected.getChildren().size(), found.getChildren().size());
		for(String id : expected.getChildren().keySet()){
			Assert.assertTrue("Missing "+id+" within "+found.getId(), found.getChildren().containsKey(id));
			compareHierarchies(expected.getChildren().get(id), found.getChildren().get(id));
		}
	}

	/**
	 * @param testManager
	 * @param m
	 */
	protected void compareData(Manager expected, Manager found, int year) {
		Hierarchy expectedHierarchy = expected.getHierarchy();
		ArrayList<String> args = new ArrayList<String>();
		compareDates(expected, found, expectedHierarchy, args, year);
	}

	private void compareDates(Manager expected, Manager found, Hierarchy h,
			List<String> args, int year) {
				Set<LocalDate> expectedHolidays = expected.getHolidays(year, args.toArray(new String[]{}));
				Set<LocalDate> foundHolidays = found.getHolidays(year, args.toArray(new String[]{}));
				Assert.assertEquals("Wrong dates found for "+args, expectedHolidays, foundHolidays);
				for(String id : h.getChildren().keySet()){
					ArrayList<String> newArgs = new ArrayList<String>(args);
					newArgs.add(id);
					compareDates(expected, found, h.getChildren().get(id), newArgs, year);
				}
			}

	protected void validateCountryData(String countryCode, int year)
			throws Exception {
				Manager dataManager = Manager.getInstance(countryCode);
				Manager testManager = Manager.getInstance("test_"+countryCode+"_"+Integer.toString(year));
			
				Hierarchy dataHierarchy = dataManager.getHierarchy();
				Hierarchy testHierarchy = testManager.getHierarchy();
				
				compareHierarchies(testHierarchy, dataHierarchy);
				compareData(testManager, dataManager, year);
			}

}
