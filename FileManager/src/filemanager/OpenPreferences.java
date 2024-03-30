/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package filemanager;

import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 *
 * @author ivan
 */
public class OpenPreferences {
    public static String getSuffix(String file){
        if(!file.contains("."))
            return "noextensions";
        return file.substring(file.lastIndexOf("."));
    }
    public static String getDefaultForExtension(String name){
        String suffix=getSuffix(name);
        CommandManager cmm=new CommandManager();
        Vector<String> commands=cmm.getVectorS(suffix);
        if(commands.isEmpty())
            return null;
        return commands.firstElement();
    }
    public static void runCommand(String command,String file){
        
            String scmd[]=command.split(" ");
              int add=1;
              if(file==null)
                  add=0;
             String newsl[]=new String[scmd.length+add];
             
             int i=0;
             for(String cmdc:scmd){
                 newsl[i]=cmdc;
                 i++;
             }
             if(file!=null)
                 newsl[scmd.length]=file;
                
            
            try {
                ProcessBuilder psb=new ProcessBuilder(newsl);
                psb.start();
                
            } catch (IOException ex) {
                Logger.getLogger(mainView.class.getName()).log(Level.SEVERE, null, ex);
            }
    }
    public static void LaunchDialogue(File in,Component parent,String customsuffix){
        
        String esuffix="";
        if(in!=null)
                esuffix=getSuffix(in.getName());
        if(customsuffix!=null)
             esuffix=customsuffix;
        String suffix=esuffix;
        
        CommandManager cmm=new CommandManager();
        Vector<String> commands=cmm.getVectorS(suffix);
        JPanel jshow=new JPanel();
        jshow.setLayout(new GridLayout(1,2));
        JPanel jbtn=new JPanel();
        jbtn.setLayout(new GridLayout(5,1));
        JList jl=new JList();
        jl.setListData(commands);
        jshow.add(jl);
        jl.setSelectedIndex(0);
        JButton add=new JButton("add");
        JButton remove=new JButton("remove");
        
        JButton launch=new JButton("launch");
        
        JButton sup=new JButton("shift down");
        JButton edit=new JButton("edit");
        jbtn.add(add);
        jshow.add(jbtn);
        jbtn.add(remove);
        jbtn.add(launch);
        jbtn.add(sup);
        jbtn.add(edit);
        edit.addActionListener((ActionEvent e)->{
             String command=(String)jl.getSelectedValue();
             if(command!=null){
                 String prompt=JOptionPane.showInputDialog(parent,"edit",command);
                 if(prompt!=null){
                     if(!commands.contains(prompt)){
                         commands.remove(command);
                         commands.add(prompt);
                         cmm.clearVector(suffix);
                         cmm.putVectorS(suffix, commands);
                         jl.setListData(commands);
                     }
                 }
             }
        });
        
        add.addActionListener((ActionEvent e) -> {
            String cmdprompt=JOptionPane.showInputDialog("open command");
            if(cmdprompt!=null&&!commands.contains(cmdprompt)){
                commands.add(cmdprompt);
                cmm.clearVector(suffix);
                cmm.putVectorS(suffix, commands);
                jl.setListData(commands);
            }
        });
        launch.addActionListener((ActionEvent e) -> {
            String command=(String)jl.getSelectedValue();
            SwingUtilities.getWindowAncestor(launch).dispose();
            if(command!=null){
                if(in==null)
                     runCommand(command,null);
                else
                    runCommand(command,in.getAbsolutePath());
            }
            
        });
        remove.addActionListener((ActionEvent e) -> {
            String command=(String)jl.getSelectedValue();
            if(command!=null){
               commands.remove(command);
               cmm.clearVector(suffix);
               cmm.putVectorS(suffix, commands);
               jl.setListData(commands);
            }
        });
        sup.addActionListener((ActionEvent e)->{
            String command=(String)jl.getSelectedValue();
            if(command!=null&&commands.size()>1){
               commands.remove(command);
               commands.add(command);
               cmm.clearVector(suffix);
               cmm.putVectorS(suffix, commands);
               jl.setListData(commands);
            }
        });
        
        JOptionPane.showMessageDialog(parent, jshow);
        
    }
}
