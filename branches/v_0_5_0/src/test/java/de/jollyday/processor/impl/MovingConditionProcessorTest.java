/**
 * Copyright 2012 Sven Diedrichsen 
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
package de.jollyday.processor.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.joda.time.DateTimeConstants;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;

import de.jollyday.config.MovingCondition;
import de.jollyday.config.To;
import de.jollyday.config.Weekday;

/**
 * @author sven
 * 
 */
public class MovingConditionProcessorTest {

	private MovingConditionProcessor movingConditionProcessor;
	private MovingCondition movingCondition;

	@Before
	public void setup() {
		movingCondition = new MovingCondition();
		movingConditionProcessor = new MovingConditionProcessor(movingCondition);
	}

	/**
	 * Test method for
	 * {@link de.jollyday.processor.impl.MovingConditionProcessor#MovingConditionProcessor(de.jollyday.config.MovingCondition)}
	 * .
	 */
	@Test(expected = NullPointerException.class)
	public void testMovingConditionProcessor() {
		new MovingConditionProcessor(null);
	}

	/**
	 * Test method for
	 * {@link de.jollyday.processor.impl.MovingConditionProcessor#process(org.joda.time.LocalDate)}
	 * .
	 */
	@Test
	public void testProcess() {

		movingCondition.setWeekday(Weekday.FRIDAY);
		movingCondition.setTo(To.NEXT);
		movingCondition.getWhen().add(Weekday.MONDAY);

		LocalDate movedDate = movingConditionProcessor.process(new LocalDate(2012, DateTimeConstants.JANUARY, 2));
		assertNotNull(movedDate);
		assertEquals(new LocalDate(2012, DateTimeConstants.JANUARY, 6), movedDate);

	}

}
