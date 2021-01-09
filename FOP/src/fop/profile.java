package fop;
import static fop.YuuTube.*;
import java.io.*;
import java.util.*;

public class profile {
    //history
    static String[] profile_actions = {
        "Go back", 
        "Home Page",
        "Change Email",
        "Change Password",                               
        "History",
        "Liked Videos",
        "Delete Account"
    };
    
    public static void profile(int userID){
        User user = id_to_users.get(userID);
        int c;
        do{
            System.out.println("--- My Profile ---");
            System.out.println(user.one_toString());
            System.out.println("Available Actions: ");
            for(int i=0; i<profile_actions.length; i++){
                System.out.printf("%d. %s\n",i,profile_actions[i]);
            }
            c = prompt_input0(profile_actions.length);
            if(c==0)System.out.println("--- "+profile_actions[c]+" ---");
            //c=0, go back
            if(c==1){
                //home page
                ret_home=true;
            }else if(c==2){
                //change email
                auth.chg_email(userID);
            }else if(c==3){
                //change password
                auth.chg_password(userID);
            }else if(c==4){
                //history
                history_page(userID);
            }else if(c==5){
                //like
                liked_page(userID);
            }else if(c==6){
                auth.deleteAccount(userID);
            }
            if(ret_home||ret_start)break;
        }while(c!=0);
    }
    
    private static String[] history_actions={
        "Go Back",
        "Home Page",
        "Play video (on the history list)"
    };
    
    public static void history_page(int userID){
        int c;
        User user = id_to_users.get(userID);
        do{
            //videos sorted by latest
            System.out.println("--- History ---");
            ArrayList<String>History = user.getHistory();
            ArrayList<String>videoName = new ArrayList<String>(); 
            Iterator<String> it=History.iterator();  
            int ptr = 1;
            while(it.hasNext())  
            {   
                String name = it.next();
                videoName.add(name);
            }  
            //reverse
            for(int i=0; i<videoName.size()/2; i++){
                //swap
                String fname = videoName.get(i);
                String lname = videoName.get(videoName.size()-1-i);
                videoName.set(i,lname);
                videoName.set(videoName.size()-1-i, fname);
            }
            for(int i=0; i<videoName.size(); i++){
                System.out.printf("%d. %s\n",i+1,videoName.get(i));
            }
            
            if(videoName.size()==0){
                //oof, no vidoes shown
                System.out.println("There are no videos in history, please play one to view.");
                prompt_any();
                return;
            }
        
            System.out.println("Available Actions: ");
            for(int i=0; i<history_actions.length; i++){
                System.out.printf("%d. %s\n",i,history_actions[i]);
            }
            c = prompt_input0(history_actions.length);
            System.out.println("--- "+history_actions[c]+" ---");
            //c=0, go back
            if(c==1){
                //home page
                ret_home=true;
            }else if(c==2){
                //play video
                
                for(int i=0; i<videoName.size(); i++)
                    System.out.printf("%d. %s\n",i+1,videoName.get(i));
                System.out.println("Please select your video no. (liked videos) to play.");
                int cc = prompt_input1(videoName.size());
                play_page(userID,videoName.get(cc));
            }
            if(ret_home||ret_start)break;
        }while(c!=0);
    }
    
    private static String[] liked_actions={
        "Go Back",
        "Home Page",
        "Play video (only liked videos)",
        "Remove Like"
    };
    
    public static void liked_page(int userID){
        int c;
        User user = id_to_users.get(userID);
        do{
            System.out.println("--- Liked Videos ---");
            HashMap<Integer, Integer>id_to_like = user.getId_to_like();
            ArrayList<String>videoName = new ArrayList<String>();
            ArrayList<Integer>videoIDs = new ArrayList<Integer>();
            for (Map.Entry mapElement : id_to_like.entrySet()) { 
                int videoID = (int)mapElement.getKey(); 
                int likey = ((int)mapElement.getValue()); 
                if(likey==1){
                    Video v = id_to_videos.get(videoID);
                    if(v==null)continue;
                    String name = v.getTitle();
                    System.out.printf("%d. %s\n",videoName.size()+1,name);
                    videoName.add(name);
                    videoIDs.add(videoID);
                }
            }
            
            if(videoName.size()==0){
                //oof, no vidoes shown
                System.out.println("There are no videos you have liked, please like one to view.");
                prompt_any();
                return;
            }
            
            System.out.println("Available Actions: ");
            for(int i=0; i<liked_actions.length; i++){
                System.out.printf("%d. %s\n",i,liked_actions[i]);
            }
            c = prompt_input0(liked_actions.length);
            System.out.println("--- "+liked_actions[c]+" ---");
            //c=0, go back
            if(c==1){
                //home page
                ret_home=true;
            }else if(c==2){
                //play video
                for(int i=0; i<videoName.size(); i++)
                    System.out.printf("%d. %s\n",i+1,videoName.get(i));
                System.out.println("Please select your video no. (liked videos) to play.");
                int cc = prompt_input1(videoName.size());
                play_page(userID,videoName.get(cc));
            }else if(c==3){
                //remove like
                for(int i=0; i<videoName.size(); i++)
                    System.out.printf("%d. %s\n",i+1,videoName.get(i));
                System.out.println("Please select your video no. (liked videos) to remove like.");
                int cc = prompt_input1(videoName.size());
                int videoID = videoIDs.get(cc);
                Video v = id_to_videos.get(videoID);
                v.decLike();
                user.set_like(videoID,0);
            }
            if(ret_home||ret_start)break;
        }while(c!=0);
    }
}
