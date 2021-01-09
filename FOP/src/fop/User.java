package fop;

import static fop.YuuTube.*;
import static fop.authentication.*;
import java.util.*;

public class User implements java.io.Serializable{
    private String name, email, password;
    private int id, subscribersCount, totalViews;
    private ArrayList<Integer> videos;
    private ArrayList<String>history;
    private HashMap<Integer,Integer>id_to_like;
    private HashMap<Integer,Boolean>id_to_sub;
    
    public User(){
        name=email=password="";
        id=subscribersCount=totalViews=0;
        videos=new ArrayList<Integer>();
        history=new ArrayList<String>();
        id_to_like=new HashMap<Integer,Integer>();
        id_to_sub=new HashMap<Integer,Boolean>();
    }

    public User(String name, String email, String password, int id) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.id = id;
        subscribersCount=totalViews=0;
        videos=new ArrayList<Integer>();
        history=new ArrayList<String>();
        id_to_like=new HashMap<Integer,Integer>();
        id_to_sub=new HashMap<Integer,Boolean>();
    }
    
    public String one_toString(){ 
        String res = "";
        res+="Name: "+name+"\n";
        res+="Email: "+email+"\n";
        res+="videosCount: "+videos.size()+"\n";
        res+="subscribers Count: "+subscribersCount+"\n";
        res+="totalViews: "+totalViews+"\n";
        return res;
    }
    
    public void add_videos(int videoID){
        this.videos.add(videoID);
    }
    
    public void del_videos(int pos){
        this.videos.remove(pos);
    }
    
    public void del_his(int pos){
        this.history.remove(pos);
    }
    
    public void hadd_videos(String name){
        this.history.removeIf(str->str.contains(name));
        this.history.add(name);
    }
    
    public void rem_sub(int pos){
        if(id_to_sub.get(pos)!=null)id_to_sub.remove(pos);
    }
    
    public void rem_like(int pos){
        if(id_to_like.get(pos)!=null)id_to_sub.remove(pos);
    }
    
    public int get_like(int videoID){
        if(id_to_like.get(videoID)==null)return 0;
        else return id_to_like.get(videoID);
    }
    
    public void set_like(int videoID, int val){
        id_to_like.put(videoID, val);
    }
    
    public boolean get_sub(int authorID){
        if(id_to_sub.get(authorID)==null){
            id_to_sub.put(authorID, Boolean.FALSE);
        }
        return id_to_sub.get(authorID);
    }
    
    public void set_sub(int authorID){
        id_to_sub.put(authorID,!id_to_sub.get(authorID));
    }
    
    public void incSubs(int val){
        this.subscribersCount += val;
    }
    
    public void incViews(){
        this.totalViews++;
    }
    
    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
    
    public int getId() {
        return id;
    }

    public int getSubsribersCount() {
        return subscribersCount;
    }

    public int getTotalViews() {
        return totalViews;
    }

    public ArrayList<Integer> getVideos() {
        return videos;
    }

    public ArrayList<String> getHistory() {
        return history;
    }

    public HashMap<Integer, Integer> getId_to_like() {
        return id_to_like;
    }

    public HashMap<Integer, Boolean> getId_to_sub() {
        return id_to_sub;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setSubsribersCount(int subscribersCount) {
        this.subscribersCount = subscribersCount;
    }

    public void setTotalViews(int totalViews) {
        this.totalViews = totalViews;
    }

    public void setVideos(ArrayList<Integer> videos) {
        this.videos = videos;
    }

    public void setHistory(ArrayList<String> history) {
        this.history = history;
    }

    public void setId_to_like(HashMap<Integer, Integer> id_to_like) {
        this.id_to_like = id_to_like;
    }
    
    public void setId_to_sub(HashMap<Integer, Boolean> id_to_sub) {
        this.id_to_sub = id_to_sub;
    }
    
    
    /*
    public String toString(){ //for test
        String res = "";
        res+="Name: "+name+"\n";
        res+="Email: "+email+"\n";
        res+="Password: "+password+"\n";
        res+="id: "+id+"\n";
        res+="videosCount: "+videos.size()+"\n";
        res+="subscribers Count: "+subscribersCount+"\n";
        res+="totalViews: "+totalViews+"\n";
        res+="VideosID: \n";
        if(videos.size()!=0){
            for(int i=0; i<videos.size(); i++){
               res+=videos.get(i)+", ";
            }
            res+="\n";
        }else{
            res+="empty la wei\n";
        }
        return res;
    }
    */
}
