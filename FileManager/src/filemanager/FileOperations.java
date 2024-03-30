/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package filemanager;

import java.awt.Component;
import java.awt.GridLayout;
import java.awt.Window;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Enumeration;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 *
 * @author ivan
 */
public class FileOperations {
    private static int overwrite=0;
    private static long files_copied=0;
    public static void delete(File file){
        if(file.isFile()){
            try{
             file.delete();
            }catch(Exception e){}
            return;
        }
        File list[]=file.listFiles();
        if(list==null)
            return;
        for(File f:list){
            delete(f);
        }
        try{
          file.delete();
        }catch(Exception e){}
    }
    private static void copy_binary(File src,File dest){
        try {
            FileInputStream in=new FileInputStream(src);
            FileOutputStream out=new FileOutputStream(dest);
            byte data[]=new byte[4096];
            int size;
            while((size=in.read(data))!=-1){
                out.write(data,0,size);
                
            }
            in.close();
            out.close();
        } catch (Exception ex) {
            Logger.getLogger(FileOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private static boolean file_exists_dialogue(File targ,Component parent){
        if(!targ.exists())
            return true;
        if(overwrite!=0)
            return overwrite>0;
        JCheckBox remember=new JCheckBox();
        remember.setText("remember my choice");
        JPanel jp=new JPanel();
        jp.setLayout(new GridLayout(2,1));
        jp.add(new JLabel("file "+targ.getAbsolutePath()+" already exists, overwrite?"));
        jp.add(remember);
        if(JOptionPane.showConfirmDialog(parent, jp, "confirm",JOptionPane.YES_NO_OPTION )==JOptionPane.YES_OPTION){
            if(remember.isSelected())
                 overwrite=1;
            return true;
        }
        if(remember.isSelected())
              overwrite=-1;
        return false;
    }
    private static void copy(File src,File dest,JLabel data_id){
        if(src.isFile()){
            File write=new File(dest.getAbsolutePath()+File.separator+src.getName());
            if(file_exists_dialogue(write,data_id)){
                copy_binary(src,write);
                files_copied++;
                data_id.setText("Files copied: "+Long.toString(files_copied));
                data_id.validate();
              
            }
            return;
        }
        File dlist[]=src.listFiles();
        for(File f:dlist){
            if(f.isDirectory()){
             File mkdirs=new File(dest.getAbsolutePath()+File.separator+f.getName());
             mkdirs.mkdir();
             copy(f,mkdirs,data_id);
            }else{
                copy(f,dest,data_id);
            }
            
        }
    }
    public static void copyFile(File src,File dest,JFrame dialogue_parent,boolean move){
        JLabel copy_data=new JLabel("starting");
        Thread run;
        
        JDialog jf=new JDialog(dialogue_parent);
        jf.setSize(200, 70);
        jf.setLocation(dialogue_parent.getX()+dialogue_parent.getWidth()/2-100, dialogue_parent.getY()+dialogue_parent.getHeight()/2-35);
        jf.setLayout(new GridLayout(1,1));
        jf.add(copy_data);
        jf.setVisible(true);
        run =new Thread(){
            @Override
            public void run(){
                copy(src,dest,copy_data);
                if(move)
                    delete(src);
                
               jf.dispose();
                
            }  
        };run.start();
        jf.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {
            }

            @Override
            public void windowClosing(WindowEvent e) {
            }

            @Override
            public void windowClosed(WindowEvent e) {
                if(run.isAlive()){
                    run.interrupt();
                }
            }

            @Override
            public void windowIconified(WindowEvent e) {
            }

            @Override
            public void windowDeiconified(WindowEvent e) {
            }

            @Override
            public void windowActivated(WindowEvent e) {
            }

            @Override
            public void windowDeactivated(WindowEvent e) {
            }
        });
    }
    
    public static void queueCopy(Vector<File> selection){
        CommandManager cmdm=new CommandManager();
        cmdm.clearVector("copyfiles");
        cmdm.putVector("copyfiles", selection);
    }
    public static void CopyFromQueue(File dest,JFrame parent,boolean move){
        overwrite=0;
        files_copied=0;
        CommandManager cmdm=new CommandManager();
        Enumeration<File> files=cmdm.getVector("copyfiles").elements();
        while(files.hasMoreElements()){
            File src=files.nextElement();
            File tdest=dest;
            if(src.isDirectory()){
                tdest=new File(dest.getAbsolutePath()+File.separator+src.getName());
                tdest.mkdir();
            }
            if(!src.getParent().equals(dest.getAbsolutePath()))
                  copyFile(src,tdest,parent,move);
        }
        cmdm.clearVector("copyfiles");
        cmdm.flush();
    }
     
}
