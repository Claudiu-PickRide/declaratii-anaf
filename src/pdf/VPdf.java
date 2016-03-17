/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pdf;

/**
 *
 * @author 22276637
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PRStream;
import com.itextpdf.text.pdf.PdfArray;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfPKCS7;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.XfaForm;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.util.ArrayList;
import java.util.Calendar;

import java.util.Iterator;
import java.util.Set;
import org.w3c.dom.Document;

/**
 *
 * @author Dell
 */
public class VPdf
{
    public String getAn_r()
    {
        return an_r;
    }

    public String getCif()
    {
        return cif;
    }

    public String getD_rec()
    {
        return d_rec;
    }

    public String getLuna_r()
    {
        return luna_r;
    }

    public String getRetAtach()
    {
        return retAtach;
    }

    public String getTotalPlata_A()
    {
        return totalPlata_A;
    }

    public String getUniversalCode()
    {
        return universalCode;
    }
    private static String _newLine = System.getProperty("line.separator");
    private String cif = "";
    private String an_r = "";
    private String luna_r = "";
    private String d_rec = "";
    private String totalPlata_A = "";
    private String universalCode = "";
    private String retSignature = "";

    public String getRetSignature()
    {
        return retSignature;
    }
    private String retAtach = "";

    //functie citire pdf
    public int citirePdf(File pdf)
    {
        int rez = 0;
        PdfReader reader = null;
        try
        {
            reader = new PdfReader(pdf.getPath());
            AcroFields form = reader.getAcroFields();
            cif = adjustString(form.getField("cif"));
            an_r = adjustString(form.getField("an_r"));
            luna_r = adjustString(form.getField("luna_r"));
            d_rec = adjustString(form.getField("d_rec"));
            totalPlata_A = adjustString(form.getField("totalPlata_A"));
            universalCode = adjustString(form.getField("universalCode"));
            if(/*cif.equals("") == true || an_r.equals("") == true
                || luna_r.equals("") == true || d_rec.equals("") == true
                || totalPlata_A.equals("") == true
                || */universalCode.equals("") == true)
            {
                rez = citirePdfXfa(form);
            }
            universalCode = universalCode.substring(0, Math.max(universalCode.indexOf("_"), Math.min(4, universalCode.length())));
        }
        catch(Throwable e)
        {
            // TODO Auto-generated catch block
            return -1;
        }
        return rez;
    }

    public int citirePdfXfa(AcroFields form)
    {
        try
        {
            XfaForm xfa = form.getXfa();
            Document doc = xfa.getDomDocument();
            if(doc != null)
            {
                cif = adjustString(doc.getElementsByTagName("cif").item(0).getFirstChild().getNodeValue());
                an_r = adjustString(doc.getElementsByTagName("an_r").item(0).getFirstChild().getNodeValue());
                luna_r = adjustString(doc.getElementsByTagName("luna_r").item(0).getFirstChild().getNodeValue());
                d_rec = adjustString(doc.getElementsByTagName("d_rec").item(0).getFirstChild().getNodeValue());
                totalPlata_A = adjustString(doc.getElementsByTagName("totalPlata_A").item(0).getFirstChild().getNodeValue());
                //universalCode = Main.adjustString(doc.getElementsByTagName("universalCode").item(0).getFirstChild().getNodeValue());
                try
                {
                    universalCode = adjustString(doc.getElementsByTagName("universalCode").item(0).getFirstChild().getNodeValue());
                }
                catch(Throwable e)
                {
                }
            }
            else
            {
                return -1;
            }
        }
        catch(Throwable e)
        {
            // TODO Auto-generated catch block
            return -1;
        }
        return 0;
    }

    //functie copiere fisier
    public int copyFile(File in, File out)
    {
        try
        {
            FileInputStream pdf_contribuabil = new FileInputStream(in);
            FileOutputStream pdf_copiat = new FileOutputStream(out);
            byte[] buf = new byte[1024];
            int i = 0;
            while((i = pdf_contribuabil.read(buf)) != -1)
            {
                pdf_copiat.write(buf, 0, i);
            }
            pdf_contribuabil.close();
            pdf_copiat.close();
        }
        catch(Throwable ex)
        {
            return -1;
        }
        return 0;
    }

