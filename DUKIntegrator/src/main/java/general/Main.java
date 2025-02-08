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

import dec.LogTrace;
import dec.Params;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.zip.ZipFile;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import pdf.Pdf;
import pdf.PdfRoot;
import pdf.PdfSuperCreator;
import ui.DUKFrame;
import validator.Validator;

public class Main
{
    //versiunea Integratorului are forma: i.c.s.j.p, unde:
    //  i = versiunea librariilor suport (iText-5.0.4.jar, bcprov-jdk15-145.jar, bcmail-jdk15-145.jar)
    //  c = versiunea fisierelor .cfg si config.properties
    //      la aparitia unui nou .cfg nu se modifica versiunea ci se copiaza fisierele .cfg noi
    //      la schimbarea versiunii se reboteaza vechile fisiere cu .old in coada si se recopiaza toate fisierele
    //  s = versiunea librariilor proprii (Validator.jar, DecValidation.jar, DUKIntegrator.jar)
    //  j = versiunea de compatibilitate intre interfata dec.Validation si jar-urile Validator
    //  p = versiunea de compatibilitate intre interfata dec.Pdf si jar-urile DxxxPdf
    public static final String _version = "1.2.13.3.3";
    //_mode = 0 - UI
    //_mode = 1 - batch
    public static final String _CURRENT_VERSIONS = "versiuniCurente.txt";
    public static int _mode = 0;
    public static String _newLine = System.getProperty("line.separator");
    public static String _rootPath = null;
    public static String _libPath = null;
    public static String _configPath = null;
    public static String _xmlFile = null;
    public static String _errFile = null;
    public static String _pdfFile = null;
    public static String _zipFile = null;
    public static String _pin = null;
    public static String _smartCard = null;
    public static String _decType = null;
    public static String _procType = null;
    public static int _chooserIndex = 0;
    public static int _option = 0;
    public static String _urlVersiuni = null;
    public static String _offLine = null;
    public static String _javaStartPrefix = null;
    public static String _javaVersion = null;

