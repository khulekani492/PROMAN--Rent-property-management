package Invalidhandler;

import io.javalin.http.Handler;

import java.util.HashMap;

public class NoEmail implements ErrorHandler {
    @Override
    public Handler error_message() {
        return ctx -> {
            String attempted_email = ctx.sessionAttribute("login_email");
            System.out.println(attempted_email);
            HashMap<String,String> model = new HashMap<>();
            model.put("error", attempted_email + " EMAIL NOT FOUND"   );
            model.put("Create","Sign up");
            ctx.render("templates/login.html",model);
        };
    }
}
