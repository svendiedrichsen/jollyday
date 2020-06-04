/**
 * Copyright 2011 Sven Diedrichsen
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
package de.jollyday.parser.impl;

import de.jollyday.Holiday;
import de.jollyday.parser.functions.CalculateEasterSunday;
import de.jollyday.parser.functions.CreateHoliday;
import de.jollyday.parser.predicates.ValidLimitation;
import de.jollyday.spi.RelativeToEasterSunday;

import java.util.Collection;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * This parser creates holidays relative to easter sunday.
 *
 * @author Sven Diedrichsen
 * @version $Id: $
 */
public class RelativeToEasterSundayParser implements Function<Integer, Stream<Holiday>> {

	private Stream<RelativeToEasterSunday> relativeToEasterSundays;

	public RelativeToEasterSundayParser(Stream<RelativeToEasterSunday> relativeToEasterSundays) {
		this.relativeToEasterSundays = relativeToEasterSundays;
	}

	@Override
	public Stream<Holiday> apply(Integer year) {
		return relativeToEasterSundays
				.filter(new ValidLimitation(year))
				.map(res -> new DescribedDateHolder(res, new CalculateEasterSunday(year).apply(res.chronology()).plus(res.days())))
				.map(holder -> new CreateHoliday(holder.getDate()).apply(holder.getDescribed()));
	}

}
