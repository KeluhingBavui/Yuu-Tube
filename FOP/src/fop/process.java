package fop;

import static fop.YuuTube.*;
import java.io.*;
import java.util.*;

public class process{
    static int videoptr=0;
    
    private static String[] viewAuthor_actions={
        "Go Back",
        "Home Page",
        "Play video (only from this channel)",
        "Subscribe/Unsubscribe"
    };
    
    public static void viewAuthor(int authorID, int userID){
        User author = id_to_users.get(authorID);
        User user = id_to_users.get(userID);
        int c;
        do{
            String res = "~~~ "+author.getName()+" ~~~\n";
            res += "Total Subscribers: "+author.getSubsribersCount()+"\n";
            res += "Total Views: "+author.getTotalViews()+"\n";
            System.out.println(res);
            System.out.println(show_videos(authorID));
            System.out.println("Available Actions: ");
            boolean subscribe = user.get_sub(authorID);
            for(int i=0; i<viewAuthor_actions.length-(authorID==userID?1:0); i++){
                if(i==3){
                    //you cannot subscribe to self
                    if(!subscribe)System.out.println("3. Subscribe");
                    else System.out.println("3. Unsubscribe");
                    continue;
                }
                System.out.printf("%d. %s\n",i,viewAuthor_actions[i]);
            }
            c = prompt_input0(viewAuthor_actions.length-(authorID==userID?1:0));
            if(c!=3)System.out.println("--- "+viewAuthor_actions[c]+" ---");
            if(c==1){
                //home page
                ret_home=true;
            }else if(c==2){
                //play video
                System.out.println("Please select video no. (on this channel) to play.");
                ArrayList<Integer>videos = author.getVideos();
                int cc = prompt_input1(videos.size());
                int videoID = videos.get(cc);
                String videoName = (id_to_videos.get(videoID)).getTitle();
                play_page(userID,videoName);
            }else if(c==3){
                //subscribe/unsubscribe
                System.out.println("--- "+(subscribe?"Unsubscribe":"Subscribe")+" ---");
                author.incSubs(subscribe?-1:1);
                user.set_sub(authorID);
            } 
            if(ret_home||ret_start)break;
        }while(c!=0);
    }
    
    
    private static String[] viewComment_actions = {
        "Go Back",
        "Home Page",
        "View Commenter"
    };
    
    public static void viewComment(int videoID, int pos, int userID){
        Video v = id_to_videos.get(videoID);
        Comment com = v.getComments().get(pos);
        int commenterID = com.getUserID();
        User commenter = id_to_users.get(commenterID);
        System.out.println("Comment: "+com.getComment());
        System.out.println("Commentor: "+commenter.getName());
        int c;
        do{
            System.out.println("Available Actions: ");
            for(int i=0; i<viewComment_actions.length; i++){
                System.out.printf("%d. %s\n",i,viewComment_actions[i]);
            }
            c = prompt_input0(viewComment_actions.length);
            //c=0, go back
            if(c==1){
                //home page
                ret_home=true;
            }else if(c==2){
                //view commenter
                viewAuthor(commenterID,userID);
            }
            if(ret_home||ret_start)break;
        }while(c!=0);
    }
    
    public static String show_videos(int authorID){
        String top5 = top5();
        User author = id_to_users.get(authorID);
        String res = "";
        int cnt;
        res += "No.|";
        res += "Title";
        cnt=15;     while(cnt-->0)res+=" ";     res += "|";
        res += "Likes|";
        res += "Dislikes|";
        res += "Views|";
        res += "Comments|";
        res += "Top 5\n";
        
        cnt=3;      while(cnt-->0)res+="-";     res += "+";
        cnt=20;     while(cnt-->0)res+="-";     res += "+";
        cnt=5;      while(cnt-->0)res+="-";     res += "+";
        cnt=8;      while(cnt-->0)res+="-";     res += "+";
        cnt=5;      while(cnt-->0)res+="-";     res += "+";
        cnt=8;      while(cnt-->0)res+="-";     res += "+";
        cnt=5;      while(cnt-->0)res+="-";     res += "\n";
        
        ArrayList<Integer>videos = author.getVideos();
        for(int i=0; i<videos.size(); i++){
            int videoID=videos.get(i);
            Video v = id_to_videos.get(videoID);
            if(i+1<10)res+="  ";
            else if(i+1<100)res+=" ";
            res+=i+1+"|"; 
            res+=v.table_toString();
        }
       return res;
    }
    
