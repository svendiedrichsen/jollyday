package de.jollyday.configuration.impl;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class DefaultConfigurationProviderContentTest {

	private static final Set<String> KEYS_DEFAULT_CONFIG = new HashSet<>(Arrays.asList("manager.impl",
			"manager.impl.jp","configuration.datasource.impl","parser.impl.de.jollyday.config.Fixed",
			"parser.impl.de.jollyday.config.FixedWeekdayInMonth", "parser.impl.de.jollyday.config.IslamicHoliday",
			"parser.impl.de.jollyday.config.ChristianHoliday", "parser.impl.de.jollyday.config.RelativeToFixed",
			"parser.impl.de.jollyday.config.RelativeToWeekdayInMonth",
			"parser.impl.de.jollyday.config.FixedWeekdayBetweenFixed",
			"parser.impl.de.jollyday.config.FixedWeekdayRelativeToFixed",
			"parser.impl.de.jollyday.config.EthiopianOrthodoxHoliday",
			"parser.impl.de.jollyday.config.RelativeToEasterSunday"));

	DefaultConfigurationProvider configurationProvider = new DefaultConfigurationProvider();

	@Test
	public void testPutConfiguration() {
		Properties p = configurationProvider.getProperties();
		assertFalse(p.isEmpty(), "Properties shouldn't be empty.");
		assertEquals(KEYS_DEFAULT_CONFIG, p.keySet(), "Default properties are not as expected.");
	}

}
