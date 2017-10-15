package de.jollyday.parameter;

import java.util.Properties;

import de.jollyday.ManagerParameter;

public abstract class BaseManagerParameter implements ManagerParameter {

	private Properties properties = new Properties();

	public BaseManagerParameter(Properties properties){
		if(properties != null){
			this.properties.putAll(properties);
		}
	}

	@Override
	public void mergeProperties(Properties properties) {
		if(properties != null){
			Properties mergedProperties = new Properties();
			mergedProperties.putAll(properties);
			mergedProperties.putAll(this.properties);
			this.properties = mergedProperties;
		}
	}

	@Override
	public String getProperty(String key){
		return properties.getProperty(key);
	}

	@Override
	public void setProperty(String key, String value){
		this.properties.setProperty(key, value);
	}

	@Override
	public String getManagerImplClassName() {
		return getProperty(MANAGER_IMPL_CLASS_PREFIX);
	}

}