    public static boolean play(String pathname){
        String pathdir = path+(os?"\\":"/");
        
        File ff = new File(pathdir+pathname);
        //System.err.println(pathdir+"< and >"+pathname);
        
        try(FileInputStream inputStream = new FileInputStream(ff)){
            //System.err.println("found");
            try {
                if(os){
                    String[] command = {"cmd.exe","/c",pathdir+pathname};
                    Runtime.getRuntime().exec(command);
                }else{ 
                    String[] command = {"/bin/bash", "-c","open "+pathdir+"\""+pathname+"\""};
                    Runtime.getRuntime().exec(command);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        }catch(FileNotFoundException e){
            System.out.println("Your video is not found");
            prompt_any();
            return false;
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }
    
    public static void make_dir(){
        try {
            if(os){     
                String[] command = {"cmd.exe","/k","md "+path};
                Runtime.getRuntime().exec(command);
            }else Runtime.getRuntime().exec("mkdir "+path); 
        } catch (Exception e) {
            //means directory is made
            //e.printStackTrace();
        }
    }
    
    public static void viewVideo(int authorID){
        System.out.println(show_videos(authorID));
        User author = id_to_users.get(authorID);
        ArrayList<Integer>videos = author.getVideos();
        if(videos.size()==0){
            System.out.println("There are no videos to view, please upload one to view.");
            prompt_any();
            return;
        }
        System.out.println("Please select video no. to view details");
        int c = prompt_input1(videos.size());
        int videoID=videos.get(c);
        Video v = id_to_videos.get(videoID);
        System.out.println(v.one_toString());
        
        System.out.print("Do you want to play this video? (y/n): ");
        String confirm = in.nextLine();
        if(confirm.equals("y")){
            play_page(authorID,v.getTitle());
        }
    }
    
    public static void upload(int authorID){
        make_dir();
        System.out.println("Please ensure you have uploaded your video in this file/directory: "+path);
        System.out.println("Example: ");
        System.out.println("Video Name (on Yuu-Tube): BTS");
        System.out.println("Video name in the file/directory: bts.mp4");
        String vidname;
        do{
            System.out.print("Video Name (on Yuu-Tube): ");
            vidname = in.nextLine();
            if(name_to_id.get(vidname)==null)break;
            else System.out.println("This title has been taken");
        }while(true);
        System.out.print("Video name in the file/directory: ");
        String vidpath = in.nextLine();
        
        if(!play(vidpath)){
            return;
        }
        
        System.out.print("Your video is supposed to pop out now. \nConfirm to upload this video? (y/n): ");
        if(prompt_yn()==true){
            User author = id_to_users.get(authorID);
            author.add_videos(videoptr);
            Video new_vid = new Video(videoptr,vidpath,vidname,authorID);
            id_to_videos.put(videoptr,new_vid);
            name_to_id.put(vidname,videoptr);
            System.out.println("Your video has been uploaded!");
            videoptr++;
        }
        return;
    }
    
    public static void delete(int authorID){
        System.out.println("Select your video no. to delete");
        System.out.println(show_videos(authorID));
        User author = id_to_users.get(authorID);
        ArrayList<Integer>videos = author.getVideos();
        //for(int i:videos)System.err.print(i+" "); System.err.println("");
        int c = prompt_input1(videos.size());
        int videoID=videos.get(c);
        Video v = id_to_videos.get(videoID);
        
        //show details
        System.out.println(v.one_toString());
        
        //last permission
        System.out.print("Are you sure you want to delete this video? (y/n): ");
        if(prompt_yn()==true){
            int views = v.getViewsCount();
            int totalviews = author.getTotalViews();
            author.setTotalViews(totalviews-views);
            //remove history and liked videos of others
            for(Map.Entry iter:id_to_users.entrySet()){
                User u = (User)iter.getValue();
                u.del_his(v.getTitle()); 
                HashMap<Integer,Integer> like_others = u.getId_to_like();
                u.rem_like(videoID);
            }
            author.del_videos(c);
            id_to_videos.remove(videoID);
            name_to_id.remove(v.getTitle());
            System.out.println(v.getTitle()+" has been deleted.");
        }
        return;
    }
    
    public static void topp(){
        //rank
        top = new ArrayList<Rank>();
        for(Map.Entry iter:id_to_videos.entrySet()){
            Video v = (Video)iter.getValue();
            int videoID = (int)iter.getKey();
            if(v!=null){
                int views = v.getViewsCount();
                int likes = v.getLikeCount();
                int dislikes = v.getDislikeCount();
                Rank rk = new Rank(videoID,views,likes,dislikes);
                top.add(rk);
            }
        }
        top.sort(Comparator.comparing(Rank::getRating).reversed());
        return;
    }
    
    public static String top5(){
        for(int i=0; i<Math.min(5,top.size()); i++){
            int videoID = top.get(i).getId();
            Video v = id_to_videos.get(videoID);
            if(v!=null)v.setTrendingNow(false);
        }
        topp();
        String res = "";
        for(int i=0; i<Math.min(5,top.size()); i++){
            int videoID = top.get(i).getId();
            Video v = id_to_videos.get(videoID);
            res += String.format("%d. %s\n",i+1,v.getTitle());
            v.setTrendingNow(true);
        }
        return res;
    }
}
/*
//https://crunchify.com/how-to-run-windowsmac-commands-in-java-and-return-the-text-result/
public printOutput getStreamWrapper(InputStream is, String type) {
    return new printOutput(is, type);
}
//private static process rte = new process();
//private static printOutput errorReported, outputMessage;
errorReported = rte.getStreamWrapper(p.getErrorStream(), "ERROR");
outputMessage = rte.getStreamWrapper(p.getInputStream(), "OUTPUT");
errorReported.start();
outputMessage.start();
private class printOutput extends Thread {
    InputStream is = null;

    printOutput(InputStream is, String type) {
        this.is = is;
    }

    public void run() {
        String s = null;
        try {
            BufferedReader br = new BufferedReader(
                new InputStreamReader(is));
            while ((s = br.readLine()) != null) {
                System.out.println(s);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
*/

