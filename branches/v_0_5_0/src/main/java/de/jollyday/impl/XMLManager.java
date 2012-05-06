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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.joda.time.ReadableInterval;

import de.jollyday.CalendarHierarchy;
import de.jollyday.Holiday;
import de.jollyday.HolidayManager;
import de.jollyday.config.Configuration;
import de.jollyday.persistence.IPersistenceManager;
import de.jollyday.persistence.PersistenceManagerFactory;
import de.jollyday.processor.impl.ConfigurationProcessor;

/**
 * Manager implementation for reading data from XML files. The files with the
 * name pattern Holidays_[country].xml will be read from the system classpath.
 * It uses a list a parsers for parsing the different type of XML nodes.
 * 
 * @author Sven Diedrichsen
 * @version $Id: $
 */
public class XMLManager extends HolidayManager {

	/**
	 * Logger.
	 */
	private static final Logger LOG = Logger.getLogger(XMLManager.class.getName());
	/**
	 * the processor for calculating holidays
	 */
	protected ConfigurationProcessor processor;

	/**
	 * the persistence manager to use for configuration retrieval
	 */
	private IPersistenceManager persistenceManager = PersistenceManagerFactory.createPersistenceManager();

	/**
	 * {@inheritDoc}
	 * 
	 * Calls
	 * <code>Set&lt;LocalDate&gt; getHolidays(int year, Configuration c, String... args)</code>
	 * with the configuration from initialization.
	 * 
	 * @see getHolidays(int year, Configuration c, String... args)
	 */
	@Override
	public Set<Holiday> getHolidays(int year, final String... args) {
		return processor.process(year, args);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * Calls <code>getHolidays(year, args)</code> for each year within the
	 * interval and returns a list of holidays which are then contained in the
	 * interval.
	 */
	@Override
	public Set<Holiday> getHolidays(ReadableInterval interval, final String... args) {
		if (interval == null) {
			throw new IllegalArgumentException("Interval is NULL.");
		}
		Set<Holiday> holidays = new HashSet<Holiday>();
		for (int year = interval.getStart().getYear(); year <= interval.getEnd().getYear(); year++) {
			Set<Holiday> yearHolidays = getHolidays(year, args);
			for (Holiday h : yearHolidays) {
				if (interval.contains(h.getDate().toDateMidnight())) {
					holidays.add(h);
				}
			}
		}
		return holidays;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * Initializes the XMLManager by loading the holidays XML file as resource
	 * from the classpath. When the XML file is found it will be unmarshalled
	 * with JAXB to some Java classes.
	 */
	@Override
	public void init(final String calendarName) {
		Configuration configuration = persistenceManager.getConfiguration(calendarName);
		validateConfigurationHierarchy(configuration);
		logHierarchy(configuration, 0);
		processor = new ConfigurationProcessor(configuration);
		processor.init();
	}

	/**
	 * Logs the hierarchy structure.
	 * 
	 * @param c
	 *            Configuration to log hierarchy for.
	 * @param level
	 *            a int.
	 */
	protected static void logHierarchy(final Configuration c, int level) {
		if (LOG.isLoggable(Level.FINER)) {
			StringBuilder space = new StringBuilder();
			for (int i = 0; i < level; i++) {
				space.append("-");
			}
			LOG.finer(space + " " + c.getDescription() + "(" + c.getHierarchy() + ").");
			for (Configuration sub : c.getSubConfigurations()) {
				logHierarchy(sub, level + 1);
			}
		}
	}

	/**
	 * Validates the content of the provided configuration by checking for
	 * multiple hierarchy entries within one configuration. It traverses down
	 * the configuration tree.
	 * 
	 * @param c
	 *            a {@link de.jollyday.config.Configuration} object.
	 */
	protected static void validateConfigurationHierarchy(final Configuration c) {
		Map<String, Integer> hierarchyMap = new HashMap<String, Integer>();
		Set<String> multipleHierarchies = new HashSet<String>();
		for (Configuration subConfig : c.getSubConfigurations()) {
			String hierarchy = subConfig.getHierarchy();
			if (!hierarchyMap.containsKey(hierarchy)) {
				hierarchyMap.put(hierarchy, Integer.valueOf(1));
			} else {
				int count = hierarchyMap.get(hierarchy).intValue();
				hierarchyMap.put(hierarchy, Integer.valueOf(++count));
				multipleHierarchies.add(hierarchy);
			}
		}
		if (multipleHierarchies.size() > 0) {
			StringBuilder msg = new StringBuilder();
			msg.append("Configuration for " + c.getHierarchy()
					+ " contains  multiple SubConfigurations with the same hierarchy id. ");
			for (String hierarchy : multipleHierarchies) {
				msg.append(hierarchy + " " + hierarchyMap.get(hierarchy).toString() + " times ");
			}
			throw new IllegalArgumentException(msg.toString().trim());
		}
		for (Configuration subConfig : c.getSubConfigurations()) {
			validateConfigurationHierarchy(subConfig);
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * Returns the configurations hierarchy.<br>
	 * i.e. Hierarchy 'us' -> Children 'al','ak','ar', ... ,'wv','wy'. Every
	 * child might itself have children. The ids be used to call
	 * getHolidays()/isHoliday().
	 */
	@Override
	public CalendarHierarchy getCalendarHierarchy() {
		return createConfigurationHierarchy(processor.getConfiguration(), null);
	}

	/**
	 * Creates the configuration hierarchy for the provided configuration.
	 * 
	 * @param c
	 * @return configuration hierarchy
	 */
	private static CalendarHierarchy createConfigurationHierarchy(final Configuration c, CalendarHierarchy h) {
		h = new CalendarHierarchy(h, c.getHierarchy());
		for (Configuration sub : c.getSubConfigurations()) {
			CalendarHierarchy subHierarchy = createConfigurationHierarchy(sub, h);
			h.getChildren().put(subHierarchy.getId(), subHierarchy);
		}
		return h;
	}

}
