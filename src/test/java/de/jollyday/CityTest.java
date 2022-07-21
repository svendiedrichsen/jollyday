package de.jollyday;

import java.util.Locale;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CityTest {

	private static final String COUNTRY_AUSTRALIA_KEY = "au";
	private static final String REGION_TASMANIA_KEY = "tas";
	private static final String CITY_HOBARD_AREA_KEY = "ho";

	@Before
	public void setup() {
		Locale.setDefault(Locale.ENGLISH);
	}

	@Test
	public void testI18nWorksCorrectly() {
		City region = new City(COUNTRY_AUSTRALIA_KEY, REGION_TASMANIA_KEY, CITY_HOBARD_AREA_KEY);
		Assert.assertEquals(CITY_HOBARD_AREA_KEY, region.getCode());
		Assert.assertEquals(REGION_TASMANIA_KEY, region.getRegionCode());
		Assert.assertEquals(COUNTRY_AUSTRALIA_KEY, region.getISOCode());
		Assert.assertEquals(COUNTRY_AUSTRALIA_KEY + "." + REGION_TASMANIA_KEY + "." + CITY_HOBARD_AREA_KEY,
				region.getPropertiesKey());

		Assert.assertEquals("Hobard Area", region.getDescription());
		Assert.assertEquals("Hobart", region.getDescription(Locale.GERMAN));
	}
}
