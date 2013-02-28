package de.jollyday.parameter;

import java.net.URL;
import java.util.Properties;

public class UrlManagerParameter extends BaseManagerParameter {

	private URL calendarFileUrl;

	public UrlManagerParameter(URL calendarFileUrl, Properties properties) {
		super(properties);
		this.calendarFileUrl = calendarFileUrl;
	}

	public String createCacheKey() {
		return calendarFileUrl.toString();
	}

	public String getDisplayName() {
		return calendarFileUrl.toString();
	}

	public URL createResourceUrl() {
		return calendarFileUrl;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + " - "
				+ this.calendarFileUrl.toString();
	}
}
