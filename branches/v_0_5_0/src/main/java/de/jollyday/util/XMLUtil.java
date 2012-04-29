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
import java.net.URL;
import java.util.logging.Logger;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXB;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.joda.time.DateTimeConstants;
import org.xml.sax.SAXException;

import de.jollyday.HolidayType;
import de.jollyday.config.Configuration;
import de.jollyday.config.HolidayRule;
import de.jollyday.config.Month;
import de.jollyday.config.ObjectFactory;
import de.jollyday.config.Weekday;
import de.jollyday.holidaytype.LocalizedHolidayType;

public class XMLUtil {

	/**
	 * The schema files name
	 */
	private static final String SCHEMA_FILE_NAME = "Holiday.xsd";

	/**
	 * the package name to search for the generated java classes.
	 */
	public static final String PACKAGE = "de.jollyday.config";

	private static Logger LOG = Logger.getLogger(XMLUtil.class.getName());

	/**
	 * Unmarshalls the configuration from the stream. Uses {@link JAXB} for this
	 * and validates the against Holidays.xsd schema.
	 * 
	 * @param stream
	 *            a {@link java.io.InputStream} object.
	 * @return The unmarshalled configuration.
	 */
	public static Configuration unmarshallConfiguration(InputStream stream) {
		Check.notNull(stream, "Stream");
		try {
			JAXBContext ctx = createJAXBContext();
			Unmarshaller um = createUnmarshaller(ctx);
			@SuppressWarnings("unchecked")
			JAXBElement<Configuration> el = (JAXBElement<Configuration>) um.unmarshal(stream);
			return el.getValue();
		} catch (Exception ue) {
			throw new IllegalStateException("Cannot parse holidays XML file.", ue);
		} finally {
			try {
				stream.close();
			} catch (IOException e) {
				throw new IllegalStateException("Cannot close file stream.", e);
			}
		}
	}

	/**
	 * Creates the {@link Unmarshaller} from the {@link JAXBContext} and sets a
	 * {@link Schema}
	 * 
	 * @param ctx
	 *            the {@link JAXBContext} to use
	 * @return the {@link Unmarshaller} created
	 * @throws JAXBException
	 * @throws SAXException
	 */
	private static Unmarshaller createUnmarshaller(JAXBContext ctx) throws JAXBException, SAXException {
		Unmarshaller um = ctx.createUnmarshaller();
		URL resource = getSchemaResourceURL();
		if (resource != null) {
			SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			Schema schema = schemaFactory.newSchema(resource);
			um.setSchema(schema);
		} else {
			LOG.warning("Cannot find XSD schema " + SCHEMA_FILE_NAME
					+ " for validation. Will load from stream without schema validation.");
		}
		return um;
	}

	/**
	 * Creates the {@link JAXBContext} from the current thread or the
	 * {@link ObjectFactory}s classloader.
	 * 
	 * @return the {@link JAXBContext}
	 * @throws JAXBException
	 */
	private static JAXBContext createJAXBContext() throws JAXBException {
		JAXBContext ctx = null;
		try {
			ctx = createJAXBContext(Thread.currentThread().getContextClassLoader());
		} catch (Exception e) {
			LOG.warning("Could not create JAXB context using the current threads context classloader. Defaulting to ObjectFactory classloader.");
			ctx = null;
		}
		if (ctx == null) {
			ctx = createJAXBContext(ObjectFactory.class.getClassLoader());
		}
		return ctx;
	}

	/**
	 * Returns the schema resource {@link URL}.
	 * 
	 * @return Schema URL
	 */
	private static URL getSchemaResourceURL() {
		URL resource = Thread.currentThread().getContextClassLoader().getResource(SCHEMA_FILE_NAME);
		if (resource == null) {
			LOG.info("Cannot load XSD schema from current threads classloader. Using ObjectFactorys classloader.");
			resource = ObjectFactory.class.getClassLoader().getResource(SCHEMA_FILE_NAME);
		}
		return resource;
	}

	/**
	 * Loads the {@link JAXBContext} using the provided {@link ClassLoader}.
	 * 
	 * @param classLoader
	 *            The {@link ClassLoader} to use
	 * @return JAXBContext the created context
	 * @throws JAXBException
	 *             Anything goes wrong with the context creation
	 */
	private static JAXBContext createJAXBContext(ClassLoader classLoader) throws JAXBException {
		Check.notNull(classLoader, "ClassLoader");
		return JAXBContext.newInstance(XMLUtil.PACKAGE, classLoader);
	}

