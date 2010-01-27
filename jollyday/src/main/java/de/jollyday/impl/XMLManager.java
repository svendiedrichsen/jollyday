package de.jollyday.impl;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Unmarshaller;

import org.joda.time.LocalDate;

import de.jollyday.Manager;
import de.jollyday.config.Configuration;
import de.jollyday.config.Holidays;
import de.jollyday.parser.HolidayParser;
import de.jollyday.parser.impl.FixedMovingParser;
import de.jollyday.parser.impl.FixedParser;
import de.jollyday.parser.impl.FixedWeekdayInMonthParser;
import de.jollyday.parser.impl.RelativeToEasternParser;
import de.jollyday.parser.impl.RelativeToFixedParser;

public class XMLManager extends Manager {

	private static final Logger LOG = Logger.getLogger(XMLManager.class.getName());
	private static final String PACKAGE = "de.jollyday.config";
	private static final String FILE_PREFIX = "Holidays";
	private static final String FILE_SUFFIX = ".xml";

	private static final Collection<HolidayParser> PARSER = new HashSet<HolidayParser>();
	
	private Configuration configuration;
	
	static{
		PARSER.add(new FixedWeekdayInMonthParser());
		PARSER.add(new FixedParser());
		PARSER.add(new FixedMovingParser());
		PARSER.add(new RelativeToEasternParser());
		PARSER.add(new RelativeToFixedParser());
	}
	
	@Override
	public Set<LocalDate> getHolidays(int year, String... args) {
		return getHolidays(year, configuration, args);
	}
	
	private Set<LocalDate> getHolidays(int year, Configuration c, String... args) {
		Set<LocalDate> holidaySet = new HashSet<LocalDate>();
		if(LOG.isLoggable(Level.FINER)){
			LOG.finer("Adding holidays for "+c.getDescription());
		}
		parseHolidays(year, holidaySet, c.getHolidays());
		if(args != null && args.length > 0){
			for(Configuration config : c.getSubConfigurations()){
				if(args[0].equalsIgnoreCase(config.getHierarchy())){
					holidaySet.addAll(getHolidays(year, config, Arrays.copyOfRange(args, 1, args.length)));
					break;
				}
			}
		}
		return holidaySet;
	}

	private void parseHolidays(int year, Set<LocalDate> holidays, Holidays config) {
		for(HolidayParser p : PARSER){
			p.parse(year, holidays, config);
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
		if(LOG.isLoggable(Level.FINER)){
			LOG.finer("Found configuration for "+configuration.getDescription());
			for(Configuration c : configuration.getSubConfigurations()){
				LOG.finer("Sub-configuration "+c.getDescription()+"("+c.getHierarchy()+").");
			}
		}
	}

}
