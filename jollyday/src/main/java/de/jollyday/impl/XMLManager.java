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
package de.jollyday.impl;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Unmarshaller;

import org.joda.time.LocalDate;

import sun.reflect.misc.ReflectUtil;

import de.jollyday.Hierarchy;
import de.jollyday.Manager;
import de.jollyday.config.Configuration;
import de.jollyday.config.Holidays;
import de.jollyday.parser.HolidayParser;
import de.jollyday.parser.impl.FixedMovingOnWeekendParser;
import de.jollyday.parser.impl.FixedParser;
import de.jollyday.parser.impl.FixedWeekdayInMonthParser;
import de.jollyday.parser.impl.IslamicHolidayParser;
import de.jollyday.parser.impl.RelativeToEasternParser;
import de.jollyday.parser.impl.RelativeToFixedParser;
import de.jollyday.parser.impl.RelativeToWeekdayInMonthParser;

/**
 * Manager implementation for reading data from XML files. The files
 * with the name pattern Holidays_[country].xml will be read from
 * the system classpath. It uses a list a parsers for parsing the
 * different type of XML nodes.
 * @author Sven Diedrichsen
 *
 */
public class XMLManager extends Manager {

	private static final Logger LOG = Logger.getLogger(XMLManager.class.getName());
	/**
	 * the package name to search for the generated java classes.
	 */
	private static final String PACKAGE = "de.jollyday.config";
	/**
	 * prefix of the config files.
	 */
	private static final String FILE_PREFIX = "Holidays";
	/**
	 * suffix of the config files.
	 */
	private static final String FILE_SUFFIX = ".xml";
	/**
	 * Configuration parsed on initialization.
	 */
	private Configuration configuration;

	/**
	 * Calls <code>Set&lt;LocalDate&gt; getHolidays(int year, Configuration c, String... args)</code>
	 * with the configuration from initialization.
	 * @see getHolidays(int year, Configuration c, String... args)
	 */
	@Override
	public Set<LocalDate> getHolidays(int year, String... args) {
		return getHolidays(year, configuration, args);
	}

	/**
	 * Parses the provided configuration for the provided year and
	 * fills the list of holidays.
	 * @param year
	 * @param c
	 * @param args
	 * @return
	 */
	private Set<LocalDate> getHolidays(int year, Configuration c,
			String... args) {
		Set<LocalDate> holidaySet = new HashSet<LocalDate>();
		if (LOG.isLoggable(Level.FINER)) {
			LOG.finer("Adding holidays for " + c.getDescription());
		}
		parseHolidays(year, holidaySet, c.getHolidays());
		if (args != null && args.length > 0) {
			String hierarchy = args[0];
			for (Configuration config : c.getSubConfigurations()) {
				if (hierarchy.equalsIgnoreCase(config.getHierarchy())) {
					Set<LocalDate> subHolidays = 
						getHolidays(year, config, Arrays.copyOfRange(args, 1, args.length));
					holidaySet.addAll(subHolidays);
					break;
				}
			}
		}
		return holidaySet;
	}
	
	/**
	 * Iterates of the list of parsers and calls parse on each of them.
	 * @param year
	 * @param holidays
	 * @param config
	 */
	private void parseHolidays(int year, Set<LocalDate> holidays,
			Holidays config) {
		for (HolidayParser p : getParsers(config)) {
			p.parse(year, holidays, config);
		}
	}
	
	private Collection<HolidayParser> getParsers(Holidays config){
		Collection<HolidayParser> parsers = new HashSet<HolidayParser>();
 		for(Method m : config.getClass().getMethods()){
 			if(isGetter(m) && m.getReturnType() == List.class){
 				try {
					List l = (List)m.invoke(config);
					if(l.size() >  0){
						String className = l.get(0).getClass().getName();
						String propName = "parser.impl."+className;
						if(properties.stringPropertyNames().contains(propName)){
							HolidayParser hp = (HolidayParser)Class.forName(properties.getProperty(propName)).newInstance();
							parsers.add(hp);
						}
					}
				} catch (Exception e) {
					throw new IllegalStateException("Cannot create parsers.", e);
				}
 			}
 		}
		return parsers;
	}

	public static boolean isGetter(Method method){
		  if(!method.getName().startsWith("get"))      return false;
		  if(method.getParameterTypes().length != 0)   return false;  
		  if(void.class.equals(method.getReturnType())) return false;
		  return true;
	}


	/**
	 * Initializes the XMLManager by loading the holidays XML file as resource
	 * from the system classpath. When the XML file is found it will be 
	 * unmarshalled with JAXB to some Java classes.
	 */
	@Override
	public void init(String country) throws Exception {
		InputStream stream = ClassLoader.getSystemResourceAsStream(FILE_PREFIX
				+ "_" + country + FILE_SUFFIX);
		try {
			JAXBContext ctx = JAXBContext.newInstance(PACKAGE);
			Unmarshaller um = ctx.createUnmarshaller();
			JAXBElement<Configuration> el = (JAXBElement<Configuration>) um.unmarshal(stream);
			configuration = el.getValue();
		} finally {
			stream.close();
		}
		if (LOG.isLoggable(Level.FINER)) {
			LOG.finer("Found configuration for "
					+ configuration.getDescription());
			for (Configuration c : configuration.getSubConfigurations()) {
				LOG.finer("Sub-configuration " + c.getDescription() + "("
						+ c.getHierarchy() + ").");
			}
		}
	}

	/**
	 * Returns the configurations hierarchy.<br>
	 * i.e. Hierarchy 'us' -> Children 'al','ak','ar', ... ,'wv','wy'.
	 * Every child might itself have children. The ids be used
	 * to call getHolidays()/isHoliday().
	 */
	@Override
	public Hierarchy getHierarchy() {
		return createConfigurationHierarchy(configuration);
	}

	/**
	 * Creates the configuration hierarchy for the provided configuration.
	 * @param c
	 * @return configuration hierarchy
	 */
	private static Hierarchy createConfigurationHierarchy(Configuration c) {
		Hierarchy h = new Hierarchy();
		h.setId(c.getHierarchy());
		h.setDescription(c.getDescription());
		for(Configuration sub : c.getSubConfigurations()){
			h.getChildren().add(createConfigurationHierarchy(sub));
		}
		return h;
	}

}
