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

import org.xml.sax.SAXException;

import de.jollyday.config.Configuration;
import de.jollyday.config.ObjectFactory;
import de.jollyday.util.Check;
import de.jollyday.util.XMLUtil;

/**
 * @author sven
 * 
 */
public class ClasspathPersistenceManager implements IPersistenceManager {

	private static final Logger LOG = Logger.getLogger(ClasspathPersistenceManager.class.getName());

	/**
	 * prefix of the config files.
	 */
	private static final String FILE_PREFIX = "holidays/Holidays";

	/**
	 * suffix of the config files.
	 */
	private static final String FILE_SUFFIX = ".xml";

	/**
	 * The schema files name
	 */
	private static final String SCHEMA_FILE_NAME = "Holiday.xsd";

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.jollyday.persistence.IPersistenceManager#getConfiguration(java.lang
	 * .String)
	 */
	public Configuration getConfiguration(String calendarName) {
		String fileName = getConfigurationFileName(calendarName);
		ClassLoader classLoader = getClass().getClassLoader();
		URL resource = classLoader.getResource(fileName);
		if (resource == null) {
			throw new IllegalStateException("Cannot find configuration resource " + fileName + ".");
		}
		try {
			Configuration configuration = unmarshallConfiguration(resource.openStream());
			return configuration;
		} catch (IOException e) {
			throw new IllegalStateException("Cannot read configuration for calendar " + calendarName + ".", e);
		}
	}

	/**
	 * Returns the configuration file name for the country.
	 * 
	 * @param calendarName
	 *            the calendar name
	 * @return file name
	 */
	private static String getConfigurationFileName(final String calendarName) {
		return FILE_PREFIX + "_" + calendarName + FILE_SUFFIX;
	}

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

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.jollyday.persistence.IPersistenceManager#flushCache()
	 */
	public void flushCache() {
	}

}
