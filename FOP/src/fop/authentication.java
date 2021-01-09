package fop;

import static fop.YuuTube.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class authentication {
    
    public static void chg_email(int userID){
        User cur = id_to_users.get(userID);
        System.out.println("--- Change Email ---");
        String oldemail = cur.getEmail();
        System.out.println("Old Email: "+oldemail);
        System.out.print("New Email: ");
        String newemail = in.nextLine();
        System.out.print("Confirm changing email from "+oldemail+" to "+newemail+"(y/n): ");
        if(prompt_yn()==true){
            emailDB = newemail;
            cur.setEmail(emailDB);
            email_to_id.remove(oldemail);
            email_to_id.put(newemail, userID);
            UpdateEmailDB(emailDB);
        }
    }
    
    public static void UpdateEmailDB(String newemail) {
        PreparedStatement ps;
        String query = "UPDATE userdata SET useremail = ? WHERE userid = ?";
        int affectedrows = 0;
        try {
            ps = ConnectionDB.dbConnection().prepareStatement(query);
            
            ps.setString(1, newemail);
            ps.setInt(2, email_to_id.get(newemail));
            
            affectedrows = ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(LoginForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void chg_password(int userID){
        User cur = id_to_users.get(userID);
        String old_pass = cur.getPassword();
        System.out.println("--- Change Password ---");
        System.out.print("Old Password: ");
        String in_old_pass = in.nextLine();
        
        if(!in_old_pass.equals(old_pass)){
            System.out.println("This does not match with old password");
            prompt_any();
            return; //old not same
        }
        System.out.println("Requires length of password of at least 8. ");
        do{
            System.out.print("New Password: ");
            String new_pass = in.nextLine();
            System.out.print("Re-type New Password: ");
            String new2_pass = in.nextLine();
            if(!new_pass.equals(new2_pass)){
                System.out.println("New passwords does not match");
                prompt_any();
                return; //new not same
            }else if(password_length(new_pass)){
                UpdatePasswordDB(new_pass);
                cur.setPassword(new_pass);
                System.out.println("New password has been set");
                prompt_any();
                return;
            }else{
                System.out.println("Password doesn't have length of at least 8.");
                prompt_any();
            }
        }while(true);
    }
    
    public static boolean password_length(String s){
        return (s.length()>=8);
    }
    
    public static void UpdatePasswordDB(String new_pass) {
        /*Since we're using both database and fileio at the same time, whenever a
        user changes the password, this method will update the new password in the
        database too instead of just updating the details in the binary file
        */
        PreparedStatement ps;
        String query = "UPDATE userdata SET password = ? WHERE useremail = ?";
        int affectedrows = 0;
        try {
            ps = ConnectionDB.dbConnection().prepareStatement(query);
            
            ps.setString(1, new_pass);
            ps.setString(2, emailDB);
            
            affectedrows = ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(LoginForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void deleteAccount(int userID){
        User cur = id_to_users.get(userID);
        System.out.println("--- Delete Account ---");
        System.out.println("Once you delete your account,");
        System.out.println("your likes, dislikes and comments, subscribe counts to other channels, your video(s) also be deleted");
        System.out.print("Are you sure you want to delete this account? (y/n): ");
        if(prompt_yn()==true){
            //unsubscribe from all channels
            for(Map.Entry iter:cur.getId_to_sub().entrySet()){
                int authorID = (int)iter.getKey();
                boolean sub = (boolean)iter.getValue();
                User author = id_to_users.get(authorID);
                author.incSubs(-1);
            }
            //remove likes and dislikes
            for(Map.Entry iter:cur.getId_to_like().entrySet()){
                int videoID = (int)iter.getKey();
                int like = (int)iter.getValue();
                Video v = id_to_videos.get(videoID);
                if(like==1)v.decLike();
                else if(like==-1)v.decDislike();
            }
            //remove comments
            for(Map.Entry iter:id_to_videos.entrySet()){
                Video v = (Video)iter.getValue();
                ArrayList<Comment>cm=v.getComments();
                ArrayList<Integer>rm=new ArrayList<Integer>();
                for(int i=0; i<cm.size(); i++){
                    Comment c = cm.get(i);
                    if(c.getUserID()==userID){
                        rm.add(i);
                    }
                }
                for(int i=rm.size()-1; i>=0; i--){
                    v.delcomment(rm.get(i));
                }
            }
            //delete own videos
            for(int videoID:cur.getVideos()){
                id_to_videos.remove(videoID);
            }
            
            String email = cur.getEmail();
            email_to_id.remove(email);
            DelAccDB(userID);
        }
    }
    
    public static void DelAccDB(int userID) {
        PreparedStatement ps;
        String query = "DELETE FROM userdata WHERE userid = ?";
        int affectedrows = 0;
        try {
            ps = ConnectionDB.dbConnection().prepareStatement(query);
            
            ps.setInt(1, userID);
            
            affectedrows = ps.executeUpdate();
            ret_start=true;
        } catch (SQLException ex) {
            Logger.getLogger(LoginForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
