/**
 * Copyright 2013 Sven Diedrichsen
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
package de.jollyday.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Cache implementation which handles concurrent access to cached values.
 *
 * @param <VALUE>
 *            the type of cached values
 */
public class Cache<VALUE> {
	/**
	 * Map for caching
	 */
	private final Map<String, VALUE> cachingMap = new ConcurrentHashMap<>();
	/**
	 * Returns the value defined by the {@link ValueHandler}
	 *
	 * @param valueHandler
	 *            which creates the key and the value if necessary
	 * @return the eventually cached value
	 */
	public VALUE get(ValueHandler<VALUE> valueHandler) {
		final String key = valueHandler.getKey();
		// Try to first get the value which is most likely cached to avoid creating a lambda.
		final VALUE value = cachingMap.get(key);
		return value != null ? value : cachingMap.computeIfAbsent(key, k -> valueHandler.createValue());
	}

	/**
	 * Clears the cache.
	 */
	public void clear() {
		cachingMap.clear();
	}

	public interface ValueHandler<VALUE> {
		String getKey();
		VALUE createValue();
	}

}
