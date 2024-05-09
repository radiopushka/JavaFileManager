/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package filemanager;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Vector;
//import java.util.logging.Level;
//import java.util.logging.Logger;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.awt.Font;

/**
 *
 * @author ivan
 */
public class CustomIcons {
    private final CommandManager cmd;
    private final Hashtable<String,BufferedImage> hsht;
    private final Vector<String> nullables;
private final int icon_size=64;
private final int font_size=12;
private final BufferedImage imagecache[]=new BufferedImage[5];
private final String font_size_string;
private final int max_width;
private final String[] imageformats;

    public CustomIcons(){
        cmd=new CommandManager();
        hsht=new Hashtable<>();
        nullables=new Vector<>();
        font_size_string=Integer.toString(font_size);
        max_width=((int)(icon_size/(font_size*1.33))*2)+1;
         imageformats=ImageIO.getReaderFileSuffixes();
    }
    public BufferedImage getForExtension(String name,int w,int h){
        
        String suffix=name.substring(name.lastIndexOf(".")).toLowerCase();
        BufferedImage ret;
        if((ret=hsht.get(suffix))!=null){
            return ret;
        }
        if(nullables.contains(suffix))
            return null;
        
        String path=cmd.getString("ICON"+suffix, null);
        if(path==null){
            nullables.add(suffix);
            hsht.remove(suffix);
            return null;
        }
        try {
            BufferedImage icon=ImageIO.read(new File(path));
            icon=clone_image(icon,w,h);
            if(w>1&&h>1)
                 hsht.put(suffix, icon);
            return icon;
            
        } catch (IOException ex) {
            //Logger.getLogger(CustomIcons.class.getName()).log(Level.SEVERE, null, ex);
              System.out.println(ex.toString());
        }
        nullables.add(suffix);
        hsht.remove(suffix);
        return null;
    }
    public void putForExtension(String name,File image){
        String suffix=name.substring(name.lastIndexOf(".")).toLowerCase();
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
        String suffix=name.substring(name.lastIndexOf(".")).toLowerCase();
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
   public static BufferedImage clone_image(BufferedImage in, int width,int height){
        BufferedImage buff=new BufferedImage(width,height,BufferedImage.TYPE_4BYTE_ABGR);
        buff.createGraphics().drawImage(in,0, 0, width, height, null);
        return buff;
    }
    private BufferedImage drim=null;
    private Graphics2D grph=null;
    protected BufferedImage generateIconForFile(File in,boolean isdir){
        int extrabtm=0;
        if(drim==null){
            drim=new BufferedImage(icon_size,icon_size+font_size*5,BufferedImage.TYPE_4BYTE_ABGR);
            grph=drim.createGraphics();
            grph.setFont(Font.decode("default-"+font_size_string));
            grph.setBackground(new Color(0,true));
            grph.setColor(Color.BLACK);

        }else{


            grph.clearRect(0, 0, icon_size, icon_size+font_size*5);
        }

        String name=in.getName();
        String suffix=null;

        draw_name(name,grph);
        String inlower=in.getName().toLowerCase();
        BufferedImage tmp;
        try{
            BufferedImage impose;
            if(isdir){
                 if(imagecache[0]==null){
                     impose=ImageIO.read(CustomIcons.class.getResourceAsStream("dir.png"));
                     impose=clone_image(impose,icon_size,icon_size-extrabtm);
                     imagecache[0]=impose;
                 }else
                     impose=imagecache[0];
            }else if(!inlower.substring(1).contains(".")){
                 if(imagecache[3]==null){
                  impose=ImageIO.read(CustomIcons.class.getResourceAsStream("file.png"));
                  impose=clone_image(impose,icon_size,icon_size-extrabtm);
                  imagecache[3]=impose;
                }else{
                    impose=imagecache[3];
                }
            }else if((tmp=getForExtension(inlower,icon_size,icon_size-extrabtm))!=null){
                impose=tmp;
            }
            else if(isFileImage(inlower)){

                if(imagecache[1]==null){
                    impose=ImageIO.read(CustomIcons.class.getResourceAsStream("image.png"));
                    impose=clone_image(impose,icon_size,icon_size-extrabtm);
                    imagecache[1]=impose;
                }else{
                    impose=imagecache[1];
                }
            }else if(isFileMusic(inlower)){
                if(imagecache[2]==null){
                     impose=ImageIO.read(CustomIcons.class.getResourceAsStream("audio.png"));
                     impose=clone_image(impose,icon_size,icon_size-extrabtm);
                     imagecache[2]=impose;
                }else{
                    impose=imagecache[2];
                }
            }else if(isVideo(inlower)){
                if(imagecache[4]==null){
                     impose=ImageIO.read(CustomIcons.class.getResourceAsStream("video.png"));
                     impose=clone_image(impose,icon_size,icon_size-extrabtm);
                     imagecache[4]=impose;
                }else{
                    impose=imagecache[4];
                }

            }else {
                if(imagecache[3]==null){
                  impose=ImageIO.read(CustomIcons.class.getResourceAsStream("file.png"));
                  impose=clone_image(impose,icon_size,icon_size-extrabtm);
                  imagecache[3]=impose;
                }else{
                    impose=imagecache[3];
                }
                if(name.contains(".")){

     suffix=name.substring(name.lastIndexOf(".")+1);
                    if(("."+suffix).equals(name)){
                        suffix=null;
                    }

                }
            }
            grph.drawImage(impose, 0, 0, null);
            if(suffix!=null){
                grph.drawString(suffix, icon_size>>2, font_size);
            }
        }catch(IOException e){}
        BufferedImage ret=new BufferedImage(icon_size,icon_size+font_size*5,BufferedImage.TYPE_4BYTE_ABGR);
        ret.setData(drim.getData());
        return ret;
    }
  protected void draw_name(String name,Graphics2D grph){
                           int length=name.length();
                           double dltmp=length/(max_width*1.0);
                           int dl=(int)dltmp;
                           if(dltmp-dl>0.4){
                               dl++;
                           }
                           if(dl>3){
                                int half=(length>>1);
                                int bhalf=half-(max_width>>1);
                                if(bhalf<max_width){
                                    bhalf=max_width;
                                }
                                int thalf=bhalf+(max_width-3);
                                String top_s=name.substring(0,max_width);
                                String b_s=name.substring(length-max_width);
                                int tsi=length-max_width;
                                if(b_s.contains(".")){
                                    int dotindex=name.lastIndexOf(".");
                                    int start=dotindex-max_width;
                                    if(start<0){
                                        start=0;
                                    }
                                    tsi=start;
                                    b_s=name.substring(start,dotindex);
                                }
                               if(thalf>tsi){
                                   thalf=tsi;
                               }
                               int total_size=thalf-bhalf;
                               String append="";
                               if(total_size>1){
                                   append=name.substring(bhalf,thalf);
                               }
                               grph.drawString(top_s, 0, icon_size+font_size);
                               grph.drawString("..."+append, 0, icon_size+(font_size<<1));
                               grph.drawString(b_s, 0, icon_size+font_size+(font_size<<1));

                            }else if(dl>2){
                                grph.drawString(name.substring(0,max_width), 0, icon_size+font_size);
                                grph.drawString(name.substring(max_width,max_width<<1), 0, icon_size+(font_size<<1));
                                grph.drawString(name.substring(max_width<<1), 0, icon_size+(font_size<<1)+font_size);
                            }else if(dl>1){
                        grph.drawString(name.substring(0,max_width), 0, icon_size+font_size);
                                grph.drawString(name.substring(max_width), 0, icon_size+(font_size<<1));
                            }else{
                                grph.drawString(name, 0, icon_size+font_size);

                            }
    }
    protected boolean isFileImage(String inlower){


        for(String suff:imageformats){
            if(inlower.endsWith("."+suff)){
                return true;
            }
        }
        return false;
    }
    protected boolean isFileMusic(String inlower){
        String audioformats[]={".mp3",".wav",".ogg",".wma",".aac",".flac",".aiff"};

        for(String name:audioformats){
            if(inlower.endsWith(name)){
                    return true;
             }
        }
        return false;
    }
    protected boolean isVideo(String inlower){
        String audioformats[]={".mp4",".webm",".mkv",".mov",".avi",".flv",".wmv"};

        for(String name:audioformats){
            if(inlower.endsWith(name)){
                    return true;
             }
        }
        return false;
    }
    
}
