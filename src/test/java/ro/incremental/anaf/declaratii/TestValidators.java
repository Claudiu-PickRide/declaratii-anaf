package ro.incremental.anaf.declaratii;

import com.google.common.io.Files;
import org.json.JSONML;
import org.json.JSONObject;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 * Created by Alex Proca <alex.proca@gmail.com> on 26/03/16.
 */
public class TestValidators {

    @Test
    public void D106() throws Exception {

        System.out.println(validateXmlDecl("D106"));
    }

    @Test
    public void D390() throws Exception {

        System.out.println(validateXmlDecl("D390"));

    }

    @Test
    public void XML2JsonML() throws Exception {

        System.out.println(xmlToJsonML("D390"));
    }

    private JSONObject xmlToJsonML(String declName) throws IOException {
        File xmlFile = new File("examples", declName + ".xml");
        String fileContent = fileToString(xmlFile);
        return JSONML.toJSONObject(fileContent);
    }

    private Result validateXmlDecl(String declName) throws IOException {

        File xmlFile = new File("examples", declName + ".xml");

        return Result.generateFromXMLString(fileToString(xmlFile), declName);
    }

    private String fileToString(File file) throws IOException {

        StringBuilder xmlBuffer = new StringBuilder();
        if(file.exists()) {
            BufferedReader reader = Files.newReader(file, Charset.forName("UTF-8"));
            String line;
            while ((line = reader.readLine()) != null) {
                xmlBuffer.append(line).append(System.lineSeparator());
            }
        }
        return xmlBuffer.toString();
    }


}
