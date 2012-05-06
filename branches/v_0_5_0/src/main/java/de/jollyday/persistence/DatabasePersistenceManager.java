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
package de.jollyday.persistence;

import de.jollyday.config.Configuration;

/**
 * TODO: implement db persistence
 * 
 * @author sven
 * 
 */
public class DatabasePersistenceManager implements IPersistenceManager {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.jollyday.persistence.IPersistenceManager#getConfiguration(java.lang
	 * .String)
	 */
	public Configuration getConfiguration(String calendarName) {
		throw new IllegalStateException("Not implemented yet.");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.jollyday.persistence.IPersistenceManager#flushCache()
	 */
	public void flushCache() {
		throw new IllegalStateException("Not implemented yet.");
	}

}
