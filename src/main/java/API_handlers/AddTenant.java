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
            //access property name and address
            String property_name = ctx.sessionAttribute("property_name");
            String property_address =  ctx.sessionAttribute("property_address");
            Integer chosen_unit = Integer.valueOf(ctx.formParam("unit_number"));
            Integer landlordId = ctx.sessionAttribute("user_ID");
            residence property_information = new residence();
            property_information.setProperty_Name(property_name);
            property_information.setProperty_address(property_address);
            property_information.setProperty_unit(chosen_unit);
            property_information.setlandlord(landlordId);

            try {
                try {
                    property_information.insert_information();
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    ctx.sessionAttribute("error", "Unit already taken");
                    ctx.redirect("/error/same_unit");
                    return;
                }

                // Insert tenant into general_user table for landlord manual insertion
                String name = ctx.formParam("tenant_name");
                ctx.sessionAttribute("tenant_name",name);
                String number = ctx.formParam("cell_number");
                ctx.sessionAttribute("tenant_number",number);
                String rent_pay = ctx.formParam("rent_amount");
                ctx.sessionAttribute("tenant_pay",rent_pay);


                System.out.println("Imali yami " + rent_pay);
                String user_type = "tenant";

                general add_newTenant = new general(name, number, user_type);
                try {
                    add_newTenant.landlord_insert_tenant();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

                // Access the unique ID from the general_user table
                Integer tenantUniqueID = add_newTenant.tenant_ID();
                System.out.println("Tenant ID: " + tenantUniqueID);

                // Update properties table with tenant unique ID
                System.out.println("Landlord ID: " + landlordId);

                property_information.setTenantId(tenantUniqueID);

                property_information.setRent(rent_pay);
                System.out.println(rent_pay +" rent amount");

                try {
                    property_information.Insert_tenatId();

                } catch (SQLException e) {
                    ctx.redirect("/user_sign_up/error");
                    e.printStackTrace();  // <-- ensures the exception appears in your terminal
                    System.err.println("Error: " + e.getMessage());

                }


                // Additional tenant info
                Date moveIn = Date.valueOf(ctx.formParam("move_in"));
                ctx.sessionAttribute("tenant_moveIn",moveIn);
                String employment_status = ctx.formParam("employment");
                String kin_name = ctx.formParam("kin_name");
                ctx.sessionAttribute("tenant_kin_name",kin_name);
                String kin_number = ctx.formParam("kin_number");
                ctx.sessionAttribute("tenant_kin_number",kin_number);
                String rent_payment_day = ctx.formParam("rent_day");
                ctx.sessionAttribute("tenant_rent_payment",rent_payment_day);
                Integer debt = Integer.valueOf(ctx.formParam("debt"));
                ctx.sessionAttribute("tenant_debt",debt);

                try {
                    tenant additional_information = new tenant(moveIn, employment_status, kin_name, kin_number,rent_payment_day,debt);
                    additional_information.setTenantId(tenantUniqueID);

                    additional_information.insert_information();
                } catch (Exception e) {

                    throw new RuntimeException(e);
                }


                // Render dashboard with updated model
//                String username = ctx.sessionAttribute("name");
//                //String property_name = ctx.sessionAttribute("property_Name");
//                Integer unit_number = ctx.sessionAttribute("_unit");

//                Map<String, Object> model = new HashMap<>();
//                model.put("name", property_name);
             //   ctx.redirect("/dashboard");
                ctx.redirect("/dashboard");

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
