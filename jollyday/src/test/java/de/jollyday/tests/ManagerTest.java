package de.jollyday.tests;

import java.util.Calendar;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import de.jollyday.Manager;

public class ManagerTest {

	@Test
	public void testManagerDE() throws Exception{
		Manager m = Manager.getInstance("de");
		Set<Calendar> holidays = m.getHolidays(2010);
		Assert.assertEquals("Wrong number of holidays.", 9, holidays.size());
	}
	
	@Test
	public void testManagerDE_BY() throws Exception{
		Manager m = Manager.getInstance("de", "by");
		Set<Calendar> holidays = m.getHolidays(2010);
		Assert.assertEquals("Wrong number of holidays.", 12, holidays.size());
	}

	@Test
	public void testManagerDE_SN() throws Exception{
		Manager m = Manager.getInstance("de", "sn");
		Set<Calendar> holidays = m.getHolidays(2010);
		Assert.assertEquals("Wrong number of holidays.", 11, holidays.size());
	}
	
}
