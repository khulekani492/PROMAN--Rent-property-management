package API_handlers;

import io.javalin.http.Handler;

import java.util.HashMap;

public class Save_unit {

    public Handler save_property_unit(){
        return  ctx -> {
            String getUsername = ctx.sessionAttribute("loginUsername");
            String themeColor = ctx.sessionAttribute("theme_color");
            String  property_name = ctx.sessionAttribute("current_property" );
            HashMap<String,String> model = new HashMap<>();
            model.put("user_name",getUsername);
            model.put("user_chosen_theme",themeColor);
            model.put("property_name",property_name);
            ctx.render("templates/property.html",model);
        };
    }
}
