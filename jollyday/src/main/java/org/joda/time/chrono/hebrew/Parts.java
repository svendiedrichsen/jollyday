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
package org.joda.time.chrono.hebrew;

import org.joda.time.DurationFieldType;
import org.joda.time.PeriodType;
import org.joda.time.base.BaseSingleFieldPeriod;

/**
 * @author Sven
 * 
 */
public class Parts extends BaseSingleFieldPeriod {

	private static final long serialVersionUID = -4176038471643062232L;

	public Parts() {
		super(0);
	}

	public Parts(int p) {
		super(p);
	}

	public static Parts parts(int p) {
		return new Parts(p);
	}

	public int getParts() {
		return super.getValue();
	}

	@Override
	public DurationFieldType getFieldType() {
		return new PartsDurationFieldType();
	}

	@Override
	public PeriodType getPeriodType() {
		return new PartsPeriodType();
	}

}
