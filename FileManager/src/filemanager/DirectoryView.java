/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package filemanager;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author ivan
 */
public class DirectoryView {
    private final JPanel mainView;
    public String currentdir=null;
    private final int icon_size=64;
    private final CustomIcons cmicons;
    private final int font_size=12;
    private final String[] imageformats;
    private final String font_size_string;
    private final Vector<Thread> threads;
    private final JFrame holder;
    private final Vector<File> selection=new Vector<>();
    private final BufferedImage imagecache[]=new BufferedImage[5];
    private File prev[]=null;
    private DirChangeListener drls=null;
    private boolean wassearch=false;
    public DirectoryView(JPanel jscp,JFrame window){
        imageformats=ImageIO.getReaderFileSuffixes();
        cmicons=new CustomIcons();
        holder=window;
        mainView=jscp;
        threads=new Vector<>();
        font_size_string=Integer.toString(font_size);
        int i;
        for(i=0;i<5;i++){
            imagecache[i]=null;
        }
    }
    public void drawDirectory(String path,boolean repaint){
        drawDirectory(path,repaint,null);
    }
    private File[] sort(File[] input){
        if(input==null)
            return null;
        
        File list[]=new File[input.length];
        File tmplist[]=new File[input.length];
        long dates[]=new long[input.length];
        int i=0;
        for(File f:input){
            tmplist[i]=f;
            dates[i]=f.lastModified();
            i++;
        }
        long last_date;
        File fcopy=null;
        int i2;
        int li=0;
        int size=input.length;
        for(i=0;i<size;i++){
            last_date=-1;
            for(i2=0;i2<size;i2++){
                File f=tmplist[i2];
                if(f!=null){
                    //System.out.println(f.lastModified());
                    if(dates[i2]>last_date){
                        last_date=dates[i2];
                        fcopy=f;
                        li=i2;
                    }
                }
            }
            
            list[i]=fcopy;
            tmplist[li]=null;
            
        }
        return list;
    }
    private boolean compare(File[] list,File[] list2){
        if(list.length!=list2.length)
            return false;
        int size=list.length;
        int i;
        for(i=0;i<size;i++){
            if(!list[i].getAbsolutePath().equals(list2[i].getAbsolutePath())){
                return false;
            }
        }
        return true;
    }
   
