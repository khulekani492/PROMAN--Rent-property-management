package Invalidhandler;

import io.javalin.http.Handler;

import java.util.HashMap;

public class Property_address implements  ErrorHandler{
    @Override
    public Handler error_message() {
        return ctx ->{
            String property_name = ctx.sessionAttribute("property_name");
            String property_address = ctx.sessionAttribute("property_address");
            HashMap<String,String> model = new HashMap<>();
            String themeColor = ctx.sessionAttribute("theme_color");
            model.put("user_chosen_theme",themeColor);
            model.put("property_error",  property_name  );
            model.put("property_name",property_address);
            ctx.render("templates/property.html",model);
        };
    }
}
