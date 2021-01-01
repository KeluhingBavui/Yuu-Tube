package fop;

import static fop.FOP.*;

public class authentication {
    public static int prompt_user(){
        System.out.print("Email: ");
        String email = in.nextLine();
        
        System.out.print("Password: ");
        String password = in.nextLine();
        
        if(email_to_id.get(email)==null)return -2;
        
        int id = (int)email_to_id.get(email);
        
        System.err.println("id found when auth: "+id);
        return check(id,password);
    }
    
    public static int check(int id, String in_password){
        //return -2 when account not found
        //return -1 when password incorrect
        //return -1 if not found
        User curr = id_to_users.get(id);
        if(curr==null)return -2;
        String password = curr.getPassword();
        if(password.equals(in_password))return id;
        return -1;
    }
    
    public static void chg_password(int id){
        User cur = id_to_users.get(id);
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
    
}
