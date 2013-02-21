package de.jollyday.datasource.impl;

import static org.junit.Assert.*;

import org.junit.Test;

import de.jollyday.config.Configuration;

public class XmlFileDataSourceTest {

	private XmlFileDataSource xmlFileDataSource = new XmlFileDataSource();
	
	@Test(expected=IllegalStateException.class)
	public void testGetConfigurationUndefinedFile() {
		xmlFileDataSource.getConfiguration("undefined");
	}
	
	@Test
	public void testGetConfiguration() {
		Configuration configuration = xmlFileDataSource.getConfiguration("de");
		assertNotNull(configuration);
	}	

	@Test
	public void testGetConfigurationFileName() {
		assertEquals("Unexpected file name.", "holidays/Holidays_de.xml", XmlFileDataSource.getConfigurationFileName("de"));
	}

}
