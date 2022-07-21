package de.jollyday.configuration.impl;

import java.util.Collection;

import org.junit.Assert;
import org.junit.Test;

import de.jollyday.City;
import de.jollyday.Country;
import de.jollyday.Region;
import de.jollyday.util.ResourceUtil;

public class CountryDescriptionProviderImplTest {

	private static final String COUNTRY_AUSTRALIA_KEY = "au";
	private static final String REGION_TASMANIA_KEY = "tas";
	private static final String CITY_HOBARD_AREA_KEY = "ho";

	private CountryDescriptionProviderImpl provider = new CountryDescriptionProviderImpl();
	private ResourceUtil resourceUtil = new ResourceUtil();

	@Test
	public void testParsingOfCountriesWorksCorrecly() {
		Assert.assertNotEquals(0, provider.countries);
		Assert.assertEquals(resourceUtil.getISOCodes().size(), provider.countries.size());

		Collection<Country> countries = provider.getCountries();
		Assert.assertEquals(provider.countries.size(), countries.size());

		Assert.assertTrue(
				countries.stream().filter(c -> c.getISOCode().equals(COUNTRY_AUSTRALIA_KEY)).findFirst().isPresent());
	}

	public void testParsingOfRegionsWorksCorrectly() {
		Assert.assertNotEquals(0, provider.regions);

		Collection<Region> regions = provider.getRegions(COUNTRY_AUSTRALIA_KEY);
		Assert.assertEquals(8, regions.size());

		Assert.assertTrue(regions.contains(new Region(COUNTRY_AUSTRALIA_KEY, REGION_TASMANIA_KEY)));
	}

	public void testParsingOfCitiesWorksCorrectly() {
		Assert.assertNotEquals(0, provider.cities);

		Collection<City> cities = provider.getCities(REGION_TASMANIA_KEY);
		Assert.assertEquals(2, cities.size());

		Assert.assertTrue(cities.contains(new City(COUNTRY_AUSTRALIA_KEY, REGION_TASMANIA_KEY, CITY_HOBARD_AREA_KEY)));
	}
}
