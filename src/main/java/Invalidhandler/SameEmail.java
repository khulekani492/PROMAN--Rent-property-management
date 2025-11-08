package Invalidhandler;

import io.javalin.http.Handler;

import java.util.HashMap;

public class SameEmail implements ErrorHandler{
    @Override
    public Handler error_message() {
        return ctx -> {
            String update_user = ctx.sessionAttribute("error1");
            String username = ctx.sessionAttribute("user_name");
            String contact = ctx.sessionAttribute("contact");
            String email = ctx.sessionAttribute("email");
            String password = ctx.sessionAttribute("password");
            HashMap<String,String> model = new HashMap<>();
            model.put("error", " Email already " + update_user +" Log in" );
            model.put("username",username);
            model.put("contact",contact);
            model.put("email",email);
            model.put("password",password);
            ctx.render("templates/landlord.html",model);
        };
    }
}
