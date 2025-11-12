package API_handlers;

import io.javalin.http.Handler;
import model.database.general;
import model.database.residence;
import model.database.tenant;

import java.sql.Date;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class AddTenant {
    public Handler addTenant() {
        return ctx -> {
            try {
                // Insert tenant into general_user table for landlord manual insertion
                String name = ctx.formParam("tenant_name");
                String number = ctx.formParam("cell_number");
                String user_type = "tenant";

                general addnewTenant = new general(name, number, user_type);
                addnewTenant.landlord_insert_tenant();

                // Access the unique ID from the general_user table
                Integer tenantUniqueID = addnewTenant.tenant_ID();
                System.out.println("Tenant ID: " + tenantUniqueID);

                // Update properties table with tenant unique ID
                Integer landlordId = ctx.sessionAttribute("user_ID");
                Integer property_unit = ctx.sessionAttribute("property_unit");
                String property_Name = ctx.sessionAttribute("property_name");
                String property = ctx.sessionAttribute("residence");
                System.out.println("Property_name from cookie session: " + property_Name );
                System.out.println("Property_residence: " + property );
                System.out.println("Landlord ID: " + landlordId);
                System.out.println("Property unit: " + property_unit);

                residence addTenantUnit = new residence();
                addTenantUnit.setProperty_unit(property_unit);
                addTenantUnit.setProperty_Name(property_Name);
                addTenantUnit.setlandlord(landlordId);
                addTenantUnit.setTenantId(tenantUniqueID);

                String tenant_name = addnewTenant.getTenantnamebyId(tenantUniqueID);
                System.out.println(tenant_name + " tenant_name by Id");


                try {

                    addTenantUnit.Insert_tenatId();

                } catch (SQLException e) {
                    ctx.redirect("/user_sign_up/error");
                    e.printStackTrace();  // <-- ensures the exception appears in your terminal
                    System.err.println("Error: " + e.getMessage());

                }
                // Additional tenant info
                Date moveIn = Date.valueOf(ctx.formParam("move_in"));
                String employment_status = ctx.formParam("employment");
                String kin_name = ctx.formParam("kin_name");
                String kin_number = ctx.formParam("kin_number");
                String rent_payment_day = ctx.formParam("rent_payment_day");
                Integer debt = Integer.valueOf(ctx.formParam("debt"));
                try {
                    tenant additional_information = new tenant(moveIn, employment_status, kin_name, kin_number,rent_payment_day,debt);
                    additional_information.setTenantId(tenantUniqueID);
                    additional_information.insert_information();

                } catch (Exception e) {

                    throw new RuntimeException(e);
                }


                // Render dashboard with updated model
                String username = ctx.sessionAttribute("name");
                String property_name = ctx.sessionAttribute("property_Name");
                Integer unit_number = ctx.sessionAttribute("_unit");

                Map<String, Object> model = new HashMap<>();
                model.put("name", property_name);


                ctx.redirect("/dashboard");
                //ctx.render("/templates/dashboard.html", model);

            } catch (SQLException e) {
                ctx.status(500).json("error: " + e.getMessage());
                e.printStackTrace();
            } catch (Exception e) {
                ctx.status(400).result("Error: " + e.getMessage());
                e.printStackTrace();
            }
        };
    }
}
