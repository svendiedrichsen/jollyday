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

import de.jollyday.CalendarHierarchy;
import de.jollyday.Holiday;
import de.jollyday.HolidayManager;
import de.jollyday.config.Configuration;
import de.jollyday.config.Holidays;
import de.jollyday.parser.HolidayParser;
import de.jollyday.util.ClassLoadingUtil;

import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.time.LocalDate;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Manager implementation for reading data from the configuration datasource.
 * It uses a list a parsers for parsing the different type of XML nodes.
 *
 * @author Sven Diedrichsen
 */
public class DefaultHolidayManager extends HolidayManager {

	private static final Logger LOG = Logger.getLogger(DefaultHolidayManager.class.getName());
	/**
	 * The configuration prefix for parser implementations.
	 */
	private static final String PARSER_IMPL_PREFIX = "parser.impl.";
	/**
	 * Parser cache by XML class name.
	 */
	private final Map<String, HolidayParser> parserCache = new HashMap<>();
	/**
	 * Configuration parsed on initialization.
	 */
	protected Configuration configuration;
	/**
	 * Utility class to handle class loading
	 */
	private final ClassLoadingUtil classLoadingUtil = new ClassLoadingUtil();

	/**
	 * {@inheritDoc}
	 *
	 * Calls
	 * <code>Set&lt;LocalDate&gt; getHolidays(int year, Configuration c, String... args)</code>
	 * with the configuration from initialization.
	 */
	@Override
	public Set<Holiday> getHolidays(int year, final String... args) {
		Set<Holiday> holidaySet = Collections.synchronizedSet(new HashSet<>());
		getHolidays(year, configuration, holidaySet, args);
		return holidaySet;
	}

	/**
	 * {@inheritDoc}
	 *
	 * Calls <code>getHolidays(year, args)</code> for each year within the
	 * interval and returns a list of holidays which are then contained in the
	 * interval.
	 */
	@Override
	public Set<Holiday> getHolidays(LocalDate startDateInclusive, LocalDate endDateInclusive, final String... args) {
		Objects.requireNonNull(startDateInclusive, "startDateInclusive is null");
		Objects.requireNonNull(endDateInclusive, "endInclusive is null");
		Set<Holiday> holidays = new HashSet<>();
		for (int year = startDateInclusive.getYear(); year <= endDateInclusive.getYear(); year++) {
			Set<Holiday> yearHolidays = getHolidays(year, args);
			for (Holiday h : yearHolidays) {
				if (!startDateInclusive.isAfter(h.getDate()) && !endDateInclusive.isBefore(h.getDate())) {
					holidays.add(h);
				}
			}
		}
		return holidays;
	}

	/**
	 * Parses the provided configuration for the provided year and fills the
	 * list of holidays.
	 *
	 * @param year the year to get the holidays for
	 * @param c the holiday configuration
	 * @param holidaySet the set of holidays
	 * @param args the arguments to descend down the configuration tree
	 */
	private void getHolidays(int year, final Configuration c, Set<Holiday> holidaySet, final String... args) {
		if (LOG.isLoggable(Level.FINER)) {
			LOG.finer("Adding holidays for " + c.getDescription());
		}
		parseHolidays(year, holidaySet, c.getHolidays());
		if (args != null && args.length > 0) {
			String hierarchy = args[0];
			for (Configuration config : c.getSubConfigurations()) {
				if (hierarchy.equalsIgnoreCase(config.getHierarchy())) {
					getHolidays(year, config, holidaySet, copyOfRange(args, 1, args.length));
					break;
				}
			}
		}
	}

	/**
	 * Copies the specified range from the original array to a new array. This
	 * is a replacement for Java 1.6 Arrays.copyOfRange() specialized in String.
	 *
	 * @param original
	 *            the original array to copy range from
	 * @param from
	 *            the start of the range to copy from the original array
	 * @param to
	 *            the inclusive end of the range to copy from the original array
	 * @return the copied range
	 */
	private String[] copyOfRange(final String[] original, int from, int to) {
		int newLength = to - from;
		if (newLength < 0) {
			throw new IllegalArgumentException(from + " > " + to);
		}
		String[] copy = new String[newLength];
		System.arraycopy(original, from, copy, 0, Math.min(original.length - from, newLength));
		return copy;
	}

