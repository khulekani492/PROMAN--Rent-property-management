package API_handlers;

import API.state;
import io.javalin.http.Handler;
import model.database.tenant;
import model.database.counter;
import model.database.general;
import model.database.residence;

import java.sql.Date;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static API.SecurityUtil.hashPassword;

public class apiHandler {
    public Handler sign_up() {
        return ctx -> {
            try {
                String user_name = ctx.formParam("user_name");

                ctx.sessionAttribute("user_name", user_name);

                String contact = ctx.formParam("contact");
                String property_address = ctx.formParam("address");
                String email = ctx.formParam("user_email");
                String password = ctx.formParam("password");
                String user_type = ctx.formParam("user_type");
                ctx.sessionAttribute("user_name",user_name);
                String property_name = ctx.formParam("property_name");
                ctx.sessionAttribute("property_name",property_name);

                // hash password
                String hashedPassword = hashPassword(password);

                // insert user into database
                general new_user = new general(user_name, contact, email, hashedPassword,user_type,property_address,property_name);
                new_user.insert_information();

                // get new user ID
                Integer new_userID = new_user.UniqueID();

                // set session attributes
                ctx.sessionAttribute("user_ID", new_userID);
                ctx.sessionAttribute("password", hashedPassword);

                // set session attributes the username and property_name
                String residence_name = ctx.formParam("property_name");
                String landlord_username = ctx.formParam("user_name");
                Map<String, Object> sessionMap = ctx.sessionAttributeMap();
                ctx.json(sessionMap);

                Map<String, Object> model = new HashMap<>();
                model.put("user_name", landlord_username);
                model.put("residence_name",residence_name);

                // render property page
                ctx.render("/templates/property.html",model);

            } catch (SQLException e) {
                ctx.status(500).result("Database error: " + e.getMessage());
                e.printStackTrace();
            } catch (Exception e) {
                ctx.status(400).result("Error: " + e.getMessage());
                e.printStackTrace();
            }
        };
    }

    public Handler addproperty(){
        return  ctx ->{
           try{
               Integer unit = ctx.sessionAttribute("unit_add");
               System.out.println(unit + "Makasana");
            Integer propertyUnit = Integer.valueOf(Objects.requireNonNull(ctx.formParam("property_unit")));
            ctx.sessionAttribute("property_unit",propertyUnit);
            Integer rent =  Integer.parseInt(Objects.requireNonNull(ctx.formParam("rent")));
            String occupation = ctx.formParam("occupation");
            //String contact = ctx.formParam("contact");
            Integer property_owner = ctx.sessionAttribute("user_ID");

            Integer pay_day = Integer.valueOf(ctx.formParam("pay_day"));

            //Insert into property table property_information
            residence property_information = new residence(propertyUnit,rent,occupation);
            property_information.setlandlord(property_owner);
            property_information.setRentDay(pay_day);

            property_information.insert_information();
            //entity relationship with the Users table
            Integer owner_property = property_information.UniqueID();
               System.out.println("properties_id " + owner_property);
               String property_name =  ctx.sessionAttribute("property_name");
               ctx.sessionAttribute("_unit",propertyUnit);


               Map<String, Object> model = new HashMap<>();
               model.put("unit_add", propertyUnit);
            ctx.render("/templates/tenant_form.html",model);
           }catch (SQLException e) {
               ctx.status(500).result("Database error: " + e.getMessage());
               e.printStackTrace();
           } catch (Exception e) {
               ctx.status(400).result("Error: " + e.getMessage());
               e.printStackTrace();
           }
    };
    }
    /**
     * Gets the number of rooms the landlord has submitted.
     * <p>
     * Keeps rendering the same form repeatedly until the counter
     * reaches the last room.
     */
    public Handler addTenant(){
        state duplicateNUmberchecker = new state(0);
        counter count = new counter();

        return ctx ->{
            try{
                //Insert tenant to general_user table for landlord Manual insertion
                String name = ctx.formParam("tenant_name");
                String number = ctx.formParam("cell_number");
                System.out.println("Sweet love : " + name);
                System.out.println("lil wayne down: " + number);
                String user_type = "tenant";
                general addnewTenant = new general(name,number,user_type);

                addnewTenant.landlord_insert_tenant();
                //Access the unique ID from the general_user table
                Integer tenantUniqueID =  addnewTenant.tenant_ID();

                System.out.println("ID " + tenantUniqueID);
                //update properties table and includes the tenant unique ID
                Integer landlordId = ctx.sessionAttribute("user_ID");
                System.out.println("amen " + landlordId);


                residence addTenantUnit = new residence();

                addTenantUnit.setlandlord(landlordId);
                addTenantUnit.setTenantId(tenantUniqueID);
                addTenantUnit.Insert_tenatId();
                //System.out.println(landlordId);
                //update the properties row with the tenant occupying the room/unit
               // addTenantUnit.Insert_tenatId();
                //additional information about the tenant --> tenants_information table
                //how does htm send range values
                Date moveIn = Date.valueOf(ctx.formParam("move_in"));
                String employment_status = ctx.formParam("employment");
                String kin_name = ctx.formParam("kin_name");
                String kin_number = ctx.formParam("kin_number");

                //additional information about the tenant --> tenants_information table
                tenant additional_information = new tenant(moveIn,employment_status,kin_name,kin_number);
                additional_information.setTenantId(tenantUniqueID);
                additional_information.insert_information();
                /**
                 * If the counter equals the number of rooms, render the user_profile HTML.
                 * Otherwise, keep rendering the tenant_form.
                 */
                String username = ctx.sessionAttribute("name");
                System.out.println(username + "username");
                //String property_name = ctx.sessionAttribute("property_name");
                String property_name =  ctx.sessionAttribute("property_name");
                Integer unit_number = ctx.sessionAttribute("_unit");


                Map<String, Object> model = new HashMap<>();
                model.put("property_name",property_name);
                model.put("unit_add", unit_number);
                ctx.render("/templates/dashboard.html",model);

            } catch (Exception e) {
                ctx.status(400).result("Error: " + e.getMessage());
                e.printStackTrace();
            }
        };

    }

}
