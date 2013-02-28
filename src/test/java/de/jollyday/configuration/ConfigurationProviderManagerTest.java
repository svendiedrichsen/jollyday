package de.jollyday.configuration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.verify;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import de.jollyday.ManagerParameter;
import de.jollyday.ManagerParameters;

@RunWith(MockitoJUnitRunner.class)
public class ConfigurationProviderManagerTest {

	@Mock
	ConfigurationProvider defaultConfigurationProvider;
	@Mock
	ConfigurationProvider urlConfigurationProvider;

	@InjectMocks
	ConfigurationProviderManager configurationProviderManager = new ConfigurationProviderManager();
	
	ManagerParameter managerParameter = ManagerParameters.create((String)null);

	@After
	public void teardown() {
		System.clearProperty(ConfigurationProvider.CONFIG_PROVIDERS_PROPERTY);
	}

	@Test
	public void testGetPropertiesWithEmptyProvidersList() {
		System.setProperty(ConfigurationProvider.CONFIG_PROVIDERS_PROPERTY, "");
		configurationProviderManager.mergeConfigurationProperties(managerParameter);
		assertResult(managerParameter);
	}

	@Test
	public void testGetPropertiesWithWrongClass() {
		System.setProperty(ConfigurationProvider.CONFIG_PROVIDERS_PROPERTY, "java.lang.String");
		configurationProviderManager.mergeConfigurationProperties(managerParameter);
		assertResult(managerParameter);
	}

	@Test
	public void testGetPropertiesWithCorrectClass() {
		System.setProperty(ConfigurationProvider.CONFIG_PROVIDERS_PROPERTY, getClass().getPackage().getName()
				+ ".TestProvider");
		configurationProviderManager.mergeConfigurationProperties(managerParameter);
		assertResult(managerParameter);
		assertEquals("Wrong value for property: key", "value", managerParameter.getProperty("key"));
	}

	@Test
	public void testGetPropertiesWithWrongAndCorrectClass() {
		System.setProperty(ConfigurationProvider.CONFIG_PROVIDERS_PROPERTY, getClass().getPackage().getName()
				+ ".TestProvider,java.lang.String");
		configurationProviderManager.mergeConfigurationProperties(managerParameter);
		assertResult(managerParameter);
		assertEquals("Wrong value for property: key", "value", managerParameter.getProperty("key"));
	}

	@Test
	public void testGetPropertiesWithManualOverride() {
		managerParameter.setProperty("MANUAL_KEY", "MANUAL_VALUE");
		managerParameter.setProperty("manager.impl", "NewImpl");

		configurationProviderManager.mergeConfigurationProperties(managerParameter);

		assertResult(managerParameter);
		assertEquals("Wrong value for property: MANUAL_KEY", "MANUAL_VALUE", managerParameter.getProperty("MANUAL_KEY"));
		assertEquals("Wrong value for property: manager.impl", "NewImpl", managerParameter.getProperty("manager.impl"));
	}

	private void assertResult(ManagerParameter parameter) {
		assertNotNull(parameter);
		verify(defaultConfigurationProvider).getProperties();
		verify(urlConfigurationProvider).getProperties();
	}

}
