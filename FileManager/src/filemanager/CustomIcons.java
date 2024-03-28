/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package filemanager;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author ivan
 */
public class CustomIcons {
    private final CommandManager cmd;
    private final Hashtable<String,BufferedImage> hsht;
    private final Vector<String> nullables;
    public CustomIcons(){
        cmd=new CommandManager();
        hsht=new Hashtable<>();
        nullables=new Vector<>();
    }
    public BufferedImage getForExtension(String name,int w,int h){
        
        String suffix=name.substring(name.lastIndexOf("."));
        if(nullables.contains(suffix))
            return null;
        if(hsht.contains(suffix)){
            return hsht.get(suffix);
        }
        String path=cmd.getString("ICON"+suffix, null);
        if(path==null){
            nullables.add(suffix);
            hsht.remove(suffix);
            return null;
        }
        try {
            BufferedImage icon=ImageIO.read(new File(path));
            icon=DirectoryView.clone_image(icon,w,h);
            if(w>1&&h>1)
                 hsht.put(suffix, icon);
            return icon;
            
        } catch (IOException ex) {
            Logger.getLogger(CustomIcons.class.getName()).log(Level.SEVERE, null, ex);
        }
        nullables.add(suffix);
        hsht.remove(suffix);
        return null;
    }
    public void putForExtension(String name,File image){
        String suffix=name.substring(name.lastIndexOf("."));
        BufferedImage icon;
        try{
        icon=ImageIO.read(image);
        }catch (IOException ex) {
          return;
        }
        hsht.put(suffix, icon);
        if(nullables.contains(suffix))
            nullables.remove(suffix);
        cmd.putString("ICON"+suffix, image.getAbsolutePath());
        cmd.flush();
        
    }
    public void removeForExtension(String name){
        String suffix=name.substring(name.lastIndexOf("."));
        if(hsht.contains(suffix)){
            hsht.remove(suffix);
            nullables.add(suffix);
        }
        if(cmd.getString("ICON"+suffix, null)!=null){
            cmd.putString("ICON"+suffix, null);
            cmd.flush();
        }
    }
    public void clearCache(){
        hsht.clear();
        nullables.clear();
    }
    public void clearNullCache(){
        nullables.clear();
    }
    
}
