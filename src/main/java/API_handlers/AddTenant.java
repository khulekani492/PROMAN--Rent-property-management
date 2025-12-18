package API_handlers;

import io.javalin.http.Handler;
import model.database.CRUD.Property_Status;
import model.database.CRUD.landlord;
import model.database.general;
import model.database.residence;
import model.database.tenant;

import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AddTenant {
    public Handler addTenant() {
        return ctx -> {
            //access property name and address
            String property_name = ctx.sessionAttribute("current_property");

            String email = ctx.sessionAttribute("email");
            Integer chosen_unit = Integer.valueOf(ctx.formParam("unit_number"));

            System.out.println("chosen unit " + chosen_unit);
            ctx.sessionAttribute("chosen_unit",chosen_unit);
            ctx.sessionAttribute("occupied_unit_error",chosen_unit);

            residence property_information = new residence();

            try {
                // Insert tenant into general_user table for landlord manual insertion
                String name = ctx.formParam("tenant_name");
                ctx.sessionAttribute("tenant_name",name);
                String number = ctx.formParam("cell_number");
                ctx.sessionAttribute("tenant_number",number);
                String rent_pay = ctx.formParam("rent_amount");
                ctx.sessionAttribute("tenant_pay",rent_pay);
                String user_type = "tenant";
                general add_newTenant = new general(name, number, user_type);
                try {
                    add_newTenant.landlord_insert_tenant();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                // Access the unique ID from the general_user table
                Integer tenantUniqueID = add_newTenant.tenant_ID();
                landlord getUserID = new landlord();
                Property_Status property_units = new Property_Status();
                Integer landlordId = getUserID.landlordId(email);

                HashMap<Integer, ArrayList<String>> fetch_all = property_units.property_tenants(property_name,landlordId);


                System.out.println("Occupied UNITS " + fetch_all);

                //Validate the chosen unit is still empty
                System.out.println("Keyset of current_occupied_management");
                for( int occupied_unit : fetch_all.keySet()){
                    System.out.println(occupied_unit );
                    if(occupied_unit == chosen_unit){
                        ctx.sessionAttribute("same_unit",chosen_unit);
                        ctx.redirect(
                                "error/same_unit");
                        return;
                    }
                }
                //fetch_all.keySet();
                // Update properties table with tenant unique ID
                property_information.setTenantId(tenantUniqueID);
                property_information.setRent(rent_pay);
                property_information.setProperty_unit(chosen_unit);
                property_information.setProperty_Name(property_name);
                try {
                    property_information.insert_information();
                } catch (SQLException e) {
                    System.out.println("HOLD ON : ");
                    System.err.println("Error: " + e.getMessage());
                    ctx.redirect("/error/same_cell_number");
                    return;
                }

                // Additional tenant info
                Date moveIn = null;   // Declare outside
                try {
                    String moveInStr = ctx.formParam("move_in");
                    // Validate empty input
                    moveIn = Date.valueOf(moveInStr);  // This may throw IllegalArgumentException
                    ctx.sessionAttribute("tenant_moveIn", moveIn);

                } catch (IllegalArgumentException e) {
                    // Handle error (redirect with message or render page)
                    ctx.sessionAttribute("error_moveIn", "Invalid move-in date format. Use yyyy-mm-dd.");
                    ctx.redirect("/error/Invalid_date");
                    return; // stop execution after redirect!
                }

                String employment_status = ctx.formParam("employment");
                String kin_name = ctx.formParam("kin_name");
                ctx.sessionAttribute("tenant_kin_name",kin_name);
                String kin_number = ctx.formParam("kin_number");
                ctx.sessionAttribute("tenant_kin_number",kin_number);
                String rent_payment_day = ctx.formParam("rent_day");
                ctx.sessionAttribute("tenant_rent_payment",rent_payment_day);

                Integer debt;
                try{
                debt = Integer.valueOf(ctx.formParam("debt"));
                } catch (NumberFormatException e) {
                    debt=0;
                }
                ctx.sessionAttribute("tenant_debt",debt);
                try {
                    tenant additional_information = new tenant(moveIn, employment_status, kin_name, kin_number,rent_payment_day,debt);
                    additional_information.setTenantId(tenantUniqueID);

                    additional_information.insert_information();
                } catch (Exception e) {

                    throw new RuntimeException(e);
                }
                ctx.sessionAttribute("current_units_property",new Property_Status().property_tenants(property_name,landlordId));
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
