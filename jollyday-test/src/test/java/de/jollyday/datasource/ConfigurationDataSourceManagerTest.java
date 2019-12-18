package de.jollyday.datasource;

import static org.junit.Assert.*;

import java.util.Properties;

import org.junit.Test;

import de.jollyday.ManagerParameter;
import de.jollyday.ManagerParameters;
import de.jollyday.datasource.impl.XmlFileDataSource;

public class ConfigurationDataSourceManagerTest {

	ConfigurationDataSourceManager configurationDataSourceManager = new ConfigurationDataSourceManager();
	Properties properties = new Properties();
	ManagerParameter managerParameter = ManagerParameters.create((String)null, properties);
	
	@Test(expected=IllegalStateException.class)
	public void testGetConfigurationDataSourceMissingConfig() {
		configurationDataSourceManager.getConfigurationDataSource(managerParameter);
	}

	@Test(expected=IllegalStateException.class)
	public void testGetConfigurationDataSourceMissingClass() {
		managerParameter.setProperty(ManagerParameter.CONFIGURATION_DATASOURCE_IMPL_CLASS, "ThisIsNoClass");
		configurationDataSourceManager.getConfigurationDataSource(managerParameter);
	}

	@Test(expected=IllegalStateException.class)
	public void testGetConfigurationDataSourceMissingConstructor() {
		managerParameter.setProperty(ManagerParameter.CONFIGURATION_DATASOURCE_IMPL_CLASS, "java.util.Calendar");
		configurationDataSourceManager.getConfigurationDataSource(managerParameter);
	}

	@Test
	public void testGetConfigurationDataSourceXmlFileDataSource() {
		managerParameter.setProperty(ManagerParameter.CONFIGURATION_DATASOURCE_IMPL_CLASS, XmlFileDataSource.class.getName());
		ConfigurationDataSource datasource = configurationDataSourceManager.getConfigurationDataSource(managerParameter);
		assertNotNull("Missing datasource.", datasource);
		assertEquals("Unexpected class.", XmlFileDataSource.class, datasource.getClass());
	}

}
