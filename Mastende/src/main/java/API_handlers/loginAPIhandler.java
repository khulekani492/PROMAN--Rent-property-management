package API_handlers;

import model.database.landlord;
import io.javalin.http.Handler;

public class  loginAPIhandler {

    public Handler log_in() {
        return ctx -> {
            try {
                //get username and password from the login form
                String user_name = ctx.formParam("in_user_name");
                String email = ctx.formParam("in_user_email");
                String password = ctx.formParam("in_password");

                landlord landlordcheck = new landlord();
                //Query db search username ,user_email,password
                //boolean passwordexist = landlordcheck.confirm_password(email,password);
//                if(passwordexist){
//                    return;
//                }

                //validate the password using decryption
                //if password is successful return the user_profile url


                // render property page
                ctx.render("/templates/property.html");
            } catch (Exception e) {
                ctx.status(400).result("Error: " + e.getMessage());
                e.printStackTrace();
            }
        };
    }
}