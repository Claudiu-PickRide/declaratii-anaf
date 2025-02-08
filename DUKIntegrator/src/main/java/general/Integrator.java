/*
This file is part of DUKIntegrator.

DUKIntegrator is free software: you can redistribute it and/or modify
it under the terms of the GNU Affero General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

DUKIntegrator is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.See the
GNU Affero General Public License for more details.

You should have received a copy of the GNU Affero General Public License
along with DUKIntegrator.  If not, see <http://www.gnu.org/licenses/>.
 */
package general;

import dec.Info;
import dec.LogTrace;
import dec.Options;
import java.io.File;
import java.util.List;
import pdf.PdfSuperCreator;
import pdf.Sign;
import pdf.Sign.CertAlias;
import pdf.VPdf;
import ui.CertificateChooser;
import validator.Validator;

public class Integrator
{
    public static String _newLine = System.getProperty("line.separator");
    public static final long MAX_ZIP_LENGTH = 10000000;
    private Validator _validator = null;
    private PdfSuperCreator _pdf = null;
    private String _declType = null;
    private Info _validationInfo = null;
    private String _configPath = null;
    private String _errMessage = null;
    private StringBuilder _finalMessage = new StringBuilder(2048);
    private Sign _sign = null;
    private CertificateChooser _chooser = null;
    private int _chooserIndex = 0;
    private int _cnt = 0;
    private VPdf _vPdf = null;
    private int _inputType = 0;
    private String _extractedXmlFile = null;
    private String _extractedZipFile = null;

    //constructor
    public Integrator()
    {
        _validator = new Validator();
        _pdf = new PdfSuperCreator();
        _vPdf = new VPdf();
    }
    
    //inainte de disponibilizarea obiectului Validator.
    //nu mai trebuie sa faceti semnari cu acest obiect
    //  dupa apelul releaseToken()
    //previne blocarea la semnaturile ulterioare: pobabil un bug driver
    public void releaseToken()
    {
        if(_sign != null)
        {
            _sign.releaseToken();
        }
    }

    //intoarce obiectul Validator instantiat in constructor
    public Validator getValidator()
    {
        return _validator;
    }

    //intoarce obiectul PdfSuperCreator instantiat in constructor
    public PdfSuperCreator getPdfSuperCreator()
    {
        return _pdf;
    }

    //functie necesara pt. UI pt. a obtine ultima versiune
    //  disponibila pt. un tip de declaratie
    //intermediar intre obiectul Validator interior si clasele
    //  care folosesc obiectul Integrator
    public String getLastVersion()
    {
        return _validator.getLastVersion(_declType);
    }

    //functie necesara pt. UI pt. a obtine ultima versiune
    //  disponibila a PDF-ului pt. un tip de declaratie
    //intermediar intre obiectul _pdf interior si clasele
    //  care folosesc obiectul Integrator
    //poate folosita numai dupa un apel setDeclType()
    public String getPdfLastVersion()
    {
        return _pdf.getLastVersion(_declType);
    }

    //functie necesara pt. UI pt. a obtine optiunea de
    //  atasare zip la PDF pt. un tip de declaratie:
    //  < -2 eroare
    //  -2 necunoscut
    //  -1 daca nu se accepta zip atasat
    //  0 daca accepta zip atasat, optional
    //  1 daca este obligatorie atasarea unui zip
    //intermediar intre obiectul _pdf interior si clasele
    //  care folosesc obiectul Integrator
    //poate folosita numai dupa un apel setDeclType()
    public int getPdfZipOption()
    {
        return _pdf.getZipOption(_declType);
    }

    //functie necesara pt. UI pt. a obtine lista de optiuni
    //  disponibile pt. un tip de declaratie
    //intermediar intre obiectul Validator interior si clasele
    //  care folosesc obiectul Integrator
    public Options[] getOptions()
    {
        return _validator.getOptions(_declType);
    }

    //intoarce mesajele de eroare/atentionare/reusita dupa
    //  un apel parseDocument()/pdfCreation()/signPdf()
    public String getFinalMessage()
    {
        return _finalMessage.toString();
    }

    public String getDeclType()
    {
        return _declType;
    }

