package de.jollyday.parameter;

import java.net.URL;
import java.util.Properties;

public class UrlManagerParameter extends BaseManagerParameter {

	private final URL calendarFileUrl;

	public UrlManagerParameter(URL calendarFileUrl, Properties properties) {
		super(properties);
		this.calendarFileUrl = calendarFileUrl;
	}

	@Override
	public String createCacheKey() {
		return calendarFileUrl.toString();
	}

	@Override
	public String getDisplayName() {
		return calendarFileUrl.toString();
	}

	@Override
	public URL createResourceUrl() {
		return calendarFileUrl;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + " - "
				+ this.calendarFileUrl.toString();
	}
}
