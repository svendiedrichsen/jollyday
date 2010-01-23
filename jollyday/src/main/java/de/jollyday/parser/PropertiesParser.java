package de.jollyday.parser;

import java.util.Calendar;
import java.util.Properties;
import java.util.Set;

public interface PropertiesParser {
	Set<Calendar> parse(int year);
	void init(Properties p);
}
