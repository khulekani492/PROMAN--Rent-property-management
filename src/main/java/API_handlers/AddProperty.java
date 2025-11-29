package API_handlers;

import io.javalin.http.Handler;
import model.database.residence;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AddProperty {
    public Handler addproperty() {
        return ctx -> {
            try {

                Integer propertyUnit = Integer.valueOf(Objects.requireNonNull(ctx.formParam("property_unit")));
                String property_Name = ctx.formParam("property_Name");
                String property_address = ctx.formParam("property_Address");
                System.out.println(property_address);
                ctx.sessionAttribute("property_name", property_Name);
                ctx.sessionAttribute("property_unit", propertyUnit);
                ctx.sessionAttribute("property_address", property_address);

                Integer property_owner = ctx.sessionAttribute("user_ID");
                //Insert INTO property table property_information
                //residence property_information = new residence(property_Name, property_address);
                // property_information.setlandlord(property_owner);
                // property_information.setProperty_address(property_address);
                //property_information.setProperty_Name(property_Name);
                //TRY AND EXCEPT
                //entity relationship with the Users table
                try {
                    //property_information.insert_information();
                } catch (Exception e) {
                    ctx.sessionAttribute("error", "Unit already taken");
                    ctx.redirect("/error/same_unit");
                    return;
                }
                Map<String, Object> model = new HashMap<>();


                String user_name = ctx.sessionAttribute("user_name");

                String  propertyName = ctx.sessionAttribute("property_name");

                model.put("user_name",user_name);
                model.put("unit_add", propertyUnit);
                model.put("name", propertyName);
                ctx.render("templates/tenant_form.html",model);



            } catch (Exception e) {
                ctx.status(400).result("Error chill: " + e.getMessage());
                e.printStackTrace();
            }
        };
    }
}