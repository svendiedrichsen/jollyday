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
package de.jollyday;

/**
 * Type of holiday. Each holiday can be placed in a category and this is
 * represented by this type. The categories can mark a holiday as a official
 * holiday or not.
 *
 * @author tboven
 * @version $Id: $
 */
public enum HolidayType  {

	OFFICIAL_HOLIDAY {
		@Override
		public boolean isOfficialHoliday() {
			return true;
		}
	},

	UNOFFICIAL_HOLIDAY {
		@Override
		public boolean isOfficialHoliday() {
			return false;
		}
	};

	public abstract boolean isOfficialHoliday();

}
