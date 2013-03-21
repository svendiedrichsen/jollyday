package de.jollyday.util;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Cache implementation which handles concurring access to chached values.
 * 
 * @author sdiedrichsen
 * 
 * @param <VALUE>
 *            the type of cached values
 */
public class Cache<VALUE> {

	/**
	 * Map for chaching
	 */
	private Map<String, VALUE> cachingMap = new HashMap<String, VALUE>();
	/**
	 * Lock for accessing the map
	 */
	private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

	/**
	 * Returns the value defined by the {@link ValueHandler}
	 * 
	 * @param valueCreator
	 *            which creates the key and the value if necessary
	 * @return the eventually cached value
	 */
	public VALUE get(ValueHandler<VALUE> valueCreator) {
		String key = valueCreator.getKey();
		boolean isReadLocked = false;
		try {
			lock.readLock().lock();
			isReadLocked = true;
			if (!cachingMap.containsKey(key)) {
				lock.readLock().unlock();
				isReadLocked = false;
				lock.writeLock().lock();
				if (!cachingMap.containsKey(key)) {
					VALUE value = valueCreator.createValue();
					cachingMap.put(key, value);
				}
				lock.readLock().lock();
				isReadLocked = true;
				lock.writeLock().unlock();
			}
			return cachingMap.get(key);
		} finally {
			if (lock.writeLock().isHeldByCurrentThread()) {
				lock.writeLock().unlock();
			}
			if (isReadLocked) {
				lock.readLock().unlock();
			}
		}
	}

	/**
	 * Clears the cache.
	 */
	public void clear() {
		lock.writeLock().lock();
		cachingMap.clear();
		lock.writeLock().unlock();
	}

	public static abstract class ValueHandler<VALUE> {
		public abstract String getKey();

		public abstract VALUE createValue();
	}

}
