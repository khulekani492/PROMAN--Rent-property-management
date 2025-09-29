package API;


import model.database.Data;
import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinThymeleaf;
import model.database.landlord;

import java.sql.SQLException;
import java.util.Map;
import java.util.Objects;

import static API.SecurityUtil.hashPassword;
import static API.SessionUtil.fileSessionHandler;
//TODO https://javalin.io/tutorials/jetty-session-handling


public class umuziAPI {
    public  static Javalin startServer(int port) throws SQLException {

        Javalin app = Javalin.create(config -> {
            config.jetty.modifyServletContextHandler(handler -> handler.setSessionHandler(fileSessionHandler()));
            config.fileRenderer(new JavalinThymeleaf());
        });
        //home_page Url_end_point
        app.get("/", ctx ->{
           ctx.render("/templates/sign_up.html");
        });

        app.post("/user_sign_up", ctx ->{
            String user_name = ctx.formParam("user_name");
            ctx.sessionAttribute("user_name",user_name);
            String email = ctx.formParam(("user_email"));
            //* encrypt password before setting it   sessionAttributes
            String password = ctx.formParam("password");
            String hashedPassword = hashPassword(password);

           ;
            landlord new_user = new landlord(user_name,email,hashedPassword);
            //Feed the Model User table with the new_user_information
            //add the hashed password  to the database for security
            new_user.insert_information();

            //Get the uniqueId of the new inserted user
            // VALIDATE A SINGLE QUERY PARAMETER
            Integer new_userID = new_user.UniqueID();

            //assign new user uniqueId to session key  user_ID --> will be used by property_information URL_endpoint
            ctx.sessionAttribute("user_ID",new_userID);

            //assign new user unique_Id to session key  user_ID --> will be used to authenticate user for access
            ctx.sessionAttribute("password", hashedPassword);
            Map<String, Object> sessionMap = ctx.sessionAttributeMap();

            ctx.json(sessionMap);
            ctx.render("/templates/sign_up_property.html");
        });
        app.post("/sign_up", ctx -> {
            String propertyName = ctx.formParam("property_name");

            //set the property_name
            //dbConnector.setPropertyname(propertyName);
            int numberOfRooms =  Integer.parseInt( ctx.formParam("number_of_rooms")) ;
            int rent =  Integer.parseInt(ctx.formParam("rent")) ;
            String address = ctx.formParam("address");
            String contact = ctx.formParam("contact");

            //Check if row does not exist already.
           // dbConnector.addProperty_info(propertyName,numberOfRooms,rent,address,contact);


            System.out.println("Property: " + propertyName);
            System.out.println("Rooms: " + numberOfRooms);
            System.out.println("Rent: " + rent);
            System.out.println("Address: " + address);
            System.out.println("Contact: " + contact);

        });
        app.post("/addtenants", ctx -> {

            //Call the  getmethod() to get the name of  new user
           // String propertName = dbConnector.getPropertyname();

            //Insert ID as a foreign key to the tenants table
        //    int residenceid = dbConnector.landlordId(propertName);

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



           // dbConnector.addNewtenant(residenceid,name,moveIn,null,employment_status,cell_number,payday,room,room_price,kin_name,kin_number);


        });
        app.post("/updateTenants",ctx -> {

            ///Updating Previously occupied room with new tenant or individual update like rent price for that rent
           // String propertName = dbConnector.getPropertyname();
           // int residenceid = dbConnector.landlordId(propertName);
            String name = ctx.formParam("tenant_name");
            String moveIn = ctx.formParam("move_in");
            String employment_status = ctx.formParam("employment");
            String cell_number = ctx.formParam("cell_number");
            String payday = ctx.formParam("pay_day");

            int room = Integer.parseInt(ctx.formParam("room"));
            String room_price = ctx.formParam("room_price");

            //LOGIC update debt column when month ends and payment is still not paid
            int tenantsdebt = Integer.parseInt( ctx.formParam("tenant_debt"));
            String kin_name = ctx.formParam("kin_name");
            String kin_number = ctx.formParam("kin_number");

            //
            // dbConnector.roomStatus(name,room,moveIn,employment_status,cell_number,payday,room_price,tenantsdebt,kin_name,kin_number );

        });
        app.get("/tenants", ctx -> {

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

        // Exception handler for NullPointerException
        app.exception(NullPointerException.class, (e, ctx) -> {
            ctx.status(400);
            ctx.json(Map.of(
                    "error", "null value",
                    "message", e.getMessage()
            ));
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
