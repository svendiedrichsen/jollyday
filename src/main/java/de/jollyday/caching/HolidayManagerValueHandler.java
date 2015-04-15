package de.jollyday.caching;

import de.jollyday.HolidayManager;
import de.jollyday.ManagerParameter;
import de.jollyday.datasource.ConfigurationDataSource;
import de.jollyday.datasource.ConfigurationDataSourceManager;
import de.jollyday.util.Cache;
import de.jollyday.util.ClassLoadingUtil;

/**
 * Creates the {@link Cache.ValueHandler} which constructs a {@link HolidayManager}.
 */
public class HolidayManagerValueHandler implements Cache.ValueHandler<HolidayManager> {

	private final ManagerParameter parameter;
	private final String managerImplClassName;

	/**
	 * Manager for providing configuration data sources which return the holiday
	 * data.
	 */
	private final ConfigurationDataSourceManager configurationDataSourceManager = new ConfigurationDataSourceManager();

	/**
	 * Utility to load classes.
	 */
	private final ClassLoadingUtil classLoadingUtil = new ClassLoadingUtil();

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
