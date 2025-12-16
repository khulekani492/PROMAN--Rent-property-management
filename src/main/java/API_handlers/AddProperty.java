package API_handlers;

import io.javalin.http.Handler;
import model.database.CRUD.landlord;
import model.database.CRUD.propertyNames;
import model.database.residence;

import java.sql.SQLException;
import java.util.*;

public class AddProperty {
    public Handler addproperty() {
        return ctx -> {
            try {
                String totalUnits = ctx.formParam("property_unit");
                Integer propertyUnit = Integer.valueOf(totalUnits);
                String property_Name = ctx.formParam("property_Name");
                String property_address = ctx.formParam("property_Address");
                System.out.println(property_address);
                ctx.sessionAttribute("current_property", property_Name);
                ctx.sessionAttribute("property_address", property_address);
                ctx.sessionAttribute("property_unit", Integer.parseInt(totalUnits));
                ctx.sessionAttribute("add_property_profile","Yes ");

                String property_name = ctx.sessionAttribute("property_name");
                String user_email = ctx.sessionAttribute("email");

                //DE (Data Extraction) get landlordId from the database
               // landlord getUserID = new landlord();
                Integer landlordId = ctx.sessionAttribute("landlordID");

                //DE create rows of the property units for the landlord
                residence create_rooms = new residence();
                create_rooms.setlandlord(landlordId);
                create_rooms.setProperty_Name(property_Name);
                create_rooms.setProperty_address(property_address);
                for (int i = 1 ;i <= propertyUnit;i++){
                    try {
                        create_rooms.setProperty_unit(i);
                        create_rooms.setTotal_units();
                    } catch (SQLException e) {
                        System.out.println("please");
                        ctx.redirect("error/same_address");
                        return;
                    }

                }
                propertyNames default_properties = new propertyNames();
                Set<String> landlord_properties = default_properties.fetchAllproperty(landlordId);
                ctx.sessionAttribute("allProperties", landlord_properties);
                ArrayList<String> default_property = new ArrayList<>(landlord_properties);
                System.out.println(ctx.attributeMap());


                Map<String, Object> model = new HashMap<>();
               // System.out.println("SET total units to updated " + total_n_units);
                System.out.println("SET property_name to  updated " + property_name);
                String user_name = ctx.sessionAttribute("user_name");
                String  propertyName = ctx.sessionAttribute("property_name");
           //     ctx.sessionAttribute("property_unit", propertyUnit);
                model.put("user_name",user_name);
                model.put("unit_add", propertyUnit);
                model.put("name", propertyName);
                //ctx.render("templates/tenant_form.html",model);
                ctx.sessionAttribute("current_units_property",null);
                ctx.redirect("/add_tenant");

            } catch (Exception e) {
                ctx.status(400).result("Error chill: " + e.getMessage());
                e.printStackTrace();
            }
        };
    }
}