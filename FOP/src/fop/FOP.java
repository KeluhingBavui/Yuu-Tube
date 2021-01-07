package fop;

import java.util.*;
import java.io.*;

public class FOP {
    static Scanner in = new Scanner(System.in);
    static HashMap<Integer,User>id_to_users = new HashMap<Integer,User>(); 
    static HashMap<String,Integer>email_to_id = new HashMap<String,Integer>();
    static HashMap<Integer,Video>id_to_videos = new HashMap<Integer,Video>();
    static HashMap<String,Integer>name_to_id = new HashMap<String,Integer>();
    static ArrayList<Rank>top = new ArrayList<Rank>();
    
    static authentication auth = new authentication();
    static process proc = new process();
    static profile prof = new profile();
    static util U = new util();
    static Boolean ret_home = false;
    static final Boolean os = System.getProperty("os.name").contains("Windows");
    static String path;
    static int loginstate;
    static String emailDB,passwordDB,nameDB;
    static int userptr = 0;
    
    private static String[] start={ 
        "Stop Program",
        "Sign In",
        "Sign Up",
        "Forgot Password",
        "List all"
    };
    
    public static void main(String[] args){ //main
        //determine OS to run terminal/command prompt
        File f = new File(System.getProperty("user.dir"));
        if(os){
            path = f.toString()+"\\FOPvideos";
        }else{
            path = f.toString()+"/FOPvideos";
        }
        start_page();
    }
    
    public static void start_page(){
        int c;
        //read file
        U.readuser();
        do{
            System.out.println("--- Welcome to Yuu-Tube ---");
            for(int i=0; i<start.length; i++){
                System.out.printf("%d. %s\n",i,start[i]);
            }
            c = prompt_input0(start.length);
            
            System.out.printf("%s %s %s\n","--- ",start[c]," ---");
            //c=0,stop program
            if(c==1){
                //sign in/log in
                loginstate = 0;
                LoginForm lgf = new LoginForm();
                lgf.setVisible(true);
                lgf.pack();
                lgf.setLocationRelativeTo(null);
                System.out.print("Complete your login process first. Done? (y/n): ");
                if(prompt_yn()==true){
                    U.welcome((int) email_to_id.get(emailDB));
                    home_page((int) email_to_id.get(emailDB));
                    } else {
                        System.out.println("You haven't logged in to the system yet!");
                        start_page();
                    }
                }
            else if(c==2){
                //sign up/register
                RegistrationForm rgf = new RegistrationForm();
                rgf.setVisible(true);
                rgf.pack();
                rgf.setLocationRelativeTo(null);
                System.out.print("Complete your registration process first. Done? (y/n): ");
                String reply = in.next();
                if (prompt_yn()==true) {
                    U.hello((int) email_to_id.get(emailDB));
                    home_page((int) email_to_id.get(emailDB));
                    } else {
                        System.out.println("You haven't registered to the system yet!");
                        start_page();
                    }
                
            }else if(c == 3){
                //forgot password
            }else if(c == 4){
                //list all 
                U.list();
            }
            //keep save 
            U.save();
        }while(c!=0);
        //save file
        U.save();
    }
    
    /*  Home Page
            Trending videos:
                Display Top Fives
            Actions:
                play videos
                    run command prompt/terminal
                search videos
                    type to search
                your videos
                    View video
                    upload video
                    delete video
                your profile
                    change password
                    history
                    liked videos
                logout
    */
    
    private static String[] home_actions = {
        "Sign out",
        "Play video",
        "Search video",
        "My videos",
        "My profile"
    };
    
    public static void home_page(int userID){ //main
        int c;
        do{
            System.out.println("--- Yuu-Tube ---");
            System.out.println("What's hot now!");
            System.out.println(proc.top5());
            
            System.out.println("Available Actions: ");
            for(int i=0; i<home_actions.length; i++){
                System.out.printf("%d. %s\n",i,home_actions[i]);
            }
            c = prompt_input0(home_actions.length);
            
            if(c==0){
                //sign out
                loginstate = 0;
                System.out.printf("%s %s %s\n","--- ",home_actions[c]," ---");
            }else if(c==1){
                //play videos
                play_page(userID,null);
            }else if(c==2){
                //search videos
                search_page(userID);
            }else if(c == 3){
                //your videos
                videos_page(userID);
            }else if(c == 4){
                //your profile
                prof.profile(userID);
            }
            if(ret_home)ret_home=false;
        }while(c!=0);
    }
    /*
        prompt name
        play
        like, dislike, comment
    */
    private static String[] play_actions={   
        "Go Back",
        "Home Page",
        "Replay",
        "Like / Remove Like", 
        "Dislike / Remove Dislike", 
        "Comment",
        "View Comment",
        "View Channel",
        "Subscribe / Unsubscribe"
    };
    
