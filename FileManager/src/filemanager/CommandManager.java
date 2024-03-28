/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package filemanager;

import java.io.File;
import java.util.Enumeration;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

/**
 *
 * @author ivan
 */
public class CommandManager {
    private final Preferences prefs;
    public CommandManager(){
        prefs=Preferences.userNodeForPackage(FileManager.class);
    }
    public void putVector(String key,Vector<File> data){
        int i=0;
        Enumeration<File> enl=data.elements();
        while(enl.hasMoreElements()){
            File putkey=enl.nextElement();
            prefs.put(key+"\\"+Integer.toString(i), putkey.getAbsolutePath());
            i++;
        }
        try {
            prefs.flush();
        } catch (BackingStoreException ex) {
            Logger.getLogger(CommandManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void putVectorS(String key,Vector<String> data){
        int i=0;
        Enumeration<String> enl=data.elements();
        while(enl.hasMoreElements()){
            String putkey=enl.nextElement();
            prefs.put(key+"\\"+Integer.toString(i), putkey);
            i++;
        }
        try {
            prefs.flush();
        } catch (BackingStoreException ex) {
            Logger.getLogger(CommandManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void clearVector(String key){
        int i=0;
         while((prefs.get(key+"\\"+Integer.toString(i), null))!=null){
            prefs.remove(key+"\\"+Integer.toString(i));
            i++;
        }
    }
    public Vector<File> getVector(String key){
        int i=0;
        String pdata;
        Vector<File> ret=new Vector<>();
        while((pdata=prefs.get(key+"\\"+Integer.toString(i), null))!=null){
            ret.add(new File(pdata));
            i++;
        }
        return ret;
    }
    public Vector<String> getVectorS(String key){
        int i=0;
        String pdata;
        Vector<String> ret=new Vector<>();
        while((pdata=prefs.get(key+"\\"+Integer.toString(i), null))!=null){
            ret.add(pdata);
            i++;
        }
        return ret;
    }
    public void putString(String key,String data){
        String truekey=key.replace("\\", "");
        if(data==null)
            prefs.remove(truekey);
        else
            prefs.put(truekey, data);
    }
    public String getString(String key,String def){
        String truekey=key.replace("\\", "");
        return prefs.get(truekey, def);
    }
    public void flush(){
        try {
            prefs.flush();
        } catch (BackingStoreException ex) {
            Logger.getLogger(CommandManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
}