    //apel posibil:
    //  DUKIntegrator [-c configPath] [firstFile]
    //sau
    //  DUKIntegrator [-cconfigPath] [firstFile]
    private static void analyzeParams(String[] args)
    {
        int len = 0;
        String par;
        _chooserIndex = 0;
        _option = 0;
//        for(int i = 0; i < args.length; i++)
//        {
//            System.out.println("arg " + Integer.toString(i) + ": " + args[i]);
//        }
        try
        {
            if(args.length > 0)
            {
                //parsare parametru -c
                par = args[len];
                if(par.startsWith("-c") == true)
                {
                    if(par.equals("-c") == true)
                    {
                        len++;
                        if(args.length < len + 1)
                        {
                            //linie comanda incorecta: lipsa parametru configPath
                            try
                            {
                                JOptionPane.showMessageDialog(null,//rootPane,
                                    "linie comanda incorecta: parametru 'caleConfig' incomplet");
                            }
                            catch(Throwable e)
                            {
                                System.out.println("linie comanda incorecta: parametru 'caleConfig' incomplet");
                            }
                            _mode = -1;
                            return;
                        }
                        len++;
                        par = args[len - 1];
                    }
                    else
                    {
                        len++;
                        par = par.substring(2);
                    }
                    if((new File(par)).isDirectory() == true)
                    {
                        //corectie 15-Feb-2011: adaugare padPath()
                        //  esta asta cauza erorii "Smart Card neselectat" ?
                        _configPath = padPath(par);
                    }
                    else
                    {
                        //parametru configPath incorect (par)
                        try
                        {
                            JOptionPane.showMessageDialog(null,//rootPane,
                                "parametru caleConfig incorect:" + _newLine + par);
                        }
                        catch(Throwable e)
                        {
                            System.out.println("parametru caleConfig incorect:" + _newLine + par);
                        }
                        _mode = -1;
                        return;
                    }
                }
            }
            //daca parametrul -c lipseste incercam stabilirea lui _configPath
            findConfig();
            if(_mode < 0)
            {
                return;
            }
            //parsare parametru tip procesare
            if(len < args.length)
            {
                if(args[len].equals("-v") == true
                    || args[len].equals("-p") == true
                    || args[len].equals("-s") == true
                    || args[len].equals("-d") == true
                    || args[len].equals("-f") == true
                    || args[len].equals("-F") == true)
                {
                    _procType = args[len].substring(1);
                    if(_procType.equals("d") == true)
                    {
                        len++;
                    }
                    else
                    {
                        _mode = 1;  //direct processing
                        len++;
                        if(_procType.equals("f") == true
                            || _procType.equals("F") == true)
                        {
                            return;
                        }
                        if(args.length == len)
                        {
                            System.out.println("linie comanda incompleta");
                            _mode = -1;
                            return;
                        }
                        //extrage parametru tip declaratie
                        _decType = args[len];
                        len++;
                    }
                }
            }
            //extragem parametri xmlFile si errFile pt ambele moduri
            if(len < args.length)
            {
                _xmlFile = args[len];
                len++;
                if(len < args.length)
                {
                    _errFile = args[len];
                    len++;
                }
            }
            else
            {
                //in mod 1 xmlFile este obligatoriu
                if(_mode != 0)
                {
                    System.out.println("linie comanda incompleta");
                    _mode = -1;
                }
                return;
            }
            if(_mode == 0)
            {
                //ignoram eventuali alti parametri
                return;
            }
            //extragem optiunile
            if(len < args.length)
            {
                par = args[len];
                if(par.equals("$") == false)
                {
                    try
                    {
                        _option = Integer.parseInt(par);
                    }
                    catch(Throwable e)
                    {
                        System.out.println("optiuni nenumerice");
                        _mode = -1;
                    }
                }
                len++;
            }
            if(_procType.equals("v") == true)
            {
                //ignoram eventuali alti parametri
                return;
            }
            //extragem fisier ZIP
            if(len < args.length)
            {
                _zipFile = args[len];
                len++;
            }
            //extragem fisier PDF
            if(len < args.length)
            {
                _pdfFile = args[len];
                len++;
            }
            if(_procType.equals("p") == true)
            {
                //ignoram eventuali alti parametri
                return;
            }
            if(len < args.length)
            {
                //extragem pin
                _pin = args[len];
                len++;
                if(len < args.length)
                {
                    //extragem cfgFile
                    _smartCard = args[len];
                    len++;
                    if(len < args.length)
                    {
                        //extragem selector certificat (prin lipsa 0)
                        par = args[len];
                        try
                        {
                            if(par.equals("$") == false)
                            {
                                _chooserIndex = Integer.parseInt(par);
                            }
                        }
                        catch(Throwable e)
                        {
                            System.out.println("selector certificat nenumeric");
                            _mode = -1;
                        }
                    }
                    return;
                }
                else
                {
                    System.out.println("linie comanda incompleta");
                    _mode = -1;
                    return;
                }
            }
            System.out.println("linie comanda incompleta");
            _mode = -1;
        }
        catch(Throwable e)
        {
            System.out.println("linie comanda eronata");
            _mode = -1;
        }
    }

    private static void findConfig()
    {
        int len = 0;
        String par;
        String rootPar;
        try
        {
            //determinare configPath
            if(_configPath == null || _configPath.equals("") == true)
            {
                URL url = Main.class.getProtectionDomain().getCodeSource().getLocation();
                try
                {
                    par = url.toURI().getPath();
//                    System.out.println("cale OK:" + par + "  url:" + url.toString() + "  uri:" + url.toURI().toString());
                }
                catch(Throwable ex)
                {
                    par = url.getPath();
//                    System.out.println("cale KO:" + par);
                }
//                System.out.println("cale:" + par);
                len = par.indexOf("DUKIntegrator.jar");
                if(len < 0)
                {
                    rootPar = padPath(par);
                    par = padPath(par) + "config";
//                    System.out.println("1cale:" + par + " cale root:" + rootPar);
                }
                else
                {
                    rootPar = padPath(par.substring(0, len));
                    par = padPath(par.substring(0, len)) + "config";
//                    System.out.println("2cale:" + par + " cale root:" + rootPar);
                }
                File cfg = new File(par);
                if(cfg.isDirectory() == true)
                {
                    _rootPath = rootPar;
                    _configPath = padPath(par);
                    _libPath = padPath(padPath(cfg.getParent()) + "lib");
//                    System.out.println("3cale:" + par + " cale root:" + rootPar + " cale lib:" + _libPath);
                    return;
                }
                cfg = new File("config");
                if(cfg.isDirectory() == true)
                {
                    try
                    {
                        _configPath = padPath(cfg.getCanonicalPath());
                        cfg = new File(_configPath);
                        _libPath = padPath(padPath(cfg.getParent()) + "lib");
                        _rootPath = padPath(padPath(cfg.getParent()));
                        return;
                    }
                    catch(IOException ex)
                    {
                    }
                }
                //nu pot determina configPath
                par = "folderul config nu poate fi determinat." + _newLine
                    + "Incercati apelul:" + _newLine
                    + "DUKIntegrator -c caleConfig";
                try
                {
                    JOptionPane.showMessageDialog(null,/*
                         * rootPane,
                         */ par);
                }
                catch(Throwable ex)
                {
                    System.out.println(par);
                }
                _mode = -1;
            }
        }
        catch(Throwable ex)
        {
            _mode = -1;
        }
    }

