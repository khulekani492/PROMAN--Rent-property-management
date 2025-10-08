package API;

import model.database.landlord;
import io.javalin.http.Handler;
import java.sql.SQLException;
import java.util.Map;

import static API.SecurityUtil.hashPassword;

public class  {

    public Handler sign_up() {
        return ctx -> {
            try {
                String user_name = ctx.formParam("user_name");
                ctx.sessionAttribute("user_name", user_name);

                String email = ctx.formParam("user_email");
                String password = ctx.formParam("password");

                // hash password
                String hashedPassword = hashPassword(password);

                // insert user into database
                landlord new_user = new landlord(user_name, email, hashedPassword);
                new_user.insert_information();

                // get new user ID
                Integer new_userID = new_user.UniqueID();

                // set session attributes
                ctx.sessionAttribute("user_ID", new_userID);
                ctx.sessionAttribute("password", hashedPassword);

                // optional: send back session data as JSON
                Map<String, Object> sessionMap = ctx.sessionAttributeMap();
                ctx.json(sessionMap);

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