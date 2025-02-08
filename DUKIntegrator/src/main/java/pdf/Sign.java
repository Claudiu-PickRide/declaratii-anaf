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
package pdf;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfPKCS7;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfSignatureAppearance;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfString;
import dec.LogTrace;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.KeyStoreSpi;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.ProviderException;
import java.security.Security;
import java.security.Signature;
import java.security.SignatureException;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPublicKey;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;
//import ro.certsign.nativeLibWrapper.TokenHandle;
//import sun.security.pkcs11.wrapper.CK_C_INITIALIZE_ARGS;
//import sun.security.pkcs11.wrapper.PKCS11;
//import sun.security.pkcs11.wrapper.PKCS11Constants;
import ui.CertificateChooser;

public class Sign
{
    private boolean _hasCertificare = false;
    private boolean _expired = false;
    private String _algorithm = null;
    private static String newLine = System.getProperty("line.separator");
    private String _pkcs11config = "";
    private String _library = null;
    private boolean _isSlot = false;
    private Certificate[] _chain;
    private PrivateKey _privateKey;
    Provider _etpkcs11 = null;
    CertAlias _certAlias = null;
    //pt. dll
//    TokenHandle tHandle = null;

    public void setNoCertificate()
    {
        _hasCertificare = false;
    }

    public String signPdf(String pdfFile, String pdfFileSigned, String inputPin, String cfgFile, CertificateChooser chooser)
    {
        String rez = signPdfIntern(pdfFile, pdfFileSigned, inputPin, cfgFile, chooser, null);
        if(rez == null || rez.equals("") == true)
        {
            return null;
        }
        if(_algorithm.equals("mscapi") || System.getProperty("os.name").toLowerCase().indexOf("win") < 0)
        {
            return rez;
        }
        rez = signPdfIntern(pdfFile, pdfFileSigned, inputPin, cfgFile, chooser, "mscapi");
        return rez;
    }

    private String signPdfIntern(String pdfFile, String pdfFileSigned, String inputPin, String cfgFile, CertificateChooser chooser, String algorithm)
    {
        String rez = null;
        if(_hasCertificare == false)
        {
            rez = initSignPdf(inputPin, cfgFile, chooser, algorithm);
            if(rez == null)
            {
                _hasCertificare = true;
            }
            else
            {
                return rez;
            }
        }
//        if(_algorithm.equals("dll") == false)
//        {
        rez = doSignPdf(pdfFile, pdfFileSigned);
//        _etpkcs11.clear();
        return rez;
//        }
//        else
//        {
//            return doSignPdfDll(pdfFile, pdfFileSigned);
//        }
    }

