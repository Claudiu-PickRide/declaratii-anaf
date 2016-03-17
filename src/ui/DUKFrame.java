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
package ui;

import ajutor.Ajutor;
import dec.LogTrace;
import dec.Options;
import dec.Params;
import general.Integrator;
import general.Main;
import istoric.Istoric;
import java.awt.Desktop;
import java.awt.event.ItemEvent;
import java.awt.event.WindowEvent;
import pdf.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.WindowConstants;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

public class DUKFrame extends javax.swing.JFrame implements CertificateChooser, Runnable
{
    /**
     * Creates new form DUKFrame
     */
    public DUKFrame(String rootPath, String configPath, String xmlFile, String errFile)
    {
        initComponents();
        _configPath = configPath;
        _rootPath = rootPath;
        LogTrace.init(_configPath + "emergency.log", _configPath + "emergency.trc", 0, 100, true);
        _integrator = new Integrator();
        _integrator.setChooser(this);
        _integrator.setConfigPath(_configPath);
        _xmlFile = xmlFile;
        _errFile = errFile;
        txtFileName.setText(_xmlFile);
        setVisibleValidationButton();
        populateCardList();
        getConfigDefaults();
        DefaultListModel defList = new DefaultListModel();
        if(Main.createVersionFile(_integrator.getValidator(),
            _integrator.getPdfSuperCreator(), _configPath, 1) < 0)
        {
            cmbDecType.setModel(new DefaultComboBoxModel());
            //populare lista declaratii pt. dezinstalare: goala!
            lstKept.setModel(defList);
            _decList = new String[0];
        }
        else
        {
            _decList = Main.getDeclarationList(_configPath);
            cmbDecType.setModel(new DefaultComboBoxModel(_decList));
            //populare lista declaratii pt. dezinstalare
            for(String dec: _decList)
            {
                defList.addElement(dec);
            }
            lstKept.setModel(defList);
        }
        if(lstKept.getModel().getSize() != 0)
        {
            lstKept.setSelectedIndex(0);
        }
        lstUnInstalled.setModel(new DefaultListModel());
        if(cmbDecType.getModel().getSize() != 0)
        {
            if(_defDecl != null)
            {
                cmbDecType.setSelectedItem(_defDecl);
            }
            _decType = cmbDecType.getSelectedItem().toString();
            _integrator.setDeclType(_decType);
            _zipOption = _integrator.getPdfZipOption();
            fillOptions();
            this.setTitle(_title.replaceAll("%", _decType).replaceAll("#", Main._version)
                + _integrator.getLastVersion()
                + " / " + _integrator.getPdfLastVersion());
        }
        else
        {
            JOptionPane.showMessageDialog(rootPane,
                "Nu aveti nicio declaratie instalata.");
        }
        wndChooseCertificate.setVisible(false);
        wndChooseCertificate.getRootPane().setDefaultButton(btnOK);
        wndChooseCertificate.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        wndChooseCertificate.setLocationRelativeTo(this);
        wndChooseCertificate.pack();
        wndUnInstallDec.setVisible(false);
        wndUnInstallDec.getRootPane().setDefaultButton(btnClose);
        wndUnInstallDec.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        wndUnInstallDec.setLocationRelativeTo(this);
        wndUnInstallDec.pack();
        wndOptions.setVisible(false);
        wndOptions.getRootPane().setDefaultButton(btnKeepOldValues);
        wndOptions.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        wndOptions.setLocationRelativeTo(this);
        wndOptions.pack();
        pgrBar.setVisible(false);
        btnCancel.setVisible(false);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        ctlFileChooser = new javax.swing.JFileChooser();
        wndChooseCertificate = new javax.swing.JDialog();
        jLabel1 = new javax.swing.JLabel();
        btnOK = new javax.swing.JButton();
        cmbCertificates = new javax.swing.JComboBox();
        lblFurnizor = new javax.swing.JLabel();
        lblNume = new javax.swing.JLabel();
        lblStartDate = new javax.swing.JLabel();
        lblEndDate = new javax.swing.JLabel();
        lblSerialNumber = new javax.swing.JLabel();
        txtFurnizor = new javax.swing.JTextField();
        txtNume = new javax.swing.JTextField();
        txtStartDate = new javax.swing.JTextField();
        txtEndDate = new javax.swing.JTextField();
        txtSerialNumber = new javax.swing.JTextField();
        wndUnInstallDec = new javax.swing.JDialog();
        lblUnInstallMain = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        lstKept = new javax.swing.JList();
        jScrollPane2 = new javax.swing.JScrollPane();
        lstUnInstalled = new javax.swing.JList();
        btnUnInstall = new javax.swing.JButton();
        lblKept = new javax.swing.JLabel();
        lblUnInstalled = new javax.swing.JLabel();
        btnUnInstallAll = new javax.swing.JButton();
        btnUnInstallNothing = new javax.swing.JButton();
        btnUnInstallDec = new javax.swing.JButton();
        btnKeepDec = new javax.swing.JButton();
        btnClose = new javax.swing.JButton();
        wndOptions = new javax.swing.JDialog();
        jLabel3 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        chkUserPwd = new javax.swing.JCheckBox();
        jLabel4 = new javax.swing.JLabel();
        txtUser = new javax.swing.JTextField();
        txtPwd = new javax.swing.JPasswordField();
        jLabel6 = new javax.swing.JLabel();
        txtProxyIP = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        txtProxyPort = new javax.swing.JTextField();
        chkAuto = new javax.swing.JCheckBox();
        chkNewDeclarations = new javax.swing.JCheckBox();
        btnAccept = new javax.swing.JButton();
        btnKeepOldValues = new javax.swing.JButton();
        chkAuthentication = new javax.swing.JCheckBox();
        jLabel8 = new javax.swing.JLabel();
        txtDomain = new javax.swing.JTextField();
        chkOffLine = new javax.swing.JCheckBox();
        jLabel9 = new javax.swing.JLabel();
        txtJavaStart = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        txtJavaVersion = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        lblHelp = new javax.swing.JLabel();
        txtFileName = new javax.swing.JTextField();
        btnGetFileName = new javax.swing.JButton();
        btnValidation = new javax.swing.JButton();
        btnPDF = new javax.swing.JButton();
        btnSign = new javax.swing.JButton();
        txtPin = new javax.swing.JPasswordField();
        lblPin = new javax.swing.JLabel();
        cmbCardType = new javax.swing.JComboBox();
        lblCardType = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        cmbDecType = new javax.swing.JComboBox();
        lblOptions = new javax.swing.JLabel();
        cmbOptions = new javax.swing.JComboBox();
        jPanel4 = new javax.swing.JPanel();
        pnlResults = new javax.swing.JScrollPane();
        txtResults = new javax.swing.JTextArea();
        pgrBar = new javax.swing.JProgressBar();
        btnCancel = new javax.swing.JButton();
        mnuMain = new javax.swing.JMenuBar();
        mnuFile = new javax.swing.JMenu();
        mniExit = new javax.swing.JMenuItem();
        mnuTools = new javax.swing.JMenu();
        mniUnInstallDec = new javax.swing.JMenuItem();
        mniOptions = new javax.swing.JMenuItem();
        mnuHelp = new javax.swing.JMenu();
        mniHelp = new javax.swing.JMenuItem();
        mniHistory = new javax.swing.JMenuItem();

        wndChooseCertificate.setTitle("Alegeti un certficat pt. semnare");
        wndChooseCertificate.setModal(true);
        wndChooseCertificate.setResizable(false);

        jLabel1.setText("Token cu mai multe certificate. Selectati certificatul dorit");

        btnOK.setText("OK");
        btnOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOKActionPerformed(evt);
            }
        });

        cmbCertificates.setAutoscrolls(true);
        cmbCertificates.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbCertificatesActionPerformed(evt);
            }
        });

        lblFurnizor.setText("Furnizor:");

        lblNume.setText("Nume:");

        lblStartDate.setText("StartValabilitate:");

        lblEndDate.setText("EndValabilitate:");

        lblSerialNumber.setText("SerialNumber:");

        txtFurnizor.setEditable(false);

        txtNume.setEditable(false);

        txtStartDate.setEditable(false);

        txtEndDate.setEditable(false);

        txtSerialNumber.setEditable(false);

        javax.swing.GroupLayout wndChooseCertificateLayout = new javax.swing.GroupLayout(wndChooseCertificate.getContentPane());
        wndChooseCertificate.getContentPane().setLayout(wndChooseCertificateLayout);
        wndChooseCertificateLayout.setHorizontalGroup(
            wndChooseCertificateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(wndChooseCertificateLayout.createSequentialGroup()
                .addGroup(wndChooseCertificateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(wndChooseCertificateLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(cmbCertificates, 0, 441, Short.MAX_VALUE))
                    .addGroup(wndChooseCertificateLayout.createSequentialGroup()
                        .addGap(56, 56, 56)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 272, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
            .addGroup(wndChooseCertificateLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(wndChooseCertificateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(wndChooseCertificateLayout.createSequentialGroup()
                        .addGroup(wndChooseCertificateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(lblSerialNumber, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblEndDate, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblStartDate, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblNume, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblFurnizor, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 81, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(wndChooseCertificateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtFurnizor, javax.swing.GroupLayout.DEFAULT_SIZE, 350, Short.MAX_VALUE)
                            .addComponent(txtNume, javax.swing.GroupLayout.DEFAULT_SIZE, 350, Short.MAX_VALUE)
                            .addComponent(txtStartDate, javax.swing.GroupLayout.DEFAULT_SIZE, 350, Short.MAX_VALUE)
                            .addComponent(txtEndDate, javax.swing.GroupLayout.DEFAULT_SIZE, 350, Short.MAX_VALUE)
                            .addComponent(txtSerialNumber, javax.swing.GroupLayout.DEFAULT_SIZE, 350, Short.MAX_VALUE))
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, wndChooseCertificateLayout.createSequentialGroup()
                        .addGap(0, 206, Short.MAX_VALUE)
                        .addComponent(btnOK)
                        .addGap(196, 196, 196))))
        );
        wndChooseCertificateLayout.setVerticalGroup(
            wndChooseCertificateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(wndChooseCertificateLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(28, 28, 28)
                .addComponent(cmbCertificates, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(wndChooseCertificateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblFurnizor)
                    .addComponent(txtFurnizor, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(11, 11, 11)
                .addGroup(wndChooseCertificateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblNume)
                    .addComponent(txtNume, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(11, 11, 11)
                .addGroup(wndChooseCertificateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblStartDate)
                    .addComponent(txtStartDate, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(11, 11, 11)
                .addGroup(wndChooseCertificateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblEndDate)
                    .addComponent(txtEndDate, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(wndChooseCertificateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblSerialNumber)
                    .addComponent(txtSerialNumber, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 17, Short.MAX_VALUE)
                .addComponent(btnOK)
                .addContainerGap())
        );

        wndUnInstallDec.setTitle("Dezinstalare declaratii (o singura data pe sesiune)");
        wndUnInstallDec.setModal(true);
        wndUnInstallDec.setResizable(false);
        wndUnInstallDec.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                wndUnInstallDecWindowClosing(evt);
            }
        });

        lblUnInstallMain.setText("Dezinstalarea declaratiilor selectate este efectiva dupa ce terminati programul");

        lstKept.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane1.setViewportView(lstKept);

        lstUnInstalled.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane2.setViewportView(lstUnInstalled);

        btnUnInstall.setText("Dezinstaleaza");
        btnUnInstall.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUnInstallActionPerformed(evt);
            }
        });

        lblKept.setText("Declaratii ramase instalate");

        lblUnInstalled.setText("Declaratii de dezinstalat");

        btnUnInstallAll.setText(">>");
        btnUnInstallAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUnInstallAllActionPerformed(evt);
            }
        });

        btnUnInstallNothing.setText("<<");
        btnUnInstallNothing.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUnInstallNothingActionPerformed(evt);
            }
        });

        btnUnInstallDec.setText(">");
        btnUnInstallDec.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUnInstallDecActionPerformed(evt);
            }
        });

        btnKeepDec.setText("<");
        btnKeepDec.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnKeepDecActionPerformed(evt);
            }
        });

        btnClose.setText("Inchide");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout wndUnInstallDecLayout = new javax.swing.GroupLayout(wndUnInstallDec.getContentPane());
        wndUnInstallDec.getContentPane().setLayout(wndUnInstallDecLayout);
        wndUnInstallDecLayout.setHorizontalGroup(
            wndUnInstallDecLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(wndUnInstallDecLayout.createSequentialGroup()
                .addGroup(wndUnInstallDecLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(wndUnInstallDecLayout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addGroup(wndUnInstallDecLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblUnInstallMain)
                            .addGroup(wndUnInstallDecLayout.createSequentialGroup()
                                .addComponent(lblKept)
                                .addGap(146, 146, 146)
                                .addComponent(lblUnInstalled))))
                    .addGroup(wndUnInstallDecLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(wndUnInstallDecLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(btnUnInstall)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(wndUnInstallDecLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnKeepDec, 0, 0, Short.MAX_VALUE)
                            .addComponent(btnUnInstallNothing, 0, 0, Short.MAX_VALUE)
                            .addComponent(btnUnInstallAll, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnUnInstallDec, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(wndUnInstallDecLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnClose)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        wndUnInstallDecLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {btnKeepDec, btnUnInstallAll, btnUnInstallDec, btnUnInstallNothing});

        wndUnInstallDecLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {btnClose, btnUnInstall});

        wndUnInstallDecLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jScrollPane1, jScrollPane2});

        wndUnInstallDecLayout.setVerticalGroup(
            wndUnInstallDecLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(wndUnInstallDecLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblUnInstallMain)
                .addGap(40, 40, 40)
                .addGroup(wndUnInstallDecLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblKept)
                    .addComponent(lblUnInstalled))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(wndUnInstallDecLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(wndUnInstallDecLayout.createSequentialGroup()
                        .addComponent(btnUnInstallAll)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnUnInstallNothing)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnUnInstallDec)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnKeepDec))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 210, Short.MAX_VALUE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 210, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(wndUnInstallDecLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnUnInstall)
                    .addComponent(btnClose))
                .addContainerGap())
        );

        wndOptions.setTitle("Optiuni download");
        wndOptions.setModal(true);
        wndOptions.setResizable(false);
        wndOptions.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                wndOptionsWindowClosing(evt);
            }
        });

        jLabel3.setText("Optiunile selectate vor fi inscrise in fisierul de configurare si vor deveni active la urmatoarea lansare DUKIntegrator");

        jLabel5.setText("Parola:");

        chkUserPwd.setText("Autentificare user/pwd pt. proxy");
        chkUserPwd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkUserPwdActionPerformed(evt);
            }
        });

        jLabel4.setText("User:");

        jLabel6.setText("IP proxy:");

        jLabel7.setText("Port proxy:");

        chkAuto.setText("Incearca determinare automata IP/port proxy");

        chkNewDeclarations.setText("Vreau sa stiu daca apar declaratii/documente noi");

        btnAccept.setText("OK");
        btnAccept.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAcceptActionPerformed(evt);
            }
        });

        btnKeepOldValues.setText("Anuleaza");
        btnKeepOldValues.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnKeepOldValuesActionPerformed(evt);
            }
        });

        chkAuthentication.setText("Metoda alternativa de autentificare");

        jLabel8.setText("Domeniu:");

        chkOffLine.setText("functionare off line");
        chkOffLine.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkOffLineActionPerformed(evt);
            }
        });

        jLabel9.setText("start java (ex: java -Xmx1024M)");

        jLabel10.setText("(prin lipsa se lanseaza cu parametri impliciti)");

        jLabel11.setText("versiune java (ex: -version:1.6)");

        jLabel12.setText("(prin lipsa se lanseaza versiunea java implicita)");

        javax.swing.GroupLayout wndOptionsLayout = new javax.swing.GroupLayout(wndOptions.getContentPane());
        wndOptions.getContentPane().setLayout(wndOptionsLayout);
        wndOptionsLayout.setHorizontalGroup(
            wndOptionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(wndOptionsLayout.createSequentialGroup()
                .addGroup(wndOptionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(wndOptionsLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(wndOptionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(wndOptionsLayout.createSequentialGroup()
                                .addGroup(wndOptionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(chkUserPwd)
                                    .addGroup(wndOptionsLayout.createSequentialGroup()
                                        .addGap(42, 42, 42)
                                        .addGroup(wndOptionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(chkAuthentication)
                                            .addGroup(wndOptionsLayout.createSequentialGroup()
                                                .addGroup(wndOptionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(jLabel4)
                                                    .addComponent(jLabel5)
                                                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGap(14, 14, 14)
                                                .addGroup(wndOptionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                    .addComponent(txtPwd)
                                                    .addComponent(txtUser)
                                                    .addComponent(txtDomain, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGap(33, 33, 33)
                                                .addGroup(wndOptionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                    .addComponent(chkNewDeclarations)
                                                    .addGroup(wndOptionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(chkOffLine)
                                                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(txtJavaVersion, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(txtJavaStart, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                                    .addGroup(wndOptionsLayout.createSequentialGroup()
                                        .addGap(4, 4, 4)
                                        .addGroup(wndOptionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel7)
                                            .addComponent(jLabel6))
                                        .addGap(18, 18, 18)
                                        .addGroup(wndOptionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(txtProxyPort, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(txtProxyIP, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addComponent(chkAuto))
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(wndOptionsLayout.createSequentialGroup()
                                .addGap(91, 91, 91)
                                .addComponent(btnAccept, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(153, 153, 153)
                                .addComponent(btnKeepOldValues, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(114, 114, 114))))
                    .addGroup(wndOptionsLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );

        wndOptionsLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {btnAccept, btnKeepOldValues});

        wndOptionsLayout.setVerticalGroup(
            wndOptionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(wndOptionsLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(wndOptionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(chkAuto)
                    .addComponent(chkNewDeclarations))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(wndOptionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(wndOptionsLayout.createSequentialGroup()
                        .addGroup(wndOptionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtProxyIP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6))
                        .addGap(18, 18, 18)
                        .addGroup(wndOptionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7)
                            .addComponent(txtProxyPort, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(chkUserPwd)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(wndOptionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(txtUser))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(wndOptionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(txtPwd))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(wndOptionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel8)
                            .addComponent(txtDomain, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(wndOptionsLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(chkOffLine)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtJavaStart, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel10)
                        .addGap(17, 17, 17)
                        .addComponent(jLabel11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtJavaVersion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel12)))
                .addGap(13, 13, 13)
                .addComponent(chkAuthentication)
                .addGap(18, 18, 18)
                .addGroup(wndOptionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAccept)
                    .addComponent(btnKeepOldValues))
                .addGap(32, 32, 32))
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Procesare XML");
        setName("frmMain"); // NOI18N
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        lblHelp.setText("introduceti nume fisier XML sau nume folder (pt. a prelucra toate fisierele XML din el) sau folositi butonul <Alege fisiere>");

        txtFileName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtFileNameKeyReleased(evt);
            }
        });

        btnGetFileName.setText("Alege fisiere");
        btnGetFileName.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnGetFileNameMouseClicked(evt);
            }
        });

        btnValidation.setText("Validare");
        btnValidation.setEnabled(false);
        btnValidation.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnValidationMouseClicked(evt);
            }
        });

        btnPDF.setText("Validare + creare PDF");
        btnPDF.setEnabled(false);
        btnPDF.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnPDFMouseClicked(evt);
            }
        });

        btnSign.setText("Validare + creare PDF semnat");
        btnSign.setEnabled(false);
        btnSign.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnSignMouseClicked(evt);
            }
        });

        txtPin.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtPinKeyReleased(evt);
            }
        });

        lblPin.setText("introduceti pinul pentru semnare:");

        cmbCardType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbCardTypeActionPerformed(evt);
            }
        });

        lblCardType.setText("selectati tipul de Smart Card");

        jLabel2.setText("alegeti tipul declaratiei");

        cmbDecType.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbDecType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbDecTypeActionPerformed(evt);
            }
        });

        lblOptions.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblOptions.setText("alegeti optiuni validare");

        cmbOptions.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbOptionsItemStateChanged(evt);
            }
        });

        txtResults.setColumns(20);
        txtResults.setEditable(false);
        txtResults.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        txtResults.setLineWrap(true);
        txtResults.setRows(5);
        pnlResults.setViewportView(txtResults);

        btnCancel.setText("Anulare");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(pgrBar, javax.swing.GroupLayout.PREFERRED_SIZE, 460, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnCancel, javax.swing.GroupLayout.DEFAULT_SIZE, 115, Short.MAX_VALUE))
            .addComponent(pnlResults, javax.swing.GroupLayout.DEFAULT_SIZE, 593, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addComponent(pnlResults, javax.swing.GroupLayout.DEFAULT_SIZE, 390, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pgrBar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        mnuFile.setText("Fisier");

        mniExit.setText("Terminare");
        mniExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mniExitActionPerformed(evt);
            }
        });
        mnuFile.add(mniExit);

        mnuMain.add(mnuFile);

        mnuTools.setText("Unelte");

        mniUnInstallDec.setText("Dezinstalare declaratii");
        mniUnInstallDec.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mniUnInstallDecActionPerformed(evt);
            }
        });
        mnuTools.add(mniUnInstallDec);

        mniOptions.setText("Optiuni download");
        mniOptions.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mniOptionsActionPerformed(evt);
            }
        });
        mnuTools.add(mniOptions);

        mnuMain.add(mnuTools);

        mnuHelp.setText("Ajutor");

        mniHelp.setText("Ajutor");
        mniHelp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mniHelpActionPerformed(evt);
            }
        });
        mnuHelp.add(mniHelp);

        mniHistory.setText("Istoria versiunilor");
        mniHistory.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mniHistoryActionPerformed(evt);
            }
        });
        mnuHelp.add(mniHistory);

        mnuMain.add(mnuHelp);

        setJMenuBar(mnuMain);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblHelp, javax.swing.GroupLayout.DEFAULT_SIZE, 583, Short.MAX_VALUE)
                        .addContainerGap(20, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(txtFileName, javax.swing.GroupLayout.DEFAULT_SIZE, 494, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnGetFileName)
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(cmbCardType, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblCardType, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(txtPin)
                                    .addComponent(lblPin, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, 12, Short.MAX_VALUE)
                                .addComponent(btnSign, javax.swing.GroupLayout.PREFERRED_SIZE, 222, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(cmbDecType, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(cmbOptions, 0, 239, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnValidation)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnPDF, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(lblOptions, javax.swing.GroupLayout.PREFERRED_SIZE, 216, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblHelp)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtFileName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnGetFileName))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(lblOptions))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnPDF)
                    .addComponent(btnValidation)
                    .addComponent(cmbDecType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbOptions, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblPin)
                    .addComponent(lblCardType))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSign)
                    .addComponent(txtPin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbCardType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public Sign.CertAlias chooseCertificate(List coll)
    {
        DefaultComboBoxModel cmbModel = new DefaultComboBoxModel();
        for(Object cert: coll)
        {
            cmbModel.addElement(cert);
        }
        cmbCertificates.setModel(cmbModel);
        changeStatusCombo();
        wndChooseCertificate.setVisible(true);
        return (Sign.CertAlias)cmbCertificates.getSelectedItem();
    }

    private void populateCardList()
    {
        if(_configPath.equals("") == true)
        {
            return;
        }
        File cfg = new File(_configPath);
        if(cfg.isDirectory() == false)
        {
            return;
        }
        String[] cfgList = cfg.list(new FilenameFilter()
        {
            public boolean accept(File dir, String name)
            {
                return name.endsWith(".cfg");
            }
        });
        if(cfgList == null || cfgList.length == 0)
        {
            return;
        }
        cmbCardType.addItem(_AUTO_DETECT);
        for(int i = 0; i < cfgList.length; i++)
        {
            cmbCardType.addItem(cfgList[i].substring(0, cfgList[i].length() - 4));
        }
//        cmbCardType.setSelectedIndex(-1);
        cmbCardType.setSelectedIndex(0);
    }

    private int validateXML(String fileName)
    {
        int returns;
        try
        {
            returns = _integrator.parseDocument(fileName, null);
            txtResults.append(_integrator.getFinalMessage());
            return returns;
        }
        catch(Throwable e)
        {
            txtResults.append("Eroare de deployment!" + _newLine);
            txtResults.append("       mesaj: " + e.toString() + _newLine);
            return -2;
        }
    }

    private int pdfCreation(String fileName)
    {
        int ret = 0;
        try
        {
            ret = _integrator.pdfCreation(fileName, null, null, null);
            txtResults.append(_integrator.getFinalMessage());
            return ret;
        }
        catch(Throwable e)
        {
            txtResults.append("Eroare de deployment!" + _newLine);
            txtResults.append("       mesaj: " + e.toString() + _newLine);
            return -2;
        }
    }

    private int pdfSigning(String fileName)
    {
        int ret = 0;
        try
        {
            String msg;
            String smartCard = (String)cmbCardType.getSelectedItem();
            if(smartCard.equals(_AUTO_DETECT) == false)
            {
                ret = _integrator.signPdf(fileName, null, null,
                    null, txtPin.getText(), smartCard);
                msg = _integrator.getFinalMessage();
                txtResults.append(msg);
                return ret;
            }
            else
            {
                int i = 0;
                for(; i < cmbCardType.getModel().getSize(); i++)
                {
                    smartCard = (String)cmbCardType.getModel().getElementAt(i);
                    if(smartCard.equals(_AUTO_DETECT) == true)
                    {
                        continue;
                    }
                    ret = _integrator.signPdf(fileName, null, null,
                        null, txtPin.getText(), smartCard);
                    msg = _integrator.getFinalMessage();
                    if(ret < 0)
                    {
                        continue;
                    }
                    else
                    {
                        txtResults.append(msg);
                        cmbCardType.setSelectedIndex(i);
                        return 0;
                    }
                }
                txtResults.append("Nu am putut detecta tipul SmartCardului!" + _newLine);
                txtResults.append("       " + "Fie este un tip nou, necunoscut, fie pinul introdus nu este corect" + _newLine);
                return ret;
            }
        }
        catch(Throwable e)
        {
            txtResults.append("Eroare de deployment!" + _newLine);
            txtResults.append("       mesaj: " + e.toString() + _newLine);
            return -2;
        }
    }

    private void enableUI(boolean flag)
    {
        cmbCardType.setEnabled(flag);
        cmbDecType.setEnabled(flag);
        btnValidation.setEnabled(flag);
        btnPDF.setEnabled(flag);
        txtPin.setEnabled(flag);
        txtFileName.setEnabled(flag);
        btnGetFileName.setEnabled(flag);
        if(flag == false)
        {
            _getEnableOptions = cmbOptions.isEnabled();
            _getEnableSign = btnSign.isEnabled();
            cmbOptions.setEnabled(flag);
            btnSign.setEnabled(flag);
        }
        else
        {
            cmbOptions.setEnabled(_getEnableOptions);
            btnSign.setEnabled(_getEnableSign);
        }
    }

    private void enableUIProgBar(boolean flag, int max)
    {
        pgrBar.setVisible(!flag);
        btnCancel.setVisible(!flag);
        _stopProcessing = flag;
        if(flag == false)
        {
            pgrBar.setMinimum(0);
            pgrBar.setMaximum(max);
            pgrBar.setValue(0);
        }
    }

    private void processFiles(int type)
    {
        _processingType = type;
        enableUI(false);
        Thread th = new Thread(this);
        th.start();
    }

    public void run()
    {
        int ret = 0;
        File file = new File(_xmlFile);
        File[] fileList = null;
        if(file.isDirectory() == true)
        {
            try
            {
                fileList = file.listFiles(_crtFilter);
            }
            catch(Throwable ex)
            {
                fileList = null;
            }
//            fileList = file.listFiles(new FilenameFilter()
//            {
//
//                public boolean accept(File dir, String name)
//                {
//                    return name.toLowerCase().endsWith(".xml");
//                }
//            });
            enableUIProgBar(false, fileList.length);
            for(int i = 0; i < fileList.length; i++)
            {
                if(_stopProcessing == true)
                {
                    break;
                }
                if(fileList[i].isDirectory() == true)
                {
                    continue;
                }
                switch(_processingType)
                {
                    case 0:
                        ret = validateXML(fileList[i].getPath());
                        break;
                    case 1:
                        ret = pdfCreation(fileList[i].getPath());
                        break;
                    case 2:
                        ret = pdfSigning(fileList[i].getPath());
                        break;
                }
                if(ret < -100)
                {
                    break;
                }
                pgrBar.setValue(i + 1);
            }
            enableUIProgBar(true, 0);
        }
        else if(file.isFile() == true)
        {
            switch(_processingType)
            {
                case 0:
                    ret = validateXML(_xmlFile);
                    break;
                case 1:
                    ret = pdfCreation(_xmlFile);
                    break;
                case 2:
                    ret = pdfSigning(_xmlFile);
                    break;
            }
        }
        else
        {
            JOptionPane.showMessageDialog(rootPane, "introduceti corect calea si numele fisierului");
        }
        enableUI(true);
    }

    private void signStatus()
    {
        if(txtFileName.getText().equals("") == false
            && txtPin.getText().equals("") == false
            && cmbCardType.getSelectedIndex() >= 0)
        {
            btnSign.setEnabled(true);
        }
        else
        {
            btnSign.setEnabled(false);
        }
    }

    private void changeStatusCombo()
    {
        Sign.CertAlias certAlias = (Sign.CertAlias)cmbCertificates.getSelectedItem();
        X509Certificate cert = certAlias._cert;
        txtFurnizor.setText(cert.getIssuerDN().getName());
        txtFurnizor.setCaretPosition(0);
        txtNume.setText(cert.getSubjectDN().getName());
        txtNume.setCaretPosition(0);
        txtStartDate.setText(dateFormat.format(cert.getNotBefore()));
        txtEndDate.setText(dateFormat.format(cert.getNotAfter()));
        txtSerialNumber.setText(cert.getSerialNumber().toString());
        txtSerialNumber.setCaretPosition(0);
    }

    private void fillOptions()
    {
        cmbOptions.removeAllItems();
        Options[] options = _integrator.getOptions();
        if(options == null)
        {
            cmbOptions.setEnabled(false);
        }
        else
        {
            cmbOptions.setModel(new DefaultComboBoxModel(options));
            try
            {
                String opt = Params.getProperty("_" + _decType);
                for(int i = 0; i < options.length; i++)
                {
                    if(options[i]._name.equals(opt) == true)
                    {
                        _flag = false;
                        cmbOptions.setSelectedIndex(i);
                        _flag = true;
                        break;
                    }
                }
            }
            catch(Throwable ex)
            {
            }
            cmbOptions.setEnabled(true);
        }
    }

    private void setVisibleValidationButton()
    {
        if(txtFileName.getText().equals("") == false)
        {
            btnValidation.setEnabled(true);
            btnPDF.setEnabled(true);
        }
        else
        {
            btnValidation.setEnabled(false);
            btnPDF.setEnabled(false);
        }
        signStatus();
    }

    private void getConfigDefaults()
    {
        if(_configPath.equals("") == true)
        {
            return;
        }
        try
        {
//            _props = new Properties();
//            FileInputStream file = new FileInputStream(_configPath + "config.properties");
//            _props.load(file);
//            file.close();
            Params.init(_configPath + "config.properties");
            String folder = Params.getProperty("defFolder");
            if(folder != null)
            {
                ctlFileChooser.setCurrentDirectory(new File(folder));
            }
            String card = Params.getProperty("defSmartCard");
            if(card != null)
            {
                cmbCardType.setSelectedItem(card);
            }
            _urlVersiuni = Params.getProperty("urlVersiuni");
            if(_urlVersiuni != null && _urlVersiuni.equals("") == true)
            {
                _urlVersiuni = null;
            }
            _offLine = Params.getProperty("offLine");
            if(_offLine != null && _offLine.equals("Y") == false)
            {
                _offLine = null;
            }
            _javaStartPrefix = Params.getProperty("javaStartPrefix");
            _javaVersion = Params.getProperty("javaVersion");
            _ignoraDeclaratiiNoi = Params.getProperty("ignoraDeclaratiiNoi");
            if(_ignoraDeclaratiiNoi == null || _ignoraDeclaratiiNoi.equals("") == true)
            {
                _ignoraDeclaratiiNoi = "N";
            }
            _user = Params.getProperty("user");
            _pwd = Params.getProperty("pwd");
            _domain = Params.getProperty("domain");
            _authentication = Params.getProperty("authentication");
            _proxy = Params.getProperty("proxy");
            _proxyIP = Params.getProperty("proxyIP");
            _proxyPort = Params.getProperty("proxyPort");
            _defDecl = Params.getProperty("tipDeclaratie");
        }
        //catch exception in case properties file does not exist
        catch(Throwable e)
        {
            return;
        }
    }

    private void setConfigDefaults()
    {
        try
        {
            Params.setProperty("defFolder", ctlFileChooser.getCurrentDirectory().getCanonicalPath());
            if(cmbCardType.getSelectedIndex() >= 0)
            {
                Params.setProperty("defSmartCard", cmbCardType.getSelectedItem().toString());
            }
            Params.setProperty("tipDeclaratie", _decType);
            //dezinstalare declaratii
            String dec = null;
            String prefix = null;
            StringBuilder unIns = new StringBuilder();
            for(int i = 0; i < lstUnInstalled.getModel().getSize(); i++)
            {
                dec = lstUnInstalled.getModel().getElementAt(i).toString();
                prefix = Main._libPath + dec;
//                (new File(prefix + "Validator.jar")).deleteOnExit();
//                (new File(prefix + "Pdf.jar")).deleteOnExit();
                if(i != 0)
                {
                    unIns.append("|");
                }
                unIns.append(dec);
            }
//            (new File(_configPath + Main._CURRENT_VERSIONS)).deleteOnExit();
            Params.setProperty("declaratiiDezinstalate", unIns.toString());
            if(_user != null)
            {
                Params.setProperty("user", _user);
            }
            else
            {
                Params.removeProperty("user");
            }
            if(_pwd != null)
            {
                Params.setProperty("pwd", _pwd);
            }
            else
            {
                Params.removeProperty("pwd");
            }
            if(_domain != null)
            {
                Params.setProperty("domain", _domain);
            }
            else
            {
                Params.removeProperty("domain");
            }
            if(_authentication != null)
            {
                Params.setProperty("authentication", _authentication);
            }
            else
            {
                Params.removeProperty("authentication");
            }
            if(_proxy != null && _proxy.equals("auto") == true)
            {
                Params.setProperty("proxy", "auto");
            }
            else
            {
                Params.removeProperty("proxy");
            }
            if(_proxyIP != null && _proxyIP.equals("") == false)
            {
                Params.setProperty("proxyIP", _proxyIP);
            }
            else
            {
                Params.removeProperty("proxyIP");
            }
            if(_proxyPort != null && _proxyPort.equals("") == false)
            {
                Params.setProperty("proxyPort", _proxyPort);
            }
            else
            {
                Params.removeProperty("proxyPort");
            }
            if(_ignoraDeclaratiiNoi != null && _ignoraDeclaratiiNoi.equals("D") == true)
            {
                Params.setProperty("ignoraDeclaratiiNoi", _ignoraDeclaratiiNoi);
            }
            else
            {
                Params.removeProperty("ignoraDeclaratiiNoi");
            }
            if(_urlVersiuni != null)
            {
                Params.setProperty("urlVersiuni", _urlVersiuni);
            }
            else
            {
                Params.removeProperty("urlVersiuni");
            }
            if(_offLine != null)
            {
                Params.setProperty("offLine", _offLine);
            }
            else
            {
                Params.removeProperty("offLine");
            }
            if(_javaStartPrefix != null && _javaStartPrefix.equals("") == false)
            {
                Params.setProperty("javaStartPrefix", _javaStartPrefix);
            }
            else
            {
                Params.removeProperty("javaStartPrefix");
            }
            if(_javaVersion != null && _javaVersion.equals("") == false)
            {
                Params.setProperty("javaVersion", _javaVersion);
            }
            else
            {
                Params.removeProperty("javaVersion");
            }
            Params.close();
        }
        catch(Throwable e)
        {
        }
    }

    public static String adjustString(String par)
    {
        return (par == null) ? "" : par;
    }

    public String chooseZipFile(String xmlFile, int zipOption)
    {
        return getZipFile(xmlFile, zipOption);
    }

    public String getZipFile(String xmlFile, int zipOption)
    {
        String zipFile = null;
        String text = null;
        int rez = 0;
        if(zipOption < 0)
        {
            return "";
        }
        else if(zipOption == 0)
        {
            text = "(optional)";
        }
        else
        {
            text = "(obligatoriu)";
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
//            ctlFileChooser.setSelectedFile(file);
        }
        ctlFileChooser.setDialogTitle("alegeti un fisier zip pt. atasare la fisierul pdf "
            + text);
        ctlFileChooser.resetChoosableFileFilters();
        ctlFileChooser.setFileFilter(new FileNameExtensionFilter("fisiere ZIP", "zip"));
        ctlFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        ctlFileChooser.setAcceptAllFileFilterUsed(false);
        if((rez = ctlFileChooser.showOpenDialog(this)) == JFileChooser.APPROVE_OPTION)
        {
            return ctlFileChooser.getSelectedFile().getPath();
        }
        if(zipOption == 0)
        {
            return "";
        }
        else
        {
            //eroare: zip obligatoriu dar selectia refuzata
            return null;
        }
    }

    private void btnValidationMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_btnValidationMouseClicked
    {//GEN-HEADEREND:event_btnValidationMouseClicked
        if(btnValidation.isEnabled() == true)
        {
            processFiles(0);
        }
    }//GEN-LAST:event_btnValidationMouseClicked

    private void btnGetFileNameMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_btnGetFileNameMouseClicked
    {//GEN-HEADEREND:event_btnGetFileNameMouseClicked
        if(btnGetFileName.isEnabled() == false)
        {
            return;
        }
        ctlFileChooser.setDialogTitle("Alegeti un fisier XML sau PDF sau un FOLDER (pentru toate fisierele XML si PDF)");
        ctlFileChooser.resetChoosableFileFilters();
//        ctlFileChooser.setFileFilter(new FileNameExtensionFilter("fisiere XML", "xml"));
        ctlFileChooser.resetChoosableFileFilters();
        ctlFileChooser.setAcceptAllFileFilterUsed(false);
        FileFilter filter = null;
        ctlFileChooser.addChoosableFileFilter(filter = new ExtensionFileFilter(
            new String[]
            {
                ".XML"
            },
            "Fisiere XML (*.XML)"));
        ctlFileChooser.addChoosableFileFilter(new ExtensionFileFilter(
            new String[]
            {
                ".PDF"
            },
            "Fisiere PDF (*.PDF)"));
        ctlFileChooser.addChoosableFileFilter(new ExtensionFileFilter(
            new String[]
            {
                ".XML", ".PDF"
            },
            "Fisiere XML si PDF (*.XML|*.PDF)"));
        ctlFileChooser.setFileFilter(filter);
        ctlFileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
//        ctlFileChooser.setAcceptAllFileFilterUsed(false);
        ctlFileChooser.setSelectedFile(null);
        if(ctlFileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
        {
            _xmlFile = ctlFileChooser.getSelectedFile().getPath();
            _crtFilter = (java.io.FileFilter)ctlFileChooser.getFileFilter();
            txtFileName.setText(_xmlFile);
            setVisibleValidationButton();
        }
    }//GEN-LAST:event_btnGetFileNameMouseClicked

    private void txtFileNameKeyReleased(java.awt.event.KeyEvent evt)//GEN-FIRST:event_txtFileNameKeyReleased
    {//GEN-HEADEREND:event_txtFileNameKeyReleased
        setVisibleValidationButton();
        _xmlFile = txtFileName.getText();
    }//GEN-LAST:event_txtFileNameKeyReleased

    private void btnPDFMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_btnPDFMouseClicked
    {//GEN-HEADEREND:event_btnPDFMouseClicked
        if(btnPDF.isEnabled() == true)
        {
            processFiles(1);
        }
    }//GEN-LAST:event_btnPDFMouseClicked

    private void btnSignMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_btnSignMouseClicked
    {//GEN-HEADEREND:event_btnSignMouseClicked
        if(btnSign.isEnabled() == true)
        {
            processFiles(2);
        }
    }//GEN-LAST:event_btnSignMouseClicked

    private void txtPinKeyReleased(java.awt.event.KeyEvent evt)//GEN-FIRST:event_txtPinKeyReleased
    {//GEN-HEADEREND:event_txtPinKeyReleased
        signStatus();
        _integrator.setNoCertificate();
    }//GEN-LAST:event_txtPinKeyReleased

    private void cmbCardTypeActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cmbCardTypeActionPerformed
    {//GEN-HEADEREND:event_cmbCardTypeActionPerformed
        signStatus();
        _integrator.setNoCertificate();
    }//GEN-LAST:event_cmbCardTypeActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt)//GEN-FIRST:event_formWindowClosing
    {//GEN-HEADEREND:event_formWindowClosing
        setConfigDefaults();
        _integrator.releaseToken();
    }//GEN-LAST:event_formWindowClosing

    private void btnOKActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnOKActionPerformed
    {//GEN-HEADEREND:event_btnOKActionPerformed
        //System.out.println("OK " + (String)cmbCertificates.getSelectedItem());
        wndChooseCertificate.setVisible(false);
    }//GEN-LAST:event_btnOKActionPerformed

    private void cmbCertificatesActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cmbCertificatesActionPerformed
    {//GEN-HEADEREND:event_cmbCertificatesActionPerformed
        // TODO add your handling code here:
        changeStatusCombo();
    }//GEN-LAST:event_cmbCertificatesActionPerformed

    private void cmbDecTypeActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cmbDecTypeActionPerformed
    {//GEN-HEADEREND:event_cmbDecTypeActionPerformed
        // TODO add your handling code here:
        _decType = cmbDecType.getSelectedItem().toString();
        _integrator.setDeclType(_decType);
        _zipOption = _integrator.getPdfZipOption();
        this.setTitle(_title.replaceAll("%", _decType).replaceAll("#", Main._version)
            + _integrator.getLastVersion()
            + " / " + _integrator.getPdfLastVersion());
        fillOptions();
    }//GEN-LAST:event_cmbDecTypeActionPerformed

    private void cmbOptionsItemStateChanged(java.awt.event.ItemEvent evt)//GEN-FIRST:event_cmbOptionsItemStateChanged
    {//GEN-HEADEREND:event_cmbOptionsItemStateChanged
        // TODO add your handling code here:
        if(evt.getStateChange() == ItemEvent.SELECTED)
        {
            try
            {
                Options option = (Options)cmbOptions.getSelectedItem();
                String opt = adjustString(option._comments);
                if(opt.equals("") == false && _flag == true)
                {
                    cmbOptions.hidePopup();
                    cmbDecType.hidePopup();
                    JOptionPane.showMessageDialog(rootPane, opt, "Atentie!", JOptionPane.WARNING_MESSAGE);
//                    System.out.println("salut");
                }
                Params.setProperty("_" + _decType, option._name);
                _integrator.useOptions(option._options);
            }
            catch(Throwable e)
            {
            }
        }
    }//GEN-LAST:event_cmbOptionsItemStateChanged

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnCancelActionPerformed
    {//GEN-HEADEREND:event_btnCancelActionPerformed
        // TODO add your handling code here:
        _stopProcessing = true;
    }//GEN-LAST:event_btnCancelActionPerformed

    private void mniExitActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_mniExitActionPerformed
    {//GEN-HEADEREND:event_mniExitActionPerformed
        //setConfigDefaults();
        WindowEvent windowClosing = new WindowEvent(this, WindowEvent.WINDOW_CLOSING);
        this.dispatchEvent(windowClosing);
    }//GEN-LAST:event_mniExitActionPerformed

    private void moveAll(JList dest, JList sou)
    {
        DefaultListModel lstModelDest = (DefaultListModel)dest.getModel();
        DefaultListModel lstModelSou = (DefaultListModel)sou.getModel();
        for(int i = 0; i < lstModelSou.getSize(); i++)
        {
            lstModelDest.addElement(lstModelSou.getElementAt(i));
        }
        lstModelSou.removeAllElements();
        //setButtonStatus();
        dest.setSelectedIndex(0);
    }

    private void moveOne(JList dest, JList sou)
    {
        DefaultListModel lstModelDest = (DefaultListModel)dest.getModel();
        DefaultListModel lstModelSou = (DefaultListModel)sou.getModel();
        int i = sou.getSelectedIndex();
        if(i < 0)
        {
            return;
        }
        lstModelDest.addElement(lstModelSou.getElementAt(i));
        lstModelSou.removeElementAt(i);
        //setButtonStatus();
        int j = sou.getModel().getSize();
        if(j > 0)
        {
            if(i >= j)
            {
                i = j - 1;
            }
            sou.setSelectedIndex(i);
        }
    }

    private void setMoveButtons()
    {
        btnUnInstall.setEnabled(false);
        btnUnInstallAll.setEnabled(false);
        btnUnInstallNothing.setEnabled(false);
        btnUnInstallDec.setEnabled(false);
        btnKeepDec.setEnabled(false);
        if(lstUnInstalled.getModel().getSize() != 0)
        {
            btnUnInstall.setEnabled(true);
            btnUnInstallNothing.setEnabled(true);
            btnKeepDec.setEnabled(true);
        }
        if(lstKept.getModel().getSize() != 0)
        {
            btnUnInstallAll.setEnabled(true);
            btnUnInstallDec.setEnabled(true);
        }
    }

    private void btnUnInstallAllActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnUnInstallAllActionPerformed
    {//GEN-HEADEREND:event_btnUnInstallAllActionPerformed
        //muta toate declaratiile in categoria dezinstalate
        moveAll(lstUnInstalled, lstKept);
        setMoveButtons();
    }//GEN-LAST:event_btnUnInstallAllActionPerformed

    private void btnUnInstallNothingActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnUnInstallNothingActionPerformed
    {//GEN-HEADEREND:event_btnUnInstallNothingActionPerformed
        //muta toate declaratiile in categoria instalate
        moveAll(lstKept, lstUnInstalled);
        setMoveButtons();
    }//GEN-LAST:event_btnUnInstallNothingActionPerformed

    private void btnUnInstallDecActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnUnInstallDecActionPerformed
    {//GEN-HEADEREND:event_btnUnInstallDecActionPerformed
        //muta o declaratie in categoria dezinstalate
        moveOne(lstUnInstalled, lstKept);
        setMoveButtons();
    }//GEN-LAST:event_btnUnInstallDecActionPerformed

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnCloseActionPerformed
    {//GEN-HEADEREND:event_btnCloseActionPerformed
        //inchide forma dezinstalare declaratii
        //refacere stare inainte de apel
        lstKept.setModel(_lstKeepModel);
        lstUnInstalled.setModel(_lstUnInstalledModel);
        wndUnInstallDec.setVisible(false);
    }//GEN-LAST:event_btnCloseActionPerformed

    private void btnUnInstallActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnUnInstallActionPerformed
    {//GEN-HEADEREND:event_btnUnInstallActionPerformed
        //dezinstalare declaratii
        wndUnInstallDec.setVisible(false);
    }//GEN-LAST:event_btnUnInstallActionPerformed

    private void mniUnInstallDecActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_mniUnInstallDecActionPerformed
    {//GEN-HEADEREND:event_mniUnInstallDecActionPerformed
        //activare meniu dezinstalare declaratii
        //salvare stare curenta a controalelor lst...
        DefaultListModel model = (DefaultListModel)lstKept.getModel();
        _lstKeepModel = new DefaultListModel();
        for(int i = 0; i < model.getSize(); i++)
        {
            _lstKeepModel.addElement(model.getElementAt(i));
        }
        model = (DefaultListModel)lstUnInstalled.getModel();
        _lstUnInstalledModel = new DefaultListModel();
        for(int i = 0; i < model.getSize(); i++)
        {
            _lstUnInstalledModel.addElement(model.getElementAt(i));
        }
        setMoveButtons();
        wndUnInstallDec.setVisible(true);
    }//GEN-LAST:event_mniUnInstallDecActionPerformed

    private void btnKeepDecActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnKeepDecActionPerformed
    {//GEN-HEADEREND:event_btnKeepDecActionPerformed
        //muta o declaratie in categoria instalate
        moveOne(lstKept, lstUnInstalled);
        setMoveButtons();
    }//GEN-LAST:event_btnKeepDecActionPerformed

    private void wndUnInstallDecWindowClosing(java.awt.event.WindowEvent evt)//GEN-FIRST:event_wndUnInstallDecWindowClosing
    {//GEN-HEADEREND:event_wndUnInstallDecWindowClosing
        wndUnInstallDec.setVisible(false);
    }//GEN-LAST:event_wndUnInstallDecWindowClosing

    private void chkUserPwdActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_chkUserPwdActionPerformed
    {//GEN-HEADEREND:event_chkUserPwdActionPerformed
        boolean flag = chkUserPwd.isSelected();
        txtUser.setEnabled(flag);
        txtPwd.setEnabled(flag);
        txtDomain.setEnabled(flag);
        chkAuthentication.setEnabled(flag);
    }//GEN-LAST:event_chkUserPwdActionPerformed

    private void mniOptionsActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_mniOptionsActionPerformed
    {//GEN-HEADEREND:event_mniOptionsActionPerformed
        txtUser.setText("");
        txtPwd.setText("");
        txtDomain.setText("");
        chkAuthentication.setSelected(false);
        if(_user == null && _pwd == null && _domain == null)
        {
            txtUser.setEnabled(false);
            txtPwd.setEnabled(false);
            txtDomain.setEnabled(false);
            chkAuthentication.setEnabled(false);
            chkUserPwd.setSelected(false);
        }
        else
        {
            txtUser.setEnabled(true);
            txtPwd.setEnabled(true);
            txtDomain.setEnabled(true);
            chkAuthentication.setEnabled(true);
            if(_user != null)
            {
                txtUser.setText(_user);
            }
            if(_pwd != null)
            {
                txtPwd.setText(_pwd);
            }
            if(_domain != null)
            {
                txtDomain.setText(_domain);
            }
            if(_authentication != null)
            {
                chkAuthentication.setSelected(true);
            }
            chkUserPwd.setSelected(true);
        }
        if(_proxy != null && _proxy.equals("auto") == true)
        {
            chkAuto.setSelected(true);
        }
        else
        {
            chkAuto.setSelected(false);
        }
        txtProxyIP.setText("");
        txtProxyPort.setText("");
        if(_proxyIP != null && _proxyIP.equals("") == false)
        {
            txtProxyIP.setText(_proxyIP);
            if(_proxyPort != null && _proxyPort.equals("") == false)
            {
                try
                {
                    long port = Long.parseLong(_proxyPort);
                    txtProxyPort.setText(Long.toString(port));
                }
                catch(Throwable ex)
                {
                    txtProxyPort.setText("80");
                }
            }
            else
            {
                txtProxyPort.setText("80");
            }
        }
        if(_ignoraDeclaratiiNoi == null || _ignoraDeclaratiiNoi.equals("D") == false)
        {
            chkNewDeclarations.setSelected(true);
        }
        else
        {
            chkNewDeclarations.setSelected(false);
        }
        if(_javaStartPrefix != null)
        {
            txtJavaStart.setText(_javaStartPrefix);
        }
        if(_javaVersion != null)
        {
            txtJavaVersion.setText(_javaVersion);
        }
        if(_urlVersiuni == null)
        {
            chkOffLine.setSelected(true);
            chkOffLine.setEnabled(false);
            changeState(false);
            JOptionPane.showMessageDialog(null,
                "Lipsa urlVersion. Sunteti in mod off line si nu puteti folosi optiunile java start si versiune java."
                + _newLine
                + "Lansati java dintr-un .BAT sau in mod linie comanda, de ex:"
                + _newLine
                + "java -version:1.6 -Xmx1024M -jar DUKIntegrator.jar");
        }
        else
        {
            boolean sw = (_offLine == null) ? true : false;
            chkOffLine.setSelected(!sw);
            changeState(sw);
        }
        wndOptions.setVisible(true);
    }//GEN-LAST:event_mniOptionsActionPerformed

    private void wndOptionsWindowClosing(java.awt.event.WindowEvent evt)//GEN-FIRST:event_wndOptionsWindowClosing
    {//GEN-HEADEREND:event_wndOptionsWindowClosing
        wndOptions.setVisible(false);
    }//GEN-LAST:event_wndOptionsWindowClosing

    private void btnAcceptActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnAcceptActionPerformed
    {//GEN-HEADEREND:event_btnAcceptActionPerformed
        _user = null;
        _pwd = null;
        _domain = null;
        _authentication = null;
        if(chkUserPwd.isSelected() == true)
        {
            _user = txtUser.getText();
            _pwd = txtPwd.getText();
            _domain = txtDomain.getText();
            if(chkAuthentication.isSelected() == true)
            {
                _authentication = "Y";
            }
        }
        if(chkAuto.isSelected() == true)
        {
            _proxy = "auto";
        }
        else
        {
            _proxy = null;
        }
        _proxyIP = txtProxyIP.getText();
        _proxyPort = txtProxyPort.getText();
        if(chkNewDeclarations.isSelected() == false)
        {
            _ignoraDeclaratiiNoi = "D";
        }
        else
        {
            _ignoraDeclaratiiNoi = "N";
        }
        _javaStartPrefix = txtJavaStart.getText();
        if(_javaStartPrefix != null && _javaStartPrefix.equals("") == true)
        {
            _javaStartPrefix = null;
        }
        _javaVersion = txtJavaVersion.getText();
        if(_javaVersion != null && _javaVersion.equals("") == true)
        {
            _javaVersion = null;
        }
        wndOptions.setVisible(false);
    }//GEN-LAST:event_btnAcceptActionPerformed

    private void btnKeepOldValuesActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnKeepOldValuesActionPerformed
    {//GEN-HEADEREND:event_btnKeepOldValuesActionPerformed
        wndOptions.setVisible(false);
    }//GEN-LAST:event_btnKeepOldValuesActionPerformed

    private void mniHelpActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_mniHelpActionPerformed
    {//GEN-HEADEREND:event_mniHelpActionPerformed
        if(Desktop.isDesktopSupported())
        {
            try
            {
                Desktop.getDesktop().open(new File(_rootPath + "ajutor.chm"));
            }
            catch(Throwable ex)
            {
                JOptionPane.showMessageDialog(null, "Nu pot deschide fisierul: " + ex.toString());
            }
        }
        else
        {
            JOptionPane.showMessageDialog(null, "Nu pot deschide fisierul ajutor.chm pe aceasta platforma");
        }
//        if(_frmAjutor == null)
//        {
//            _frmAjutor = new Ajutor(_rootPath);
//        }
//        _frmAjutor.setVisible(true);
//        if(_frmAjutor.stare < 0)
//        {
//            _frmAjutor.dispose();
//        }
    }//GEN-LAST:event_mniHelpActionPerformed

    private void mniHistoryActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_mniHistoryActionPerformed
    {//GEN-HEADEREND:event_mniHistoryActionPerformed
        if(_frmIstorie == null)
        {
            _frmIstorie = new Istoric(_rootPath, _decList);
        }
        _frmIstorie.setVisible(true);
        if(_frmIstorie.stare < 0)
        {
            _frmIstorie.dispose();
        }
    }//GEN-LAST:event_mniHistoryActionPerformed

    private void changeState(boolean flag)
    {
        boolean flag1 = flag & chkUserPwd.isSelected();
        txtJavaStart.setEnabled(flag);
        txtJavaVersion.setEnabled(flag);
        txtProxyIP.setEnabled(flag);
        txtProxyPort.setEnabled(flag);
        txtPwd.setEnabled(flag1);
        txtUser.setEnabled(flag1);
        txtDomain.setEnabled(flag1);
        chkAuthentication.setEnabled(flag1);
        chkAuto.setEnabled(flag);
        chkNewDeclarations.setEnabled(flag);
        chkUserPwd.setEnabled(flag);
        jLabel4.setEnabled(flag);
        jLabel5.setEnabled(flag);
        jLabel6.setEnabled(flag);
        jLabel7.setEnabled(flag);
        jLabel8.setEnabled(flag);
        jLabel9.setEnabled(flag);
        jLabel10.setEnabled(flag);
        jLabel11.setEnabled(flag);
        jLabel12.setEnabled(flag);
    }

    private void chkOffLineActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_chkOffLineActionPerformed
    {//GEN-HEADEREND:event_chkOffLineActionPerformed
        // TODO add your handling code here:
        boolean flag = chkOffLine.isSelected();
        changeState(!flag);
        if(flag == true)
        {
            _offLine = "Y";
            JOptionPane.showMessageDialog(null,
                "In mod off line nu puteti folosi optiunile java start si versiune java."
                + _newLine
                + "Lansati java dintr-un .BAT sau in mod linie comanda, de ex:"
                + _newLine
                + "java -version:1.6 -Xmx1024M -jar DUKIntegrator.jar");
        }
        else
        {
            _offLine = null;
        }
    }//GEN-LAST:event_chkOffLineActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAccept;
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnGetFileName;
    private javax.swing.JButton btnKeepDec;
    private javax.swing.JButton btnKeepOldValues;
    private javax.swing.JButton btnOK;
    private javax.swing.JButton btnPDF;
    private javax.swing.JButton btnSign;
    private javax.swing.JButton btnUnInstall;
    private javax.swing.JButton btnUnInstallAll;
    private javax.swing.JButton btnUnInstallDec;
    private javax.swing.JButton btnUnInstallNothing;
    private javax.swing.JButton btnValidation;
    private javax.swing.JCheckBox chkAuthentication;
    private javax.swing.JCheckBox chkAuto;
    private javax.swing.JCheckBox chkNewDeclarations;
    private javax.swing.JCheckBox chkOffLine;
    private javax.swing.JCheckBox chkUserPwd;
    private javax.swing.JComboBox cmbCardType;
    private javax.swing.JComboBox cmbCertificates;
    private javax.swing.JComboBox cmbDecType;
    private javax.swing.JComboBox cmbOptions;
    private javax.swing.JFileChooser ctlFileChooser;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblCardType;
    private javax.swing.JLabel lblEndDate;
    private javax.swing.JLabel lblFurnizor;
    private javax.swing.JLabel lblHelp;
    private javax.swing.JLabel lblKept;
    private javax.swing.JLabel lblNume;
    private javax.swing.JLabel lblOptions;
    private javax.swing.JLabel lblPin;
    private javax.swing.JLabel lblSerialNumber;
    private javax.swing.JLabel lblStartDate;
    private javax.swing.JLabel lblUnInstallMain;
    private javax.swing.JLabel lblUnInstalled;
    private javax.swing.JList lstKept;
    private javax.swing.JList lstUnInstalled;
    private javax.swing.JMenuItem mniExit;
    private javax.swing.JMenuItem mniHelp;
    private javax.swing.JMenuItem mniHistory;
    private javax.swing.JMenuItem mniOptions;
    private javax.swing.JMenuItem mniUnInstallDec;
    private javax.swing.JMenu mnuFile;
    private javax.swing.JMenu mnuHelp;
    private javax.swing.JMenuBar mnuMain;
    private javax.swing.JMenu mnuTools;
    private javax.swing.JProgressBar pgrBar;
    private javax.swing.JScrollPane pnlResults;
    private javax.swing.JTextField txtDomain;
    private javax.swing.JTextField txtEndDate;
    private javax.swing.JTextField txtFileName;
    private javax.swing.JTextField txtFurnizor;
    private javax.swing.JTextField txtJavaStart;
    private javax.swing.JTextField txtJavaVersion;
    private javax.swing.JTextField txtNume;
    private javax.swing.JPasswordField txtPin;
    private javax.swing.JTextField txtProxyIP;
    private javax.swing.JTextField txtProxyPort;
    private javax.swing.JPasswordField txtPwd;
    private javax.swing.JTextArea txtResults;
    private javax.swing.JTextField txtSerialNumber;
    private javax.swing.JTextField txtStartDate;
    private javax.swing.JTextField txtUser;
    private javax.swing.JDialog wndChooseCertificate;
    private javax.swing.JDialog wndOptions;
    private javax.swing.JDialog wndUnInstallDec;
    // End of variables declaration//GEN-END:variables
    static public String _newLine = System.getProperty("line.separator");
    static private final String _AUTO_DETECT = "*autoDetect";
    static private final String _title = "DUKIntegrator - versiune: # - % - versiune validator: ";
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
    private String _configPath = "";
    private String _rootPath = "";
    private String _xmlFile = "";
    private String _errFile = "";
//    private Properties _props = null;
    private boolean _flag = true;
    private String _decType = null;
    private String _defDecl = null;
    private Integrator _integrator = null;
    public String _urlVersiuni = null;
    public String _offLine = null;
    public String _javaStartPrefix = null;
    public String _javaVersion = null;
    public String _user = null;
    public String _pwd = null;
    public String _domain = null;
    public String _authentication = null;
    public String _ignoraDeclaratiiNoi = null;
    public String _proxy = null;
    public String _proxyIP = null;
    public String _proxyPort = null;
    private int _processingType;
    private boolean _stopProcessing = false;
    private boolean _getEnableOptions;
    private boolean _getEnableSign;
    private int _zipOption;
    private java.io.FileFilter _crtFilter = null;
    private DefaultListModel _lstKeepModel = null;
    private DefaultListModel _lstUnInstalledModel = null;
    private Ajutor _frmAjutor = null;
    private Istoric _frmIstorie = null;
    private String[] _decList = null;

    //clasa extinde o clasa abstracta si implementeaza o interfata care au
    //  in comun o metoda (accept()) cu aceeasi semnatura. Aceasta metoda
    //  admite, din fericire, aceeasi implementare (altfel convietuirea celor
    //  2 clasa/interfata nu pare posibila). Metoda este definita o singura data
    static class ExtensionFileFilter extends FileFilter
        implements java.io.FileFilter
    {
        private java.util.List<String> extensions;
        private String description;

        public ExtensionFileFilter(String[] exts, String desc)
        {
            if(exts != null)
            {
                extensions = new java.util.ArrayList<String>();

                for(String ext: exts)
                {
                    extensions.add(ext.replace(".", "").trim().toLowerCase());
                }
            }
            description = (desc != null) ? desc.trim() : "";
        }

        @Override
        public boolean accept(File f)
        {

            if(f.isDirectory())
            {
                return true;
            }
            if(extensions == null)
            {
                return false;
            }
            for(String ext: extensions)
            {
                if(f.getName().toLowerCase().endsWith("." + ext))
                {
                    return true;
                }
            }
            return false;
        }

        @Override
        public String getDescription()
        {
            return description;
        }
    }
}
