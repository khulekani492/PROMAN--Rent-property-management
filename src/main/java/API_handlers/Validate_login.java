package API_handlers;

import io.javalin.http.Handler;

import static API.SecurityUtil.checkPassword;
import static API.SecurityUtil.hashPassword;

public class Validate_login {

    public Handler authuticate(){
        return ctx -> {
            String user_email = ctx.formParam("email");
            String password = ctx.formParam("password");

            //
            String hashedPassword = "";

            boolean isMatch = checkPassword(password, hashedPassword);
            System.out.println("Password matches: " + isMatch);

        };


        }
    }

