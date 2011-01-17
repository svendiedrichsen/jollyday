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

import org.joda.time.PeriodType;

/**
 * @author Sven
 *
 */
public class PartsPeriodType extends PeriodType{

	private static final long serialVersionUID = 4993755239509123208L;

	protected PartsPeriodType() {
		super("PartsPeriod", new PartsDurationFieldType[]{PartsDurationFieldType.parts()}, new int[]{3});
	}
	
}
