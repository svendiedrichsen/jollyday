package de.jollyday.configuration.internal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import org.junit.Test;

import de.jollyday.configuration.impl.DefaultConfigurationProvider;

public class DefaultConfigurationProviderTest {

	private static final Set<String> KEYS_DEFAULT_CONFIG = new HashSet<String>(Arrays.asList("manager.impl",
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
		assertFalse("Properties shouldn't be empty.", p.isEmpty());
		assertEquals("Default properties are not as expected.", KEYS_DEFAULT_CONFIG, p.keySet());
	}

}
