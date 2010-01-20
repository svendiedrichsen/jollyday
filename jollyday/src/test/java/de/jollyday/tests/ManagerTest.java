package de.jollyday.tests;

import java.util.Calendar;
import java.util.Set;

import de.jollyday.Manager;

public class ManagerTest {

	public void testManagerDE() throws Exception{
		Manager m = Manager.getInstance("de");
		Set<Calendar> holidays = m.getHolidays(2010);
		holidays.size();
	}
	
}
