package API_handlers;

import io.javalin.http.Handler;
import jakarta.servlet.http.HttpSession;
import model.database.CRUD.landlord;
import model.database.CRUD.propertyNames;
import model.database.general;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static API.SecurityUtil.hashPassword;

public class Signup {
    public Handler sign_up() {
        return ctx -> {
            try {
                //Delete any existing session memory for new sign up
                HttpSession session = ctx.req().getSession(false);
                if (session != null)   {   session.invalidate();    }


                String user_name = ctx.formParam("user_name");

                String contact = ctx.formParam("contact");
                String email = ctx.formParam("user_email");
                System.out.println(email);
                String password = ctx.formParam("password");
                String user_type = ctx.formParam("user_type");

                landlord authenticate = new landlord();
                ctx.sessionAttribute("loginUsername",user_name);
                ctx.sessionAttribute("contact",contact);
                ctx.sessionAttribute("email",email);
                ctx.sessionAttribute("password",password);

                // hash password
                String hashedPassword = hashPassword(password);

                // insert user into database
                general new_user = new general(user_name, contact, email, hashedPassword,user_type);
                try{
                    new_user.insert_information();
                    Integer new_userID = new_user.UniqueID();
                    // set session attributes
                    ctx.sessionAttribute("user_ID", new_userID);
                    ctx.sessionAttribute("password", hashedPassword);

                    //String  login_user_name = authenticate.landlord_username(landlord_id);
                    ctx.sessionAttribute("landlordID", new_userID);
                    ctx.sessionAttribute("loginUsername",user_name);

                    // set session attributes the username and property_name
                    String landlord_username = ctx.formParam("user_name");
                    Map<String, Object> sessionMap = ctx.sessionAttributeMap();
                    System.out.println(sessionMap);

                    Map<String, Object> model = new HashMap<>();
                    model.put("user_name", landlord_username);

                    ctx.redirect("/add_property_unit");

                } catch (SQLException e){
                    if ("exists".equals(e.getMessage())) {
                        System.out.println(ctx.attributeMap());
                        ctx.sessionAttribute("error1",e.getMessage());
                        ctx.redirect("/error/same_email");
                    } else {
                        ctx.status(500).result("Unexpected error: " + e.getMessage());
                        //   ctx.redirect("/user_sign_up/error");
                    }
                }


                // get new user ID


            } catch (SQLException e) {
                ctx.status(500).result("Database wonder error: " + e.getMessage());
                e.printStackTrace();
            } catch (Exception e) {
                ctx.status(400).result("Error: " + e.getMessage());
                e.printStackTrace();
            }
        };
    }
}
