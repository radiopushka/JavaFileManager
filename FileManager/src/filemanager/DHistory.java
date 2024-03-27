/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package filemanager;

import java.util.Vector;

/**
 *
 * @author ivan
 */
public class DHistory {
    private final Vector<String> pathhistory=new Vector<>();
    private int index=0;
    public void addPath(String path){
        if(path==null){
            return;
        }
        if(index<pathhistory.size()){
             if(index<0)
                 index=0;
             String prev=null;
             if(index>0){
                 prev=pathhistory.elementAt(index-1);
             }
             if(prev==null||!prev.equals(path)){
                 pathhistory.set(index, path);
                 index++;
             }
        }else{
            if(pathhistory.size()<1||!pathhistory.lastElement().equals(path)){
                 pathhistory.add(path);
                 index++;
            }
        }
       
        if(pathhistory.size()>20){
            pathhistory.remove(pathhistory.firstElement());
            index--;
        }
    }
    public String getPath(){
        if(pathhistory.isEmpty()||index<1)
            return null;
        index--;
        return pathhistory.get(index);
    }
    public String getFWD(){
        if(pathhistory.isEmpty()||index>pathhistory.size()-2)
            return null;
        index++;
        return pathhistory.get(index);
    }
}
