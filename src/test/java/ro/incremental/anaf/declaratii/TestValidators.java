package ro.incremental.anaf.declaratii;

import dec.Info;
import dec.LogTrace;
import dec.Validation;
import org.junit.Test;
import org.reflections.Reflections;
import pdf.PdfCreation;
import pdf.PdfCreatorRoot;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertTrue;

/**
 * Created by Alex Proca <alex.proca@gmail.com> on 26/03/16.
 */
public class TestValidators {

    @Test
    public void testValidators() throws Exception {

        InputStream packages = Thread.currentThread().getContextClassLoader().getResourceAsStream("packages.txt");
        BufferedReader reader = new BufferedReader(new InputStreamReader(packages));

        Map<String, Class<PdfCreation>> creators = new HashMap<>();
        Map<String, Class<Validation>> validators = new HashMap<>();

        String line;
        while((line = reader.readLine()) != null) {
            creators.put(line, (Class<PdfCreation>)Class.forName(line + ".PdfCreator"));
            validators.put(line, (Class<Validation>)Class.forName(line + "validator.Validator"));
        }

//        for(String key: creators.keySet()) {
//            System.out.println(key + "->" + creators.get(key).toString());
//            System.out.println(key + "->" + validators.get(key).toString());
//            PdfCreatorRoot creator = (PdfCreatorRoot) creators.get(key);
//        }

        String declName = "d106";

//        boolean containsDec = false;
//        for(Package p : Package.getPackages()) {
//
//            containsDec |= p.getName().contains(declName);
//
//        }

        Validation validation = validators.get(declName).newInstance();
        PdfCreation pdfCreation = creators.get(declName).newInstance();

        int code = parseDocumentXML("./examples/declaratie106.xml", validation, pdfCreation, declName.toUpperCase());
        System.out.println(code);

//        assertTrue("Anaf libraries are in test classpath", containsDec);

    }

    private int parseDocumentXML(String xmlFile, Validation _validator, PdfCreation pdfCreation, String _declType)
    {
        int returns;
        try
        {

            String errFile = xmlFile + ".err.txt";
            StringBuffer _finalMessage = new StringBuffer();
            String _newLine = System.lineSeparator();
            Info _validationInfo;

            returns = _validator.parseDocument(xmlFile, errFile);

            if(returns < 0)
            {
                if(returns > -4)
                {
                    _finalMessage.append("Erori la validare fisier: ").append(xmlFile).append(_newLine);
                    _finalMessage.append("       Erorile au fost scrise in fisierul: ").append(errFile).append(_newLine);
                }
                else if(returns == -4)
                {
                    _finalMessage.append("Perioada raportare eronata: ").append(_declType).append(_newLine);
                }
                else if(returns == -8)
                {
                    _finalMessage.append("Tip declaratie necunoscut: ").append(_declType).append(_newLine);
                }
                else
                {
                    _finalMessage.append("Erori la validare fisier; cod eroare=").append(Integer.toString(returns)).append(_newLine);
                }
            }
            else if(returns > 0)
            {
                _finalMessage.append("Atentionari la validare fisier: ").append(xmlFile).append(_newLine);
                _finalMessage.append("       Atentionarile au fost scrise in fisierul: ").append(errFile).append(_newLine);
            }
            else
            {
                _finalMessage.append("Validare fara erori fisier: ").append(xmlFile).append(_newLine);
            }

            if(returns >= 0)
            {
                _validationInfo = _validator.getInfo();

                pdfCreation.createPdf(_validationInfo, xmlFile+".pdf", xmlFile, "");
            }
            else
            {
                _validationInfo = null;
            }
            System.out.println(_finalMessage);

            return returns;
        }
        catch(Throwable e)
        {
            return -9;
        }
    }



//    private int validateXML(String fileName)
//    {
//        int returns;
//        try
//        {
//            returns = _integrator.parseDocument(fileName, null);
//            txtResults.append(_integrator.getFinalMessage());
//            return returns;
//        }
//        catch(Throwable e)
//        {
//            txtResults.append("Eroare de deployment!" + _newLine);
//            txtResults.append("       mesaj: " + e.toString() + _newLine);
//            return -2;
//        }
//    }
//
//    private int pdfCreation(String fileName)
//    {
//        int ret = 0;
//        try
//        {
//            ret = _integrator.pdfCreation(fileName, null, null, null);
//            txtResults.append(_integrator.getFinalMessage());
//            return ret;
//        }
//        catch(Throwable e)
//        {
//            txtResults.append("Eroare de deployment!" + _newLine);
//            txtResults.append("       mesaj: " + e.toString() + _newLine);
//            return -2;
//        }
//    }
}