    private String initSignPdf(String inputPin, String cfgFile, CertificateChooser chooser, String algorithm)
    {
        _pkcs11config = "";
        _algorithm = null;
        _library = null;
        _expired = false;
        _isSlot = false;
        BufferedReader cfg = null;
        String line = null;
        try
        {
            cfg = new BufferedReader(new FileReader(cfgFile));
            do
            {
                line = cfg.readLine();
                if(line == null)
                {
                    break;
                }
                line = line.trim();
                if(line.startsWith("#") || line.startsWith(";"))
                {
                    continue;
                }
                String[] parts = line.split("=", 2);
                if(parts.length != 2)
                {
                    continue;
                }
                if(parts[0].trim().equals("library"))
                {
                    _library = parts[1].trim();
                }
                else if(parts[0].trim().equals("slotListIndex")
                    || parts[0].trim().equals("slot"))
                {
                    _isSlot = true;
                }
                if(parts[0].trim().equals("algorithm"))
                {
                    _algorithm = parts[1].trim();
                }
                else
                {
                    _pkcs11config += line + newLine;
                }
            }
            while(true);
        }
        catch(Throwable ex)
        {
            return "eroare fisier configurare: " + ex.getMessage();
        }
        finally
        {
            if(cfg != null)
            {
                try
                {
                    cfg.close();
                }
                catch(IOException ex)
                {
                    return "eroare inchidere fisier configurare: " + ex.getMessage();
                }
            }
        }
        if(_library == null)
        {
            return "fisierul de configurare nu contine atributul 'library'";
        }
        if(_algorithm == null)
        {
            //alegere mai judicioasa!!!
            if(algorithm != null)
            {
                _algorithm = algorithm;
            }
            else
            {
                _algorithm = "sunpkcs11";
            }
        }
        if(_algorithm.equals("sunpkcs11"))
        {
            //In mod empiric am constatat ca, pe token-urile care au
            //  certificate reinnoite CertSign sub acelasi alias, se selecteaza
            //  aleatoriu, cand certificatul valid, cand cel expirat.
            //Incercam sa prindem, facand mai multe incercari,
            //  certificatul valid
            String err = null;
            for(int i = 0; i < 10; i++)
            {
                _expired = false;
                err = initSunpkcs11(inputPin, cfgFile, chooser);
                if(_expired == false)
                {
                    return err;
                }
                _isSlot = true;
            }
            return err;
        }
        else if(_algorithm.equals("mscapi"))
        {
            return initMscapi(inputPin, cfgFile, chooser);
        }
//        else if(_algorithm.equals("dll"))
//        {
//            return initDll(inputPin, cfgFile, chooser);
//        }
        return "algoritm semnare necunoscut. Corectati in fisierul " + cfgFile + " valoarea atributului 'algorithm'";
    }

