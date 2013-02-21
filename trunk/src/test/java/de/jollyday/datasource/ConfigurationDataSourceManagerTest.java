package de.jollyday.datasource;

import static org.junit.Assert.*;

import java.util.Properties;

import org.junit.Test;

import de.jollyday.datasource.impl.XmlFileDataSource;

public class ConfigurationDataSourceManagerTest {

	ConfigurationDataSourceManager configurationDataSourceManager = new ConfigurationDataSourceManager();
	Properties properties = new Properties();
	
	@Test(expected=IllegalStateException.class)
	public void testGetConfigurationDataSourceMissingConfig() {
		configurationDataSourceManager.getConfigurationDataSource(properties);
	}

	@Test(expected=IllegalStateException.class)
	public void testGetConfigurationDataSourceMissingClass() {
		properties.setProperty(ConfigurationDataSourceManager.CONFIGURATION_DATASOURCE_IMPL_CLASS, "ThisIsNoClass");
		configurationDataSourceManager.getConfigurationDataSource(properties);
	}

	@Test(expected=IllegalStateException.class)
	public void testGetConfigurationDataSourceMissingConstructor() {
		properties.setProperty(ConfigurationDataSourceManager.CONFIGURATION_DATASOURCE_IMPL_CLASS, "java.util.Calendar");
		configurationDataSourceManager.getConfigurationDataSource(properties);
	}

	@Test
	public void testGetConfigurationDataSourceXmlFileDataSource() {
		properties.setProperty(ConfigurationDataSourceManager.CONFIGURATION_DATASOURCE_IMPL_CLASS, XmlFileDataSource.class.getName());
		ConfigurationDataSource datasource = configurationDataSourceManager.getConfigurationDataSource(properties);
		assertNotNull("Missing datasource.", datasource);
		assertEquals("Unexpected class.", XmlFileDataSource.class, datasource.getClass());
	}

}