    public int check_Attach(String pdf)
    {
        // String ret = null;
        retAtach = "";
        try
        {
            PdfReader reader = new PdfReader(pdf);
            PdfDictionary root = reader.getCatalog();
            PdfDictionary documentnames = root.getAsDict(PdfName.NAMES);
            PdfDictionary embeddedfiles = documentnames.getAsDict(PdfName.EMBEDDEDFILES);
            PdfArray filespecs = embeddedfiles.getAsArray(PdfName.NAMES);
            PdfDictionary filespec;
            PdfDictionary refs;
            FileOutputStream fos = null;
            PRStream stream;
            int cntXML = 0;
            int cntZIP = 0;
            for(int i = 0; i < filespecs.size();)
            {
                filespecs.getAsName(i++);
                filespec = filespecs.getAsDict(i++);
                refs = filespec.getAsDict(PdfName.EF);
                java.util.Set keys = refs.getKeys();

                Iterator iter = keys.iterator();
                String name = "";
                while(iter.hasNext())
                {
//                    System.out.println("Iterator SET");
                    PdfName key1 = (PdfName)iter.next();
                    name = filespec.getAsString(key1).toString().toLowerCase();
                }
                if(name.endsWith(".xml") == true)
                {
                    cntXML++;
                }
                else if(name.endsWith(".zip") == true)
                {
                    cntZIP++;
                }
            }
            if(cntXML == 0)
            {
                retAtach = "lipsa XML";
                return -1;
            }
            else if(cntXML > 1)
            {
                retAtach = "XML multiplu";
                return 1;
            }
            else if(cntZIP > 1)
            {
                retAtach = "ZIP multiplu";
                return 1;
            }
            else
            {
                retAtach = "Corect";
                return 0;
            }
        }
        catch(Throwable e)
        {
            retAtach = "Lipsa atasamente";
            return -1;
        }

    }

    public static String padPath(String path)
    {
        if(path.endsWith("/") == false && path.endsWith("\\") == false)
        {
            return path + File.separator;
        }
        return path;
    }

    public static String adjustString(String par)
    {
        return (par == null) ? "" : par;
    }

    public static String extractFile(String pdfName, String extensie, String fileName)
    {
        PdfReader reader = null;
        try
        {
            extensie = ".".concat(extensie).toLowerCase();
            reader = new PdfReader(pdfName);
            PdfDictionary root = reader.getCatalog();
            PdfDictionary documentnames = root.getAsDict(PdfName.NAMES);
            PdfDictionary embeddedfiles = documentnames.getAsDict(PdfName.EMBEDDEDFILES);
            PdfArray filespecs = embeddedfiles.getAsArray(PdfName.NAMES);
            PdfDictionary filespec;
            PdfDictionary refs;
            FileOutputStream fos;
            PRStream stream;
            for(int i = 0; i < filespecs.size();)
            {
                filespecs.getAsString(i++);
                filespec = filespecs.getAsDict(i++);
                refs = filespec.getAsDict(PdfName.EF);
                Set keys = refs.getKeys();
                Iterator iter = keys.iterator();
                while(iter.hasNext())
                {
                    PdfName key1 = (PdfName)iter.next();
                    String name = filespec.getAsString(key1).toString().toLowerCase();
                    if(name.endsWith(extensie) == true)
                    {
                        fos = new FileOutputStream(fileName);
                        stream = (PRStream)PdfReader.getPdfObject(
                            refs.getAsIndirectObject(key1));
                        fos.write(PdfReader.getStreamBytes(stream));
                        fos.flush();
                        fos.close();
                        return null;
                    }
                }
            }
        }
        catch(Throwable e)
        {
//            _error = e.getMessage();
            return "eroare extragere fisier" + extensie.toUpperCase() + ": " + e.toString();
        }
        if(!extensie.equals(".zip"))
        {
            return "lipsa fisier" + extensie.toUpperCase();
        }
        else
        {
            return "fara";
        }
    }

    public int verifySignature(String fileName)
    {
        int returns = 0;
        retSignature = "";
        try
        {
            KeyStore kall = PdfPKCS7.loadCacertsKeyStore();

            PdfReader reader = new PdfReader(fileName);
            AcroFields af = reader.getAcroFields();

            //Cautarea semnaturii
            ArrayList names = af.getSignatureNames();
            if(names.isEmpty())
            {
                retSignature = "Pdf-ul nu este semnat";
                returns = 1;
            }
            //Pentru fiecare semnatura:
            for(int k = 0; k < names.size(); ++k)
            {
                String name = (String)names.get(k);

                if(!af.signatureCoversWholeDocument(name))
                {
                    if(retSignature.length() > 0)
                    {
                        retSignature += _newLine;
                    }
                    retSignature += " Semnatura " + name + " nu acopera tot documentul";
                    returns = -1;
                }

                PdfPKCS7 pk = af.verifySignature(name);
                Calendar cal = pk.getSignDate();
                Certificate pkc[] = pk.getCertificates();

                if(!pk.verify())
                {
                    if(retSignature.length() > 0)
                    {
                        retSignature += _newLine;
                    }
                    retSignature += " Document modificat dupa semnare";
                    returns = -1;
                }
                // Verficare certificat - keystore
//                Object fails[] = PdfPKCS7.verifyCertificates(pkc, kall, null, cal);
//                if (fails != null) {
//                    retSignature += "Eroare certificat: " + fails[1] + _newLine;
//                    returns = -1;
//                }
            }
        }
        catch(Throwable e)
        {
            retSignature += " Eroare verificare semnatura: " + e.toString();
            return -1;
        }
        return returns;
    }
}
