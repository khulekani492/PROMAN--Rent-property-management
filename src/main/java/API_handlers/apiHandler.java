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
                String property_name = ctx.formParam("property_name");

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

                // set sesssion attributes the username and property_name
                Map<String, Object> sessionMap = ctx.sessionAttributeMap();
                ctx.json(sessionMap);

                // render property page
                ctx.render("/templates/property.html");

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

            ctx.sessionAttribute("propertyId",owner_property);
            Integer property_unit =  ctx.sessionAttribute("property_unit");
//            Integer current_landlord = ctx.sessionAttribute("user_ID");
//            Integer landlord_property = ctx.sessionAttribute("propertyId");
               Map<String, Object> model = new HashMap<>();
               model.put("property_unit",property_unit);
               model.put("unit_add", propertyUnit);

            ctx.render("/templates/dashboard.html",model);
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
                Integer numberofRooms = ctx.sessionAttribute("roomsOccupied");
                // check-logic for preventing inserting tenant with the same room number to the database
//                if (duplicateNUmberchecker.getCurrent_track().equals(room.intValue())) {
//                    System.out.println("same Room " + duplicateNUmberchecker.getCurrent_track());
//                    ctx.json(Map.of("message", "room taken", "status", 200));
//                    return;
//                }else{
//                    duplicateNUmberchecker.setCurrent_track(room);
//                    System.out.println("Previous room number: " + duplicateNUmberchecker.getCurrent_track());
//                }

                //Insert tenant to general_user table for landlord Manual insertion
                String name = ctx.formParam("tenant_name");
                String number = ctx.formParam("cell_number");
                System.out.println("Sweet love : " + name);
                System.out.println("lil wayne down: " + number);
                String user_type = "tenant";
                general addnewTenant = new general(name,number,user_type);
                addnewTenant.insert_information();
                //addnewTenant.landlord_insert_tenant();

                //Access the unique ID from the general_user table
                Integer tenantUniqueID =  addnewTenant.UniqueID();
                //update properties table and includes the tenant unique ID
                Integer tenantId = addnewTenant.UniqueID();
                Integer landlordId = ctx.sessionAttribute("user_ID");
                residence addTenantUnit = new residence();
                //update the properties row with the tenant occupying the room/unit
                addTenantUnit.Insert_tenatId(tenantId,landlordId);
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
                String username = ctx.sessionAttribute("user_name");
                System.out.println(username + "username");
                String propertyname = ctx.sessionAttribute("propertyname");
                Map<String, Object> model = new HashMap<>();
                model.put("username",username);
                model.put("age", propertyname);
                ctx.render("/templates/property.html",model);

            } catch (Exception e) {
                ctx.status(400).result("Error: " + e.getMessage());
                e.printStackTrace();
            }
        };

    }

}
