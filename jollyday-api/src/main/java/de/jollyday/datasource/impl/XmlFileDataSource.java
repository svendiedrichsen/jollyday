/**
 * Copyright 2013 Sven Diedrichsen
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package de.jollyday.datasource.impl;

import de.jollyday.ManagerParameter;
import de.jollyday.config.Configuration;
import de.jollyday.datasource.ConfigurationDataSource;
import de.jollyday.util.XMLUtil;

import java.io.InputStream;
import java.net.URL;

/**
 * This {@link ConfigurationDataSource} implementation reads XML files as resources
 * from the classpath.
 */
public class XmlFileDataSource implements ConfigurationDataSource {

    /**
     * XML utility class.
     */
    private XMLUtil xmlUtil = new XMLUtil();

    @Override
    public Configuration getConfiguration(ManagerParameter parameter) {
        URL resourceUrl = parameter.createResourceUrl();
        try(InputStream inputStream = resourceUrl.openStream()) {
            return xmlUtil.unmarshallConfiguration(inputStream);
        } catch (Exception e) {
            throw new IllegalStateException("Cannot instantiate configuration from URL '"+resourceUrl+"'.", e);
        }
    }


}
