package de.jollyday.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.jollyday.Manager;
import de.jollyday.parser.FixedParser;
import de.jollyday.parser.PropertiesParser;
import de.jollyday.parser.RelativeToEasternParser;
import de.jollyday.parser.RelativeToFixedParser;

public class PropertiesManager extends Manager {

	private static final Logger LOG = Logger.getLogger(PropertiesManager.class.getName());
	private static final String FILE_PREFIX = "holidays";
	private static final String FILE_SUFFIX = ".properties";
	private static Map<String, Class> PARSER_CLASSES = new HashMap<String, Class>();

	private Map<String, PropertiesParser> parser = new HashMap<String, PropertiesParser>();

	static{
		PARSER_CLASSES.put("holidays.fixed", FixedParser.class);
		PARSER_CLASSES.put("holidays.relative.eastern", RelativeToEasternParser.class);
		PARSER_CLASSES.put("holidays.relative.to.fixed", RelativeToFixedParser.class);
	}
	
	@Override
	public Set<Calendar> getHolidays(int year) {
		Set<Calendar> holidays = new HashSet<Calendar>();
		for(PropertiesParser p : this.parser.values()){
			holidays.addAll(p.parse(year));
		}
		return holidays;
	}
	
	@Override
	public void init(String country, String... args) throws IOException, InstantiationException, IllegalAccessException {
		loadProperties(country);
		String sub = country;
		for (String arg : args) {
			sub += "_" + arg;
			try {
				loadProperties(sub);
			} catch (Exception ioe) {
				if (LOG.isLoggable(Level.WARNING)) {
					LOG.log(Level.WARNING, "Could not load properties for '"
							+ sub + "'.");
				}
			}
		}
	}

	private void loadProperties(String subName) throws IOException, InstantiationException, IllegalAccessException {
		Properties p = new Properties();
		InputStream stream = ClassLoader.getSystemResourceAsStream(FILE_PREFIX + "_"
				+ subName + FILE_SUFFIX);
		p.load(stream);
		stream.close();
		for(String name : p.stringPropertyNames()){
			PropertiesParser pParser = parser.get(name);
			if(null == pParser){
				pParser = (PropertiesParser)PARSER_CLASSES.get(name).newInstance();
				parser.put(name, pParser);
			}
			pParser.init(p);
		}
	}

}
