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
import de.jollyday.parser.functions.CalculateEasterSunday;
import de.jollyday.parser.functions.CreateHoliday;
import de.jollyday.parser.functions.MoveDateRelative;
import de.jollyday.parser.predicates.ValidLimitation;
import de.jollyday.spi.ChristianHoliday;

import java.time.LocalDate;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * This parser creates christian holidays for the given year relative to easter
 * sunday.
 *
 * @author Sven Diedrichsen
 * @version $Id: $
 */
public class ChristianHolidayParser implements Function<Integer, Stream<Holiday>> {

	private Stream<ChristianHoliday> christianHolidays;

	public ChristianHolidayParser(Stream<ChristianHoliday> christianHolidays) {
		this.christianHolidays = christianHolidays;
	}

	@Override
	public Stream<Holiday> apply(Integer year) {
		return christianHolidays
				.filter(new ValidLimitation(year))
				.map(ch -> {
					LocalDate easterSunday =  new CalculateEasterSunday(year).apply(ch.chronology());
					switch (ch.type()) {
						case EASTER:
							break;
						case CLEAN_MONDAY:
						case SHROVE_MONDAY:
							easterSunday = easterSunday.minusDays(48);
							break;
						case MARDI_GRAS:
						case CARNIVAL:
							easterSunday = easterSunday.minusDays(47);
							break;
						case ASH_WEDNESDAY:
							easterSunday = easterSunday.minusDays(46);
							break;
						case MAUNDY_THURSDAY:
							easterSunday = easterSunday.minusDays(3);
							break;
						case GOOD_FRIDAY:
							easterSunday = easterSunday.minusDays(2);
							break;
						case EASTER_SATURDAY:
							easterSunday = easterSunday.minusDays(1);
							break;
						case EASTER_MONDAY:
							easterSunday = easterSunday.plusDays(1);
							break;
						case EASTER_TUESDAY:
							easterSunday = easterSunday.plusDays(2);
							break;
						case GENERAL_PRAYER_DAY:
							easterSunday = easterSunday.plusDays(26);
							break;
						case ASCENSION_DAY:
							easterSunday = easterSunday.plusDays(39);
							break;
						case PENTECOST:
						case WHIT_SUNDAY:
							easterSunday = easterSunday.plusDays(49);
							break;
						case WHIT_MONDAY:
						case PENTECOST_MONDAY:
							easterSunday = easterSunday.plusDays(50);
							break;
						case CORPUS_CHRISTI:
							easterSunday = easterSunday.plusDays(60);
							break;
						case SACRED_HEART:
							easterSunday = easterSunday.plusDays(68);
							break;
						default:
							throw new IllegalArgumentException(
									"Unknown christian holiday type " + ch.type());
					}
					easterSunday = new MoveDateRelative(easterSunday).apply(ch);
					return new CreateHoliday(easterSunday).apply(ch);
				});
	}

}
