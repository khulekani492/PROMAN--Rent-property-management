package API;

//import com.mitchellbosecke.pebble.PebbleEngine;
//import com.mitchellbosecke.pebble.template.PebbleTemplate;
import backend.database.Data;
import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.staticfiles.Location;
import io.javalin.rendering.template.JavalinThymeleaf;

import java.sql.SQLException;
import java.util.Map;

public class umuziAPI {
    public  static Javalin startServer(int port) throws SQLException {
        Data dbConnector = new Data("jdbc:sqlite:smoked.db");
        Javalin app = Javalin.create(config -> {
            config.fileRenderer(new JavalinThymeleaf());
        });

        app.post("/sign_up", ctx -> {
            String propertyName = ctx.formParam("property_name");

            //set the property_name
            dbConnector.setPropertyname(propertyName);

            int numberOfRooms =  Integer.parseInt( ctx.formParam("number_of_rooms")) ;
            int rent =  Integer.parseInt(ctx.formParam("rent")) ;
            String address = ctx.formParam("address");
            String contact = ctx.formParam("contact");

            dbConnector.addProperty_info(propertyName,numberOfRooms,rent,address,contact);
            System.out.println("Property: " + propertyName);
            System.out.println("Rooms: " + numberOfRooms);
            System.out.println("Rent: " + rent);
            System.out.println("Address: " + address);
            System.out.println("Contact: " + contact);

        });
        app.post("/addtenants", ctx -> {

            //Call the  getmethod() to get the name of  new user
            String propertName = dbConnector.getPropertyname();

            //Insert ID as a foreign key to the tenants table
            int residenceid = dbConnector.landlordId(propertName);

            //Access the form input --->  by accessing the value given to "name=value" in the frontEnd
            String name = ctx.formParam("tenant_name");
            String moveIn = ctx.formParam("move_in");
            String employment_status = ctx.formParam("employment");
            String cell_number = ctx.formParam("cell_number");
            String payday = ctx.formParam("pay_day");
            int room = Integer.parseInt(ctx.formParam("room"));
            String room_price = ctx.formParam("room_price");
            String kin_name = ctx.formParam("kin_name");
            String kin_number = ctx.formParam("kin_number");



            dbConnector.addNewtenant(residenceid,name,moveIn,null,employment_status,cell_number,payday,room,room_price,kin_name,kin_number);


        });
        app.post("/updateTenants",ctx -> {

            ///Updating Previously occupied room with new tenant
            String propertName = dbConnector.getPropertyname();
           // int residenceid = dbConnector.landlordId(propertName);
            String name = ctx.formParam("tenant_name");
            String moveIn = ctx.formParam("move_in");
            String employment_status = ctx.formParam("employment");
            String cell_number = ctx.formParam("cell_number");
            String payday = ctx.formParam("pay_day");
            int room = Integer.parseInt(ctx.formParam("room"));
            String room_price = ctx.formParam("room_price");
            String kin_name = ctx.formParam("kin_name");
            String kin_number = ctx.formParam("kin_number");

            dbConnector.roomStatus(name,room,moveIn,employment_status,cell_number,payday,room_price,kin_name,kin_number );


            //Get the form values

            //Check what is being updated String name case switch(Sting parameter ==>) execute updateLandlord method and update the tenants info


        });
        app.get("/tenants", ctx -> {

            // take from the form the id and put the information to the tenants table
            // respond ok or error js
            ctx.render("/templates/property_form.html");
        });



        app.exception(SQLException.class, (e, ctx) -> {
            if (e.getMessage().contains("UNIQUE constraint failed")) {
                ctx.status(400).json(Map.of(
                        "error", "Property already exists",
                        "field", "property_name"
                ));
            } else {
                ctx.status(500).json(Map.of(
                        "error", "Database error"
                ));
            }
        });

        app.exception(Exception.class, (e, ctx) -> {
            e.printStackTrace();
            if (e.getMessage().contains("UNIQUE constraint failed")) {
                ctx.status(400).json(Map.of(
                        "error", "Property already exists",
                        "field", "property_name"
                ));
            } else {
                ctx.status(500).json(Map.of(
                        "error", "Database error",
                        "message",e.getMessage()
                ));
            }

        });


        app.start(port);
        return app;
    }

    /**
     * Main entry point of the application.
     * <p>
     * Starts the API server on port 7070.
     *
     * @param args command-line arguments (not used)
     * @throws SQLException if database initialization fails
     */
    public static void main(String[] args) throws SQLException {
        startServer(7070);
    }
}