    //defineste tipul declaratiei (ex: setDeclType("D112") inaintea
    //  unui apel parseDocument()/pdfCreation()/signPdf()
    //Obligatoriu cel putin un apel pe obiect Integrator
    public void setDeclType(String _declType)
    {
        this._declType = _declType;
    }

    //defineste optiunea curenta folosita la validarea declaratiei
    //  (prin lipsa se foloseste optiunea=0); trebuie apelata
    //  inaintea unui apel parseDocument()/pdfCreation()/signPdf()
    public void useOptions(long option)
    {
        _validator.useOptions(_declType, option);
    }

    //valideaza fisierul XML folosind versiunea data de perioada din el insusi
    //intoarce:
    //-9 - exceptie neasteptata (nu ar trebui sa apara)
    //-8 - declaratie necunoscuta
    //-7 - exceptie InstantiationException sau IllegalAccessException (nu ar trebui sa apara)
    //-6 - exceptie ClassNotFoundException (nu ar trebui sa apara)
    //-5 - exceptie neasteptata (nu ar trebui sa apara)
    //-3 - validare cu erori tehnice
    //-2 - validare cu erori de structura
    //-1 - validare cu erori de business
    // 0 - validare fara erori/avertismente
    // 1 - validare cu avertismente dar fara erori
    //xmlFile indica numele fisierului xml sau numele
    //  fisierului pdf. Daca extensia este pdf se incearca validarea
    //  ca fisier pdf. altfel, indiferent de extensie, se considera
    //  a fi un fisier xml
    //errFile indica numele complet al fisierului de erori sau poate fi
    //  null, sir vid sau '$'; in aceste cazuri numele fisierului de erori
    //  va fi xmlFile + ".err.txt"
    public int parseDocument(String xmlFile, String errFile)
    {
        int ret = parseDocumentIntern(xmlFile, errFile);
        if(_inputType != 0 && _extractedXmlFile != null)
        {
            (new File(_extractedXmlFile)).delete();
            _extractedXmlFile = null;
        }
        return ret;
    }

    //verificare fisier PDF - structura, atasamente, semnatura
    private int verificarePdf(String pdfFile)
    {
        int ret = 0, general = 0, returns = 0;
        ;
        ret = _vPdf.citirePdf(new File(pdfFile));
        if(ret != 0)
        {
            _finalMessage.append("Eroare structura PDF").append(_newLine);
            general = -1;
        }
        ret = _vPdf.check_Attach(pdfFile);
        if(ret != 0)
        {
            _finalMessage.append("Eroare atasament PDF: ").append(_vPdf.getRetAtach()).append(_newLine);
            general = -1;
        }
        if(ret < 0)
        {
            returns = -1;
        }
        ret = _vPdf.verifySignature(pdfFile);
        if(ret == -1)
        {
            _finalMessage.append("Eroare semnatura PDF: ").append(_vPdf.getRetSignature()).append(_newLine);
            general = -1;
        }
        if(ret == 1)
        {
            _finalMessage.append("Atentionare semnatura PDF: ").append(_vPdf.getRetSignature()).append(". ").append("Documentul nu poate fi transmis prin internet").append(_newLine);
        }
        if(general >= 0)
        {
            _finalMessage.append("Validare fara erori fisier PDF ").append(_newLine);
        }
        return returns;
    }

