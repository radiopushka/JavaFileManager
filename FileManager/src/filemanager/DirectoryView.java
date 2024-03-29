/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package filemanager;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

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
    private final int max_width;
    private final String[] imageformats;
    private final String font_size_string;
    private final Vector<JButton> thread_btn;
    private final Vector<File> thread_file;
    private final JFrame holder;
    private final Vector<File> selection=new Vector<>();
    private BufferedImage load_icon;
    private final BufferedImage imagecache[]=new BufferedImage[5];
    private File prev[]=null;
    private int maxfiles=400;
    private DirChangeListener drls=null;
    private boolean wassearch=false;
    public DirectoryView(JPanel jscp,JFrame window){
        imageformats=ImageIO.getReaderFileSuffixes();
        DisplayMode dsp=GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode();
        maxfiles=(dsp.getWidth()/icon_size)*(dsp.getHeight()/icon_size);
        cmicons=new CustomIcons();
        max_width=((int)(icon_size/(font_size*1.33))*2)+1;
        holder=window;
        try {
            load_icon=ImageIO.read(FileManager.class.getResourceAsStream("load.png"));
        } catch (IOException ex) {
            load_icon=new BufferedImage(icon_size,icon_size,BufferedImage.TYPE_4BYTE_ABGR);
            Logger.getLogger(DirectoryView.class.getName()).log(Level.SEVERE, null, ex);
        }
        mainView=jscp;
        thread_btn=new Vector<>();
        thread_file=new Vector<>();
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
         if(current_layout_size>=0&&search==null&&prev!=null&&compare(prev,dirlist)){
             int elements=current_layout_size+1;
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
        System.gc();
        prev=dirlist;
        mainView.removeAll();
        
        mainView.add(new JLabel("files: "+Integer.toString(dirlist.length)));
        
        int max=maxfiles;
        if(search!=null){
            max=dirlist.length;
        }
       drawButtons(dirlist,1,0,dirlist.length,max,repaint,path,search);
        
    }
    private int current_layout_size=-1;
    private void drawButtons(File dirlist[],int offset,int startindex,int maxindex,int increment,boolean repaint,String path,String search){
         int i;
         int width=holder.getWidth()>>1;
        int cols=(width/(icon_size+5));
        int elements=startindex+increment;
        if(elements>maxindex){
            elements=maxindex;
        }
        
        current_layout_size=elements+offset;
        int rows=(offset+elements)/cols;
        while(rows*cols<(elements+offset)){
            rows++;
        }
        mainView.setLayout(new GridLayout(rows,cols));
        for(i=startindex;i<elements;i++){
          if(search==null||dirlist[i].getName().toLowerCase().contains(search.toLowerCase())){
            final File fcopy=dirlist[i];
            JButton click=new JButton();
            
    
            final boolean isdir=fcopy.isDirectory();
            
            if(isFileImage(fcopy.getName().toLowerCase())){
                   setButtonImageResp(click, fcopy);
             }else{
                   click.setIcon(new ImageIcon(generateIconForFile(fcopy,isdir)));

             }
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
            
           
            if(i==startindex+increment-1)
              click.addAncestorListener(new AncestorListener() {
                @Override
                public void ancestorAdded(AncestorEvent event) {
                }

                @Override
                public void ancestorRemoved(AncestorEvent event) {
                }

                @Override
                public void ancestorMoved(AncestorEvent event) {
                    JScrollPane scroll=(JScrollPane)mainView.getParent().getParent();//get our scroll bar
                    int max=(scroll.getVerticalScrollBar().getMaximum()>>1)-10;
                    if(max>scroll.getVerticalScrollBar().getValue())
                        return;
                    click.removeAncestorListener(this);
                    drawButtons(dirlist,offset,startindex+increment,maxindex,increment,repaint,path,search);
                }
            });
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
                   /* if(bd[1])
                        return;
                    bd[1]=true;
                    if(isFileImage(fcopy.getName().toLowerCase())){
                        setButtonImageResp(click, fcopy,bd);
                    }*/
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
        if(dispatched==null)
            return;
       if(dispatched.isAlive())
           dispatched.interrupt();
       thread_btn.clear();
       
       thread_file.clear();
    }
   
    private Thread dispatched=null;
    private void setButtonImageResp(JButton in, File imagein){
        //boolean bd[]=new boolean[1];
       
              
                 //draw load icon
                         BufferedImage impose=load_icon;
                         BufferedImage drim2=new BufferedImage(icon_size,icon_size+font_size*5,BufferedImage.TYPE_4BYTE_ABGR);
                         Graphics2D grph2=drim2.createGraphics();
                         grph2.setFont(Font.decode("dialogue-"+font_size_string));
                         grph2.setColor(Color.BLACK);
                         draw_name(imagein.getName(),grph2);
                         grph2.drawImage(impose, 0, 0,icon_size,icon_size, null);
                        in.setIcon(new ImageIcon(drim2));
                        in.validate();
                        holder.repaint();
                 thread_btn.add(in);
                 thread_file.add(imagein);
                 if(dispatched!=null){
                     if(dispatched.isAlive())
                         return;
                 }
                 dispatched=new Thread(){
                   @Override
                   public void run(){
                   
                    
                    while(!thread_file.isEmpty()){
                        JButton top=thread_btn.firstElement();
                        File image=thread_file.firstElement();
                        thread_btn.remove(top);
                        thread_file.remove(image);
                        String name=image.getName();
                        try {
                            
                            ImageInputStream imns=ImageIO.createImageInputStream(image);
                            Iterator<ImageReader> imr=ImageIO.getImageReaders(imns);
                            BufferedImage impose;
                            if(!imr.hasNext()){
                               
                                impose=ImageIO.read(image);
                            }else{
                            
                              ImageReader im=imr.next();
                              im.setInput(imns);
                              int iw=im.getWidth(0)/icon_size;
                              if(iw<1)
                                    iw=1;
                            
                              int ih=im.getHeight(0)/icon_size;
                              if(ih<1)
                                  ih=1;
                              ImageReadParam pram=new ImageReadParam();
                            
                              pram.setSourceProgressivePasses(1, 1);
                              if(pram.canSetSourceRenderSize())
                                 pram.setSourceRenderSize(new Dimension(icon_size,icon_size));
                               else{
                                 pram.setSourceSubsampling(iw, ih, 0, 0);
                               }
                                
                                impose=im.read(0,pram);
                            }
                            
                            BufferedImage drim=new BufferedImage(icon_size,icon_size+font_size*5,BufferedImage.TYPE_4BYTE_ABGR);
                            Graphics2D grph=drim.createGraphics();
                            grph.setFont(Font.decode("dialogue-"+font_size_string));
                            grph.setColor(Color.BLACK);
                            draw_name(name,grph);
                            grph.drawImage(impose, 0, 0,icon_size,icon_size, null);
                            top.setIcon(new ImageIcon(drim));
                            top.validate();
                        } catch (IOException | UnsupportedOperationException ex) {
                            Logger.getLogger(DirectoryView.class.getName()).log(Level.SEVERE, null, ex);
                         
                             BufferedImage preload=cmicons.getForExtension(image.getName().toLowerCase(),icon_size,icon_size);
                             if(preload!=null)
                                 top.setIcon(new ImageIcon(preload));
                             else{
                                try{
                                  if(imagecache[1]==null){
                                        imagecache[1]=ImageIO.read(FileManager.class.getResourceAsStream("image.png"));
                                        imagecache[1]=clone_image(imagecache[1],icon_size,icon_size);
                                        
                                    }
                                  top.setIcon(new ImageIcon(imagecache[1]));
                                }catch(IOException e){}
                                  
                             }
                                 
                             
                               
                        }
                        try {
                            Thread.sleep(50);
                        } catch (InterruptedException ex) {
                           
                        }
                    }
                    
                   }};
                
                 dispatched.start();
                
          
    }
    private void draw_name(String name,Graphics2D grph){
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
        BufferedImage ret=new BufferedImage(icon_size,icon_size+font_size*5,BufferedImage.TYPE_4BYTE_ABGR);
        ret.setData(drim.getData());
        return ret;
    }
    public void clearCache(){
        cmicons.clearCache();
    }
}
