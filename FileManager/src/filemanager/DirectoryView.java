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
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author ivan
 */
public class DirectoryView {
    private final JPanel mainView;
    public String currentdir=null;
    private final int icon_size=64;
    private final int font_size=12;
    private final String font_size_string;
    private final JFrame holder;
    private final Vector<File> selection=new Vector<>();
    private final BufferedImage imagecache[]=new BufferedImage[5];
    private File prev[]=null;
    private DirChangeListener drls=null;
    private boolean wassearch=false;
    public DirectoryView(JPanel jscp,JFrame window){
        holder=window;
        mainView=jscp;
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
    private Thread runable=null;
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
             int elements=dirlist.length;
             int rows=elements/cols;
             Component cmp[]=mainView.getComponents();
             for(Component cm:cmp){
                 JButton btn=(JButton)cm;
                 btn.setText("");
             }
             while(rows*cols<elements){
                  rows++;
             }
             mainView.setLayout(new GridLayout(rows,cols));
             
             holder.repaint();
             return;
         }
        if(search!=null){
            wassearch=true;
        }else{
            wassearch=false;
        }
        prev=dirlist;
        mainView.removeAll();
        int elements=dirlist.length;
        int rows=elements/cols;
        while(rows*cols<elements){
            rows++;
        }
        mainView.setLayout(new GridLayout(rows,cols));
        int i;
        for(i=0;i<elements;i++){
          if(search==null||dirlist[i].getName().toLowerCase().contains(search.toLowerCase())){
            final int icopy=i;
            JButton click=new JButton();
            
            setMultiSelectListener(click,dirlist[i]);
            final boolean isdir=dirlist[i].isDirectory();
            click.setIcon(new ImageIcon(generateIconForFile(dirlist[i],isdir,click)));
            click.setToolTipText(dirlist[icopy].getName()+" - "+SpacetoInt(dirlist[icopy].length()));
            click.addActionListener((ActionEvent e) -> {
                 if((e.getModifiers()|ActionEvent.SHIFT_MASK)==e.getModifiers())
                     return;
                if(isdir){
                    drawDirectory(dirlist[icopy].getAbsolutePath(),true,null);
                    if(drls!=null){
                        drls.dirChanged(dirlist[icopy].getAbsolutePath(),path);
                    }
                }else{
                    String cmd=OpenPreferences.getDefaultForExtension(dirlist[icopy].getName());
                    if(cmd!=null)
                        OpenPreferences.runCommand(cmd, dirlist[icopy].getAbsolutePath());
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
                        if(!selection.contains(dirlist[icopy]))
                             selection.add(dirlist[icopy]);
                        ContextMenu cm=new ContextMenu(holder);
                        cm.optionsDialogue(selection,new File(currentdir)
                        );
                        drawDirectory(path,true,null);
                    }
                }

                @Override
                public void mouseEntered(MouseEvent e) {
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
    private void setMultiSelectListener(JButton in,File file){
        in.addActionListener((ActionEvent e) -> {
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
        });
      
    }
    private void setButtonImageResp(JButton in, File image){
        boolean bd[]=new boolean[1];
        bd[0]=false;
        in.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                if(!bd[0]){
                 bd[0]=true;
                 new Thread(){
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
                            in.setIcon(new ImageIcon(drim));
                            in.validate();
                        } catch (IOException ex) {
                           // Logger.getLogger(DirectoryView.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } catch (IOException ex) {
                        Logger.getLogger(DirectoryView.class.getName()).log(Level.SEVERE, null, ex);
                    }
                   }}.start();
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        });
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
    private boolean isFileImage(File input){
        String lists[]=ImageIO.getReaderFileSuffixes();
        String inlower=input.getName().toLowerCase();
        for(String suff:lists){
            if(inlower.endsWith("."+suff)){
                return true;
            }
        }
        return false;
    }
    private boolean isFileMusic(File input){
        String audioformats[]={".mp3",".wav",".ogg",".wma",".aac",".flac",".aiff"};
        String inlower=input.getName().toLowerCase();
        for(String name:audioformats){
            if(inlower.endsWith(name)){
                    return true;
             }
        }
        return false;
    }
    private boolean isVideo(File input){
        String audioformats[]={".mp4",".webm",".mkv",".mov",".avi",".flv",".wmv"};
        String inlower=input.getName().toLowerCase();
        for(String name:audioformats){
            if(inlower.endsWith(name)){
                    return true;
             }
        }
        return false;
    }
    private BufferedImage drim=null;
    private Graphics2D grph=null;
    private BufferedImage generateIconForFile(File in,boolean isdir,JButton btn){
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
        try{
            BufferedImage impose;
            if(isdir){
                 if(imagecache[0]==null){
                     impose=ImageIO.read(FileManager.class.getResourceAsStream("dir.png"));
                     imagecache[0]=impose;
                 }else
                     impose=imagecache[0];
            }else if(isFileImage(in)){
                setButtonImageResp(btn, in);
                if(imagecache[1]==null){
                    impose=ImageIO.read(FileManager.class.getResourceAsStream("image.png"));
                    imagecache[1]=impose;
                }else{
                    impose=imagecache[1];
                }
            }else if(isFileMusic(in)){
                if(imagecache[2]==null){
                     impose=ImageIO.read(FileManager.class.getResourceAsStream("audio.png"));
                     imagecache[2]=impose;
                }else{
                    impose=imagecache[2];
                }
            }else if(isVideo(in)){
                if(imagecache[4]==null){
                     impose=ImageIO.read(FileManager.class.getResourceAsStream("video.png"));
                     imagecache[4]=impose;
                }else{
                    impose=imagecache[4];
                }
            }else {
                if(imagecache[3]==null){
                  impose=ImageIO.read(FileManager.class.getResourceAsStream("file.png"));
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
            grph.drawImage(impose, 0, 0,icon_size,icon_size-extrabtm, null);
            if(suffix!=null){
                grph.drawString(suffix, icon_size>>2, extrabtm);
            }
        }catch(IOException e){}
        BufferedImage ret=new BufferedImage(icon_size,icon_size+extrabtm,BufferedImage.TYPE_4BYTE_ABGR);
        ret.setData(drim.getData());
        return ret;
    }
    
}
