package de.jollyday;

import java.util.Locale;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class RegionTest {

	private static final String COUNTRY_AUSTRALIA_KEY = "au";
	private static final String REGION_TASMANIA_KEY = "tas";

	@Before
	public void init() {
		Locale.setDefault(Locale.ENGLISH);
	}

	@Test
	public void testI18nWorksCorrectly() {
		Region region = new Region(COUNTRY_AUSTRALIA_KEY, REGION_TASMANIA_KEY);
		Assert.assertEquals(REGION_TASMANIA_KEY, region.getCode());
		Assert.assertEquals(COUNTRY_AUSTRALIA_KEY, region.getISOCode());
		Assert.assertEquals(COUNTRY_AUSTRALIA_KEY + "." + REGION_TASMANIA_KEY, region.getPropertiesKey());

		Assert.assertEquals("Tasmania", region.getDescription());
		Assert.assertEquals("Tasmanien", region.getDescription(Locale.GERMAN));
	}
}
