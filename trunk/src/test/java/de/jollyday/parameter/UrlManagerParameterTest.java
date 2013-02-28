package de.jollyday.parameter;

import static org.junit.Assert.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

import org.junit.Before;
import org.junit.Test;

public class UrlManagerParameterTest {
	
	private UrlManagerParameter urlManagerParameter;
	private URL url;
	private Properties properties;
	
	@Before
	public void setup() throws MalformedURLException{
		url = new URL("http://www.google.de");
		properties = new Properties();
		urlManagerParameter = new UrlManagerParameter(url, properties);
	}

	@Test
	public void testCreateCacheKey() {
		assertEquals("Unexpected cache key.", "http://www.google.de", urlManagerParameter.createCacheKey());
	}

	@Test
	public void testGetDisplayName() {
		assertEquals("Unexpected display name.", "http://www.google.de", urlManagerParameter.getDisplayName());
	}

	@Test
	public void testCreateResourceUrl() {
		assertEquals("Unexpected url.", url, urlManagerParameter.createResourceUrl());
	}

}
