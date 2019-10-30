/**
 * Copyright 2019 Sven Diedrichsen
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
package de.jollyday.configuration.impl;

import static de.jollyday.util.ResourceUtil.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import de.jollyday.City;
import de.jollyday.Country;
import de.jollyday.Region;
import de.jollyday.configuration.CountryDescriptionProvider;
import de.jollyday.util.ResourceUtil;

/**
 * An implementation of the Jollyday country description provider.
 *
 * @author Christoph Weitkamp
 * @version $Id: $
 */
public class CountryDescriptionProviderImpl implements CountryDescriptionProvider {
	/**
	 * Internal map of ISO code / country
	 */
	final Map<String, Country> countries = new HashMap<>();
	/**
	 * Internal map of ISO code / list of regions
	 */
	final Map<String, List<Region>> regions = new HashMap<>();
	/**
	 * Internal map of region code / list of cities
	 */
	final Map<String, List<City>> cities = new HashMap<>();

	private final ResourceUtil resourceUtil = new ResourceUtil();

	public CountryDescriptionProviderImpl() {
		parseCountryDescriptions();
	}

	@Override
	public Collection<Country> getCountries() {
		return Collections.unmodifiableCollection(countries.values());
	}

	@Override
	public Collection<Region> getRegions(String isoCode) {
		if (regions.containsKey(isoCode)) {
			return Collections.unmodifiableCollection(regions.get(isoCode));
		} else {
			throw new IllegalArgumentException(String.format("Country with ISO code '%s' does not exist.", isoCode));
		}
	}

	@Override
	public Collection<City> getCities(String regionCode) {
		if (cities.containsKey(regionCode)) {
			return Collections.unmodifiableCollection(cities.get(regionCode));
		} else {
			throw new IllegalArgumentException(String.format("Region with code '%s' does not exist.", regionCode));
		}
	}

	private void parseCountryDescriptions() {
		// temporary map of region code / region
		final Map<String, Region> tempRegions = new HashMap<>();
		final ResourceBundle countryDescriptions = resourceUtil.getCountryDescriptions(Locale.getDefault());
		for (String property : Collections.list(countryDescriptions.getKeys())) {
			final String[] split = property.split("\\.");
			if (split.length > COUNTRY_INDEX) {
				final String countryCode = split[COUNTRY_INDEX].toLowerCase();
				switch (split.length) {
				case COUNTRY_INDEX + 1:
					countries.put(countryCode, new Country(countryCode));
					break;
				case REGION_INDEX + 1:
					final Region region = new Region(countryCode, split[REGION_INDEX].toLowerCase());
					if (regions.containsKey(region.getISOCode())) {
						regions.get(region.getISOCode()).add(region);
					} else {
						List<Region> localRegions = new ArrayList<>(0);
						localRegions.add(region);
						regions.put(region.getISOCode(), localRegions);
					}
					if (countries.containsKey(region.getISOCode())) {
						countries.get(region.getISOCode()).addRegion(region);
					}
					tempRegions.put(region.getCode(), region);
					break;
				case CITY_INDEX + 1:
					final City city = new City(countryCode, split[REGION_INDEX].toLowerCase(),
							split[CITY_INDEX].toLowerCase());
					if (cities.containsKey(city.getRegionCode())) {
						cities.get(city.getRegionCode()).add(city);
					} else {
						List<City> localCities = new ArrayList<>(0);
						localCities.add(city);
						cities.put(city.getRegionCode(), localCities);
					}
					if (tempRegions.containsKey(city.getRegionCode())) {
						tempRegions.get(city.getRegionCode()).addCity(city);
					}
					break;
				default:
					break;
				}
			}
		}
	}
}
