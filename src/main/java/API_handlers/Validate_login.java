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


            Get_password hashed = new Get_password(user_email);
            if("NO USER FOUND".equals(hashed.compare())){
                ctx.sessionAttribute("login_email",user_email);
                ctx.redirect("/error/no_email");
                return;
            }
            boolean isMatch = false;
            try{
                 isMatch = checkPassword(password, hashed.compare());
            } catch (RuntimeException e) {
                System.out.println("error");
                System.out.println(e.getMessage());
                throw new RuntimeException(e);
            }


            try {
                if (isMatch){
                    ctx.sessionAttribute("email",user_email);
                    ctx.redirect("dashboard");
                } else {
                    ctx.result("wrong password");
                }

            } catch (RuntimeException e) {
                System.out.println(e.getMessage());
                throw new RuntimeException(e);
            }

        };


        }
    }

