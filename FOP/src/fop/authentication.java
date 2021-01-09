package fop;

import static fop.YuuTube.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
}
