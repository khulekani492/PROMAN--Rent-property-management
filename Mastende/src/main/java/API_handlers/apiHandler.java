package API_handlers;

import API.state;
import io.javalin.http.Handler;
import model.database.Tenant;
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
                Integer numberofUnits = Integer.valueOf(ctx.formParam("n_units"));

                // hash password
                String hashedPassword = hashPassword(password);

                // insert user into database
                general new_user = new general(user_name, email, hashedPassword,contact,property_address,user_type,numberofUnits);
                new_user.insert_information();

                // get new user ID
                Integer new_userID = new_user.UniqueID();

                // set session attributes
                ctx.sessionAttribute("user_ID", new_userID);
                ctx.sessionAttribute("password", hashedPassword);

                // optional: send back session data as JSON
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
            Integer propertyUnit = Integer.valueOf(ctx.formParam("property_name"));
            ctx.sessionAttribute("propertyname",propertyUnit);
           // Integer numberOfRooms =  Integer.parseInt(Objects.requireNonNull(ctx.formParam("number_of_rooms")));
            Integer rent =  Integer.parseInt(Objects.requireNonNull(ctx.formParam("rent")));

           /// Integer OccupiedRooms =  Integer.parseInt(Objects.requireNonNull(ctx.formParam("roomsOccupied")));
            //ctx.sessionAttribute("roomsOccupied",OccupiedRooms);

            String occupation = ctx.formParam("occupation");
            //String contact = ctx.formParam("contact");
            //Get the new user unique_Id by accessing the key-value setAttributes() session set in /user_sign_up Url_end point
            Integer property_owner = ctx.sessionAttribute("user_ID");

            //Insert into property table property_information
            residence property_information = new residence(propertyUnit,rent,occupation);
            property_information.insert_information();

            //entity relationship with the Users table
            Integer owner_property = property_information.UniqueID();
            System.out.println("owner_property");
            System.out.println(owner_property);
            ctx.sessionAttribute("propertyId",owner_property);
            //ctx.sessionAttribute("roomNo",numberOfRooms);

            Integer current_landlord = ctx.sessionAttribute("user_ID");
            Integer landlord_property = ctx.sessionAttribute("propertyId");

            //property_information.assignProperty(landlord_property,current_landlord);

            ctx.render("/templates/tenant_form.html");
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

                //Access the form input --->
                Integer propertyID = ctx.sessionAttribute("propertyId");
                String name = ctx.formParam("tenant_name");
                Date moveIn = Date.valueOf(ctx.formParam("move_in"));

                String employment_status = ctx.formParam("employment");
                String cell_number = ctx.formParam("cell_number");
                Date payday = Date.valueOf( ctx.formParam("pay_day"));
                Integer room = Integer.parseInt(Objects.requireNonNull(ctx.formParam("room")));

                // check-logic for preventing inserting tenant with the same room number to the database
                if (duplicateNUmberchecker.getCurrent_track().equals(room.intValue())) {
                    System.out.println("same Room " + duplicateNUmberchecker.getCurrent_track());
                    ctx.json(Map.of("message", "room taken", "status", 200));
                    return;
                }else{
                    duplicateNUmberchecker.setCurrent_track(room);
                    System.out.println("Previous room number: " + duplicateNUmberchecker.getCurrent_track());
                }

                Integer room_price = Integer.valueOf(Objects.requireNonNull(ctx.formParam("room_price")));
                String kin_name = ctx.formParam("kin_name");
                String kin_number = ctx.formParam("kin_number");

                Tenant tenant = new Tenant(propertyID,name,moveIn,employment_status,cell_number,payday,room,room_price,kin_name,kin_number);
                tenant.insert_information();

                /**
                 * If the counter equals the number of rooms, render the user_profile HTML.
                 * Otherwise, keep rendering the tenant_form.
                 */
                if(count.getCount() == numberofRooms){
                    String username = ctx.sessionAttribute("user_name");
                    String propertyname = ctx.sessionAttribute("propertyname");
                    Map<String, Object> model = new HashMap<>();
                    model.put("username",username);
                    model.put("age", propertyname);

                    ctx.render("/templates/user_profile.html",model);
                }else {
                    count.increment();
                    ctx.render("/templates/tenant_form.html");

                }
            } catch (Exception e) {
                ctx.status(400).result("Error: " + e.getMessage());
                e.printStackTrace();
            }
        };


    }

}
