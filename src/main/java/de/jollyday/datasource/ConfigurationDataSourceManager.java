/**
 * Copyright 2013 Sven Diedrichsen 
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
package de.jollyday.datasource;

import de.jollyday.ManagerParameter;
import de.jollyday.util.ClassLoadingUtil;

/**
 * This manager is responsible for instantiating the configured configuration datasource
 * which is used to access the holiday data.
 * 
 * @author sdiedrichsen
 */
public class ConfigurationDataSourceManager {

	private final ClassLoadingUtil classLoadingUtil = new ClassLoadingUtil();

	public ConfigurationDataSource getConfigurationDataSource(ManagerParameter parameter){
		validateConfiguration(parameter);
		String dataSourceClassName = parameter.getProperty(ManagerParameter.CONFIGURATION_DATASOURCE_IMPL_CLASS);
		return instantiateDataSource(dataSourceClassName);
	}

	private ConfigurationDataSource instantiateDataSource(
			String dataSourceClassName) {
		try{
			Class<?> dataSourceClass = classLoadingUtil.getClassloader().loadClass(dataSourceClassName);
			return ConfigurationDataSource.class.cast(dataSourceClass.newInstance());
		}catch(Exception e){
			throw new IllegalStateException("Cannot instantiate datasource instance of "+dataSourceClassName,e);
		}
	}

	private void validateConfiguration(ManagerParameter parameter) {
		if(parameter.getProperty(ManagerParameter.CONFIGURATION_DATASOURCE_IMPL_CLASS) == null){
			throw new IllegalStateException("Missing holiday configuration datasource implementation class under config key "+ManagerParameter.CONFIGURATION_DATASOURCE_IMPL_CLASS);
		}
	}
	
}
