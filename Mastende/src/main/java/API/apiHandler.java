package API;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import model.database.landlord;
import model.database.residence;

import java.sql.SQLException;
import java.util.Map;
import java.util.Objects;

import static API.SecurityUtil.hashPassword;

public class apiHandler {

    public Handler sign_up() {
        return ctx -> {
            try {
                String user_name = ctx.formParam("user_name");
                ctx.sessionAttribute("user_name", user_name);

                String email = ctx.formParam("user_email");
                String password = ctx.formParam("password");

                // hash password
                String hashedPassword = hashPassword(password);

                // insert user into database
                landlord new_user = new landlord(user_name, email, hashedPassword);
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
            String propertyName = ctx.formParam("property_name");
            ctx.sessionAttribute("propertyname",propertyName);
            Integer numberOfRooms =  Integer.parseInt(Objects.requireNonNull(ctx.formParam("number_of_rooms")));
            Integer rent =  Integer.parseInt(Objects.requireNonNull(ctx.formParam("rent")));

            Integer OccupiedRooms =  Integer.parseInt(Objects.requireNonNull(ctx.formParam("roomsOccupied")));
            ctx.sessionAttribute("roomsOccupied",OccupiedRooms);

            String address = ctx.formParam("address");
            String contact = ctx.formParam("contact");
            //Get the new user unique_Id by accessing the key-value setAttributes() session set in /user_sign_up Url_end point
            Integer property_owner = ctx.sessionAttribute("user_ID");

            //Insert into property table property_information
            residence property_information = new residence(propertyName,numberOfRooms,OccupiedRooms,rent,address,contact,property_owner);
            property_information.insert_information();

            //entity relationship with the Users table
            Integer owner_property = property_information.UniqueID();
            System.out.println("owner_property");
            System.out.println(owner_property);
            ctx.sessionAttribute("propertyId",owner_property);
            ctx.sessionAttribute("roomNo",numberOfRooms);

            Integer current_landlord = ctx.sessionAttribute("user_ID");
            Integer landlord_property = ctx.sessionAttribute("propertyId");

            property_information.assignProperty(landlord_property,current_landlord);

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
}
