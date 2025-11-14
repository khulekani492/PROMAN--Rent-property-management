package API_handlers;

import io.javalin.http.Handler;

import java.util.HashMap;

public class Save_unit {

    public Handler save_property_unit(){
        return  ctx -> {
            String getUsername = ctx.sessionAttribute("user_name");
            String propertyName = ctx.sessionAttribute("property_name");
            String propertyAddress = ctx.sessionAttribute("property_address");
            System.out.println(propertyAddress + " played the game");
            HashMap<String,String> model = new HashMap<>();
            model.put("user_name",getUsername);
            model.put("property_name",propertyName);
            model.put("property_address",propertyAddress);
            ctx.render("templates/property.html",model);
        };
    }
}
