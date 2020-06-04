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
 */
package de.jollyday.parser.impl;

import de.jollyday.Holiday;
import de.jollyday.parser.functions.CreateHoliday;
import de.jollyday.parser.functions.FixedToLocalDate;
import de.jollyday.parser.functions.MoveDateRelative;
import de.jollyday.parser.predicates.ValidLimitation;
import de.jollyday.spi.Fixed;

import java.util.function.Function;
import java.util.stream.Stream;

/**
 * The Class FixedParser. Parses a fixed date to create a Holiday.
 *
 * @author tboven
 * @version $Id: $
 */
public class FixedParser implements Function<Integer, Stream<Holiday>> {

	private Stream<Fixed> fixed;

	public FixedParser(Stream<Fixed> fixed) {
		this.fixed = fixed;
	}

	@Override
	public Stream<Holiday> apply(final Integer year) {
		return fixed.filter(new ValidLimitation(year))
				.map(fixed -> new DescribedDateHolder(fixed, new MoveDateRelative(new FixedToLocalDate(year).apply(fixed)).apply(fixed)))
				.map(describedDateHolder -> new CreateHoliday(describedDateHolder.getDate()).apply(describedDateHolder.getDescribed()));
	}

}
