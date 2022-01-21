package de.jollyday.tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.xmlunit.validation.Languages;
import org.xmlunit.validation.ValidationResult;
import org.xmlunit.validation.Validator;

import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class XMLValidationTest {

    private File schemaFile;
    private Validator validator;

    @BeforeEach
    public void setUp() throws Exception {
        schemaFile = new File("src/main/xsd/Holiday.xsd");
        validator = Validator.forLanguage(Languages.W3C_XML_SCHEMA_NS_URI);
        validator.setSchemaSource(new StreamSource(schemaFile));
    }

    @Test
    public void testSchemaIsValid() throws Exception {
        assertTrue(schemaFile.exists(), "Schema file "+schemaFile+" does not exist.");
        final ValidationResult validationResult = validator.validateSchema();
        assertTrue(validationResult.isValid(), "Schema '"+ schemaFile +"' is not valid: "+validationResult.getProblems());
    }

    @Test
    public void testHolidayFilesAreValid()throws Exception {
        final Path holidaysFolder = Paths.get("src/main/resources/holidays/");
        Files.list(holidaysFolder).forEach(this::validateHolidayFile);
    }

    private void validateHolidayFile(Path path){

        try {
            final ValidationResult validationResult = validator.validateInstance(new StreamSource(new FileInputStream(path.toFile())));
            assertTrue(validationResult.isValid(), "Validation of holiday file '"+path+" failed: "+validationResult.getProblems());
        } catch (FileNotFoundException e) {
            throw new IllegalStateException("Cannot validate holiday file "+path);
        }
    }
}
