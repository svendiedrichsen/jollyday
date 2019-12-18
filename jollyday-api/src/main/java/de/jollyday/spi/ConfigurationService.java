package de.jollyday.spi;

import de.jollyday.ManagerParameter;

/**
 * Service to provide the serialised configuration from XML files.
 */
public interface ConfigurationService {
    Configuration getConfiguration(ManagerParameter parameter);
}