    private static void findRoot()
    {
        int len = 0;
        String par;
        try
        {
            //determinare configPath
            if(_rootPath == null || _rootPath.equals("") == true)
            {
                URL url = Main.class.getProtectionDomain().getCodeSource().getLocation();
                try
                {
                    par = url.toURI().getPath();
                }
                catch(Throwable ex)
                {
                    par = url.getPath();
                }
                len = par.indexOf("DUKIntegrator.jar");
                if(len < 0)
                {
                    par = "";
                }
                else
                {
                    par = padPath(par.substring(0, len));
                }
                if((new File(par)).isDirectory() == true)
                {
                    _rootPath = par;
                    return;
                }
                File root = new File(_configPath);
                if(root.isDirectory() == true)
                {
                    try
                    {
                        _rootPath = padPath(root.getParentFile().getCanonicalPath());
                        return;
                    }
                    catch(IOException ex)
                    {
                    }
                }
                //nu pot determina rootPath
                par = "folderul principal nu poate fi determinat." + _newLine
                    + "Download.jar.new trebuie redenumit ca Download.jar";
                try
                {
                    JOptionPane.showMessageDialog(null,/*
                         * rootPane,
                         */ par);
                }
                catch(Throwable ex)
                {
                    System.out.println(par);
                }
                _mode = -1;
            }
        }
        catch(Throwable ex)
        {
            _mode = -1;
        }
    }

