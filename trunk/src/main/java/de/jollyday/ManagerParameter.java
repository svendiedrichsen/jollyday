package de.jollyday;

import java.net.URL;
import java.util.Properties;

public interface ManagerParameter {

	final String CONFIGURATION_DATASOURCE_IMPL_CLASS = "configuration.datasource.impl";
	final String MANAGER_IMPL_CLASS_PREFIX = "manager.impl";
	
	void mergeProperties(Properties properties);
	String getProperty(String key);
	void setProperty(String key, String value);

	String getManangerImplClassName();
	String createCacheKey();
	String getDisplayName();
	URL createResourceUrl();

}