    public static void play_page(int userID, String videoName){
        System.out.println("--- Play video ---");
        if(videoName==null){
            System.out.print("Enter the name of videos to play: ");
            videoName = in.nextLine();
            if(name_to_id.get(videoName)==null){
                System.out.println("Sorry, we don't have the video named "+videoName);
                return;
            }
        }
        int videoID = name_to_id.get(videoName);
        Video v = id_to_videos.get(videoID);
        String pathname = v.getPath();
        int authorID = v.getAuthorID();
        User author = id_to_users.get(authorID);
        User user = id_to_users.get(userID);
        if(!proc.play(pathname))return;
        v.incViews(); user.hadd_videos(videoName);
        int c;
        boolean first = true;
        do{
            if(!first)System.out.println("--- Play video ---");
            first = false;
            System.out.println(v.one_toString());
            int likey = user.get_like(videoID);
            boolean subscribe = user.get_sub(authorID);
            boolean like = likey==1;
            boolean dislike = likey==-1;

            if(like|dislike)System.out.println("You have "+(like?"liked":"")+(dislike?"disliked":"")+" this video.");
            if(subscribe)System.out.println("You have subcribed to this channel.");
            
            System.out.println("Available Actions: ");
            for(int i=0; i<play_actions.length-(authorID==userID?1:0); i++){
                if(i==3){
                    if(like)System.out.println("3. Remove Like");
                    else System.out.println("3. Like");
                    continue;
                }
                if(i==4){
                    if(dislike)System.out.println("4. Remove Dislike");
                    else System.out.println("4. Dislike");
                    continue;
                }
                if(i==8){
                    if(subscribe)System.out.println("8. Unsubscribe");
                    else System.out.println("8. Subscribe");
                    continue;
                }               
                System.out.printf("%d. %s\n",i,play_actions[i]);
            }
            c = prompt_input0(play_actions.length-(authorID==userID?1:0));
            String command = play_actions[c];
            if(c==3){
                if(like)command="Remove Like";
                else command="Like";
            }
            if(c==4){
                if(dislike)command="Remove Dislike";
                else command="Dislike";
            }
            if(c==8){
                if(subscribe)command="Unsubscribe";
                else command="Subscribe";
            } 
            System.out.println("--- "+command+" ---");
            //c=0, go back
            if(c==1){
                //home page
                ret_home=true;
            }if(c==2){
                //replay
                proc.play(pathname);
                v.incViews();
            }else if(c==3){
                //like / remove like
                if(likey==1){
                    //remove like
                    likey=0;
                    v.decLike();
                }else if(likey==0){
                    //like
                    likey=1;
                    v.incLike();
                }else{
                    //like, remove dislike
                    likey=1;
                    v.incLike();
                    v.decDislike();
                }
            }else if(c==4){
                //dislike
                if(likey==-1){
                    //remove dislike
                    likey=0;
                    v.decDislike();
                }else if(likey==0){
                    //dislike
                    likey=-1;
                    v.incDislike();
                }else{
                    //dislike, remove like
                    likey=-1;
                    v.incDislike();
                    v.decLike();
                }
            }else if(c==5){
                //comment
                System.out.print("Type to comment: ");
                String comment = in.nextLine();
                v.addComment(userID, comment);
            }else if(c==6){
                //viewcomment;
                if(v.getComments().size()==0){
                    //oof, no comments
                    System.out.println("There isn't any comments on this video.");
                    prompt_any();
                    break;
                }
                System.out.println("Please select comment no. to view details");
                int cc = prompt_input1(v.getComments().size());
                proc.viewComment(videoID,cc,userID);
            }else if(c==7){
                //show channel
                proc.viewAuthor(authorID, userID);
            }else if(c==8){
                //subscribe
                user.set_sub(authorID);
                author.incSubs(subscribe?-1:1);
            }
            user.set_like(videoID, likey);
            if(ret_home)break;
        }while(c!=0);
        return;
    }
    
    private static String[] search_actions = {
        "Go Back",
        "Home Page",
        "Search Again",
        "Play Video",
    };
    
