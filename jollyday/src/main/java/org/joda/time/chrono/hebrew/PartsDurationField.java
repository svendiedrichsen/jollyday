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

import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;

/**
 * @author svdi1de
 * 
 */
public class PartsDurationField extends DurationField {

	@Override
	public DurationFieldType getType() {
		return PartsDurationFieldType.parts();
	}

	@Override
	public String getName() {
		return "Parts";
	}

	@Override
	public boolean isSupported() {
		return true;
	}

	@Override
	public boolean isPrecise() {
		return true;
	}

	@Override
	public long getUnitMillis() {
		return (3 + 1 / 3) * 1000;
	}

	@Override
	public int getValue(long duration) {
		return (int) getValueAsLong(duration);
	}

	@Override
	public long getValueAsLong(long duration) {
		return (long) duration / (long) (1000d * (3d + 1d / 3d));
	}

	@Override
	public int getValue(long duration, long instant) {
		return getValue(instant + duration);
	}

	@Override
	public long getValueAsLong(long duration, long instant) {
		return getValueAsLong(instant + duration);
	}

	@Override
	public long getMillis(int value) {
		return (long) ((double) value / (3d + 1d / 3d) * 1000d);
	}

	@Override
	public long getMillis(long value) {
		return (long) ((double) value / (3d + 1d / 3d) * 1000d);
	}

	@Override
	public long getMillis(int value, long instant) {
		return instant + getMillis((long) value);
	}

	@Override
	public long getMillis(long value, long instant) {
		return instant + getMillis((long) value);
	}

	@Override
	public long add(long instant, int value) {
		return instant + getMillis((long) value);
	}

	@Override
	public long add(long instant, long value) {
		return instant + getMillis((long) value);
	}

	@Override
	public int getDifference(long minuendInstant, long subtrahendInstant) {
		return (int) (minuendInstant - subtrahendInstant);
	}

	@Override
	public long getDifferenceAsLong(long minuendInstant, long subtrahendInstant) {
		return minuendInstant - subtrahendInstant;
	}

	@Override
	public int compareTo(Object durationField) {
		return (int) (getMillis(1) - ((DurationField) durationField)
				.getMillis(1));
	}

	@Override
	public String toString() {
		return "p";
	}

}