    private String initSunpkcs11(String inputPin, String cfgFile, CertificateChooser chooser)
    {
        KeyStore.PasswordProtection pin = null;
        X509Certificate cert = null;
        //compune tagul slot
        if(_isSlot == false)
        {
            long[] slots = null;
//            CK_SLOT_INFO info = null;
            try
            {
                //urmatoarele 4 instructiuni, precum si instructiunea mai indepartata
                //  -->  _etpkcs11 = new sun.security.pkcs11.SunPKCS11(configStream);
                //  au fost executate folosind java reflection, pt. a putea folosi
                //  codul si cu java 64 biti (care nu are pachetul pkcs11)
//                CK_C_INITIALIZE_ARGS initArgs = new CK_C_INITIALIZE_ARGS();
                Class initArgsClass = Class.forName("sun.security.pkcs11.wrapper.CK_C_INITIALIZE_ARGS");
                Object initArgs = initArgsClass.getConstructor().newInstance();
//                initArgs.flags = PKCS11Constants.CKF_OS_LOCKING_OK;
                Field fld = initArgsClass.getDeclaredField("flags");
                fld.setLong(initArgs,
                    Class.forName("sun.security.pkcs11.wrapper.PKCS11Constants").getField("CKF_OS_LOCKING_OK").getLong(null));
//                PKCS11 p11 = PKCS11.getInstance(_library, "C_GetFunctionList", initArgs, false);
                Class p11Class = Class.forName("sun.security.pkcs11.wrapper.PKCS11");
                Method mth = p11Class.getMethod("getInstance", String.class, String.class, initArgsClass, boolean.class);
                Object p11 = mth.invoke(null, _library, "C_GetFunctionList", initArgs, false);
//                slots = p11.C_GetSlotList(true);
                mth = p11Class.getMethod("C_GetSlotList", boolean.class);
                slots = (long[])mth.invoke(p11, true);
//                info = p11.C_GetSlotInfo(slots[0]);
                if(slots != null && slots.length > 0)
                {
//                    _pkcs11config += "slotListIndex=" + slots[0] + newLine;
                    _pkcs11config += "slot=" + slots[0] + newLine;
                }
            }
            catch(Throwable t)
            {
                return "eroare acces driver: " + _library + " (Corectati parametrul library din fisierul dist\\config\\SMART_CARD.cfg astfel incat sa indice calea reala pe calculatorul dumneavoastra catre driverul corespunzator SMART_CARD-ului folosit)" + newLine + "       (" + t + ")";
            }
        }
        try
        {
            // connect to eToken PKCS#11 provider
            byte[] pkcs11configBytes = _pkcs11config.getBytes();
            ByteArrayInputStream configStream = new ByteArrayInputStream(pkcs11configBytes);
            //blocaj cu driverul aladdin 2013-06-06:
//            _etpkcs11 = new sun.security.pkcs11.SunPKCS11(configStream);
            Constructor ct = Class.forName("sun.security.pkcs11.SunPKCS11").getConstructor(InputStream.class);
            _etpkcs11 = (Provider)ct.newInstance(configStream);
            Security.addProvider(_etpkcs11);
            // get user PIN
            pin = new KeyStore.PasswordProtection(inputPin.toCharArray());
            // create key store builder
            KeyStore.Builder keyStoreBuilder = KeyStore.Builder.newInstance("PKCS11", _etpkcs11, pin);
            // create key store
            //blocaj cu driverul aladdin 2013-06-06:
            KeyStore keyStore = keyStoreBuilder.getKeyStore();
            String alias = null;
            String error = "certificatul nu a putut fi detectat";
            int cnt = 0, flag = 0;
            Enumeration e = keyStore.aliases();
            List coll = new ArrayList();
            do
            {
                cnt++;
                alias = String.valueOf(e.nextElement());
                if(keyStore.isKeyEntry(alias) == true)
                {
                    cert = (X509Certificate)keyStore.getCertificate(alias);
                    try
                    {
                        cert.checkValidity();
                        coll.add(new CertAlias(alias, cert));
//                            break;
                    }
                    catch(CertificateExpiredException ex)
                    {
                        error = "Certificat expirat: " + ex.toString();
                        _expired = true;
                        flag |= 1;
                    }
                    catch(CertificateNotYetValidException ex)
                    {
                        error = "Certificat nu este inca valid: " + ex.toString();
                        flag |= 2;
                    }
                    catch(Throwable ex)
                    {
                        error = "Certificat eronat: " + ex.toString();
                        logError(30, ex);
                        flag |= 4;
                    }
//                            StringBuffer bf = new StringBuffer();
//                            bf.append(chooser._newLine + "Alias certificat:----------------------------------------------------" + chooser._newLine);
//                            bf.append(alias);
//                            bf.append(chooser._newLine + "Certificat----------------------------------------------------" + chooser._newLine);
//                            bf.append(cert);
//                            bf.append(chooser._newLine + "Private key: ----------------------------------------------------" + chooser._newLine);
//                            bf.append(keyStore.getKey(alias, null));
//                            bf.append(chooser._newLine + "Sfarsit certificat----------------------------------------------------" + chooser._newLine);
//                            chooser.insertMessage(bf.toString());
                }
            }
            while(e.hasMoreElements());
            if(coll.size() > 1)
            {
                _certAlias = chooser.chooseCertificate(coll);
            }
            else if(coll.size() == 0)
            {
                if(cnt > 1 && flag != 1 && flag != 2 && flag != 4)
                {
                    error = "Certificatele sunt sau expirate sau nu sunt inca valide sau eronate";
                }
                return error;
            }
            else
            {
                _certAlias = (CertAlias)coll.get(0);
            }
            cert = _certAlias._cert;
            alias = _certAlias._alias;
            _expired = false;
            _privateKey = (PrivateKey)keyStore.getKey(alias, null);
            _chain = null;
            _chain = keyStore.getCertificateChain(alias);
            _chain[0] = cert;
        }
        catch(ProviderException ex)
        {
            logError(1, ex);
            if(ex.getMessage().equals("Initialization failed"))
            {
                return ex.toString() + " (Probabil aveti un alt tip de SmartCard conectat. Deconectati alte tipuri de SmartCarduri (daca exista) si folositi optiunea \"*autoDetect\")";
            }
            else if(ex.getMessage().equals("Error parsing configuration"))
            {
                return ex.toString() + " (Calea catre driverul SmartCardului (care se afla inscrisa in fisierul .cfg corespunzator acestuia) contine unul din urmatoarele caractere: \"~()\". Solutie: Copiati continutul intregului folder in alta locatie si modificati corespunzator calea din fisierul .cfg. (vezi si http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=6581254))";
            }
            return ex.toString();
        }
        catch(KeyStoreException ex)
        {
            logError(2, ex);
            if(ex.getMessage().equals("KeyStore instantiation failed"))
            {
                return ex.toString() + " (Probabil nu aveti nici un SmartCard conectat sau PIN-ul nu este corect sau, daca SmartCardul este Schlumberger, introduceti doar primele 8 caractere ale PIN-ului)";
            }
            return ex.toString();
        }
        catch(NoSuchAlgorithmException ex)
        {
            logError(3, ex);
            return ex.toString();
        }
        catch(UnrecoverableKeyException ex)
        {
            logError(4, ex);
            return ex.toString();
        }
        catch(Throwable ex)
        {
            logError(5, ex);
            return ex.toString();
        }
        return null;
    }

