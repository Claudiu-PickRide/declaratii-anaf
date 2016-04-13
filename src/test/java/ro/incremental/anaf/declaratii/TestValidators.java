package ro.incremental.anaf.declaratii;

import com.google.common.io.Files;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.nio.charset.Charset;

import static org.junit.Assert.assertTrue;

/**
 * Created by Alex Proca <alex.proca@gmail.com> on 26/03/16.
 */
public class TestValidators {

    @Test
    public void testValidators() throws Exception {

        String declName = "d106";

        File errFile = new File("examples/declaratie106.xml");
        StringBuilder xmlBuffer = new StringBuilder();
        if(errFile.exists()) {
            BufferedReader reader = Files.newReader(errFile, Charset.forName("UTF-8"));
            String line;
            while ((line = reader.readLine()) != null) {
                xmlBuffer.append(line).append(System.lineSeparator());
            }
        }


        Result result = ValidateWebService.generateFromXMLString(xmlBuffer.toString(), declName);

        System.out.println(result.toString());
    }


}
