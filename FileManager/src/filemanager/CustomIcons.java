/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package filemanager;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
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
    public CustomIcons(){
        cmd=new CommandManager();
        hsht=new Hashtable<>();
    }
    public BufferedImage getForExtension(String name){
        
        String suffix=name.substring(name.lastIndexOf("."));
        if(hsht.contains(suffix)){
            return hsht.get(suffix);
        }
        String path=cmd.getString("ICON"+suffix, null);
        if(path==null){
            hsht.remove(suffix);
            return null;
        }
        try {
            BufferedImage icon=ImageIO.read(new File(path));
            hsht.remove(suffix);
            return icon;
            
        } catch (IOException ex) {
            Logger.getLogger(CustomIcons.class.getName()).log(Level.SEVERE, null, ex);
        }
        hsht.put(suffix, null);
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
        cmd.putString("ICON"+suffix, image.getAbsolutePath());
        cmd.flush();
        
    }
    public void removeForExtension(String name){
        String suffix=name.substring(name.lastIndexOf("."));
        if(hsht.contains(suffix)){
            hsht.remove(suffix);
        }
        if(cmd.getString("ICON"+suffix, null)!=null){
            cmd.putString("ICON"+suffix, null);
            cmd.flush();
        }
    }
    public void clearCache(){
        hsht.clear();
    }
    
}