	/**
	 * Iterates of the list of parsers and calls parse on each of them.
	 *
	 * @param year the year to parse the holidays for
	 * @param holidays the set to put the holidays into
	 * @param config the holiday configuration
	 */
	private void parseHolidays(int year, Set<Holiday> holidays, final Holidays config) {
		Collection<HolidayParser> parsers = getParsers(config);
		for (HolidayParser p : parsers) {
			p.parse(year, holidays, config);
		}
	}

	/**
	 * Creates a list of parsers by reading the configuration and trying to find
	 * an <code>HolidayParser</code> implementation for by XML class type.
	 *
	 * @param config the holiday configuration
	 * @return A list of parsers to for this configuration.
	 */
	private Collection<HolidayParser> getParsers(final Holidays config) {
		Collection<HolidayParser> parsers = new HashSet<>();
		try {
			PropertyDescriptor[] propertiesDescs = Introspector.getBeanInfo(config.getClass()).getPropertyDescriptors();
			for (PropertyDescriptor propertyDescriptor : propertiesDescs) {
				if (List.class.isAssignableFrom(propertyDescriptor.getPropertyType())) {
					List<?> l = (List<?>) propertyDescriptor.getReadMethod().invoke(config);
					if (!l.isEmpty()) {
						String className = l.get(0).getClass().getName();
						if (!parserCache.containsKey(className)) {
							String propName = PARSER_IMPL_PREFIX + className;
							String parserClassName = getManagerParameter().getProperty(propName);
							if (parserClassName != null) {
								Class<?> parserClass = classLoadingUtil.loadClass(parserClassName);
								Object parserObject = parserClass.newInstance();
								HolidayParser hp = HolidayParser.class.cast(parserObject);
								parserCache.put(className, hp);
							}
						}
						if (parserCache.containsKey(className)) {
							parsers.add(parserCache.get(className));
						}
					}
				}
			}
		} catch (Exception e) {
			throw new IllegalStateException("Cannot create parsers.", e);
		}
		return parsers;
	}

	/**
	 * {@inheritDoc}
	 *
	 * Initializes the DefaultHolidayManager by loading the holidays XML file as resource
	 * from the classpath. When the XML file is found it will be unmarshalled
	 * with JAXB to some Java classes.
	 */
	@Override
	public void doInit() {
		configuration = getConfigurationDataSource().getConfiguration(getManagerParameter());
		validateConfigurationHierarchy(configuration);
		logHierarchy(configuration, 0);
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
	 * @param c a {@link de.jollyday.config.Configuration} object.
	 */
	protected static void validateConfigurationHierarchy(final Configuration c) {
		Map<String, Integer> hierarchyMap = new HashMap<>();
		Set<String> multipleHierarchies = new HashSet<>();
		for (Configuration subConfig : c.getSubConfigurations()) {
			String hierarchy = subConfig.getHierarchy();
			if (!hierarchyMap.containsKey(hierarchy)) {
				hierarchyMap.put(hierarchy, 1);
			} else {
				int count = hierarchyMap.get(hierarchy);
				hierarchyMap.put(hierarchy, ++count);
				multipleHierarchies.add(hierarchy);
			}
		}
		if (multipleHierarchies.size() > 0) {
			StringBuilder msg = new StringBuilder();
			msg.append("Configuration for ").append(c.getHierarchy())
					.append(" contains  multiple SubConfigurations with the same hierarchy id. ");
			for (String hierarchy : multipleHierarchies) {
				msg.append(hierarchy).append(" ").append(hierarchyMap.get(hierarchy)
						.toString()).append(" times ");
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
	 * i.e. Hierarchy 'us' -&gt; Children 'al','ak','ar', ... ,'wv','wy'. Every
	 * child might itself have children. The ids be used to call
	 * getHolidays()/isHoliday().
	 */
	@Override
	public CalendarHierarchy getCalendarHierarchy() {
		return createConfigurationHierarchy(configuration, null);
	}

	/**
	 * Creates the configuration hierarchy for the provided configuration.
	 *
	 * @param c the full configuration
     * @param h the calendars hierarchy
	 * @return configuration hierarchy
	 */
	private static CalendarHierarchy createConfigurationHierarchy(final Configuration c, CalendarHierarchy h) {
		h = new CalendarHierarchy(h, c.getHierarchy());
		h.setFallbackDescription(c.getDescription());
		for (Configuration sub : c.getSubConfigurations()) {
			CalendarHierarchy subHierarchy = createConfigurationHierarchy(sub, h);
			h.getChildren().put(subHierarchy.getId(), subHierarchy);
		}
		return h;
	}

}
