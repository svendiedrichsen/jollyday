package de.jollyday.configuration;

import de.jollyday.ManagerParameter;
import de.jollyday.ManagerParameters;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ConfigurationProviderManagerTest {

	@Mock
	ConfigurationProvider defaultConfigurationProvider;
	@Mock
	ConfigurationProvider urlConfigurationProvider;

	@InjectMocks
	ConfigurationProviderManager configurationProviderManager = new ConfigurationProviderManager();

	ManagerParameter managerParameter = ManagerParameters.create((String)null);

	@AfterEach
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
		assertEquals("value", managerParameter.getProperty("key"), "Wrong value for property: key");
	}

	@Test
	public void testGetPropertiesWithWrongAndCorrectClass() {
		System.setProperty(ConfigurationProvider.CONFIG_PROVIDERS_PROPERTY, getClass().getPackage().getName()
				+ ".TestProvider,java.lang.String");
		configurationProviderManager.mergeConfigurationProperties(managerParameter);
		assertResult(managerParameter);
		assertEquals( "value", managerParameter.getProperty("key"), "Wrong value for property: key");
	}

	@Test
	public void testGetPropertiesWithManualOverride() {
		managerParameter.setProperty("MANUAL_KEY", "MANUAL_VALUE");
		managerParameter.setProperty("manager.impl", "NewImpl");

		configurationProviderManager.mergeConfigurationProperties(managerParameter);

		assertResult(managerParameter);
		assertEquals("MANUAL_VALUE", managerParameter.getProperty("MANUAL_KEY"), "Wrong value for property: MANUAL_KEY");
		assertEquals("NewImpl", managerParameter.getProperty("manager.impl"), "Wrong value for property: manager.impl");
	}

	private void assertResult(ManagerParameter parameter) {
		assertNotNull(parameter);
		verify(defaultConfigurationProvider).getProperties();
		verify(urlConfigurationProvider).getProperties();
	}

}
