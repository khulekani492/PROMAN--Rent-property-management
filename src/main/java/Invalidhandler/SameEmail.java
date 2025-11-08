package Invalidhandler;

import io.javalin.http.Handler;

import java.util.HashMap;

public class SameEmail implements ErrorHandler{
    @Override
    public Handler error_message() {
        return ctx -> {
            //String messagE = ctx.sessionAttribute("error");
            String  update_user = ctx.sessionAttribute("error");
            //ErrorHandler  updateUser = new SameUnit;
            //updateUser.update(Update)
            String property_name = ctx.sessionAttribute("property_name");
            Integer property_unit = ctx.sessionAttribute("property_unit");
            String property_address = ctx.sessionAttribute("property address");
            String property_occupation = ctx.sessionAttribute("occupation");

            HashMap<String,String> model = new HashMap<>();
            model.put("error",update_user);
            model.put("property_name",property_name);
            model.put("property_unit", String.valueOf(property_unit));
            model.put("property_address",property_address);
            model.put("property_occupation",property_occupation);
            ctx.render("templates/property.html",model);
        };
    }
}
