package fop;

import static fop.FOP.*;
import java.io.*;
import java.util.*;

public class Rank implements java.io.Serializable{
    private int id,viewsCount,likeCount,dislikeCount;

    public Rank(){
        viewsCount=likeCount=dislikeCount=0;
    }
    
    public Rank(int id, int viewsCount, int likeCount, int dislikeCount) {
        this.id = id;
        this.viewsCount = viewsCount;
        this.likeCount = likeCount;
        this.dislikeCount = dislikeCount;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    public void setViewsCount(int viewsCount) {
        this.viewsCount = viewsCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public void setDislikeCount(int dislikeCount) {
        this.dislikeCount = dislikeCount;
    }
    
    public int getRating(){
        int score = viewsCount + likeCount*5 - dislikeCount*3;
        return score;
    }

    public int getId() {
        return id;
    }
    
    public int getViewsCount() {
        return viewsCount;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public int getDislikeCount() {
        return dislikeCount;
    }
}
