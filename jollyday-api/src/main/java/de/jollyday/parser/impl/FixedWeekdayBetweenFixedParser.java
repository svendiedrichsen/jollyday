/**
 * Copyright 2010-2019 Sven Diedrichsen
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
import de.jollyday.parser.functions.CreateHoliday;
import de.jollyday.parser.functions.FindWeekDayBetween;
import de.jollyday.parser.functions.FixedToLocalDate;
import de.jollyday.parser.predicates.ValidLimitation;
import de.jollyday.spi.FixedWeekdayBetweenFixed;

import java.util.function.Function;
import java.util.stream.Stream;

/**
 * Parses the configuration for fixed weekdays between two fixed dates.
 *
 * @author Sven Diedrichsen
 * @version $Id: $
 */
public class FixedWeekdayBetweenFixedParser implements Function<Integer, Stream<Holiday>> {

	private Stream<FixedWeekdayBetweenFixed> fixedWeekdayBetweenFixed;

	public FixedWeekdayBetweenFixedParser(Stream<FixedWeekdayBetweenFixed> fixedWeekdayBetweenFixed) {
		this.fixedWeekdayBetweenFixed = fixedWeekdayBetweenFixed;
	}

	@Override
	public Stream<Holiday> apply(Integer year) {
		return fixedWeekdayBetweenFixed
				.filter(new ValidLimitation(year))
				.map(fwm -> new DescribedDateHolder(fwm,
								new FindWeekDayBetween(
										new FixedToLocalDate(year).apply(fwm.from()),
										new FixedToLocalDate(year).apply(fwm.to())
								).apply(fwm)
							)
				)
				.map(holder -> new CreateHoliday(holder.getDate()).apply(holder.getDescribed()));
	}

}
