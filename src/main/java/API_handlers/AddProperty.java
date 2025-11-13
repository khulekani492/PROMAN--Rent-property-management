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
                String occupation = ctx.formParam("occupation");
                String rent = ctx.formParam("rent");

                ctx.sessionAttribute("property_name", property_Name);
                ctx.sessionAttribute("property_unit", propertyUnit);
                ctx.sessionAttribute("property_address", property_address);
                ctx.sessionAttribute("occupation", occupation);
                Integer property_owner = ctx.sessionAttribute("user_ID");

                //Insert INTO property table property_information
                residence property_information = new residence(propertyUnit, rent, occupation, property_Name);
                property_information.setlandlord(property_owner);
                property_information.setProperty_address(property_address);
                property_information.setProperty_Name(property_Name);
                //TRY AND EXCEPT
                //entity relationship with the Users table
                try {
                    System.out.println("Bricks");
                    property_information.insert_information();
                } catch (Exception e) {
                    ctx.redirect("/error/same_unit");
                }

                Integer owner_property = property_information.UniqueID();
                System.out.println("properties_id " + owner_property);
                String property_name = ctx.sessionAttribute("property_name");

                ctx.sessionAttribute("_unit", propertyUnit);
                String user_name = ctx.sessionAttribute("user_name");
                Map<String, Object> model = new HashMap<>();
                model.put("user_name",user_name);
                model.put("unit_add", propertyUnit);
                model.put("name", property_name);

                ctx.sessionAttribute("residence", property_name);


                if ("yes".equals(occupation)) {
                    System.out.println("yes status");
                    ctx.redirect("/add_tenant");
                    //ctx.render("/templates/tenant_form.html", model);
                } else {
                    System.out.println("no status");
                    ctx.redirect("/dashboard");
                }

            } catch (Exception e) {
                ctx.status(400).result("Error chill: " + e.getMessage());
                e.printStackTrace();
            }
        };
    }
}