    public void drawDirectory(String path,boolean repaint,String search){
        
        selection.clear();
        File cdir=new File(path);
        if(!cdir.exists()){
            return;
        }
        currentdir=path;
        
        
        int width=holder.getWidth()>>1;
        int cols=(width/(icon_size+5));
        if(cols<1){
            cols=1;
        }
        File dirlist[]=sort(cdir.listFiles());
        if(dirlist==null){
            if(repaint){
                mainView.removeAll();
                mainView.revalidate();
                holder.repaint();
            }
            return;
            
        }
        if(!wassearch)
         if(search==null&&prev!=null&&compare(prev,dirlist)){
             int elements=dirlist.length+1;
             int rows=elements/cols;
             Component cmp[]=mainView.getComponents();
             boolean first=false;
             for(Component cm:cmp){
                 if(first){
                     JButton btn=(JButton)cm;
                     btn.setText("");
                 }
                 first=true;
             }
             while(rows*cols<elements){
                  rows++;
             }
             mainView.setLayout(new GridLayout(rows,cols));
             
             holder.repaint();
             return;
         }
        wassearch = search!=null;
        killImageThreads();
        cmicons.clearNullCache();
        prev=dirlist;
        mainView.removeAll();
        int elements=dirlist.length+1;
        int rows=elements/cols;
        while(rows*cols<elements){
            rows++;
        }
        mainView.setLayout(new GridLayout(rows,cols));
        mainView.add(new JLabel("files: "+Integer.toString(dirlist.length)));
        int i;
        for(i=0;i<elements-1;i++){
          if(search==null||dirlist[i].getName().toLowerCase().contains(search.toLowerCase())){
            final File fcopy=dirlist[i];
            JButton click=new JButton();
            
    
            final boolean isdir=fcopy.isDirectory();
            click.setIcon(new ImageIcon(generateIconForFile(fcopy,isdir)));
            click.setToolTipText(fcopy.getName()+" - "+SpacetoInt(fcopy.length()));
            click.addActionListener((ActionEvent e) -> {
                MultiSelectListener(e,click,fcopy);
                 if((e.getModifiers()|ActionEvent.SHIFT_MASK)==e.getModifiers())
                     return;
                if(isdir){
                    drawDirectory(fcopy.getAbsolutePath(),true,null);
                    if(drls!=null){
                        drls.dirChanged(fcopy.getAbsolutePath(),path);
                    }
                }else{
                    String cmd=OpenPreferences.getDefaultForExtension(fcopy.getName());
                    if(cmd!=null)
                        OpenPreferences.runCommand(cmd, fcopy.getAbsolutePath());
                }
                
            });
            boolean bd[]=new boolean[2];
            bd[1]=false;
            bd[0]=false;
            click.addMouseListener(new MouseListener(){
                @Override
                public void mouseClicked(MouseEvent e) {
                }

                @Override
                public void mousePressed(MouseEvent e) {
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    if(e.getButton()==MouseEvent.BUTTON3){
                        if(!selection.contains(fcopy))
                             selection.add(fcopy);
                        ContextMenu cm=new ContextMenu(holder);
                        cm.optionsDialogue(selection,new File(currentdir),cmicons);
                        drawDirectory(path,true,null);
                    }
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    if(bd[1])
                        return;
                    bd[1]=true;
                    if(isFileImage(fcopy.getName().toLowerCase())){
                        setButtonImageResp(click, fcopy,bd, e);
                    }
                }

                @Override
                public void mouseExited(MouseEvent e) {
                }
            });
          
          mainView.add(click);
          /*if(i%20==0){
             mainView.revalidate();
             holder.repaint();
          }*/
        }
        }
        if(repaint){
         mainView.revalidate();
         holder.repaint();
        }

     
        
    }
    private void MultiSelectListener(ActionEvent e,JButton in,File file){
       
            if((e.getModifiers()|ActionEvent.SHIFT_MASK)!=e.getModifiers())
                return;
            
            if(!in.getText().equals("<>")){
                in.setText("<>");
                if(!selection.contains(file)){
                    selection.add(file);
                    in.setBackground(Color.red);
                }
     
                
            }else{
               
                if(selection.contains(file)){
                    selection.remove(file);
                    in.setText("");
                }
            }
       
      
    }
    private void killImageThreads(){
        Enumeration<Thread> thrds=threads.elements();
        while(thrds.hasMoreElements()){
            Thread th=thrds.nextElement();
            if(th.isAlive()){
                th.interrupt();
            }
        }
        threads.clear();
    }
    private void cleanThreadArray(){
         Enumeration<Thread> thrds=threads.elements();
        while(thrds.hasMoreElements()){
            Thread th=thrds.nextElement();
            if(!th.isAlive()){
                threads.remove(th);
            }
        }
    }
    private void setButtonImageResp(JButton in, File image,boolean bd[],MouseEvent e){
        //boolean bd[]=new boolean[1];
       
                if(!bd[0]){
                 bd[0]=true;
                 Thread th=new Thread(){
                   @Override
                   public void run(){
                   
                    try {
                        
                        BufferedImage impose=ImageIO.read(FileManager.class.getResourceAsStream("load.png"));
                        BufferedImage drim=new BufferedImage(icon_size,icon_size+font_size,BufferedImage.TYPE_4BYTE_ABGR);
                        Graphics2D grph=drim.createGraphics();
                        grph.setFont(Font.decode("dialogue-"+font_size_string));
                        grph.setColor(Color.BLACK);
                        grph.drawString(image.getName(), 0, icon_size);
                        grph.drawImage(impose, 0, 0,icon_size,icon_size-font_size, null);
                        if(!in.isValid())
                            return;
                        in.setIcon(new ImageIcon(drim));
                        in.validate();
                        holder.repaint();
                        try {
                            impose=ImageIO.read(image);
                            drim=new BufferedImage(icon_size,icon_size+font_size,BufferedImage.TYPE_4BYTE_ABGR);
                            grph=drim.createGraphics();
                            grph.setFont(Font.decode("dialogue-"+font_size_string));
                            grph.setColor(Color.BLACK);
                            grph.drawString(image.getName(), 0, icon_size);
                            grph.drawImage(impose, 0, 0,icon_size,icon_size-font_size, null);
                            if(!in.isValid())
                                return;
                            in.setIcon(new ImageIcon(drim));
                            in.validate();
                        } catch (IOException ex) {
                           // Logger.getLogger(DirectoryView.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } catch (IOException ex) {
                        Logger.getLogger(DirectoryView.class.getName()).log(Level.SEVERE, null, ex);
                    }
                   }};
                 cleanThreadArray();
                 threads.add(th);
                 th.start();
                }
          
    }
    private String SpacetoInt(long space){
        long KB=space/1000;
        long MB=KB/1000;
        long GB=MB/1000;
        if(GB!=0){
            return Long.toString(GB)+"GB";
        }else if(MB!=0){
            return Long.toString(MB)+"MB";
        }else if(KB!=0){
          return Long.toString(KB)+"KB";
        }
        return Long.toString(space);
    }
    public void setDirChangeListener(DirChangeListener dls){
        drls=dls;
    }
    private boolean isFileImage(String inlower){
       
        
        for(String suff:imageformats){
            if(inlower.endsWith("."+suff)){
                return true;
            }
        }
        return false;
    }
    private boolean isFileMusic(String inlower){
        String audioformats[]={".mp3",".wav",".ogg",".wma",".aac",".flac",".aiff"};
       
        for(String name:audioformats){
            if(inlower.endsWith(name)){
                    return true;
             }
        }
        return false;
    }
    private boolean isVideo(String inlower){
        String audioformats[]={".mp4",".webm",".mkv",".mov",".avi",".flv",".wmv"};

        for(String name:audioformats){
            if(inlower.endsWith(name)){
                    return true;
             }
        }
        return false;
    }
    public static BufferedImage clone_image(BufferedImage in, int width,int height){
        BufferedImage buff=new BufferedImage(width,height,BufferedImage.TYPE_4BYTE_ABGR);
        buff.createGraphics().drawImage(in,0, 0, width, height, null);
        return buff;
    }
    private BufferedImage drim=null;
    private Graphics2D grph=null;
    private BufferedImage generateIconForFile(File in,boolean isdir){
        int extrabtm=font_size;
        if(drim==null){
            drim=new BufferedImage(icon_size,icon_size+extrabtm,BufferedImage.TYPE_4BYTE_ABGR);
            grph=drim.createGraphics();
            grph.setFont(Font.decode("default-"+font_size_string));
            grph.setBackground(new Color(0,true));
            grph.setColor(Color.BLACK);
            
        }else{
            
            
            grph.clearRect(0, 0, icon_size, icon_size+extrabtm);
        }
        
        String name=in.getName();
        String suffix=null;
        grph.drawString(name, 0, icon_size);
        String inlower=in.getName().toLowerCase();
        BufferedImage tmp;
        try{
            BufferedImage impose;
            if(isdir){
                 if(imagecache[0]==null){
                     impose=ImageIO.read(FileManager.class.getResourceAsStream("dir.png"));
                     impose=clone_image(impose,icon_size,icon_size-extrabtm);
                     imagecache[0]=impose;
                 }else
                     impose=imagecache[0];
            }else if(!inlower.substring(1).contains(".")){
                 if(imagecache[3]==null){
                  impose=ImageIO.read(FileManager.class.getResourceAsStream("file.png"));
                  impose=clone_image(impose,icon_size,icon_size-extrabtm);
                  imagecache[3]=impose;
                }else{
                    impose=imagecache[3];
                }
            }else if((tmp=cmicons.getForExtension(inlower,icon_size,icon_size-extrabtm))!=null){
                impose=tmp;
            }
            else if(isFileImage(inlower)){
            
                if(imagecache[1]==null){
                    impose=ImageIO.read(FileManager.class.getResourceAsStream("image.png"));
                    impose=clone_image(impose,icon_size,icon_size-extrabtm);
                    imagecache[1]=impose;
                }else{
                    impose=imagecache[1];
                }
            }else if(isFileMusic(inlower)){
                if(imagecache[2]==null){
                     impose=ImageIO.read(FileManager.class.getResourceAsStream("audio.png"));
                     impose=clone_image(impose,icon_size,icon_size-extrabtm);
                     imagecache[2]=impose;
                }else{
                    impose=imagecache[2];
                }
            }else if(isVideo(inlower)){
                if(imagecache[4]==null){
                     impose=ImageIO.read(FileManager.class.getResourceAsStream("video.png"));
                     impose=clone_image(impose,icon_size,icon_size-extrabtm);
                     imagecache[4]=impose;
                }else{
                    impose=imagecache[4];
                }
          
            }else {
                if(imagecache[3]==null){
                  impose=ImageIO.read(FileManager.class.getResourceAsStream("file.png"));
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
                grph.drawString(suffix, icon_size>>2, extrabtm);
            }
        }catch(IOException e){}
        BufferedImage ret=new BufferedImage(icon_size,icon_size+extrabtm,BufferedImage.TYPE_4BYTE_ABGR);
        ret.setData(drim.getData());
        return ret;
    }
    public void clearCache(){
        cmicons.clearCache();
    }
}