    //valideaza xmlFile distingand intre cazul parsarii unui xml sau a unui pdf.
    //  In ultimul caz valideaza pdf-ul, extrage xml-ul si il valideaza.
    private int parseDocumentIntern(String xmlFile, String errFile)
    {
        int ret = 0, retValidare = -2;
        String raspuns = null;

        _cnt++;
        _finalMessage.setLength(0);
        _finalMessage.append(_cnt).append(".").append(_newLine);
        if(xmlFile.toLowerCase().endsWith(".pdf") == false)
        {
            _inputType = 0;
            return parseDocumentXML(xmlFile, errFile);
        }
        _inputType = 1;
        _extractedXmlFile = null;
        retValidare = -2;
        _finalMessage.append("     a. Verificare PDF: ").append(xmlFile).append(_newLine);
        ret = verificarePdf(xmlFile);
        if(ret >= 0)
        {
            _extractedXmlFile = xmlFile + ".dukintegrator.xml";
            raspuns = VPdf.extractFile(xmlFile, "xml", _extractedXmlFile);
            if(raspuns == null)
            {
                _finalMessage.append("     b. Validare XML:").append(_newLine);
                if(errFile == null || errFile.equals("") == true
                    || errFile.equals("$") == true)
                {
                    errFile = xmlFile + ".err.txt";
                }
                retValidare = parseDocumentXML(_extractedXmlFile, errFile);
            }
            else
            {
                _extractedXmlFile = null;
                _finalMessage.append(raspuns).append(_newLine);
            }
        }
        if(retValidare >= 0)
        {
            _finalMessage.append("     c. Verificare PDF vs. XML:").append(_newLine);
            StringBuilder s = new StringBuilder();
            if(!_validationInfo._an.equals(_vPdf.getAn_r()))
            {
                s.append("an");
            }
            if(!_validationInfo._luna.equals(_vPdf.getLuna_r()))
            {
                if(s.length() > 0)
                {
                    s.append(", ");
                }
                s.append("luna");
            }
            if(!_validationInfo._cif.equals(_vPdf.getCif()))
            {
                if(s.length() > 0)
                {
                    s.append(", ");
                }
                s.append("CUI");
            }
            if(!_validationInfo._sumaControl.equals(_vPdf.getTotalPlata_A()))
            {
                if(s.length() > 0)
                {
                    s.append(", ");
                }
                s.append("sumaControl");
            }
            if(_validationInfo._rec != null)
            {
                if(!_validationInfo._rec.equals(_vPdf.getD_rec().equals(" ")
                    ? "0" : (_vPdf.getD_rec().equals("X") ? "1" : _vPdf.getD_rec())))
                {
                    if(s.length() > 0)
                    {
                        s.append(", ");
                    }
                    s.append("tipDeclaratie");
                }
            }
            if(s.length() > 0)
            {
                _finalMessage.append("Erori verificare valori PDF cu valori XML: ").append("Exista diferente la urmatoarele campuri :").append(s.toString()).append(_newLine);
            }
            if(_vPdf.getUniversalCode().equals(_declType) == false)
            {
                _finalMessage.append("Tipul declaratiei (" + _declType
                    + ") nu corespunde cu cel din PDF (" + _vPdf.getUniversalCode() + ")" + _newLine);
            }
            else
            {
                if(s.length() == 0)
                {
//                    _finalMessage.append("Validare fara erori comparare valori fisier PDF cu valori fisier XML: ").append(_newLine);
                    _finalMessage.append("Valorile metadatelor din PDF corespund cu valorile din XML").append(_newLine);
                }
            }
        }
        return retValidare;
    }

