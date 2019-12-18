
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
 *
 * @author sven
 * @version $Id: $
 */
package de.jollyday.parser;

import de.jollyday.Holiday;
import de.jollyday.config.Holidays;

import java.util.Set;

public interface HolidayParser {
	/**
	 * Parses for the provided year using the {@link Holidays} config and adds
	 * to the set of holidays.
	 *
	 * @param year the year to parse the holiday for
	 * @param holidays set to add the holiday to
	 * @param config the {@link Holidays} config to use for parsing
	 */
	void parse(int year, Set<Holiday> holidays, Holidays config);
}
