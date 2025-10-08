package API_handlers;

import model.database.landlord;
import io.javalin.http.Handler;
import java.sql.SQLException;
import java.util.Map;

import static API.SecurityUtil.hashPassword;

public class  loginAPIhandler {

    public Handler log_in() {
        return ctx -> {
            try {
                //get username and password from the login form
                //validate the password using decryption
                //if password is successful return the user_profile url
//                ctx

                // render property page
                ctx.render("/templates/property.html");

            } catch (SQLException e) {
                ctx.status(500).result("Database error: " + e.getMessage());
                e.printStackTrace();
            } catch (Exception e) {
                ctx.status(400).result("Error: " + e.getMessage());
                e.printStackTrace();
            }
        };
    }
}