	/**
	 * Returns the <code>DateTimeConstants</code> value for the given weekday.
	 * 
	 * @param weekday
	 *            a {@link de.jollyday.config.Weekday} object.
	 * @return DateTimeConstants value.
	 */
	public static final int getWeekday(Weekday weekday) {
		Check.notNull(weekday, "Weekday");
		switch (weekday) {
		case MONDAY:
			return DateTimeConstants.MONDAY;
		case TUESDAY:
			return DateTimeConstants.TUESDAY;
		case WEDNESDAY:
			return DateTimeConstants.WEDNESDAY;
		case THURSDAY:
			return DateTimeConstants.THURSDAY;
		case FRIDAY:
			return DateTimeConstants.FRIDAY;
		case SATURDAY:
			return DateTimeConstants.SATURDAY;
		case SUNDAY:
			return DateTimeConstants.SUNDAY;
		default:
			throw new IllegalArgumentException("Unknown weekday " + weekday);
		}
	}

	/**
	 * Returns the {@link DateTimeConstants} value for the given month.
	 * 
	 * @param month
	 *            a {@link de.jollyday.config.Month} object.
	 * @return DateTimeConstants value.
	 */
	public static int getMonth(Month month) {
		Check.notNull(month, "Month");
		switch (month) {
		case JANUARY:
			return DateTimeConstants.JANUARY;
		case FEBRUARY:
			return DateTimeConstants.FEBRUARY;
		case MARCH:
			return DateTimeConstants.MARCH;
		case APRIL:
			return DateTimeConstants.APRIL;
		case MAY:
			return DateTimeConstants.MAY;
		case JUNE:
			return DateTimeConstants.JUNE;
		case JULY:
			return DateTimeConstants.JULY;
		case AUGUST:
			return DateTimeConstants.AUGUST;
		case SEPTEMBER:
			return DateTimeConstants.SEPTEMBER;
		case OCTOBER:
			return DateTimeConstants.OCTOBER;
		case NOVEMBER:
			return DateTimeConstants.NOVEMBER;
		case DECEMBER:
			return DateTimeConstants.DECEMBER;
		default:
			throw new IllegalArgumentException("Unknown month " + month);
		}
	}

	/**
	 * Gets the {@link HolidayType}.
	 * 
	 * @param type
	 *            the type of holiday in the config
	 * @return the type of holiday
	 */
	public static HolidayType getType(de.jollyday.config.HolidayType type) {
		Check.notNull(type, "HolidayType");
		switch (type) {
		case OFFICIAL_HOLIDAY:
			return LocalizedHolidayType.OFFICIAL_HOLIDAY;
		case UNOFFICIAL_HOLIDAY:
			return LocalizedHolidayType.UNOFFICIAL_HOLIDAY;
		default:
			throw new IllegalArgumentException("Unknown holiday type " + type);
		}
	}

	/**
	 * Shows if the year is valid for the current {@link HolidayRule}.
	 * 
	 * @param year
	 *            the year to inspect
	 * @return year is valid
	 */
	public static boolean isValidInYear(int year, HolidayRule holidayRule) {
		Check.notNull(holidayRule, "HolidayRule");
		boolean isInRange = (holidayRule.getValidFrom() == null || holidayRule.getValidFrom().intValue() <= year)
				&& (holidayRule.getValidTo() == null || holidayRule.getValidTo().intValue() >= year);
		if (isInRange && holidayRule.getValidFrom() != null && holidayRule.getEvery() != null) {
			int yearDifference = year - holidayRule.getValidFrom().intValue();
			switch (holidayRule.getEvery()) {
			case EVERY_YEAR:
				return true;
			case TWO_YEARS:
				return yearDifference % 2 == 0;
			case THREE_YEARS:
				return yearDifference % 3 == 0;
			case FOUR_YEARS:
				return yearDifference % 4 == 0;
			case FIVE_YEARS:
				return yearDifference % 5 == 0;
			case SIX_YEARS:
				return yearDifference % 6 == 0;
			case SEVEN_YEARS:
				return yearDifference % 7 == 0;
			case EVEN_YEARS:
				return year % 2 == 0;
			case ODD_YEARS:
				return year % 2 == 1;
			default:
				throw new IllegalStateException("Unhandled CycleType " + holidayRule.getEvery());
			}
		}
		return isInRange;
	}

}
