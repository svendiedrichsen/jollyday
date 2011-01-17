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

import org.joda.time.Chronology;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;

/**
 * @author Sven
 *
 */
public class PartsDurationFieldType extends DurationFieldType{

	private static final long serialVersionUID = -1519048586565098191L;

	private static final PartsDurationFieldType PARTS_FIELD_TYPE = new PartsDurationFieldType();

	protected PartsDurationFieldType() {
		super("Parts");
	}

	@Override
	public DurationField getField(Chronology chronology) {
		return new PartsDurationField();
	}
	
	public static PartsDurationFieldType parts(){
		return PARTS_FIELD_TYPE;
	}
	
}