    private static void getConfigDefaults()
    {
        if(_configPath.equals("") == true)
        {
            return;
        }
        try
        {
//            Properties _props = new Properties();
//            FileInputStream file = new FileInputStream(_configPath + "config.properties");
//            _props.load(file);
//            file.close();
            Params.init(_configPath + "config.properties");
            _urlVersiuni = Params.getProperty("urlVersiuni");
            if(_urlVersiuni != null && _urlVersiuni.equals("") == true)
            {
                _urlVersiuni = null;
            }
            _javaStartPrefix = Params.getProperty("javaStartPrefix");
            if(_javaStartPrefix == null || _javaStartPrefix.equals("") == true)
            {
                _javaStartPrefix = "java";
            }
            _javaVersion = Params.getProperty("javaVersion");
            if(_javaVersion != null && _javaVersion.equals("") == true)
            {
                _javaVersion = "-version:"
                    + System.getProperty("java.specification.version");
            }
            _offLine = Params.getProperty("offLine");
            if(_offLine != null && _offLine.equals("Y") == false)
            {
                _offLine = null;
            }
        }
        //catch exception in case properties file does not exist
        catch(Throwable e)
        {
            return;
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

    public static int createVersionFile(Validator vld, PdfSuperCreator pdf, String cfgPath, int option)
    {
        String fileName = cfgPath + _CURRENT_VERSIONS;
        File versFile = new File(fileName);
        if(versFile.exists() == true)
        {
            return 0;
        }
        BufferedWriter str = null;
        try
        {
            str = new BufferedWriter(new FileWriter(fileName));
            str.write(_version);
            String[] vldList = getListOfDeclarations();
            String pVers = null;
            String jVers = null;
            for(String vldX: vldList)
            {
                jVers = vld.getLastVersion(vldX);
                if(jVers == null)
                {
                    continue;
                }
                pVers = pdf.getLastVersion(vldX);
                if(pVers == null)
                {
                    continue;
                }
                str.write(_newLine);
                str.write(vldX);
                str.write(";");
                str.write(jVers);
                str.write(";");
                str.write(pVers);
            }
            str.write(_newLine);
            str.close();
        }
        catch(Throwable ex)
        {
            return -1;
        }
        return 0;
    }

    public static String[] getDeclarationList(String cfgPath)
    {
        String fileName = cfgPath + _CURRENT_VERSIONS;
        File versFile = new File(fileName);
        if(versFile.exists() == false)
        {
            return null;
        }
        BufferedReader str = null;
        String line = null;
        String[] cmp = null;
        ArrayList<String> list = new ArrayList<String>(10);
        try
        {
            str = new BufferedReader(new FileReader(fileName));
            line = str.readLine();
            while(true)
            {
                line = str.readLine();
                if(line == null)
                {
                    break;
                }
                cmp = line.split(";", 2);
                list.add(cmp[0]);
            }
            str.close();
        }
        catch(Throwable ex)
        {
            return null;
        }
        String[] arr = list.toArray(new String[list.size()]);
        Arrays.sort(arr);
        return arr;
    }

    private static String[] getListOfDeclarations()
    {
        File dir = new File(_libPath);
        final String vld = "Validator.jar";
        ArrayList<String> decls = new ArrayList<String>(10);
        String[] jars = dir.list(new FilenameFilter()
        {
            public boolean accept(File arg0, String arg1)
            {
                return arg1.endsWith(vld);
            }
        });
        for(String jar: jars)
        {
            if(jar.length() <= vld.length())
            {
                continue;
            }
            decls.add(jar.substring(0, jar.length() - vld.length()));
        }
        return decls.toArray(new String[decls.size()]);
    }

    public static boolean isUnix()
    {
        String os = System.getProperty("os.name").toLowerCase();
        //linux or unix
        return (os.indexOf("nix") >= 0 || os.indexOf("nux") >= 0);
    }

    //parseaza dupa separatori spatiu, dar ignora spatiu intre ' sau "
    private static void addList(ArrayList<String> cmd, String items)
    {
        String[] parts = null;
        String sep = null;
        String txt = null;
        int ind = 0;
        try
        {
            do
            {
                if(items == null || items.trim().length() == 0)
                {
                    return;
                }
                sep = items.substring(0, 1);
                if(sep.equals("\"") == true || sep.equals("\'") == true)
                {
                    ind = items.indexOf(sep, 1);
                    if(ind < 0)
                    {
                        //eroare: delimitatori nepereche: ignora totul
                        return;
                    }
                    txt = items.substring(1, ind).trim();
                    if(txt != null && txt.length() != 0)
                    {
                        cmd.add(txt);
                    }
                    items = items.substring(ind + 1);
                    continue;
                }
                parts = items.split(" ", 2);
                txt = parts[0].trim();
                if(txt != null && txt.length() != 0)
                {
                    cmd.add(txt);
                }
                if(parts.length > 1)
                {
                    items = parts[1].trim();
                    continue;
                }
                return;
            }
            while(true);
        }
        catch(Throwable ex)
        {
            return;
        }
//        for(String item: parts)
//        {
//            item = item.trim();
//            if(item.equals("") == false)
//            {
//                cmd.add(item);
//            }
//        }
    }

//-c E:\javaApp\DUKIntegrator\config -s D112 E:\javaApp\DUKValidator\XSD\DecUnicaV2.xml $ $ $ a5T6LYd5 athena 0
//-c E:\javaApp\DUKIntegrator\config -s D112 E:\javaApp\DUKValidator\XSD\DecUnicaV2.xml $ $ $ 57443765 oberthur 1
    public static void main(String args[])
    {
        String par = null;
        try
        {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            analyzeParams(args);
            if(_mode == -1)
            {
                par = "eroare in linia de comanda";
                try
                {
                    JOptionPane.showMessageDialog(null,/*
                         * rootPane,
                         */ par);
                }
                catch(Throwable ex)
                {
                    System.out.println(par);
                }
                return;
            }
            if(_mode == 0)
            {
                getConfigDefaults();
                if(_procType == null && _urlVersiuni != null
                    && _offLine == null)
                {
                    //apel Download
                    Runtime rt = Runtime.getRuntime();
                    String path = ((isUnix() == false && _rootPath.startsWith("/") == true && _rootPath.startsWith("//") == false) ? _rootPath.substring(1) : _rootPath);
                    ArrayList<String> cmd = new ArrayList<String>(5);
                    addList(cmd, _javaStartPrefix);
                    if(_javaVersion != null)
                    {
                        addList(cmd, _javaVersion);
                    }
                    cmd.add("-jar");
                    cmd.add(path + "Download.jar");
                    if(_xmlFile != null)
                    {
                        cmd.add(_xmlFile);
                    }
                    String cmdLine[] = cmd.toArray(new String[cmd.size()]);
                    Process pr = rt.exec(cmdLine);
                }
                else
                {
                    //finalizare download Download.jar
                    findRoot();
                    if(_mode >= 0)
                    {
                        boolean flag = true;
                        try
                        {
                            File dwn = new File(_rootPath + "Download.jar.new");
                            File dwnOld = new File(_rootPath + "Download.jar");
                            if(dwn.exists() == true)
                            {
                                if(dwnOld.exists() == true)
                                {
                                    //asteapta terminarea lui Download.jar
                                    for(int j = 0; j < 20; j++)
                                    {
                                        Thread.sleep(500);
                                        flag = dwnOld.delete();
                                        if(flag == true)
                                        {
                                            break;
                                        }
                                    }
                                }
                                if(flag == true)
                                {
                                    flag = dwn.renameTo(dwnOld);
                                }
                            }
                        }
                        catch(Throwable ex)
                        {
                            flag = false;
                        }
                        if(flag == false)
                        {
                            par = "Nu a reusit finalizarea downloadului lui Download.jar." + _newLine
                                + "Download.jar.new trebuie redenumit ca Download.jar";
                            JOptionPane.showMessageDialog(null, par);
                        }
                    }
                    DUKFrame frm = new DUKFrame(_rootPath,
                        _configPath, _xmlFile, _errFile);
                    frm.setVisible(true);
                }
            }
            if(_mode == 1)
            {
                //batch processing
                if(_procType.toLowerCase().equals("f") == false
                    && (_xmlFile == null || _xmlFile.equals("") == true
                    || (new File(_xmlFile)).isFile() == false))
                {
                    par = "fisier XML incorect specificat";
                    System.out.println(par);
                    return;
                }
                Integrator integrator = null;
                int returns = 0;
                try
                {
                    LogTrace.init(_configPath + "emergency.log", _configPath + "emergency.trc", 0, 100, true);
                    integrator = new Integrator();
                    integrator.setConfigPath(_configPath);
                    if(_procType.equals("f") == true
                        || _procType.equals("F") == true)
                    {
                        _option = (_procType.equals("f") == true) ? 0 : 1;
                        returns = createVersionFile(integrator.getValidator(),
                            integrator.getPdfSuperCreator(), _configPath, _option);
                        return;
                    }
                    integrator.setDeclType(_decType);
                    integrator.useOptions(_option);
                    if(_zipFile != null && _zipFile.equals("0") == true)
                    {
                        _zipFile = "";
                    }
                    if(_procType.equals("v") == true)
                    {
                        returns = integrator.parseDocument(_xmlFile, _errFile);
                    }
                    else if(_procType.equals("p") == true)
                    {
                        returns = integrator.pdfCreation(_xmlFile, _errFile,
                            _zipFile, _pdfFile);
                    }
                    else //if(_procType.equals("s") == true)
                    {
                        integrator.setChooserIndex(_chooserIndex);
                        returns = integrator.signPdf(_xmlFile, _errFile,
                            _zipFile, _pdfFile, _pin, _smartCard);
                        integrator.releaseToken();
                    }
                    if(returns == 0 || returns <= -100)
                    {
                        String rez = null;
                        if(returns == 0)
                        {
                            rez = "ok";
                        }
                        else
                        {
                            rez = integrator.getFinalMessage();
                        }
                        try
                        {
                            if(_errFile == null || _errFile.equals("") == true
                                || _errFile.equals("$") == true)
                            {
                                _errFile = _xmlFile + ".err.txt";
                            }
                            OutputStreamWriter _errorFile = new OutputStreamWriter(new FileOutputStream(_errFile), "UTF-8");
                            _errorFile.write(rez);
                            _errorFile.close();
                        }
                        catch(IOException ex)
                        {
                        }
                    }
                    System.out.println(integrator.getFinalMessage());
                }
                catch(Throwable e)
                {
                }
                return;
            }
        }
        catch(Exception e)
        {
            return;
        }
    }
}
