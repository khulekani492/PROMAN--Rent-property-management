package Invalidhandler;

import io.javalin.http.Handler;

import java.util.HashMap;
import java.util.Map;

public class SameUnit implements  ErrorHandler{
    @Override
    public Handler error_message() {
        return ctx -> {

            String  update_user = ctx.sessionAttribute("error");
            Map<String, Object> model = new HashMap<>();
            String user_name = ctx.sessionAttribute("user_name");
            String  propertyName = ctx.sessionAttribute("property_name");
            Integer  propertyUnit = ctx.sessionAttribute("property_unit");


            model.put("user_name",user_name);
            model.put("unit_add", propertyUnit);
            model.put("name", propertyName);
            model.put("error","already taken");
            System.out.println(model);
            ctx.render("templates/tenant_form.html",model);
        };
    }
}
