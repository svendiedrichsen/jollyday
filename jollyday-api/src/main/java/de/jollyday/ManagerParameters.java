package de.jollyday;

import java.net.URL;
import java.util.Locale;
import java.util.Properties;

import de.jollyday.parameter.CalendarPartManagerParameter;
import de.jollyday.parameter.UrlManagerParameter;

public final class ManagerParameters {

	private ManagerParameters(){
	}

	public static ManagerParameter create(String calendarPart){
		return create(calendarPart, null);
	}

	/**
	 * Uses the locales country if it exists or its language otherwise.
	 * @param lc The locale to create parameters from.
	 * @return parameters
	 */
	public static ManagerParameter create(Locale lc) {
		String calendarPart =
				"".equals(lc.getCountry())
				? lc.getLanguage()
				: lc.getCountry();
		return create(calendarPart, null);
	}

	public static ManagerParameter create(HolidayCalendar calendar){
		return create(calendar, null);
	}

	public static ManagerParameter create(HolidayCalendar calendar, Properties properties){
		return  create(calendar.getId(), properties);
	}

	public static ManagerParameter create(URL calendarFileUrl){
		return create(calendarFileUrl, null);
	}

	public static ManagerParameter create(String calendarPart, Properties properties){
		return  new CalendarPartManagerParameter(prepareCalendarName(calendarPart), properties);
	}

	public static ManagerParameter create(URL calendarFileUrl, Properties properties){
		return new UrlManagerParameter(calendarFileUrl, properties);
	}

	private static String prepareCalendarName(String calendar) {
		if (calendar == null || "".equals(calendar.trim())) {
			return Locale.getDefault().getCountry().toLowerCase();
		} else {
			return calendar.trim().toLowerCase();
		}
	}

}
