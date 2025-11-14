package Invalidhandler;

import io.javalin.http.Handler;

import java.util.HashMap;

public class SameUnit implements  ErrorHandler{
    @Override
    public Handler error_message() {
        return ctx -> {

            String  update_user = ctx.sessionAttribute("error");
            String property_name = ctx.sessionAttribute("property_name");
            Integer property_unit = ctx.sessionAttribute("property_unit");
            String property_address = ctx.sessionAttribute("property_address");
            String property_rent = ctx.sessionAttribute("rent");
            String property_occupation = ctx.sessionAttribute("occupation");

            HashMap<String,String> model = new HashMap<>();
            model.put("error",update_user );
            model.put("property_name",property_name);
            model.put("property_unit", String.valueOf(property_unit));
            model.put("property_address",property_address);
            model.put("property_occupation",property_occupation);
            model.getOrDefault("rent",property_rent);


            ctx.render("templates/property.html",model);
        };
    }
}