    private String initMscapi(String inputPin, String cfgFile, CertificateChooser chooser)
    {
        X509Certificate cert = null;
        //compune tagul slot
        try
        {
            //add provider
            _etpkcs11 = (Provider)Class.forName("sun.security.mscapi.SunMSCAPI").newInstance();
//            _etpkcs11 = new sun.security.mscapi.SunMSCAPI();
            Security.addProvider(_etpkcs11);
            // create key store
            KeyStore keyStore = KeyStore.getInstance("Windows-MY");
            keyStore.load(null, inputPin.toCharArray());
            fixAliases(keyStore);
            String alias = null;
            String error = "certificatul nu a putut fi detectat";
            int cnt = 0, flag = 0;
            Enumeration e = keyStore.aliases();
            List coll = new ArrayList();
            do
            {
                cnt++;
                alias = String.valueOf(e.nextElement());
                if(keyStore.isKeyEntry(alias) == true)
                {
                    cert = (X509Certificate)keyStore.getCertificate(alias);
                    try
                    {
                        cert.checkValidity();
                        coll.add(new CertAlias(alias, cert));
//                            break;
                    }
                    catch(CertificateExpiredException ex)
                    {
                        error = "Certificat expirat: " + ex.toString();
                        _expired = true;
                        flag |= 1;
                    }
                    catch(CertificateNotYetValidException ex)
                    {
                        error = "Certificat nu este inca valid: " + ex.toString();
                        flag |= 2;
                    }
                    catch(Throwable ex)
                    {
                        error = "Certificat eronat: " + ex.toString();
                        logError(30, ex);
                        flag |= 4;
                    }
//                            StringBuffer bf = new StringBuffer();
//                            bf.append(chooser._newLine + "Alias certificat:----------------------------------------------------" + chooser._newLine);
//                            bf.append(alias);
//                            bf.append(chooser._newLine + "Certificat----------------------------------------------------" + chooser._newLine);
//                            bf.append(cert);
//                            bf.append(chooser._newLine + "Private key: ----------------------------------------------------" + chooser._newLine);
//                            bf.append(keyStore.getKey(alias, null));
//                            bf.append(chooser._newLine + "Sfarsit certificat----------------------------------------------------" + chooser._newLine);
//                            chooser.insertMessage(bf.toString());
                }
            }
            while(e.hasMoreElements());
            if(coll.size() > 1)
            {
                _certAlias = chooser.chooseCertificate(coll);
            }
            else if(coll.size() == 0)
            {
                if(cnt > 1 && flag != 1 && flag != 2 && flag != 4)
                {
                    error = "Certificatele sunt sau expirate sau nu sunt inca valide sau eronate";
                }
                return error;
            }
            else
            {
                _certAlias = (CertAlias)coll.get(0);
            }
            cert = _certAlias._cert;
            alias = _certAlias._alias;
            _expired = false;

            _privateKey = (PrivateKey)keyStore.getKey(alias, inputPin.toCharArray());
            _chain = null;
            _chain = keyStore.getCertificateChain(alias);
            _chain[0] = cert;
        }
        catch(ProviderException ex)
        {
            logError(10, ex);
            if(ex.getMessage().equals("Initialization failed"))
            {
                return ex.toString() + " (Probabil aveti un alt tip de SmartCard conectat. Deconectati alte tipuri de SmartCarduri (daca exista) si folositi optiunea \"*autoDetect\")";
            }
            else if(ex.getMessage().equals("Error parsing configuration"))
            {
                return ex.toString() + " (Calea catre driverul SmartCardului (care se afla inscrisa in fisierul .cfg corespunzator acestuia) contine unul din urmatoarele caractere: \"~()\". Solutie: Copiati continutul intregului folder in alta locatie si modificati corespunzator calea din fisierul .cfg. (vezi si http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=6581254))";
            }
            return ex.toString();
        }
        catch(KeyStoreException ex)
        {
            logError(11, ex);
            if(ex.getMessage().equals("KeyStore instantiation failed"))
            {
                return ex.toString() + " (Probabil nu aveti nici un SmartCard conectat sau PIN-ul nu este corect sau, daca SmartCardul este Schlumberger, introduceti doar primele 8 caractere ale PIN-ului)";
            }
            return ex.toString();
        }
        catch(NoSuchAlgorithmException ex)
        {
            logError(12, ex);
            return ex.toString();
        }
        catch(UnrecoverableKeyException ex)
        {
            logError(13, ex);
            return ex.toString();
        }
        catch(Throwable ex)
        {
            logError(14, ex);
            return ex.toString();
        }
        return null;
    }

