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
package de.jollyday.util;

import de.jollyday.config.Configuration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.IOException;
import java.io.InputStream;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class XMLUtilTest {

	@Mock
	XMLUtil.JAXBContextCreator contextCreator;
	@Mock
	InputStream inputStream;

	@InjectMocks
	XMLUtil xmlUtil = new XMLUtil();

	@Test(expected = IllegalArgumentException.class)
	public void testUnmarshallConfigurationNullCheck() throws IOException {
		xmlUtil.unmarshallConfiguration(null);
	}

	@Test(expected = IllegalStateException.class)
	public void testUnmarshallConfigurationException() throws IOException, JAXBException {
		when(contextCreator.create(eq(XMLUtil.PACKAGE), any(ClassLoader.class))).thenThrow(new JAXBException(""))
				.thenThrow(new JAXBException(""));
		xmlUtil.unmarshallConfiguration(inputStream);
		verify(inputStream).close();
	}

	@Test
	public void testUnmarshallConfiguration() throws IOException, JAXBException {
		JAXBContext ctx = mock(JAXBContext.class);
		Unmarshaller unmarshaller = mock(Unmarshaller.class);
		@SuppressWarnings("unchecked")
		JAXBElement<Configuration> element = mock(JAXBElement.class);
		when(contextCreator.create(eq(XMLUtil.PACKAGE), any(ClassLoader.class))).thenReturn(null).thenReturn(ctx);
		when(ctx.createUnmarshaller()).thenReturn(unmarshaller);
		when(unmarshaller.unmarshal(inputStream)).thenReturn(element);
		xmlUtil.unmarshallConfiguration(inputStream);
		verify(element).getValue();
	}
}
