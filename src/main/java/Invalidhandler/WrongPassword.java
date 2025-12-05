package Invalidhandler;

import io.javalin.http.Handler;

import java.util.HashMap;

public class WrongPassword implements  ErrorHandler {

    @Override
    public Handler error_message() {
        return ctx -> {
            String attempted_email = ctx.sessionAttribute("login_email");
            HashMap<String,String> model = new HashMap<>();
            String themeColor = ctx.sessionAttribute("theme_color");
            model.put("user_chosen_theme",themeColor);
            model.put("wrong", " password Incorrect"   );
            model.put("login_email", attempted_email );
            ctx.render("templates/login.html",model);
        };
    }
}
