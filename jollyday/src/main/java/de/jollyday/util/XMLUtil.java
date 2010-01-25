package de.jollyday.util;

import org.joda.time.DateTimeConstants;

import de.jollyday.config.Month;
import de.jollyday.config.Weekday;


public class XMLUtil {

	public static final int getWeekday(Weekday w){
		switch(w){
			case MONDAY: return DateTimeConstants.MONDAY;
			case TUESDAY: return DateTimeConstants.TUESDAY;
			case WEDNESDAY: return DateTimeConstants.WEDNESDAY;
			case THURSDAY: return DateTimeConstants.THURSDAY;
			case FRIDAY: return DateTimeConstants.FRIDAY;
			case SATURDAY: return DateTimeConstants.SATURDAY;
			case SUNDAY: return DateTimeConstants.SUNDAY;
			default:
				throw new IllegalArgumentException("Unknown weekday "+w);
		}
	}

	public static int getMonth(Month month) {
		switch (month){
			case JANUARY: return DateTimeConstants.JANUARY;
			case FEBRUARY: return DateTimeConstants.FEBRUARY;
			case MARCH: return DateTimeConstants.MARCH;
			case APRIL: return DateTimeConstants.APRIL;
			case MAY: return DateTimeConstants.MAY;
			case JUNE: return DateTimeConstants.JUNE;
			case JULY: return DateTimeConstants.JULY;
			case AUGUST: return DateTimeConstants.AUGUST;
			case SEPTEMBER: return DateTimeConstants.SEPTEMBER;
			case OCTOBER: return DateTimeConstants.OCTOBER;
			case NOVEMBER: return DateTimeConstants.NOVEMBER;
			case DECEMBER: return DateTimeConstants.DECEMBER;
			default:
				throw new IllegalArgumentException("Unknown month "+month);
		}
	}
	
}
