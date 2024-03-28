/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package filemanager;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.Enumeration;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

/**
 *
 * @author ivan
 */
public class ContextMenu {
    private final JFrame parent;
    public ContextMenu(JFrame input){
        parent=input;
    }
    public void optionsDialogue(Vector<File> selection,File location,CustomIcons ics){
        if(selection.size()<1){
            return;
        }
        Enumeration<File> sel=selection.elements();
        JPanel options=new JPanel();
        String tooltip="selection:";
        while(sel.hasMoreElements()){
            File f=sel.nextElement();
            tooltip=tooltip+"|"+f.getName();
        }
        options.setLayout(new GridLayout(10,1));
        JTextField jb=new JTextField();
        jb.setText(selection.lastElement().getAbsolutePath());
        jb.setToolTipText(tooltip);
        options.add(jb);
        JButton open=new JButton("open");
        JButton iconss=new JButton("set default icon for file type");
        JButton iconsd=new JButton("remove default icon for file type");

        if(selection.size()<2){
            if(selection.lastElement().isFile())
                options.add(open);
               
            
        }
        open.addActionListener((ActionEvent e) -> {
            OpenPreferences.LaunchDialogue(selection.firstElement(),parent,null);
        });
        JButton custom=new JButton("run script");
        custom.addActionListener((ActionEvent e)->{
            OpenPreferences.LaunchDialogue(null,parent,"custom commands");
        });
        options.add(custom);
        JButton copy=new JButton("copy");
        copy.addActionListener((ActionEvent e) -> {
            FileOperations.queueCopy(selection);
            SwingUtilities.getWindowAncestor(copy).setVisible(false);
        });
        options.add(copy);
        JButton paste=new JButton("paste here");
        paste.addActionListener((ActionEvent e) -> {
             FileOperations.CopyFromQueue(location, parent);
             SwingUtilities.getWindowAncestor(paste).setVisible(false);
        });
        options.add(paste);
        JButton move=new JButton("move here");
        move.addActionListener((ActionEvent e) -> {
            FileOperations.MoveFromQueue(location, parent);
            SwingUtilities.getWindowAncestor(move).setVisible(false);
        });
        options.add(move);
        JButton delete=new JButton("delete");
        delete.addActionListener((ActionEvent e) -> {
            Enumeration<File> flist=selection.elements();
            while(flist.hasMoreElements()){
                File ele=flist.nextElement();
                FileOperations.delete(ele);
                SwingUtilities.getWindowAncestor(delete).setVisible(false);
            }
        });
        options.add(delete);
        JButton rename=new JButton("rename");
        if(selection.size()<2){
            options.add(rename);
            if(selection.lastElement().isFile()&&selection.lastElement().getName().substring(1).contains(".")){
                if(ics.getForExtension(selection.lastElement().getName(),1,1)==null)
                     options.add(iconss);
                else
                     options.add(iconsd);
            }

        }
        rename.addActionListener((ActionEvent e) -> {
            SwingUtilities.getWindowAncestor(rename).setVisible(false);
            File f=selection.firstElement();
            JTextField jft=new JTextField(f.getName());
            if(JOptionPane.showConfirmDialog(parent, jft, "rename", JOptionPane.OK_CANCEL_OPTION)==JOptionPane.OK_OPTION){
                String newname=jft.getText();
                if(!newname.isBlank()){
                    newname=newname.replace(File.separator, "");
                    File nf=new File(f.getParent()+File.separator+newname);
                    if((!nf.exists())||JOptionPane.showConfirmDialog(parent, "file "+newname+" already exists. Overwrite?", "", JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION){
                        f.renameTo(nf);
                    }
                }   
            }
        });
        iconss.addActionListener((ActionEvent e) -> {
            String path=JOptionPane.showInputDialog(parent, "path to the icon");
            if(path==null)
                return;
            ics.putForExtension(selection.lastElement().getName(),new File(path));
            SwingUtilities.getWindowAncestor(iconss).setVisible(false);

        });
        iconsd.addActionListener((ActionEvent e) -> {
            
            ics.removeForExtension(selection.lastElement().getName());
            SwingUtilities.getWindowAncestor(iconsd).setVisible(false);

        });
        JOptionPane.showMessageDialog(parent,options);
    }
}
