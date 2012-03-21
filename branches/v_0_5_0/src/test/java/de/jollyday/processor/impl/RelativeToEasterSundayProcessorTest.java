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

import static org.junit.Assert.fail;

import org.junit.Test;

import de.jollyday.config.RelativeToEasterSunday;

/**
 * @author sven
 * 
 */
public class RelativeToEasterSundayProcessorTest {

	/**
	 * Test method for
	 * {@link de.jollyday.processor.impl.RelativeToEasterSundayProcessor#RelativeToEasterSundayProcessor(de.jollyday.config.RelativeToEasterSunday)}
	 * .
	 */
	@Test(expected = NullPointerException.class)
	public void testRelativeToEasterSundayProcessorNull() {
		new RelativeToEasterSundayProcessor(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testRelativeToEasterSundayProcessorZeroDays() {
		RelativeToEasterSunday relativeToEasterSunday = new RelativeToEasterSunday();
		new RelativeToEasterSundayProcessor(relativeToEasterSunday);
	}

	@Test
	public void testRelativeToEasterSundayProcessorNonZeroDays() {
		RelativeToEasterSunday relativeToEasterSunday = new RelativeToEasterSunday();
		relativeToEasterSunday.setDays(1);
		new RelativeToEasterSundayProcessor(relativeToEasterSunday);
	}

	/**
	 * Test method for
	 * {@link de.jollyday.processor.impl.RelativeToEasterSundayProcessor#init()}
	 * .
	 */
	@Test
	public void testInit() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link de.jollyday.processor.impl.RelativeToEasterSundayProcessor#process(int, java.lang.String[])}
	 * .
	 */
	@Test
	public void testProcess() {
		fail("Not yet implemented");
	}

}
