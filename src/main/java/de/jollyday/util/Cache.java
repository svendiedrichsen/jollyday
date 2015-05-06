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

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

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
	private final Map<String, VALUE> cachingMap = new HashMap<>();
	/**
	 * Lock for accessing the map
	 */
	private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
	/**
	 * Returns the value defined by the {@link ValueHandler}
	 *
	 * @param valueHandler
	 *            which creates the key and the value if necessary
	 * @return the eventually cached value
	 */
	public VALUE get(ValueHandler<VALUE> valueHandler) {
		String key = valueHandler.getKey();
		try {
			readLock();
			if (!containsKey(key)) {
				readUnlockWriteLock();
				if (!containsKey(key)) {
					putValue(key, valueHandler.createValue());
				}
				readLockWriteUnlock();
			}
			return getValue(key);
		} finally {
			unlockBoth();
		}
	}

	private void unlockBoth() {
		writeUnlock();
		readUnlock();
	}

	private void readLockWriteUnlock() {
		readLock();
		writeUnlock();
	}

	private void readUnlockWriteLock() {
		readUnlock();
		writeLock();
	}

	private VALUE getValue(String key) {
		return cachingMap.get(key);
	}

	private void putValue(String key, VALUE value) {
		cachingMap.put(key, value);
	}

	private boolean containsKey(String key) {
		return cachingMap.containsKey(key);
	}

	private void writeUnlock() {
		if (lock.isWriteLockedByCurrentThread()) {
			lock.writeLock().unlock();
		}
	}

	private void writeLock() {
		lock.writeLock().lock();
	}

	private void readLock() {
		lock.readLock().lock();
	}

	private void readUnlock() {
		if(lock.getReadHoldCount() > 0){
			lock.readLock().unlock();
		}
	}

	/**
	 * Clears the cache.
	 */
	public void clear() {
		writeLock();
		cachingMap.clear();
		writeUnlock();
	}

	public interface ValueHandler<VALUE> {
		String getKey();
		VALUE createValue();
	}

}
