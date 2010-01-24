package de.jollyday.tests;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import de.jollyday.Manager;
import de.jollyday.util.CalendarUtil;

public class ManagerTest {

	private static final int YEAR = 2010;
	private static Set<Calendar> de = new HashSet<Calendar>();
	
	static{
		de.add(CalendarUtil.create(YEAR, Calendar.JANUARY, 1));
		de.add(CalendarUtil.create(YEAR, Calendar.MAY, 1));
		de.add(CalendarUtil.create(YEAR, Calendar.OCTOBER, 3));
		de.add(CalendarUtil.create(YEAR, Calendar.DECEMBER, 25));
		de.add(CalendarUtil.create(YEAR, Calendar.DECEMBER, 26));
		de.add(CalendarUtil.create(YEAR, Calendar.APRIL, 2));
		de.add(CalendarUtil.create(YEAR, Calendar.APRIL, 5));
		Calendar c = CalendarUtil.getEasterSunday(YEAR);
		c.add(Calendar.DAY_OF_YEAR, 39);
		de.add(c);
		c = CalendarUtil.getEasterSunday(YEAR);
		c.add(Calendar.DAY_OF_YEAR, 50);
		de.add(c);
	}
	
	@Test
	public void testManagerDE() throws Exception{
		Manager m = Manager.getInstance("de");
		Set<Calendar> holidays = m.getHolidays(2010);
		Assert.assertEquals("Wrong number of holidays.", 9, holidays.size());
	}
	
	@Test
	public void testManagerDE_BY() throws Exception{
		Manager m = Manager.getInstance("de");
		Set<Calendar> holidays = m.getHolidays(2010, "by");
		Assert.assertEquals("Wrong number of holidays.", 12, holidays.size());
	}

	@Test
	public void testManagerDE_SN() throws Exception{
		Manager m = Manager.getInstance("de");
		Set<Calendar> holidays = m.getHolidays(2010, "sn");
		Assert.assertEquals("Wrong number of holidays.", 11, holidays.size());
	}
	
}
