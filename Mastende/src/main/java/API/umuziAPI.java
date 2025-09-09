package API;

//import com.mitchellbosecke.pebble.PebbleEngine;
//import com.mitchellbosecke.pebble.template.PebbleTemplate;
import backend.database.Data;
import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import io.javalin.rendering.template.JavalinThymeleaf;

import java.sql.SQLException;
import java.util.Map;

public class umuziAPI {
    private static final String D_URL = "jdbc:sqlite:apiData.db";
    private final Data conn;

    {
        try {
            conn = new Data(D_URL);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public  static Javalin startServer(int port) throws SQLException {
        Data dbConnector = new Data("jdbc:sqlite:property.db");
        Javalin app = Javalin.create(config -> {
            config.fileRenderer(new JavalinThymeleaf());
        });

        app.post("/sign_up", ctx -> {

            String propertyName = ctx.formParam("property_name");

            String numberOfRooms = ctx.formParam("number_of_rooms");
            String rent = ctx.formParam("rent");
            String address = ctx.formParam("address");
            String contact = ctx.formParam("contact");
            dbConnector.addProperty_info(propertyName,numberOfRooms,rent,address,contact);
            System.out.println("Property: " + propertyName);
            System.out.println("Rooms: " + numberOfRooms);
            System.out.println("Rent: " + rent);
            System.out.println("Address: " + address);
            System.out.println("Contact: " + contact);



            //take from the form the id and put the information to the residence_table
            //respond ok or error in jsom
            //  ctx.render("/templates/property_form.html", Map.of("name", "Mkhulex"));
        });

        app.post("/addtenants", ctx -> {
            //TODO FIX NULL VALUES
            String name = ctx.formParam("tenant_name"); //returns null

            String moveIn = ctx.formParam("move_in");
            String employment = ctx.formParam("employment");
            String cell_number = ctx.formParam("cell_number");
            String payday = ctx.formParam("pay_day");
            String room = ctx.formParam("room");

//            dbConnector.addProperty_info(propertyName,numberOfRooms,rent,address,contact);
            System.out.println("name: " + name);
            System.out.println("move_in date: " + moveIn);
            System.out.println("employment status: " + employment);
            System.out.println("payday  " + payday);
            System.out.println("Contact: " + cell_number);
            System.out.println("room: " + room);



            //take from the form the id and put the information to the residence_table
            //respond ok or error in jsom
            //  ctx.render("/templates/property_form.html", Map.of("name", "Mkhulex"));
        });

        app.get("/tenants", ctx -> {

            // take from the form the id and put the information to the tenants table
            // respond ok or error js
            ctx.render("/templates/property_form.html");
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