    //workaround pt. cazul certificatelor reinnoite cu acelasi alias (cu cel precedent)
    //modifica aliasurile in obiectul keyStore pt. a le face unice
    private static void fixAliases(KeyStore keyStore)
    {
        Field field;
        KeyStoreSpi keyStoreVeritable;

        try
        {
            field = keyStore.getClass().getDeclaredField("keyStoreSpi");
            field.setAccessible(true);
            keyStoreVeritable = (KeyStoreSpi)field.get(keyStore);

            if("sun.security.mscapi.KeyStore$MY".equals(keyStoreVeritable.getClass().getName()))
            {
                Collection entries;
                String alias, hashCode;
                X509Certificate[] certificates;

                field = keyStoreVeritable.getClass().getEnclosingClass().getDeclaredField("entries");
                field.setAccessible(true);
                entries = (Collection)field.get(keyStoreVeritable);

                for(Object entry: entries)
                {
                    field = entry.getClass().getDeclaredField("certChain");
                    field.setAccessible(true);
                    certificates = (X509Certificate[])field.get(entry);

                    hashCode = Integer.toString(certificates[0].hashCode());

                    field = entry.getClass().getDeclaredField("alias");
                    field.setAccessible(true);
                    alias = (String)field.get(entry);

                    if(!alias.equals(hashCode))
                    {
                        field.set(entry, alias.concat(" - ").concat(hashCode));
                    } // if
                } // for
            } // if
        }
        catch(Exception exception)
        {
            logError(20, exception);
            LogTrace.log("eroare in functia fixAliases(): " + exception.toString(), 2);
        }
    }

//    private String initDll(String inputPin, String cfgFile, CertificateChooser chooser)
//    {
//        KeyStore.PasswordProtection pin = null;
//        X509Certificate cert = null;
//        int[] slots;
//        //compune tagul slot
//        try
//        {
//            tHandle = new TokenHandle(_library);
//            slots = tHandle.getSlots();
//            if(slots != null && slots.length > 0)
//            {
//                _pkcs11config += "slot=" + slots[0] + newLine;
//            }
//            else
//            {
//                return "eroare token: niciun slot disponibil";
//            }
//        }
//        catch(Throwable t)
//        {
//            return "eroare acces driver: " + _library + " (Corectati parametrul library din fisierul dist\\config\\SMART_CARD.cfg astfel incat sa indice calea reala pe calculatorul dumneavoastra catre driverul corespunzator SMART_CARD-ului folosit)" + newLine + "       (" + t + ")";
//        }
//        try
//        {
//            // connect to eToken PKCS#11 provider
//            byte[] pkcs11configBytes = _pkcs11config.getBytes();
//            ByteArrayInputStream configStream = new ByteArrayInputStream(pkcs11configBytes);
//            _etpkcs11 = new sun.security.pkcs11.SunPKCS11(configStream);
//            Security.addProvider(_etpkcs11);
//            // get user PIN
//            pin = new KeyStore.PasswordProtection(inputPin.toCharArray());
//            // create key store builder
//            KeyStore.Builder keyStoreBuilder = KeyStore.Builder.newInstance("PKCS11", _etpkcs11, pin);
//            // create key store
//            KeyStore keyStore = keyStoreBuilder.getKeyStore();
//            String alias = null;
//            String error = "certificat eronat";
//            int cnt = 0, flag = 0;
//            Enumeration e = keyStore.aliases();
//            List coll = new ArrayList();
//            tHandle.openSession(slots[0]);
//            tHandle.loginToTokenSession(inputPin);
//            byte[] publicKeyContent = null;
//            do
//            {
//                cnt++;
//                alias = String.valueOf(e.nextElement());
//                if(keyStore.isKeyEntry(alias) == true)
//                {
//                    cert = (X509Certificate)keyStore.getCertificate(alias);
//                    publicKeyContent = cert.getPublicKey().getEncoded();
//                    Certificate[] certificates = tHandle.getCertificatesByPublicKey(publicKeyContent);
//                    for(int i = 0; i < certificates.length; i++)
//                    {
//                        cert = (X509Certificate)certificates[i];
//                        try
//                        {
//                            cert.checkValidity();
//                            coll.add(new CertAlias(alias, cert));
//                        }
//                        catch(CertificateExpiredException ex)
//                        {
//                            error = "Certificat expirat: " + ex.toString();
//                            _expired = true;
//                            flag |= 1;
//                        }
//                        catch(CertificateNotYetValidException ex)
//                        {
//                            error = "Certificat nu este inca valid: " + ex.toString();
//                            flag |= 2;
//                        }
//                        catch(Throwable ex)
//                        {
//                            error = "Certificat eronat: " + ex.toString();
//                            flag |= 4;
//                        }
//                    }
//                }
//            }
//            while(e.hasMoreElements());
//            if(coll.size() > 1)
//            {
//                _certAlias = chooser.chooseCertificate(coll);
//            }
//            else if(coll.size() == 0)
//            {
//                if(cnt > 1 && flag != 1 && flag != 2 && flag != 4)
//                {
//                    error = "Certificatele sunt sau expirate sau nu sunt inca valide sau eronate";
//                }
//                return error;
//            }
//            else
//            {
//                _certAlias = (CertAlias)coll.get(0);
//            }
//            cert = _certAlias._cert;
//            alias = _certAlias._alias;
//            _expired = false;
//            _privateKey = (PrivateKey)keyStore.getKey(alias, null);
//            _chain = null;
//            _chain = keyStore.getCertificateChain(alias);
//            _chain[0] = cert;
//        }
//        catch(ProviderException ex)
//        {
//            if(ex.getMessage().equals("Initialization failed"))
//            {
//                return ex.toString() + " (Probabil aveti un alt tip de SmartCard conectat. Deconectati alte tipuri de SmartCarduri (daca exista) si folositi optiunea \"*autoDetect\")";
//            }
//            else if(ex.getMessage().equals("Error parsing configuration"))
//            {
//                return ex.toString() + " (Calea catre driverul SmartCardului (care se afla inscrisa in fisierul .cfg corespunzator acestuia) contine unul din urmatoarele caractere: \"~()\". Solutie: Copiati continutul intregului folder in alta locatie si modificati corespunzator calea din fisierul .cfg. (vezi si http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=6581254))";
//            }
//            return ex.toString();
//        }
//        catch(KeyStoreException ex)
//        {
//            if(ex.getMessage().equals("KeyStore instantiation failed"))
//            {
//                return ex.toString() + " (Probabil nu aveti nici un SmartCard conectat sau PIN-ul nu este corect sau, daca SmartCardul este Schlumberger, introduceti doar primele 8 caractere ale PIN-ului)";
//            }
//            return ex.toString();
//        }
//        catch(NoSuchAlgorithmException ex)
//        {
//            return ex.toString();
//        }
//        catch(UnrecoverableKeyException ex)
//        {
//            return ex.toString();
//        }
//        catch(Throwable ex)
//        {
//            return ex.toString();
//        }
//        return null;
//    }
    private String doSignPdf(String pdfFile, String pdfFileSigned)
    {
        try
        {
            PdfReader reader = new PdfReader(pdfFile);
            FileOutputStream fout = new FileOutputStream(pdfFileSigned);
            PdfStamper stp = PdfStamper.createSignature(reader, fout, '\0');
            PdfSignatureAppearance sap = stp.getSignatureAppearance();
            sap.setCrypto(null, _chain, null, PdfSignatureAppearance.SELF_SIGNED);
            sap.setReason("Declaratie unica");
            sap.setVisibleSignature(new Rectangle(500, 775, 600, 675), 1, null);
            sap.setExternalDigest(new byte[((RSAPublicKey)_certAlias._cert.getPublicKey()).getModulus().bitLength() / 8], null, "RSA");
            sap.preClose();
            byte[] content = streamToByteArray(sap.getRangeStream());
            Signature signature = Signature.getInstance("SHA1withRSA", _etpkcs11);
            signature.initSign((PrivateKey)_privateKey);
            signature.update(content);
            byte[] signatureBytes = signature.sign();
            // Self-Sign mode
            PdfPKCS7 sig = sap.getSigStandard().getSigner();
            sig.setExternalDigest(signatureBytes, null, "RSA");
            PdfDictionary dic = new PdfDictionary();
            dic.put(PdfName.CONTENTS, new PdfString(sig.getEncodedPKCS1()).setHexWriting(true));
            sap.close(dic);
        }
        catch(FileNotFoundException ex)
        {
            return ex.toString();
        }
        catch(ProviderException ex)
        {
            if(ex.getMessage().equals("Initialization failed"))
            {
                return ex.toString() + " (Probabil aveti un alt tip de SmartCard conectat. Deconectati alte tipuri de SmartCarduri (daca exista) si folositi optiunea \"*autoDetect\")";
            }
            else if(ex.getMessage().equals("Error parsing configuration"))
            {
                return ex.toString() + " (Calea catre driverul SmartCardului (care se afla inscrisa in fisierul .cfg corespunzator acestuia) contine unul din urmatoarele caractere: \"~()\". Solutie: Copiati continutul intregului folder in alta locatie si modificati corespunzator calea din fisierul .cfg. (vezi si http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=6581254))";
            }
            return ex.toString();
        }
        catch(NoSuchAlgorithmException ex)
        {
            return ex.toString();
        }
        catch(IOException ex)
        {
            return ex.toString();
        }
        catch(DocumentException ex)
        {
            return ex.toString();
        }
        catch(InvalidKeyException ex)
        {
            return ex.toString();
        }
        catch(SignatureException ex)
        {
            return ex.toString();
        }
        catch(Throwable ex)
        {
            return ex.toString();
        }
        finally
        {
            //wwww: eliminare key pt a putea introduce un nou pin
//            String str = pin.getPassword().toString();
//            pin.destroy();
//            Security.removeProvider(_etpkcs11.getName());//"SunPKCS11-SmartCard");
            //wwww
        }
        return "";
    }

//    private String doSignPdfDll(String pdfFile, String pdfFileSigned)
//    {
//        try
//        {
//            PdfReader reader = new PdfReader(pdfFile);
//            FileOutputStream fout = new FileOutputStream(pdfFileSigned);
//            PdfStamper stp = PdfStamper.createSignature(reader, fout, '\0');
//            PdfSignatureAppearance sap = stp.getSignatureAppearance();
//            sap.setCrypto(null, _chain, null, PdfSignatureAppearance.SELF_SIGNED);
//            sap.setReason("Declaratie unica");
//            sap.setVisibleSignature(new Rectangle(500, 775, 600, 675), 1, null);
//            sap.setExternalDigest(new byte[((RSAPublicKey)_certAlias._cert.getPublicKey()).getModulus().bitLength() / 8], null, "RSA");
//            sap.preClose();
//            byte[] content = streamToByteArray(sap.getRangeStream());
//            byte[] signatureBytes = tHandle.sign(content, _certAlias._cert.getPublicKey().getEncoded());
//            // Self-Sign mode
//            PdfPKCS7 sig = sap.getSigStandard().getSigner();
//            sig.setExternalDigest(signatureBytes, null, "RSA");
//            PdfDictionary dic = new PdfDictionary();
//            dic.put(PdfName.CONTENTS, new PdfString(sig.getEncodedPKCS1()).setHexWriting(true));
//            sap.close(dic);
//        }
//        catch(FileNotFoundException ex)
//        {
//            return ex.toString();
//        }
//        catch(ProviderException ex)
//        {
//            if(ex.getMessage().equals("Initialization failed"))
//            {
//                return ex.toString() + " (Probabil aveti un alt tip de SmartCard conectat. Deconectati alte tipuri de SmartCarduri (daca exista) si folositi optiunea \"*autoDetect\")";
//            }
//            else if(ex.getMessage().equals("Error parsing configuration"))
//            {
//                return ex.toString() + " (Calea catre driverul SmartCardului (care se afla inscrisa in fisierul .cfg corespunzator acestuia) contine unul din urmatoarele caractere: \"~()\". Solutie: Copiati continutul intregului folder in alta locatie si modificati corespunzator calea din fisierul .cfg. (vezi si http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=6581254))";
//            }
//            return ex.toString();
//        }
//        catch(IOException ex)
//        {
//            return ex.toString();
//        }
//        catch(DocumentException ex)
//        {
//            return ex.toString();
//        }
//        catch(Throwable ex)
//        {
//            return ex.toString();
//        }
//        finally
//        {
//            //wwww: eliminare key pt a putea introduce un nou pin
////            String str = pin.getPassword().toString();
////            pin.destroy();
////            Security.removeProvider(_etpkcs11.getName());//"SunPKCS11-SmartCard");
//            //wwww
//        }
//        return "";
//    }
    private byte[] streamToByteArray(InputStream is) throws IOException
    {

        byte[] buff = new byte[512];
        int read = -1;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while((read = is.read(buff)) >= 0)
        {

            bos.write(buff, 0, read);
        }
        bos.close();
        return bos.toByteArray();
    }

    public void releaseToken()
    {
        try
        {
            for(Provider p: Security.getProviders())
            {
                if(p.getName().contains("SunPKCS11"))
                {
                    Security.removeProvider(p.getName());
                }
            }
            Thread.sleep(1000);
        }
        catch(Throwable ex)
        {
        }
    }

    private static void logError(int code, Object msg)
    {
        if(msg instanceof String)
        {
            LogTrace.log("modul Sign; eroare=" + Integer.toString(code)
                + ": " + msg, 2);
        }
        else
        {
            Throwable ex = (Throwable)msg;
            StackTraceElement[] stack = ex.getStackTrace();
            LogTrace.log("modul Sign; eroare=" + Integer.toString(code)
                + ": " + ex.toString(), 2);
            for(StackTraceElement el: stack)
            {
                LogTrace.log(el.toString(), 3);
            }
        }
    }

    public class CertAlias
    {
        public String _alias;
        public X509Certificate _cert;

        public CertAlias(String alias, X509Certificate cert)
        {
            _alias = alias;
            _cert = cert;
        }

        @Override
        public String toString()
        {
            return _alias;// + _cert.getIssuerDN().getName();
        }
    }
}
