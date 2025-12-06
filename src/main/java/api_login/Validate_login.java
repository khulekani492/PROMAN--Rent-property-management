package api_login;

import io.javalin.http.Handler;
import jakarta.servlet.http.HttpSession;
import model.database.CRUD.Get_password;

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

