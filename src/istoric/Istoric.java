package istoric;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

public class Istoric extends javax.swing.JFrame
{
    public int stare = 0;
    public final String NEW_LINE = System.getProperty("line.separator");

    public Istoric(String cale, String[] decl)
    {
        initComponents();
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        Arrays.sort(decl);
        decl = Arrays.copyOf(decl, decl.length + 1);
        decl[decl.length - 1] = "DUKIntegrator";
        jTextArea1.setEditable(false);
        jTextArea1.setFont(new Font("Times New Roman", Font.PLAIN, 12));
        jTextArea1.setLineWrap(true);
        jTextArea1.setWrapStyleWord(true);
        jPanel3.setLayout(new BoxLayout(jPanel3, BoxLayout.Y_AXIS));
        JLabel[] etichete = new JLabel[decl.length];
        final String calea = cale + "doc/";
        MouseAdapter adapter = new MouseAdapter()
        {
            public void mouseReleased(MouseEvent e)
            {
                String text = ((JLabel)e.getComponent()).getText().trim();
                String s = null;
                setTitle(text);
                try
                {
                    s = "Istoria versiunilor pentru " + text + NEW_LINE
                        + NEW_LINE + readFileAsString(calea, text);
                }
                catch(Throwable ex)
                {
                    s = "Nu am putut identifica fisierul cu Istoricul declaratiei";
                }
                jTextArea1.setText(s);
            }
        };
        for(int i = 0; i < decl.length; i++)
        {
            etichete[i] = new JLabel("  " + decl[i]);
            etichete[i].setForeground(Color.blue);
            jPanel3.add(etichete[i]);
            jPanel3.add(Box.createVerticalStrut(10));
            etichete[i].addMouseListener(adapter);
        }
        try
        {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//            for(javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels())
//            {
//                if("Nimbus".equals(info.getName()))
//                {
//                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
//                    break;
//                }
//            }
        }
        catch(Throwable ex)
        {
            JOptionPane.showMessageDialog(null, "Eroare afisare istoric declaratii");
        }
//        catch(ClassNotFoundException ex)
//        {
//            JOptionPane.showMessageDialog(null, "Eroare afisare istoric declaratii");
//            //java.util.logging.Logger.getLogger(Istoric.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
//        catch(InstantiationException ex)
//        {
//            JOptionPane.showMessageDialog(null, "Eroare afisare istoric declaratii");
//            //java.util.logging.Logger.getLogger(Istoric.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
//        catch(IllegalAccessException ex)
//        {
//            JOptionPane.showMessageDialog(null, "Eroare afisare istoric declaratii");
//            //java.util.logging.Logger.getLogger(Istoric.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
//        catch(javax.swing.UnsupportedLookAndFeelException ex)
//        {
//            JOptionPane.showMessageDialog(null, "Eroare afisare istoric declaratii");
//            //java.util.logging.Logger.getLogger(Istoric.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
        setSize(800, 600);
    }

    private static String readFileAsString(String filePath, String declaratie) throws java.io.IOException
    {
        filePath = filePath + declaratie + "IstoriaVersiunilor.txt";

        byte[] buffer = new byte[(int)new File(filePath).length()];
        BufferedInputStream f = null;
        try
        {
            f = new BufferedInputStream(new FileInputStream(filePath));
            f.read(buffer);
        }
        finally
        {
            if(f != null)
            {
                try
                {
                    f.close();
                }
                catch(IOException ignored)
                {
                }
            }
        }
        return new String(buffer);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents()
    {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel3 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 117, Short.MAX_VALUE));
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 298, Short.MAX_VALUE));

        jScrollPane1.setViewportView(jPanel3);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 119, Short.MAX_VALUE));
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE));

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane2.setViewportView(jTextArea1);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 275, Short.MAX_VALUE));
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));

        pack();
    }// </editor-fold>
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextArea jTextArea1;
    // End of variables declaration
}
