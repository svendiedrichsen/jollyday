package de.jollyday.caching;

import de.jollyday.HolidayManager;
import de.jollyday.ManagerParameter;
import de.jollyday.datasource.ConfigurationDataSource;
import de.jollyday.datasource.ConfigurationDataSourceManager;
import de.jollyday.util.ClassLoadingUtil;
import de.jollyday.util.Cache.ValueHandler;

/**
 * Creates the {@link ValueHandler} which constructs a {@link HolidayManager}.
 * @param parameter the parameters to initialize the {@link HolidayManager}
 * @param managerImplClassName the {@link HolidayManager} implementing clss name.
 * @return the new {@link HolidayManager} instance
 */
public class HolidayManagerValueHandler extends ValueHandler<HolidayManager> {
	
	private ManagerParameter parameter;
	private String managerImplClassName;

	/**
	 * Manager for providing configuration data sources which return the holiday
	 * data.
	 */
	private ConfigurationDataSourceManager configurationDataSourceManager = new ConfigurationDataSourceManager();

	/**
	 * Utility to load classes.
	 */
	private ClassLoadingUtil classLoadingUtil = new ClassLoadingUtil();
	
	public HolidayManagerValueHandler(final ManagerParameter parameter, final String managerImplClassName) {
		this.parameter = parameter;
		this.managerImplClassName = managerImplClassName;
	}

	@Override
	public String getKey() {
		return parameter.createCacheKey();
	}				
	
	@Override
	public HolidayManager createValue() {
		HolidayManager manager = instantiateManagerImpl(managerImplClassName);
		ConfigurationDataSource configurationDataSource = configurationDataSourceManager.getConfigurationDataSource(parameter);
		manager.setConfigurationDataSource(configurationDataSource);
		manager.init(parameter);
		return manager;
	}

	/**
	 * Instantiates the manager implementing class.
	 * 
	 * @param managerImplClassName
	 *            the managers class name
	 * @return the implementation class instantiated
	 */
	private HolidayManager instantiateManagerImpl(
			String managerImplClassName) {
		try {
			Class<?> managerImplClass = classLoadingUtil
					.loadClass(managerImplClassName);
			Object managerImplObject = managerImplClass.newInstance();
			return HolidayManager.class.cast(managerImplObject);
		} catch (Exception e) {
			throw new IllegalStateException("Cannot create manager class "
					+ managerImplClassName, e);
		}
	}


}
