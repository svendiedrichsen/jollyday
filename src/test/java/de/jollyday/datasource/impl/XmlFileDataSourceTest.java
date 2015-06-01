package de.jollyday.datasource.impl;

import de.jollyday.ManagerParameter;
import de.jollyday.util.XMLUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.InputStream;
import java.net.URL;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class XmlFileDataSourceTest {

    @Mock
    XMLUtil xmlUtil;

    @InjectMocks
    private XmlFileDataSource xmlFileDataSource;

    @Test
    public void testGetConfiguration() throws Exception {
        final ManagerParameter managerParameter = mock(ManagerParameter.class);
        URL resourceUrl = new URL("http://www.google.de");
        when(managerParameter.createResourceUrl()).thenReturn(resourceUrl);
        xmlFileDataSource.getConfiguration(managerParameter);
        verify(xmlUtil, times(1)).unmarshallConfiguration(any(InputStream.class));
    }



}