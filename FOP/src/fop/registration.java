package fop;

import static fop.FOP.*;
import static fop.authentication.*;

public class registration {
    static int userptr = 0;
    public int create_user(){
        int id = userptr;
        System.out.print("Name: ");
        String name = in.nextLine();
        System.out.print("Email: ");
        String email = in.nextLine();
        if(email_to_id.get(email)!=null){
            System.out.println("This email has registered on Yuu-Tube. \nPlease Sign In or choose Forgot Password.");
            prompt_any();
            return -1;
        }
        String password, password2 = null;
        do{
            System.out.println("Requires length of password of at least 8. ");
            System.out.print("Password: ");
            password = in.nextLine();
            if(!password_length(password))continue;
            System.out.print("Re-type Password: ");
            password2 = in.nextLine();
        }while(!password.equals(password2));
        User new_user = new User(name,email,password,id);
        id_to_users.put(id,new_user);
        email_to_id.put(email,id);
        userptr++;
        return id;
    }
}
