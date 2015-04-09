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

import java.io.IOException;
import java.io.InputStream;
import java.time.DayOfWeek;
import java.util.logging.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import de.jollyday.HolidayType;
import de.jollyday.config.Configuration;
import de.jollyday.config.Month;
import de.jollyday.config.ObjectFactory;
import de.jollyday.config.Weekday;
import de.jollyday.holidaytype.LocalizedHolidayType;

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
			JAXBContext ctx = null;
			try {
				ctx = contextCreator.create(XMLUtil.PACKAGE, classLoadingUtil.getClassloader());
			} catch (JAXBException e) {
				LOG.warning("Could not create JAXB context using the current threads context classloader. Defaulting to ObjectFactory classloader.");
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
		} finally {
			stream.close();
		}
	}

	/**
	 * Returns the <code>DateTimeConstants</code> value for the given weekday.
	 * 
	 * @param weekday
	 *            a {@link de.jollyday.config.Weekday} object.
	 * @return a DayOfWeek instance.
	 */
	public final DayOfWeek getWeekday(Weekday weekday) {
		return DayOfWeek.valueOf(weekday.value());
		}

	/**
	 * Returns the <code>DateTimeConstants</code> value for the given month.
	 * 
	 * @param month
	 *            a {@link de.jollyday.config.Month} object.
	 * @return DateTimeConstants value.
	 */
	public int getMonth(Month month) {
		switch (month) {
		case JANUARY:
			return 1;
		case FEBRUARY:
			return 2;
		case MARCH:
			return 3;
		case APRIL:
			return 4;
		case MAY:
			return 5;
		case JUNE:
			return 6;
		case JULY:
			return 7;
		case AUGUST:
			return 8;
		case SEPTEMBER:
			return 9;
		case OCTOBER:
			return 10;
		case NOVEMBER:
			return 11;
		case DECEMBER:
			return 12;
		default:
			throw new IllegalArgumentException("Unknown month " + month);
		}
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
			return LocalizedHolidayType.OFFICIAL_HOLIDAY;
		case UNOFFICIAL_HOLIDAY:
			return LocalizedHolidayType.UNOFFICIAL_HOLIDAY;
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
