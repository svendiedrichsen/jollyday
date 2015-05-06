package de.jollyday.parameter;

import java.net.URL;
import java.util.Properties;

import de.jollyday.util.ResourceUtil;

public class CalendarPartManagerParameter extends BaseManagerParameter {

	/**
	 * prefix of the config files.
	 */
	private static final String FILE_PREFIX = "holidays/Holidays";
	/**
	 * suffix of the config files.
	 */
	private static final String FILE_SUFFIX = ".xml";
	/**
	 * The utility to load resources.
	 */
	private final ResourceUtil resourceUtil = new ResourceUtil();

	private final String calendarPart;

	public CalendarPartManagerParameter(String calendarPart,
			Properties properties) {
		super(properties);
		this.calendarPart = calendarPart;
	}

	@Override
	public String createCacheKey() {
		return calendarPart;
	}

	@Override
	public String getDisplayName() {
		return calendarPart;
	}

	@Override
	public URL createResourceUrl() {
		String configurationFileName = getConfigurationFileName(calendarPart);
		return resourceUtil.getResource(configurationFileName);
	}

	@Override
	public String getManangerImplClassName() {
		String className = getProperty(MANAGER_IMPL_CLASS_PREFIX + "."
				+ calendarPart);
		if (className == null) {
			className = super.getManangerImplClassName();
		}
		return className;
	}

	/**
	 * Returns the configuration file name for the country.
	 * 
	 * @param country
	 *            a {@link java.lang.String} object.
	 * @return file name
	 */
	public static String getConfigurationFileName(final String country) {
		return FILE_PREFIX + "_" + country + FILE_SUFFIX;
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + " - " + this.calendarPart;
	}

}
