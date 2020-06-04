package de.jollyday.jaxb;

import de.jollyday.ManagerParameter;

import java.io.InputStream;
import java.net.URL;

/**
 * @author sdiedrichsen
 * @version $
 * @since 15.03.20
 */
public class ConfigurationService implements de.jollyday.spi.ConfigurationService {

    private XMLUtil xmlUtil = new XMLUtil();

    @Override
    public de.jollyday.spi.Configuration getConfiguration(ManagerParameter parameter) {
        URL resourceUrl = parameter.createResourceUrl();
        try(InputStream inputStream = resourceUrl.openStream()) {
            return new Configuration(xmlUtil.unmarshallConfiguration(inputStream));
        } catch (Exception e) {
            throw new IllegalStateException("Cannot instantiate configuration from URL '"+resourceUrl+"'.", e);
        }
    }

}
