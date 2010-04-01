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

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.jollyday.CountryHierarchy;
import de.jollyday.Holiday;
import de.jollyday.HolidayManager;
import de.jollyday.config.Configuration;
import de.jollyday.config.Holidays;
import de.jollyday.parser.HolidayParser;
import de.jollyday.util.XMLUtil;

/**
 * Manager implementation for reading data from XML files. The files
 * with the name pattern Holidays_[country].xml will be read from
 * the system classpath. It uses a list a parsers for parsing the
 * different type of XML nodes.
 * @author Sven Diedrichsen
 *
 */
public class XMLManager extends HolidayManager {

	/**
	 * Logger
	 */
	private static final Logger LOG = Logger.getLogger(XMLManager.class.getName());
	/**
	 * The configuration prefix for parser implementations. 
	 */
	private static final String PARSER_IMPL_PREFIX = "parser.impl.";
	/**
	 * prefix of the config files.
	 */
	private static final String FILE_PREFIX = "Holidays";
	/**
	 * suffix of the config files.
	 */
	private static final String FILE_SUFFIX = ".xml";
	/**
	 * Parser cache by XML class name.
	 */
	private final Map<String, HolidayParser> parserCache = new HashMap<String, HolidayParser>();
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
	public Set<Holiday> getHolidays(int year, String... args) {
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
	private Set<Holiday> getHolidays(int year, Configuration c,
			String... args) {
		Set<Holiday> holidaySet = new HashSet<Holiday>();
		if (LOG.isLoggable(Level.FINER)) {
			LOG.finer("Adding holidays for " + c.getDescription());
		}
		parseHolidays(year, holidaySet, c.getHolidays());
		if (args != null && args.length > 0) {
			String hierarchy = args[0];
			for (Configuration config : c.getSubConfigurations()) {
				if (hierarchy.equalsIgnoreCase(config.getHierarchy())) {
					Set<Holiday> subHolidays = 
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
	private void parseHolidays(int year, Set<Holiday> holidays,
			Holidays config) {
		for (HolidayParser p : getParsers(config)) {
			p.parse(year, holidays, config);
		}
	}
	
	/**
	 * Creates a list of parsers by reading the configuration and trying to
	 * find an <code>HolidayParser</code> implementation for by XML class type.
	 * @param config
	 * @return A list of parsers to for this configuration.
	 */
	private Collection<HolidayParser> getParsers(Holidays config){
		Collection<HolidayParser> parsers = new HashSet<HolidayParser>();
 		for(Method m : config.getClass().getMethods()){
 			if(isGetter(m) && m.getReturnType() == List.class){
 				try {
					List l = (List)m.invoke(config);
					if(l.size() >  0){
						String className = l.get(0).getClass().getName();
						if(!parserCache.containsKey(className)){
							String propName = PARSER_IMPL_PREFIX+className;
							Properties configProps = getProperties();
							if(configProps.stringPropertyNames().contains(propName)){
								HolidayParser hp = (HolidayParser)Class.forName(configProps.getProperty(propName)).newInstance();
								parserCache.put(className, hp);
							}
						}
						if(parserCache.containsKey(className)){
							parsers.add(parserCache.get(className));
						}
					}
				} catch (Exception e) {
					throw new IllegalStateException("Cannot create parsers.", e);
				}
 			}
 		}
		return parsers;
	}

	/**
	 * Returns true if the provided <code>Method</code> is a getter
	 * method.
	 * @param method The method to check if it is a getter.
	 * @return is a getter method
	 */
	private static boolean isGetter(Method method){
		  return method.getName().startsWith("get") 
		  	&& method.getParameterTypes().length == 0 
		  	&& !void.class.equals(method.getReturnType());
	}


	/**
	 * Initializes the XMLManager by loading the holidays XML file as resource
	 * from the system classpath. When the XML file is found it will be 
	 * unmarshalled with JAXB to some Java classes.
	 */
	@Override
	public void init(String country) throws Exception {
		String fileName = FILE_PREFIX+ "_" + country + FILE_SUFFIX;
		configuration = XMLUtil.unmarshallConfiguration(ClassLoader.getSystemResourceAsStream(fileName));
		validateConfigurationHierarchy(configuration);
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
	 * Validates the content of the provided configuration by checking
	 * for multiple hierarchy entries within one configuration. It traverses
	 * down the configuration tree.
	 */
	private static void validateConfigurationHierarchy(Configuration c) {
		Map<String, Integer> hierarchyMap = new HashMap<String, Integer>();
		Set<String> multipleHierarchies = new HashSet<String>();
		for(Configuration subConfig : c.getSubConfigurations()){
			String hierarchy = subConfig.getHierarchy();
			if(!hierarchyMap.containsKey(hierarchy)){
				hierarchyMap.put(hierarchy, Integer.valueOf(1));
			}else{
				int count = hierarchyMap.get(hierarchy).intValue();
				hierarchyMap.put(hierarchy, Integer.valueOf(++count));
				multipleHierarchies.add(hierarchy);
			}
		}
		if(multipleHierarchies.size() > 0){
			StringBuilder msg = new StringBuilder();
			msg.append("Configuration for "+c.getHierarchy()+" contains  multiple SubConfigurations with the same hierarchy id. ");
			for(String hierarchy : multipleHierarchies){
				msg.append(hierarchy+" "+hierarchyMap.get(hierarchy).toString()+" times ");
			}
			throw new IllegalArgumentException(msg.toString().trim());
		}
		for(Configuration subConfig : c.getSubConfigurations()){
			validateConfigurationHierarchy(subConfig);
		}
	}

	/**
	 * Returns the configurations hierarchy.<br>
	 * i.e. Hierarchy 'us' -> Children 'al','ak','ar', ... ,'wv','wy'.
	 * Every child might itself have children. The ids be used
	 * to call getHolidays()/isHoliday().
	 */
	@Override
	public CountryHierarchy getHierarchy() {
		return createConfigurationHierarchy(configuration, null);
	}
	

	/**
	 * Creates the configuration hierarchy for the provided configuration.
	 * @param c
	 * @return configuration hierarchy
	 */
	private static CountryHierarchy createConfigurationHierarchy(Configuration c, CountryHierarchy h) {
		h =  new CountryHierarchy(h, c.getHierarchy());
		for(Configuration sub : c.getSubConfigurations()){
			CountryHierarchy subHierarchy = createConfigurationHierarchy(sub, h);
			h.getChildren().put(subHierarchy.getId(), subHierarchy);
		}
		return h;
	}

}
