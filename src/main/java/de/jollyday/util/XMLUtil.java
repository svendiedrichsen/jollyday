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
 *
 * @author sven
 * @version $Id: $
 */
package de.jollyday.util;

import de.jollyday.HolidayType;
import de.jollyday.config.Configuration;
import de.jollyday.config.Month;
import de.jollyday.config.ObjectFactory;
import de.jollyday.config.Weekday;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.IOException;
import java.io.InputStream;
import java.time.DayOfWeek;
import java.util.logging.Logger;

public class XMLUtil {

	/**
	 * the package name to search for the generated java classes.
	 */
	public static final String PACKAGE = "de.jollyday.config";

	private static Logger LOG = Logger.getLogger(XMLUtil.class.getName());

	private JAXBContextCreator contextCreator = new JAXBContextCreator();
	private ClassLoadingUtil classLoadingUtil = new ClassLoadingUtil();

	/**
	 * Unmarshalls the configuration from the stream. Uses <code>JAXB</code> for
	 * this.
	 *
	 * @param stream
	 *            a {@link java.io.InputStream} object.
	 * @return The unmarshalled configuration.
	 * @throws java.io.IOException
	 *             Could not close the provided stream.
	 */
	public Configuration unmarshallConfiguration(InputStream stream) throws IOException {
		if (stream == null) {
			throw new IllegalArgumentException("Stream is NULL. Cannot read XML.");
		}
		try {
			JAXBContext ctx;
			try {
				ctx = contextCreator.create(XMLUtil.PACKAGE, classLoadingUtil.getClassloader());
			} catch (JAXBException e) {
				LOG.warning("Could not create JAXB context using the current threads context classloader. Falling back to ObjectFactory class classloader.");
				ctx = null;
			}
			if (ctx == null) {
				ctx = contextCreator.create(XMLUtil.PACKAGE, ObjectFactory.class.getClassLoader());
			}
			Unmarshaller um = ctx.createUnmarshaller();
			@SuppressWarnings("unchecked")
			JAXBElement<Configuration> el = (JAXBElement<Configuration>) um.unmarshal(stream);
			return el.getValue();
		} catch (JAXBException ue) {
			throw new IllegalStateException("Cannot parse holidays XML file.", ue);
		}
	}

	/**
	 * Returns the {@link DayOfWeek} equivalent for the given weekday.
	 *
	 * @param weekday
	 *            a {@link Weekday} object.
	 * @return a DayOfWeek instance.
	 */
	public final DayOfWeek getWeekday(Weekday weekday) {
		return DayOfWeek.valueOf(weekday.value());
		}

	/**
	 * Returns the value for the given month.
	 *
	 * @param month
	 *            a {@link Month} object.
	 * @return a 1-12 value.
	 */
	public int getMonth(Month month) {
		return month.ordinal() + 1;
	}

	/**
	 * Gets the type.
	 *
	 * @param type
	 *            the type of holiday in the config
	 * @return the type of holiday
	 */
	public HolidayType getType(de.jollyday.config.HolidayType type) {
		switch (type) {
		case OFFICIAL_HOLIDAY:
			return HolidayType.OFFICIAL_HOLIDAY;
		case UNOFFICIAL_HOLIDAY:
			return HolidayType.UNOFFICIAL_HOLIDAY;
		default:
			throw new IllegalArgumentException("Unknown type " + type);
		}
	}

	public class JAXBContextCreator {
		public JAXBContext create(String packageName, ClassLoader classLoader) throws JAXBException {
			return JAXBContext.newInstance(packageName, classLoader);
		}
	}

}
