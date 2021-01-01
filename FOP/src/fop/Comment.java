package fop;

public class Comment implements java.io.Serializable{
    private int userID,videoID;
    private String comment;

    public Comment(int userID, int videoID, String comment) {
        this.userID = userID;
        this.videoID = videoID;
        this.comment = comment;
    }
    
    public String toString(){
        String res = "";
        res += "User ID: "+userID+"\n";
        res += "Video ID: "+videoID+"\n";
        res += "Comment: "+comment+"\n";
        return res;
    }

    public int getUserID() {
        return userID;
    }

    public int getVideoID() {
        return videoID;
    }

    public String getComment() {
        return comment;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public void setVideoID(int videoID) {
        this.videoID = videoID;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
