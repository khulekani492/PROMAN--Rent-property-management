package api_login;

import io.javalin.http.Handler;
import jakarta.servlet.http.HttpSession;
import model.database.CRUD.Get_password;
import model.database.CRUD.landlord;
import model.database.CRUD.propertyNames;

import java.util.ArrayList;
import java.util.Set;

import static API.SecurityUtil.checkPassword;

public class Validate_login {

    public Handler
    authenticate(){
        return ctx -> {
            String user_email = ctx.formParam("email");
            String password = ctx.formParam("password");
            System.out.println("Password : " + password);


            Get_password hashed = new Get_password(user_email);
            if("NO USER FOUND".equals(hashed.compare())){
                ctx.sessionAttribute("login_email",user_email);
                ctx.redirect("/error/no_email");
                return;
            }
            boolean isMatch ;
            try{
                 isMatch = checkPassword(password, hashed.compare());
            } catch (RuntimeException e) {
                System.out.println("error");
                System.out.println(e.getMessage());
                throw new RuntimeException(e);
            }


            try {
                if (isMatch){
                   // ctx.sessionAttribute()
                    //Kill any session instance exist if password is a match
                    HttpSession session = ctx.req().getSession(false);
                    if (session != null)   {   session.invalidate();    }
                    ctx.sessionAttribute("email",user_email);

                    String email = ctx.sessionAttribute("email");
                    propertyNames default_properties = new propertyNames();
                    landlord authenticate = new landlord();
                    Integer landlord_id =  authenticate.landlordId(email);
                    Set<String> landlord_properties = default_properties.fetchAllproperty(landlord_id);
                    ArrayList<String> default_property = new ArrayList<>(landlord_properties);
                    Integer  total_properties = default_property.size();
                    String property_name = ctx.sessionAttribute("current_property");
                    System.out.println("Current property in memory : " + property_name);
                    String  first_property_name = default_property.getFirst();;
                    ctx.sessionAttribute("current_property", first_property_name);
                    ctx.redirect("dashboard");
                } else {
                   // ctx.sessionAttribute("login_password",password);
                    ctx.redirect("/error/wrong_password");

                }

            } catch (RuntimeException e) {
                System.out.println(e.getMessage());
                throw new RuntimeException(e);
            }

        };


        }
    }

