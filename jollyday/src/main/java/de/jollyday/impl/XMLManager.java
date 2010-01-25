package de.jollyday.impl;

import java.io.InputStream;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Unmarshaller;

import org.joda.time.LocalDate;

import de.jollyday.Manager;
import de.jollyday.config.Configuration;
import de.jollyday.config.Fixed;
import de.jollyday.config.Holidays;
import de.jollyday.config.RelativeToEastern;
import de.jollyday.config.RelativeToFixed;
import de.jollyday.config.When;
import de.jollyday.util.CalendarUtil;
import de.jollyday.util.XMLUtil;

public class XMLManager extends Manager {

	private static final String PACKAGE = "de.jollyday.config";
	private static final String FILE_PREFIX = "Holidays";
	private static final String FILE_SUFFIX = ".xml";

	private Configuration configuration;
	
	@Override
	public Set<LocalDate> getHolidays(int year, String... args) {
		Set<LocalDate> holidays = new HashSet<LocalDate>();
		parseHolidays(year, holidays, configuration.getHolidays());
		for(String arg : args){
			for(Configuration config : configuration.getSubConfigurations()){
				if(arg.equalsIgnoreCase(config.getHierarchy())){
					parseHolidays(year, holidays, config.getHolidays());
					break;
				}
			}
		}
		return holidays;
	}

	private void parseHolidays(int year, Set<LocalDate> holidays, Holidays config) {
		parseFixed(year, holidays, config.getFixed());
		parseRelativeToEastern(year, holidays, config.getRelativeToEastern());
		parseRelativeToFixed(year, holidays, config.getRelativeToFixed());
	}

	private void parseRelativeToFixed(int year, Set<LocalDate> holidays,
			List<RelativeToFixed> relativeToFixed) {
		for(RelativeToFixed rf : relativeToFixed){
			LocalDate fixed = CalendarUtil.create(year, rf.getDate());
			if(rf.getWeekday() != null){
				int day = XMLUtil.getWeekday(rf.getWeekday());
				int direction = (rf.getWhen() == When.BEFORE ? -1 : 1);
				do{
					fixed = fixed.plusDays(direction);
				}while(fixed.getDayOfWeek() != day);
			}else if(rf.getDays() != null){
				fixed = fixed.plusDays( rf.getWhen() == When.BEFORE ? -rf.getDays() : rf.getDays());
			}
			holidays.add(fixed);
		}
	}

	private void parseRelativeToEastern(int year, Set<LocalDate> holidays,
			List<RelativeToEastern> relative) {
		for(RelativeToEastern re : relative){
			LocalDate easterSunday = CalendarUtil.getEasterSunday(year);
			holidays.add(easterSunday.plusDays(re.getDays()));
		}
	}

	private void parseFixed(int year, Set<LocalDate> holidays, List<Fixed> fixed) {
		for(Fixed f : fixed){
			holidays.add(CalendarUtil.create(year, f));
		}
	}

	@Override
	public void init(String country) throws Exception {
		InputStream stream = ClassLoader.getSystemResourceAsStream(FILE_PREFIX + "_"
				+ country + FILE_SUFFIX);
		JAXBContext ctx = JAXBContext.newInstance(PACKAGE);
		Unmarshaller um = ctx.createUnmarshaller();
		JAXBElement<Configuration> el = (JAXBElement<Configuration>) um.unmarshal(stream);
		configuration = el.getValue();
		stream.close();
	}

}
