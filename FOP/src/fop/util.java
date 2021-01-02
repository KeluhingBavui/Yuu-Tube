package fop;

import static fop.FOP.*;
import static fop.process.*;
import static fop.registration.*;
import java.io.*;
import java.util.*;

public class util {
    private static String[] options = {"Play video"};
    
    public boolean isInteger(String s){
        try{
            Integer.parseInt(s);
            return true;
        }catch(Exception e){
            return false;
        }
    }
    
    public static void readuser(){
        try{
            ObjectInputStream input = new ObjectInputStream(new FileInputStream("users.data"));
            id_to_users = (HashMap<Integer,User>)input.readObject();
            userptr = (int)input.readInt();
            input.close();
        }catch(FileNotFoundException e){
            
        }catch(Exception e){ 
            e.printStackTrace();
        }
        
        try{
            ObjectInputStream input = new ObjectInputStream(new FileInputStream("emails.data"));
            email_to_id = (HashMap<String,Integer>)input.readObject();
            input.close();
        }catch(FileNotFoundException e){
            
        }catch(Exception e){ 
            e.printStackTrace();
        }
        
        try{
            ObjectInputStream input = new ObjectInputStream(new FileInputStream("videos.data"));
            id_to_videos = (HashMap<Integer,Video>)input.readObject();
            name_to_id = (HashMap<String,Integer>)input.readObject();
            videoptr = (int)input.readInt();
            input.close();
        }catch(FileNotFoundException e){
            
        }catch(Exception e){ 
            e.printStackTrace();
        }
        
        try{
            ObjectInputStream input = new ObjectInputStream(new FileInputStream("top.data"));
            top = (ArrayList<Rank>)input.readObject();
            input.close();
        }catch(FileNotFoundException e){
            
        }catch(Exception e){ 
            e.printStackTrace();
        }
    }
    
    public static void save(){
        try{
            ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream("users.data"));
            output.writeObject(id_to_users);
            output.writeInt(userptr);
            output.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        
        try{
            ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream("emails.data"));
            output.writeObject(email_to_id);
            output.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        
        try{
            ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream("videos.data"));
            output.writeObject(id_to_videos);
            output.writeObject(name_to_id);
            output.writeInt(videoptr);
            output.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        
        try{
            ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream("top.data"));
            output.writeObject(top);
            output.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public static void list(){
        System.err.println("listing --- users size: "+id_to_users.size()+" "+email_to_id.size()+" "+id_to_videos.size()+" "+name_to_id.size()+"\n");
        for(Map.Entry iter:id_to_users.entrySet()){
            System.out.println("<user>");
            System.err.print(iter.getKey()+" ");
            User now = (User)iter.getValue();
            if(now!=null)System.out.println(now.toString());
        }
        
        for(Map.Entry iter:email_to_id.entrySet()){
            System.err.println(iter.getKey()+" <email> "+iter.getValue());
        }
        
        for(Map.Entry iter:id_to_videos.entrySet()){
            System.out.println("<vidname>");
            System.err.print(iter.getKey()+" ");
            Video now = (Video)iter.getValue();
            if(now!=null)System.out.println(now.toString());
        }
        
        for(Map.Entry iter:name_to_id.entrySet()){
            System.err.println(iter.getKey()+" <vidid> "+iter.getValue());
        }
        
        return;
    }
    
    public static void hello(int id){
        User now = id_to_users.get(id);
        System.out.printf("%s %s %s \n","--- Hello",now.getName(),"! ---");
    }
    
    public static void welcome(int id){
        User now = id_to_users.get(id);
        System.out.printf("%s %s %s \n","--- Welcome back",now.getName(),"! ---");
    } 
}

