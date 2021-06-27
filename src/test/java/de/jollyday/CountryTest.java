package de.jollyday;

import java.util.Locale;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CountryTest {

	private static final String COUNTRY_AUSTRALIA_KEY = "au";

	@Before
	public void setup() {
		Locale.setDefault(Locale.ENGLISH);
	}

	@Test
	public void testI18nWorksCorrectly() {
		Country country = new Country(COUNTRY_AUSTRALIA_KEY);
		Assert.assertEquals(COUNTRY_AUSTRALIA_KEY, country.getISOCode());
		Assert.assertEquals(COUNTRY_AUSTRALIA_KEY, country.getPropertiesKey());

		Assert.assertEquals("Australia", country.getDescription());
		Assert.assertEquals("Australien", country.getDescription(Locale.GERMAN));
	}
}
