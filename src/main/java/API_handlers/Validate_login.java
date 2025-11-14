package API_handlers;

import io.javalin.http.Handler;
import model.database.CRUD.Get_password;

import static API.SecurityUtil.checkPassword;
import static API.SecurityUtil.hashPassword;

public class Validate_login {

    public Handler authuticate(){
        return ctx -> {
            String user_email = ctx.formParam("email");
            String password = ctx.formParam("password");
            System.out.println(user_email);
            Get_password hashed = new Get_password(user_email);
            System.out.println(hashed);
            System.out.println("b" + hashed.compare());
            boolean isMatch = checkPassword(password, hashed.compare() );
            if (isMatch){
                ctx.sessionAttribute("email",user_email);
                ctx.redirect("dashboard");
            } else {
                ctx.result("wrong password");
            }

        };


        }
    }

