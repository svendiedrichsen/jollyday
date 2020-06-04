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
import de.jollyday.parser.functions.CalculateRelativeDatesFromChronologyWithinGregorianYear;
import de.jollyday.parser.functions.CreateHoliday;
import de.jollyday.parser.predicates.ValidLimitation;
import de.jollyday.spi.EthiopianOrthodoxHoliday;
import org.threeten.extra.chrono.CopticChronology;

import java.time.LocalDate;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * Calculates the ethiopian orthodox holidays.
 *
 * @author Sven Diedrichsen
 * @version $Id: $
 */
public class EthiopianOrthodoxHolidayParser implements Function<Integer, Stream<Holiday>> {

	private Stream<EthiopianOrthodoxHoliday> ethiopianOrthodoxHolidayStream;

	public EthiopianOrthodoxHolidayParser(Stream<EthiopianOrthodoxHoliday> ethiopianOrthodoxHolidayStream) {
		this.ethiopianOrthodoxHolidayStream = ethiopianOrthodoxHolidayStream;
	}

	@Override
	public Stream<Holiday> apply(Integer year) {
		return ethiopianOrthodoxHolidayStream
				.filter(new ValidLimitation(year))
				.flatMap(eoh -> {
					final Stream<LocalDate> ethiopianHolidays;
					switch (eoh.type()) {
					case TIMKAT:
						ethiopianHolidays = new CalculateRelativeDatesFromChronologyWithinGregorianYear(5, 10, CopticChronology.INSTANCE, 0).apply(year);
						break;
					case ENKUTATASH:
						ethiopianHolidays = new CalculateRelativeDatesFromChronologyWithinGregorianYear(1, 1, CopticChronology.INSTANCE, 0).apply(year);
						break;
					case MESKEL:
						ethiopianHolidays = new CalculateRelativeDatesFromChronologyWithinGregorianYear(1, 17, CopticChronology.INSTANCE, 0).apply(year);
						break;
					default:
						throw new IllegalArgumentException("Unknown ethiopian orthodox holiday type " + eoh.type());
					}
					return ethiopianHolidays.map(date -> new CreateHoliday(date).apply(eoh));
				});
	}

}
