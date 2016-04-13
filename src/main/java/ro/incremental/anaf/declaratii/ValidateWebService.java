package ro.incremental.anaf.declaratii;

/**
 * Created by Alex Proca <alex.proca@gmail.com> on 18/03/16.
 */

import com.google.common.base.MoreObjects;
import com.google.common.io.Files;
import dec.Validation;
import org.json.JSONObject;
import pdf.PdfCreation;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.*;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

@Path("/form")
public class ValidateWebService {

    private static final int PERIOADA_RAPORTARE_ERONATA = -4;
    private static final int FISIER_VALID = 0;
    private static final int TIP_DECLARATIE_NECUNOSCUT = -8;
    public static final int UNKNOWN_ERROR = -9;

    private static final Map<String, Class<PdfCreation>> creators = new HashMap<>();
    private static final Map<String, Class<Validation>> validators = new HashMap<>();

    static {
        InputStream packages = Thread.currentThread().getContextClassLoader().getResourceAsStream("packages.txt");
        BufferedReader reader = new BufferedReader(new InputStreamReader(packages));


        String line;

        try {
            while ((line = reader.readLine()) != null) {
                try {

                    creators.put(line, (Class<PdfCreation>) Class.forName(line + ".PdfCreator"));
                    validators.put(line, (Class<Validation>) Class.forName(line + "validator.Validator"));

                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @GET
    @Path("/available")
    @Produces(MediaType.TEXT_PLAIN)
    public String available() {
        return "yes";
    }

    @POST
    @Path("/validate")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public String available(JSONObject input) {

        String declName = input.getString("tagName").replace("declaratie", "d");
        String xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>" + org.json.JSONML.toString(input);

        Result result = generateFromXMLString(xml, declName);

        JSONObject ret = new JSONObject();

        ret.append("message", result.message);
        ret.append("pdfFilePath", result.pdfFilePath);
        ret.append("resultCode", result.resultCode);

        return ret.toString();
    }

    public static class Result {
        public String message;
        public String pdfFilePath;
        public int resultCode;

        public Result(String message, String pdfFilePath, int resultCode) {
            this.message = message;
            this.pdfFilePath = pdfFilePath;
            this.resultCode = resultCode;
        }

        @Override
        public String toString() {
            return MoreObjects.toStringHelper("Result")
                    .add("Message", message)
                    .add("PdfFilePath", pdfFilePath)
                    .add("ResultCode", resultCode)
                    .toString();
        }
    }

    public static Result generateFromXMLString(String xml, String declName) {
        try {
            File xmlFile = File.createTempFile(declName, ".tmp");
            Files.newWriter(xmlFile, Charset.forName("UTF-8")).append(xml).close();

            return generateFromXMLFile(xmlFile.getAbsolutePath(), declName);

        } catch (Throwable e) {
            e.printStackTrace();
            return new Result(e.getMessage(), "", UNKNOWN_ERROR);
        }
    }

    public static Result generateFromXMLFile(String xmlFilePath, String declName) {
        try {

            Validation validator = validators.get(declName).newInstance();
            PdfCreation pdfCreation = creators.get(declName).newInstance();

            StringBuilder finalMessage = new StringBuilder();
            String newLine = System.lineSeparator();

            String errFilePath = xmlFilePath + ".err.txt";
            String pdfFilePath = xmlFilePath + ".pdf";

            int returnCode = validator.parseDocument(xmlFilePath, errFilePath);

            switch (returnCode) {
                case PERIOADA_RAPORTARE_ERONATA:
                    finalMessage.append("Perioada raportare eronata: ").append(declName).append(newLine);
                    break;
                case TIP_DECLARATIE_NECUNOSCUT:
                    finalMessage.append("Tip declaratie necunoscut: ").append(declName).append(newLine);
                    break;
                case FISIER_VALID:
                    finalMessage.append("Validare fara erori fisier: ").append(xmlFilePath).append(newLine);
                    break;
                default:

                    File errFile = new File(errFilePath);
                    StringBuilder errorBuffer = new StringBuilder();
                    if(errFile.exists()) {
                        BufferedReader reader = Files.newReader(errFile, Charset.forName("UTF-8"));
                        String line;
                        while ((line = reader.readLine()) != null) {
                            errorBuffer.append(line).append(newLine);
                        }
                    }

                    if (returnCode > FISIER_VALID) {
                        finalMessage.append("Atentionari la validare fisier: ").append(xmlFilePath).append(newLine);
                        finalMessage.append(errorBuffer);
                        break;
                    }

                    if (returnCode > PERIOADA_RAPORTARE_ERONATA) {
                        finalMessage.append("Erori la validare fisier: ").append(xmlFilePath).append(newLine);
                        finalMessage.append(errorBuffer);
                        break;
                    }

                    finalMessage.append("Erori la validare fisier; cod eroare=").append(Integer.toString(returnCode)).append(newLine);
                    break;
            }

            if (returnCode >= FISIER_VALID) {
                pdfCreation.createPdf(validator.getInfo(), pdfFilePath, xmlFilePath, "");
                return new Result(finalMessage.toString(), pdfFilePath, returnCode);
            }

            return new Result(finalMessage.toString(), "", returnCode);
        } catch (Throwable e) {
            e.printStackTrace();
            return new Result(e.getMessage(), "", UNKNOWN_ERROR);
        }
    }

}
