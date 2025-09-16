package ro.incremental.anaf.declaratii;

import com.google.common.io.Files;
import eu.pickride.utils.FormattingUtils;

import java.io.*;
import java.nio.charset.Charset;

import static ro.incremental.anaf.declaratii.Result.UNKNOWN_ERROR;

/**
 * Created by Claudiu Bele <claudiu.bele@pickride.eu> on 26/08/25.
 */
public class ResultWithDate {
    
    public final int month;
    public final int year;
    public final Result result;
    
    public ResultWithDate(
            Result result,
            int month,
            int year,
            String userId, boolean rectifiedBool) {
        this.result = result;
        this.month = month;
        this.year = year;
    }

    public static ResultWithDate generateFullFromXMLStream(
            InputStream xmlStream,
            String declName,
            String userId,
            boolean rectifiedBool) throws IOException {

        // Convert InputStream to String
        StringBuilder xmlStringBuilder = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(xmlStream, Charset.forName("UTF-8")));
        String line;
        while ((line = reader.readLine()) != null) {
            xmlStringBuilder.append(line).append(System.lineSeparator());
        }
        String xmlString = xmlStringBuilder.toString();
        xmlString = FormattingUtils.replaceRomanianDiacritics(xmlString);
        // Delegate to generateFromXMLString
        return generateFullFromXMLString(xmlString, declName, userId, rectifiedBool);

    }

    public static ResultWithDate generateFullFromXMLString(
            String xml,
            String declName,
            String userId,
            boolean rectifiedBool) {
        try {
            File tempDir = Files.createTempDir();
            File xmlFile = new File(tempDir, declName + ".xml");
            Files.newWriter(xmlFile, Charset.forName("UTF-8")).append(xml).close();

            System.out.println("filePath: " + xmlFile.getAbsolutePath());
            System.out.println("decName: " + declName);

            int luna = 1;
            int an = -1;
            String[] xmlPieces = xml.split(" ");
            for (String xmlPiece : xmlPieces) {
                if (luna != -1 && an != -1) {
                    break;
                }
                if (xmlPiece.startsWith("luna")) {
                    luna = Integer.parseInt(xmlPiece.split("=")[1].replace("\"", ""));
                    System.out.printf("Luna %d", luna);
                }
                if (xmlPiece.startsWith("an")) {
                    an = Integer.parseInt(xmlPiece.split("=")[1].replace("\"", ""));
                    System.out.printf("An %d", an);
                }
            }
            // if we're getting a rectified d100, it must be a d710
            if(rectifiedBool && declName.equals("d100")) {
                declName = "d710";
            }
            return new ResultWithDate(
                    Result.generatePdfFromXMLFile(xmlFile.getAbsolutePath(), declName),
                    luna,
                    an,
                    userId,
                    rectifiedBool
            );

        } catch (Throwable e) {
            e.printStackTrace();
            return new ResultWithDate(
                    new Result(e.getMessage(), UNKNOWN_ERROR),
                    -1,
                    -1,
                    userId, rectifiedBool);
        }
    }
}
