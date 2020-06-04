package de.jollyday.jaxb;

import de.jollyday.spi.Holidays;

import java.util.stream.Stream;

/**
 * @author sdiedrichsen
 * @version $
 * @since 15.03.20
 */
public class Configuration implements de.jollyday.spi.Configuration {

    private de.jollyday.config.Configuration xmlConfiguration;

    public Configuration(de.jollyday.config.Configuration xmlConfiguration) {
        this.xmlConfiguration = xmlConfiguration;
    }

    @Override
    public Holidays holidays() {
        return null;
    }

    @Override
    public Stream<de.jollyday.spi.Configuration> subConfigurations() {
        return xmlConfiguration.getSubConfigurations().stream().map(Configuration::new);
    }

    @Override
    public String hierarchy() {
        return xmlConfiguration.getHierarchy();
    }

    @Override
    public String description() {
        return xmlConfiguration.getDescription();
    }

}