    public static void search_page(int userID){
        int c;
        do{
            System.out.println("--- Search Video ---");
            System.out.println("Press enter to list all videos or type to search. ");
            System.out.print("Video Name/Author Name: ");
            String searchName = in.nextLine();
            ArrayList<String>videoName = new ArrayList<String>();
            ArrayList<String>authors = new ArrayList<String>();
            ArrayList<Integer>views = new ArrayList<Integer>();
            proc.topp();
            for(int i=0; i<top.size(); i++){
                int videoID = top.get(i).getId();
                Video v = id_to_videos.get(videoID);
                String curName = v.getTitle();
                String author = (id_to_users.get(v.getAuthorID())).getName();
                if(curName.contains(searchName)||author.contains(searchName)){
                    videoName.add(curName);
                    authors.add(author);
                    views.add(v.getViewsCount());
                }
            }
            if(videoName.size()==0){
                System.out.println("Nothing was found related to "+searchName);
                prompt_any();
                return;
            }
            for(int i=0; i<videoName.size(); i++){
                System.out.printf("%d. %s %s %d %s\n",i+1,videoName.get(i)+" by",authors.get(i)+" at",views.get(i),"view(s)");
            }
            
            System.out.println("");
            System.out.println("Available Actions: ");
            for(int i=0; i<search_actions.length; i++){
                System.out.printf("%d. %s\n",i,search_actions[i]);
            }
            c = prompt_input0(search_actions.length);
            System.out.println("--- "+search_actions[c]+" ---");
            //c=0, go back
            if(c==1){
                //home page
                ret_home=true;
            }
            //c=2, search again
            if(c==3){
                //play
                for(int i=0; i<videoName.size(); i++){
                    System.out.printf("%d. %s %s %d %s\n",i+1,videoName.get(i)+" by",authors.get(i)+" at",views.get(i),"view(s)");
                }
                System.out.println("Please select your video no. on the list to play.");
                int cc = prompt_input1(videoName.size());
                String vidName = videoName.get(cc);
                play_page(userID,vidName);
            }
            if(ret_home)break;
        }while(c!=0);
    }
    
    private static String[] videos_actions={   
        "Go Back",
        "Home Page",
        "View Video",
        "Upload video", 
        "Delete Video"
    };
    
    public static void videos_page(int userID){
        int c;
        do{
            System.out.println("--- My videos ---");
            //display
            System.out.println(proc.show_videos(userID));
            //prompt for actions
            System.out.println("Available Actions: ");
            for(int i=0; i<videos_actions.length; i++){
                System.out.printf("%d. %s\n",i,videos_actions[i]);
            }
            c = prompt_input0(videos_actions.length);

            System.out.printf("%s %s %s\n","--- ",videos_actions[c]," ---");
            //c=0, go back
            if(c==1){
                //home page
                ret_home=true;
            }if(c==2){
                //viewvideo
                proc.viewVideo(userID);
            }else if(c==3){
                //upload
                proc.upload(userID);
            }else if(c==4){
                //delete
                proc.delete(userID);
            }
            if(ret_home)break;
        }while(c!=0);
    }
    
    public static int prompt_input0(int mx){
        //0-index
        System.out.print("Please select your option: ");
        String choice = in.nextLine(); 
        boolean invalid = !U.isInteger(choice);
        int c=-1;
        if(!invalid)c = Integer.parseInt(choice);
        while(invalid||c<0||c>=mx){
            System.out.println("Invalid Input.");
            System.out.printf("%s %d %s %d: ","Enter your choice between",0,"-",mx-1);
            choice = in.nextLine(); 
            invalid = !U.isInteger(choice);
            if(!invalid)c = Integer.parseInt(choice);
            System.err.println("choice: "+choice);
        }
        clearConsole();
        return c;
    }
    public static int prompt_input1(int mx){
        //read input as 1-index, returning 0-index
        System.out.print("Please select your option: ");
        String choice = in.nextLine(); 
        boolean invalid = !U.isInteger(choice);
        int c=-1;
        if(!invalid)c = Integer.parseInt(choice)-1;
        while(invalid||c<0||c>=mx){
            System.out.println("Invalid Input.");
            System.out.printf("%s %d %s %d: ","Enter your choice between",1,"-",mx);
            choice = in.nextLine(); 
            invalid = !U.isInteger(choice);
            if(!invalid)c = Integer.parseInt(choice)-1;
            System.err.println("choice: "+choice);
        }
        clearConsole();
        return c;
    }
    
    public static boolean prompt_yn(){
        do{
            String res = in.nextLine();
            if(res.equals("y"))return true;
            else if(res.equals("n"))return false;
            else{
                System.out.print("Invalid input. Please enter 'y' or 'n': ");
            }
        }while(true);
    }
    
    public static void prompt_any(){
        System.out.println("Press any key to continue.");
        in.nextLine();
        clearConsole();
    }
    public final static void clearConsole(){  
        try {   
            if (os){  
                
                String[] command = {"cmd.exe","/k","cls"};
                Runtime.getRuntime().exec(command);
            }  
            else{  
                Runtime.getRuntime().exec("clear");  
            }  
        }  
        catch (final Exception e){  
            e.printStackTrace();  
        }  
    }  
}
