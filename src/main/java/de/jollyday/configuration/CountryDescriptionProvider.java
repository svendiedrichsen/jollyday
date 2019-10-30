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
package de.jollyday.configuration;

import java.util.Collection;

import de.jollyday.City;
import de.jollyday.Country;
import de.jollyday.Region;

/**
 * The interface for Jollyday country description provider.
 *
 * @author Christoph Weitkamp
 * @version $Id: $
 */
public interface CountryDescriptionProvider {

	/**
	 * Returns a list of all {@link Country Countries}.
	 *
	 * @return an unmodifiable list of all {@link Country Countries}
	 */
	Collection<Country> getCountries();

	/**
	 * Returns a list of all {@link Region}s inside a given {@link Country}.
	 * Could be empty.
	 *
	 * @param isoCode
	 *            {@link java.lang.String} sthe country code to retrieve the
	 *            regions from.
	 * @return an unmodifiable list of all {@link Region}s inside the given
	 *         {@link Country}
	 * @throws IllegalArgumentException
	 */
	Collection<Region> getRegions(String isoCode);

	/**
	 * Returns a list of all {@link City Cities} from a given {@link Region}.
	 * Could be empty.
	 *
	 * @param regionCode
	 *            {@link java.lang.String} the region code to retrieve the
	 *            cities from.
	 * @return an unmodifiable list of all {@link City Cities} inside the given
	 *         {@link Region}
	 * @throws IllegalArgumentException
	 */
	Collection<City> getCities(String regionCode);
}
