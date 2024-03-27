package filemanager;

import java.awt.event.ActionEvent;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */

/**
 *
 * @author ivan
 */
public class mainView extends javax.swing.JFrame {
    private static DirectoryView drview;
    private static DHistory dh;
    private static Vector<String> pathprefs=null;
    /**
     * Creates new form mainView
     */
    public mainView() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jButton5 = new javax.swing.JButton();
        jSplitPane1 = new javax.swing.JSplitPane();
        jPanel1 = new javax.swing.JPanel();
        jTextField1 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        Contain = new javax.swing.JScrollPane();
        FContain = new javax.swing.JPanel();
        jButton4 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jButton3 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList<>();

        jButton5.setText("jButton5");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("JFileManager");
        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
        });

        jSplitPane1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jSplitPane1KeyPressed(evt);
            }
        });

        jPanel1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jPanel1KeyPressed(evt);
            }
        });

        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });
        jTextField1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextField1KeyPressed(evt);
            }
        });

        jButton1.setText("<");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText(">");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        Contain.setAutoscrolls(true);
        Contain.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                ContainKeyPressed(evt);
            }
        });

        FContain.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                FContainMouseDragged(evt);
            }
        });
        FContain.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                FContainKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout FContainLayout = new javax.swing.GroupLayout(FContain);
        FContain.setLayout(FContainLayout);
        FContainLayout.setHorizontalGroup(
            FContainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 727, Short.MAX_VALUE)
        );
        FContainLayout.setVerticalGroup(
            FContainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 444, Short.MAX_VALUE)
        );

        Contain.setViewportView(FContain);

        jButton4.setText("term");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/filemanager/paste.png"))); // NOI18N
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jButton7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/filemanager/refresh.png"))); // NOI18N
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jButton8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/filemanager/search.png"))); // NOI18N
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        jButton9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/filemanager/new.png"))); // NOI18N
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jTextField1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton9))
            .addComponent(Contain)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButton1)
                                    .addComponent(jButton4)
                                    .addComponent(jButton2))
                                .addComponent(jButton6, javax.swing.GroupLayout.Alignment.TRAILING))
                            .addComponent(jButton7, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addComponent(jButton8, javax.swing.GroupLayout.Alignment.TRAILING))
                    .addComponent(jButton9))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Contain, javax.swing.GroupLayout.DEFAULT_SIZE, 455, Short.MAX_VALUE))
        );

        jSplitPane1.setRightComponent(jPanel1);

        jButton3.setText("+");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jList1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jList1MouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(jList1);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jButton3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 78, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 444, Short.MAX_VALUE))
        );

        jSplitPane1.setLeftComponent(jPanel2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jSplitPane1))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane1)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        String path=dh.getPath();
        if(path!=null){
            while(path.equals(drview.currentdir)){
                path=dh.getPath();
                if(path==null)
                    return;
            }
            drview.drawDirectory(path, true);
            jTextField1.setText(drview.currentdir);
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        String path=dh.getFWD();
        if(path!=null){
             while(path.equals(drview.currentdir)){
                path=dh.getFWD();
                if(path==null)
                    return;
            }
            drview.drawDirectory(path, true);
            jTextField1.setText(drview.currentdir);
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void jTextField1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyPressed
        // TODO add your handling code here:
        if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            String path=jTextField1.getText();
            path=path.replace("//","/");
            path=path.replace("\\\\","\\");
            path=path.replace("::",":");
            dh.addPath(path);
            drview.drawDirectory(path, true);
            jTextField1.setText(drview.currentdir);
        }
    }//GEN-LAST:event_jTextField1KeyPressed

    private void FContainKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_FContainKeyPressed
        // TODO add your handling code here:
        
    }//GEN-LAST:event_FContainKeyPressed

    private void ContainKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ContainKeyPressed
        // TODO add your handling code here:
       
    }//GEN-LAST:event_ContainKeyPressed

    private void formKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyPressed
        // TODO add your handling code here:
       
    }//GEN-LAST:event_formKeyPressed

    private void jSplitPane1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jSplitPane1KeyPressed
        // TODO add your handling code here:
        
    }//GEN-LAST:event_jSplitPane1KeyPressed

    private void jPanel1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jPanel1KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_jPanel1KeyPressed

    private void FContainMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_FContainMouseDragged
        // TODO add your handling code here:
       
    }//GEN-LAST:event_FContainMouseDragged

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        if((evt.getModifiers()|ActionEvent.SHIFT_MASK)==evt.getModifiers()){
             CommandManager cmd=new CommandManager();
             String prompt=JOptionPane.showInputDialog(this, "Open Terminal Command",cmd.getString("Terminal", ""));
             if(prompt!=null){
                 cmd.putString("Terminal", prompt);
                 cmd.flush();
             }
        }else{
             CommandManager cmd=new CommandManager();
             String cmdl=cmd.getString("Terminal", null);
             if(cmdl==null){
                 String prompt=JOptionPane.showInputDialog(this, "Open Terminal Command",cmd.getString("Terminal", ""));
                 if(prompt!=null){
                   cmd.putString("Terminal", prompt);
                   cmd.flush();
                   cmdl=prompt;
                 }
             }
             OpenPreferences.runCommand(cmdl, drview.currentdir);
             
             
        }
        
    }//GEN-LAST:event_jButton4ActionPerformed
    
    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        CommandManager cmm=new CommandManager();
        Vector<String> saveddirs=cmm.getVectorS("SavedDirs");
        if(!saveddirs.contains(drview.currentdir)){
              saveddirs.add(drview.currentdir);
              cmm.clearVector("SavedDirs");
              cmm.putVectorS("SavedDirs", saveddirs);
        }
        jList1.setListData(getListFormat(saveddirs));
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jList1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jList1MouseClicked
        // TODO add your handling code here:
        if(pathprefs==null)
            return;
        int selindex=jList1.getSelectedIndex();
        if(selindex==-1)
            return;
        
        String path=pathprefs.elementAt(selindex);
        if(evt.getButton()==java.awt.event.MouseEvent.BUTTON1){
            if(!path.equals(drview.currentdir)){
                dh.addPath(drview.currentdir);
                jTextField1.setText(path);
                drview.drawDirectory(path, true);
            }
            
        }
        if(evt.getButton()==java.awt.event.MouseEvent.BUTTON3){
            if(pathprefs.contains(path)){
                pathprefs.remove(path);
                CommandManager cmm=new CommandManager();
                cmm.clearVector("SavedDirs");
                cmm.putVectorS("SavedDirs", pathprefs);
                jList1.setListData(getListFormat(pathprefs));
            }
        }
    }//GEN-LAST:event_jList1MouseClicked

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        // TODO add your handling code here:
         FileOperations.CopyFromQueue(new File(drview.currentdir), this);
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        // TODO add your handling code here:
        drview.drawDirectory(drview.currentdir, true);
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        // TODO add your handling code here:
        String prompt=JOptionPane.showInputDialog(this, "search");
        if(prompt!=null){
            drview.drawDirectory(drview.currentdir, true,prompt);
        }
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        // TODO add your handling code here:
        File nf=new File(drview.currentdir+File.separator+"NewDir");
        int ic=0;
        while(nf.exists()){
          nf=new File(drview.currentdir+File.separator+"NewDir"+Integer.toString(ic));
          ic++;
        }
        nf.mkdir();
        drview.drawDirectory(drview.currentdir, true);
    }//GEN-LAST:event_jButton9ActionPerformed

    /**
     * @param args the command line arguments
     */
    private static String[] getListFormat(Vector<String> paths){
        pathprefs=paths;
        String out[]=new String[paths.size()];
        Enumeration<String> en=paths.elements();
        int i=0;
        while(en.hasMoreElements()){
            String outst=en.nextElement();
            if(outst.contains(File.separator))
                outst=outst.substring(outst.lastIndexOf(File.separator)+1);
            out[i]=outst;
            i++;
        }
        return out;
    }
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            
            String last="javax.swing.plaf.nimbus.NimbusLookAndFeel";
           
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
               last=info.getClassName();
            }
            javax.swing.UIManager.setLookAndFeel(last);
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(mainView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(mainView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(mainView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(mainView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display th form */
        java.awt.EventQueue.invokeLater(() -> {
            mainView mv=new mainView();
            try {
                mv.setIconImage(ImageIO.read(FileManager.class.getResourceAsStream("dir.png")));
            } catch (IOException ex) {
                Logger.getLogger(mainView.class.getName()).log(Level.SEVERE, null, ex);
            }
            CommandManager cmm=new CommandManager();
            Vector<String> saveddirs=cmm.getVectorS("SavedDirs");
            mv.jList1.setListData(getListFormat(saveddirs));
            dh=new DHistory();
            drview=new DirectoryView(mv.FContain,mv);
            String startpath=System.getProperty("user.home");
            drview.drawDirectory(startpath,false);
            jTextField1.setText(startpath);
            
            //dh.addPath(startpath);
            mv.addComponentListener(new ComponentListener() {
                @Override
                public void componentResized(ComponentEvent e) {
                    drview.drawDirectory(drview.currentdir, false);
                }
                
                @Override
                public void componentMoved(ComponentEvent e) {
                }
                
                @Override
                public void componentShown(ComponentEvent e) {
                }
                
                @Override
                public void componentHidden(ComponentEvent e) {
                }
            });
            drview.setDirChangeListener(new DirChangeListener(){
                @Override
                public void dirChanged(String dir,String prev) {
                    jTextField1.setText(dir);
                    dh.addPath(prev);
                    dh.addPath(dir);
                }
            });
            mv.setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JScrollPane Contain;
    public javax.swing.JPanel FContain;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    public javax.swing.JList<String> jList1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSplitPane jSplitPane1;
    public static javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables
}