package de.jollyday.tests;

import org.junit.Before;
import org.junit.Test;
import org.xmlunit.validation.Languages;
import org.xmlunit.validation.ValidationResult;
import org.xmlunit.validation.Validator;

import javax.xml.transform.stream.StreamSource;
import java.io.File;

import static org.junit.Assert.assertTrue;

/**
 * @author sdiedrichsen
 * @version $Id$
 * @since 16.03.17
 */
public class XMLValidationTest {

    private File schemaFile;

    @Before
    public void setUp() throws Exception {
        schemaFile = new File("src/main/xsd/Holiday.xsd");
    }

    @Test
    public void testSchemaIsValid() throws Exception {
        final Validator validator = Validator.forLanguage(Languages.W3C_XML_SCHEMA_NS_URI);
        validator.setSchemaSource(new StreamSource(schemaFile));
        final ValidationResult validationResult = validator.validateSchema();
        assertTrue("Schema '"+ schemaFile +"' is not valid: "+validationResult.getProblems(), validationResult.isValid());
    }

}