    //parsare fisier xml
    private int parseDocumentXML(String xmlFile, String errFile)
    {
        int returns;
        try
        {
            if(errFile == null || errFile.equals("") == true
                || errFile.equals("$") == true)
            {
                errFile = xmlFile + ".err.txt";
            }
            LogTrace.init(xmlFile + ".log", xmlFile + ".trc", 0, 100, true);
            returns = _validator.parseDocument(_declType, xmlFile, errFile);
            LogTrace.close();
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
            }
            else
            {
                _validationInfo = null;
            }
            return returns;
        }
        catch(Throwable e)
        {
            return -9;
        }
    }

    private void errPdf(String err)
    {
        _finalMessage.append("Eroare creare PDF!").append(_newLine);
        _finalMessage.append("       ").append(err).append(_newLine);
    }

    //valideaza fisierul XML folosind versiunea data de perioada din el insusi
    //  si creaza capacul PDF cu informatiile specifice din XML
    //intoarce:
    //-100 - eroare de creare PDF
    //0 - PDF creat cu succes
    //daca validarea fisierului esueaza pot aparea erorile:
    //-9 - exceptie neasteptata (nu ar trebui sa apara)
    //-8 - declaratie necunoscuta
    //-7 - exceptie InstantiationException sau IllegalAccessException (nu ar trebui sa apara)
    //-6 - exceptie ClassNotFoundException (nu ar trebui sa apara)
    //-5 - exceptie neasteptata (nu ar trebui sa apara)
    //-3 - validare cu erori tehnice
    //-2 - validare cu erori de structura
    //-1 - validare cu erori de business
    //xmlFile indica numele fisierului xml sau numele
    //  fisierului pdf. Daca extensia este pdf se incearca validarea
    //  ca fisier pdf. altfel, indiferent de extensie, se considera
    //  a fi un fisier xml
    //errFile indica numele complet al fisierului de erori sau poate fi
    //  null, sir vid sau '$'; in aceste cazuri numele fisierului de erori
    //  va fi xmlFile + ".err.txt"
    //zipFile indica numele complet al fizierului zip de atasat la PDF.
    //  daca xmlFile este un fisier pdf si daca declaratia accepta zip
    //  se incearca recuperarea lui din fisierul pdf (attachment); daca nu
    //  se gaseste nimic aici sau daca xmlFile este un fisier xml atunci:
    //  daca este '$' fisierul deriva din xmlFile cu schimbarea extensiei
    //  daca este sirul vid nu se ataseaza numic (zip optional)
    //  daca este null se incearca obtinerea unui fisier prin interfata
    //      CertificateChooser.chooseZipFile()
    //pdfFile indica numele complet al fisierului PDF sau poate fi
    //  null, sir vid sau '$'; in aceste cazuri numele fisierului PDF
    //  va fi xmlFile (fara extensia ".xml") + ".pdf"
    //daca xmlFile este un fisier pdf numele fisierelor xml si zip
    //  atasate vor fi <xmlFile>.dukintegrator.xml/zip. La fel, numele
    //  fisierului pdf creat va fi, prin lipsa, <xmlFile>.dukintegrator.pdf
    public int pdfCreation(String xmlFile, String errFile,
        String zipFile, String pdfFile)
    {
        int ret = pdfCreationIntern(xmlFile, errFile, zipFile, pdfFile);
        if(_inputType != 0)
        {
            if(_extractedXmlFile != null)
            {
                (new File(_extractedXmlFile)).delete();
                _extractedXmlFile = null;
            }
            if(_extractedZipFile != null)
            {
                (new File(_extractedZipFile)).delete();
                _extractedZipFile = null;
            }
        }
        return ret;
    }

    //creare fisier pdf pornind de la un xml sau de la un pdf
    private int pdfCreationIntern(String xmlFile, String errFile,
        String zipFile, String pdfFile)
    {
        int ret = 0;
        String rez = null;
        int zipOption = -2;
        CertificateChooser ch = _chooser;

        _extractedZipFile = null;
        ret = parseDocumentIntern(xmlFile, errFile);
        if(ret < 0)
        {
            return ret;
        }
//        if(_pdf == null)
//        {
//            _finalMessage.append("eroare creare obiect PDF: ").append(_errMessage).append(_newLine);
//            return -10;
//        }
        zipOption = _pdf.getZipOption(_declType, _validationInfo);
        if(ch == null)
        {
            ch = new CertificateChooserImpl(_chooserIndex);
        }
        if(pdfFile == null || pdfFile.equals("") == true
            || pdfFile.equals("$") == true)
        {
            if(_inputType == 0)
            {
                if(xmlFile.toLowerCase().endsWith(".xml") == true)
                {
                    pdfFile = xmlFile.substring(0, xmlFile.length() - 4) + ".pdf";
                }
                else
                {
                    pdfFile = xmlFile + ".pdf";
                }
            }
            else
            {
                pdfFile = xmlFile + ".dukintegrator.pdf";
            }
        }
        if(zipFile != null && zipFile.equals("") == false
            && zipFile.equals("$") == false && zipOption == -1)
        {
            errPdf("fisier zip specificat dar nepermis in acest document");
            return -100;
        }
        if(zipFile == null || zipFile.equals("") == true
            || zipFile.equals("$") == true)
        {
            if(_inputType != 0 && zipOption >= 0)
            {
                _extractedZipFile = xmlFile + ".dukintegrator.zip";
                rez = VPdf.extractFile(xmlFile, "zip", _extractedZipFile);
                if(rez == null)
                {
                    zipFile = _extractedZipFile;
                    _finalMessage.append("Se va folosi fisierul ZIP atasat").append(_newLine);
                }
                else
                {
                    _extractedZipFile = null;
                    _finalMessage.append("Nu exista fisier ZIP atasat").append(_newLine);
                }
            }
        }
        if(zipFile == null)
        {
            zipFile = ch.chooseZipFile(xmlFile, zipOption);
        }
        else if(zipFile.equals("$") == true)
        {
            if(xmlFile.toLowerCase().endsWith(".xml") == true)
            {
                zipFile = xmlFile.substring(0, xmlFile.length() - 4) + ".zip";
            }
            else
            {
                zipFile = xmlFile + ".zip";
            }
            if((new File(zipFile)).exists() == false)
            {
                zipFile = null;
            }
        }
        else if(zipFile.equals("") == true)
        {
            if(zipOption > 0)
            {
                errPdf("zip obligatoriu dar nespecificat");
                return -100;
            }
        }
        else
        {
            if((new File(zipFile)).exists() == false)
            {
                zipFile = null;
            }
        }
        if(zipFile == null)
        {
            errPdf("fisier zip negasit");
            return -100;
        }
        //nu se admit fisiere ZIP mai mari decat MAX_ZIP_LENGTH bytes
        if(zipFile.equals("") == false)
        {
            if((new File(zipFile)).length() > MAX_ZIP_LENGTH)
            {
                errPdf("fisier ZIP mai lung de "
                    + Long.toString(MAX_ZIP_LENGTH) + " bytes");
                return -100;
            }
        }
        String xmlFileFinal = xmlFile;
        if(_inputType != 0)
        {
            xmlFileFinal = _extractedXmlFile;
        }
        String zipFileFinal = zipFile;
        File file = null;
        File fileSou = null;
        if(xmlFileFinal.toLowerCase().endsWith(".xml") == false)
        {
            xmlFileFinal = xmlFileFinal + "_redenumit.xml";
            file = new File(xmlFileFinal);
            if(file.exists() == true)
            {
                errPdf("folositi un fisier cu extensia .xml");
                return -100;
            }
            else
            {
                fileSou = new File(xmlFile);
                if(fileSou.renameTo(file) == false)
                {
                    errPdf("esec redenumire fisier; folositi un fisier cu extensia .xml");
                    return -100;
                }
            }
        }
        if(zipFileFinal != null && zipFileFinal.equals("") == false
            && zipFileFinal.toLowerCase().endsWith(".zip") == false)
        {
            zipFileFinal = zipFileFinal + "_redenumit.zip";
            file = new File(zipFileFinal);
            if(file.exists() == true)
            {
                errPdf("folositi un fisier cu extensia .zip");
                return -100;
            }
            else
            {
                fileSou = new File(zipFile);
                if(fileSou.renameTo(file) == false)
                {
                    errPdf("esec redenumire fisier; folositi un fisier cu extensia .zip");
                    return -100;
                }
            }
        }
        rez = _pdf.createPdf(_declType, _validationInfo, pdfFile, xmlFileFinal, zipFileFinal);
        if(xmlFile.equals(xmlFileFinal) == false && _inputType == 0)
        {
            fileSou = new File(xmlFileFinal);
            file = new File(xmlFile);
            if(fileSou.renameTo(file) == false)
            {
                xmlFileFinal = "fisierul " + xmlFile + " a fost redenumit "
                    + xmlFileFinal + "; nu am reusit redenumirea inversa";
            }
            else
            {
                xmlFileFinal = null;
            }
        }
        else
        {
            xmlFileFinal = null;
        }
        if(zipFile.equals(zipFileFinal) == false)
        {
            fileSou = new File(zipFileFinal);
            file = new File(zipFile);
            if(fileSou.renameTo(file) == false)
            {
                zipFileFinal = "fisierul " + zipFile + " a fost redenumit "
                    + zipFileFinal + "; nu am reusit redenumirea inversa";
            }
            else
            {
                zipFileFinal = null;
            }
        }
        else
        {
            zipFileFinal = null;
        }
        if(rez != null && rez.equals("") == false)
        {
            errPdf(rez);
            if(xmlFileFinal != null)
            {
                _finalMessage.append("       ").append(xmlFileFinal).append(_newLine);
            }
            if(zipFileFinal != null)
            {
                _finalMessage.append("       ").append(zipFileFinal).append(_newLine);
            }
            return -100;
        }
        else
        {
            //nu se admit fisiere PDF mai mari decat MAX_ZIP_LENGTH bytes
//            file = new File(pdfFile);
//            if(file.length() > MAX_ZIP_LENGTH)
//            {
//                errPdf("fisier PDF mai lung de "
//                    + Long.toString(MAX_ZIP_LENGTH) + " bytes");
//                file.delete();
//                return -100;
//            }
            rez = "";
            if(zipOption >= 0)
            {
                if(zipFile.equals("") == false)
                {
                    rez = " (cu";
                }
                else
                {
                    rez = " (fara";
                }
                rez += " fisier zip atasat)";
            }
            _finalMessage.append("Fisierul PDF a fost creat cu succes"
                + rez + ":").append(_newLine);
            _finalMessage.append("       ").append(pdfFile).append(_newLine);
            if(xmlFileFinal != null)
            {
                _finalMessage.append("       ").append(xmlFileFinal).append(_newLine);
            }
            if(zipFileFinal != null)
            {
                _finalMessage.append("       ").append(zipFileFinal).append(_newLine);
            }
            return 0;
        }
    }

    //functia poate fi apelata inainte de apelul signPdf() sau pdfCreation()
    //  de catre o aplicatie care semneaza folosind un smartCard
    //  ce poate contine mai multe certificate, sau creaza/semneaza PDF-uri
    //  care au zip atasat.
    //furnizeaza un obiect care implementeaza interfata CertificateChooser
    //metoda chooseCertificate() a interfetei ofera dezvoltatorilor
    //  prilejul alegerii dupa criteriile dorite a certificatului cu
    //  care se face semnarea
    //metoda chooseZipFile() permite alegerea unui fisier zip care
    //  trebuie atasat PDF-ului.
    public void setChooser(CertificateChooser _chooser)
    {
        this._chooser = _chooser;
    }

    //functia este o alternativa mai simpla la functia setChooser();
    //  indica doar indexul certificatului cu care se va semna
    //  de pe un smartCard cu mai multe certificate (primul index este 0)
    //se apeleaza inaintea apelului signPdf()
    public void setChooserIndex(int _chooserIndex)
    {
        this._chooserIndex = _chooserIndex;
    }

    public String getConfigPath()
    {
        return _configPath;
    }

    //seteaza calea spre folderul config, unde se gasesc fisierele
    //  smartCard.cfg folosite de algoritmul de semnare digitala
    //desi este o functie de initializare a obiectului Integrator
    //  apelul este necesar numai in cazul in care se va apela
    //  metoda signPdf() a obiectului
    public void setConfigPath(String _configPath)
    {
        this._configPath = _configPath;
    }

    //daca un obiect Integrator se foloseste pt. semnarea mai multor
    //  documente acest apel este necesar la schimbarea smartCard-ului,
    //  inainte de semnarea cu noul smartCard (in caz contrar semnarea
    //  se va face cu vechiul smartCard, chiar daca acesta a fost, fizic,
    //  inlocuit cu un altul)
    public void setNoCertificate()
    {
        if(_sign != null)
        {
            _sign.setNoCertificate();
        }
    }

    //valideaza fisierul XML folosind versiunea data de perioada din el insusi,
    //  creaza capacul PDF cu informatiile specifice din XML si
    //  semneaza fisierul PDF format
    //intoarce:
    //-200 - eroare semnare PDF
    //-100 - eroare de creare PDF
    //0 - PDF semnat cu succes
    //daca validarea fisierului esueaza pot aparea erorile:
    //-9 - exceptie neasteptata (nu ar trebui sa apara)
    //-8 - declaratie necunoscuta
    //-7 - exceptie InstantiationException sau IllegalAccessException (nu ar trebui sa apara)
    //-6 - exceptie ClassNotFoundException (nu ar trebui sa apara)
    //-5 - exceptie neasteptata (nu ar trebui sa apara)
    //-3 - validare cu erori tehnice
    //-2 - validare cu erori de structura
    //-1 - validare cu erori de business
    //xmlFile indica numele fisierului xml sau numele
    //  fisierului pdf. Daca extensia este pdf se incearca validarea
    //  ca fisier pdf. altfel, indiferent de extensie, se considera
    //  a fi un fisier xml
    //errFile indica numele complet al fisierului de erori sau poate fi
    //  null, sir vid sau '$'; in aceste cazuri numele fisierului de erori
    //  va fi xmlFile + ".err.txt"
    //zipFile indica numele complet al fizierului zip de atasat la PDF.
    //  daca xmlFile este un fisier pdf si daca declaratia accepta zip
    //  se incearca recuperarea lui din fisierul pdf (attachment); daca nu
    //  se gaseste nimic aici sau daca xmlFile este un fisier xml atunci:
    //  daca este '$' fisierul deriva din xmlFile cu schimbarea extensiei
    //  daca este sirul vid nu se ataseaza numic (zip optional)
    //  daca este null se incearca obtinerea unui fisier prin interfata
    //      CertificateChooser.chooseZipFile()
    //pdfFile indica numele complet al fisierului PDF sau poate fi
    //  null, sir vid sau '$'; in aceste cazuri numele fisierului PDF
    //  va fi xmlFile (fara extensia ".xml") + ".pdf"
    //daca xmlFile este un fisier pdf numele fisierelor xml si zip
    //  atasate vor fi <xmlFile>.dukintegrator.xml/zip. La fel, numele
    //  fisierului pdf creat va fi, prin lipsa, <xmlFile>.dukintegrator.pdf
    //pin este pinul folosit la semnarea cu smartCardul
    //smartCard este numele smartCard-ului folosit pt. semnare
    //  (ex: "aladdin")
    public int signPdf(String xmlFile, String errFile,
        String zipFile, String pdfFile,
        String pin, String smartCard)
    {
        String ret = null;
        String pdfTemp = xmlFile + ".pdf";
        CertificateChooser ch = _chooser;
        int rez = 0;

        if(_sign == null)
        {
            _sign = new Sign();
        }
        rez = pdfCreation(xmlFile, errFile, zipFile, pdfTemp);
        if(rez < 0)
        {
            return rez;
        }
        if(_chooser == null)
        {
            ch = new CertificateChooserImpl(_chooserIndex);
        }
        if(pdfFile == null || pdfFile.equals("") == true
            || pdfFile.equals("$") == true)
        {
            if(xmlFile.toLowerCase().endsWith(".xml") == true)
            {
                pdfFile = xmlFile.substring(0, xmlFile.length() - 4) + "-semnat.pdf";
            }
            else
            {
                pdfFile = xmlFile + "-semnat.pdf";
            }
        }
        ret = _sign.signPdf(pdfTemp, pdfFile, pin,
            padPath(_configPath) + smartCard + ".cfg", ch);
        File file = new File(pdfTemp);
        file.delete();
        if(ret != null && ret.equals("") == false)
        {
            _finalMessage.append("Eroare semnare PDF!").append(_newLine);
            _finalMessage.append("       ").append(ret).append(_newLine);
//            if(adjustString(System.getProperty("os.name")).toLowerCase().indexOf("win") >= 0
//                && adjustString(System.getProperty("os.arch")).indexOf("64") >= 0)
//            {
//                _finalMessage.append("       ").append("Instalati java pe 32 biti. Semnarea nu functioneaza pe Windows cu java pe 64 biti").append(_newLine);
//            }
            return -200;
        }
        else
        {
            _finalMessage.append("Fisierul PDF a fost semnat cu succes:").append(_newLine);
            _finalMessage.append("       ").append(pdfFile).append(_newLine);
            return 0;
        }
    }

    private static String padPath(String path)
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
}

class CertificateChooserImpl implements CertificateChooser
{
    int _index = 0;

    public CertificateChooserImpl(int index)
    {
        _index = index;
    }

    public CertAlias chooseCertificate(List col)
    {
        return (CertAlias)col.get(_index);
    }

    //intoarce:
    //  null daca este eroare (zip obligatoriu dar imposibil de identificat)
    //  "" (sir vid) lipsa zip, dar zip optional
    //  nume fisier zip de atasat
    public String chooseZipFile(String xmlFile, int zipOption)
    {
        String zipFile = null;
        if(zipOption < 0)
        {
            //fisier zip nepermis
            return "";
        }
        if(xmlFile.toLowerCase().endsWith(".xml") == true)
        {
            zipFile = xmlFile.substring(0, xmlFile.length() - 4) + ".zip";
        }
        else
        {
            zipFile = xmlFile + ".zip";
        }
        File file = new File(zipFile);
        if(file.exists() == true)
        {
            return zipFile;
        }
        if(zipOption <= 0)
        {
            return "";
        }
        return null;
    }
}
