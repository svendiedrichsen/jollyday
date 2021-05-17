package de.jollyday.datasource;

import de.jollyday.ManagerParameter;
import de.jollyday.ManagerParameters;
import de.jollyday.datasource.impl.XmlFileDataSource;
import org.junit.jupiter.api.Test;

import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

public class ConfigurationDataSourceManagerTest {

	ConfigurationDataSourceManager configurationDataSourceManager = new ConfigurationDataSourceManager();
	Properties properties = new Properties();
	ManagerParameter managerParameter = ManagerParameters.create((String)null, properties);

	@Test
	public void testGetConfigurationDataSourceMissingConfig() {
		assertThrows(IllegalStateException.class, () -> configurationDataSourceManager.getConfigurationDataSource(managerParameter));
	}

	@Test
	public void testGetConfigurationDataSourceMissingClass() {
		managerParameter.setProperty(ManagerParameter.CONFIGURATION_DATASOURCE_IMPL_CLASS, "ThisIsNoClass");
		assertThrows(IllegalStateException.class, () -> configurationDataSourceManager.getConfigurationDataSource(managerParameter));
	}

	@Test
	public void testGetConfigurationDataSourceMissingConstructor() {
		managerParameter.setProperty(ManagerParameter.CONFIGURATION_DATASOURCE_IMPL_CLASS, "java.util.Calendar");
		assertThrows(IllegalStateException.class, () -> configurationDataSourceManager.getConfigurationDataSource(managerParameter));
	}

	@Test
	public void testGetConfigurationDataSourceXmlFileDataSource() {
		managerParameter.setProperty(ManagerParameter.CONFIGURATION_DATASOURCE_IMPL_CLASS, XmlFileDataSource.class.getName());
		ConfigurationDataSource datasource = configurationDataSourceManager.getConfigurationDataSource(managerParameter);
		assertNotNull(datasource, "Missing datasource.");
		assertEquals(XmlFileDataSource.class, datasource.getClass(), "Unexpected class.");
	}

}
