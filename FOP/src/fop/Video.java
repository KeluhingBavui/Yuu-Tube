package fop;

import static fop.YuuTube.*;
import java.util.*;

public class Video implements java.io.Serializable{
    private String path, title;
    private int videoID, authorID, likeCount, dislikeCount, viewsCount;
    private boolean trendingNow;
    private ArrayList<Comment> comments;
    
    public Video(){
        path=title="";
        videoID=authorID=likeCount=dislikeCount=viewsCount=0;
        trendingNow=false;
        comments=new ArrayList<Comment>();
    }

    public Video(int videoID, String path, String title, int author) {
        this.videoID = videoID;
        this.path = path;
        this.title = title;
        this.authorID = author;
        this.likeCount = 0;
        this.dislikeCount = 0;
        this.viewsCount = 0;
        this.trendingNow = false;
        this.comments = new ArrayList<Comment>();
    }
    
    public String all_toString(){
        String res = "";
        res += "VideoID: "+ videoID +"\n";
        res += "Path: "+ path + "\n";
        res += "Title: "+ title + "\n";
        res += "Author: "+id_to_users.get(authorID).getName()+"\n";
        
        res += "Likes: "+likeCount+"\n";
        res += "Dislikes: "+dislikeCount+"\n";
        res += "Views: "+viewsCount+"\n";
        res += "trendingNow: "+trendingNow+"\n";
        res += "Comments: \n";
        for(int i=0; i<Math.min(3,comments.size()); i++){
            Comment cur = comments.get(i);
            User commentor = id_to_users.get(cur.getUserID());
            System.out.println(cur.getComment()+"by "+commentor.getName());   
        }
        if(comments.size()>3)System.out.println("... \n \n");
        
        return res;
    }
    
    public String one_toString(){
        String res = "";
        if(trendingNow)res+="This video is trending now. \n";
        res += ">>> "+ title + " <<<\n";
        res += "Likes: "+likeCount+"\n";
        res += "Dislikes: "+dislikeCount+"\n";
        res += "Views: "+viewsCount+"\n";
        res += "Comments: \n";
        for(int i=0; i<comments.size(); i++){
            Comment cur = comments.get(i);
            User commenter = id_to_users.get(cur.getUserID());
            res+=i+1+". "+cur.getComment()+" by "+commenter.getName()+"\n";
        }
        return res;
    }
    
    public String table_toString(){
        String res = "";
        for(int i=0; i<20-title.length(); i++)res+=" ";
        res += title;
        res += "|";
        
        for(int i=0; i<5-Integer.toString(likeCount).length(); i++)res+=" ";
        res += likeCount;
        res += "|";
        
        for(int i=0; i<8-Integer.toString(dislikeCount).length(); i++)res+=" ";
        res += dislikeCount;
        res += "|";
        
        for(int i=0; i<5-Integer.toString(viewsCount).length(); i++)res+=" ";
        res += viewsCount;
        res += "|";
        
        for(int i=0; i<8-Integer.toString(comments.size()).length(); i++)res+=" ";
        res += comments.size();
        res += "|";
        
        if(trendingNow==true)res+=" ";
        else res += "";
        res += trendingNow;
        res += "\n";
        return res;
    }
    
    public void incLike(){
        this.likeCount++;
    }
    
    public void decLike(){
        this.likeCount--;
    }
    
    public void incDislike(){
        this.dislikeCount++;
    }
    
    public void decDislike(){
        this.dislikeCount--;
    }
    
    public void addComment(int userID, String comment){
        Comment c = new Comment(userID,videoID,comment);
        comments.add(c);
    }
    
    public void incViews(){
        this.viewsCount++;
        User author = id_to_users.get(authorID);
        author.incViews();
    }
    
    public void comment(){
        
    }
    
    public void setPath(String path) {
        this.path = path;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setVideoID(int videoID) {
        this.videoID = videoID;
    }

    public void setAuthorID(int authorID) {
        this.authorID = authorID;
    }

    public void setTrendingNow(boolean trendingNow) {
        this.trendingNow = trendingNow;
    }

    public void setComments(ArrayList<Comment> comments) {
        this.comments = comments;
    }
    
    public String getPath() {
        return path;
    }

    public String getTitle() {
        return title;
    }

    public int getVideoID() {
        return videoID;
    }

    public int getAuthorID() {
        return authorID;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public int getDislikeCount() {
        return dislikeCount;
    }

    public int getViewsCount() {
        return viewsCount;
    }

    public boolean isTrendingNow() {
        return trendingNow;
    }

    public ArrayList<Comment> getComments() {
        return comments;
    }
}
