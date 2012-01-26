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
package de.jollyday.holidaytype;

import de.jollyday.HolidayType;

/**
 * Represents a localized version of a holiday type. This type identifies a
 * holiday and can be used to make a distinction for all returned holidays.
 * (e.g. is the holiday a public holiday or not)
 *
 * @author tboven
 * @version $Id: $
 */
public enum LocalizedHolidayType implements HolidayType {
	OFFICIAL_HOLIDAY {
		/*
		 * (non-Javadoc)
		 * 
		 * @see de.jollyday.HolidayType#isOfficialHoliday()
		 */
		public boolean isOfficialHoliday() {
			return true;
		}
	},
	UNOFFICIAL_HOLIDAY {
		/*
		 * (non-Javadoc)
		 * 
		 * @see de.jollyday.HolidayType#isOfficialHoliday()
		 */
		public boolean isOfficialHoliday() {
			return false;
		}
	};

}
