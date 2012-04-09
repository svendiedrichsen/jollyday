/**
 * Copyright 2012 Sven Diedrichsen 
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
package de.jollyday.processor.impl;

import static de.jollyday.util.Check.notNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.jollyday.Holiday;
import de.jollyday.config.Configuration;
import de.jollyday.config.HolidayRule;
import de.jollyday.processor.HolidayProcessor;
import de.jollyday.util.GeneralUtils;

/**
 * Processor for {@link Configuration} instances.
 * 
 * @author sven
 * 
 */
public class ConfigurationProcessor implements HolidayProcessor {

	private Configuration configuration;

	private Map<String, HolidayProcessor> subConfigurationProcessors = new HashMap<String, HolidayProcessor>();
	private List<HolidayProcessor> ruleProcessors = new ArrayList<HolidayProcessor>();

	public ConfigurationProcessor(Configuration configuration) {
		notNull(configuration, "configuration");
		this.configuration = configuration;
	}

	public void init() {
		for (HolidayRule rule : configuration.getHoliday()) {
			HolidayRuleProcessor ruleProcessor = new HolidayRuleProcessor(rule);
			ruleProcessor.init();
			ruleProcessors.add(ruleProcessor);
		}
		for (Configuration subConfiguration : configuration.getSubConfigurations()) {
			ConfigurationProcessor subProcessor = new ConfigurationProcessor(subConfiguration);
			subProcessor.init();
			subConfigurationProcessors.put(subConfiguration.getHierarchy(), subProcessor);
		}
	}

	List<HolidayProcessor> getRuleProcessors() {
		return ruleProcessors;
	}

	Map<String, HolidayProcessor> getSubConfigurationProcessors() {
		return subConfigurationProcessors;
	}

	public Set<Holiday> process(int year, String... args) {
		Set<Holiday> holidays = new HashSet<Holiday>();
		for (HolidayProcessor processor : ruleProcessors) {
			holidays.addAll(processor.process(year, args));
		}
		if (args != null && args.length > 0) {
			HolidayProcessor configurationProcessor = subConfigurationProcessors.get(args[0]);
			if (configurationProcessor != null) {
				holidays.addAll(configurationProcessor.process(year, GeneralUtils.copyOfRange(args, 1, args.length)));
			}
		}
		return holidays;
	}

	/**
	 * @return the configuration
	 */
	public Configuration getConfiguration() {
		return configuration;
	}

}
