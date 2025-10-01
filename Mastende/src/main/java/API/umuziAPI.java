package API;


import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinThymeleaf;
import model.database.Tenant;
import model.database.landlord;
import model.database.residence;

import java.sql.SQLException;
import java.time.LocalDate;
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
           ctx.render("/templates/sign_up_user.html");
        });

        app.post("/user_sign_up", ctx ->{
            String user_name = ctx.formParam("user_name");
            ctx.sessionAttribute("user_name",user_name);
            String email = ctx.formParam(("user_email"));
            //* encrypt password before setting it   sessionAttributes
            String password = ctx.formParam("password");

            //add the hashed password  to the database for security
            String hashedPassword = hashPassword(password);

            //Insert into User table new_user_information
            landlord new_user = new landlord(user_name,email,hashedPassword);

            //call the abstract Method of Property to insert information to the database
            new_user.insert_information();

            //Get the uniqueId of the new inserted user
            Integer new_userID = new_user.UniqueID();

            //assign new user uniqueId to session key  user_ID --> will be used by property_information URL_endpoint
            ctx.sessionAttribute("user_ID",new_userID);

            //assign new user unique_Id to session key  user_ID --> will be used to authenticate user for access
            ctx.sessionAttribute("password", hashedPassword);
            Map<String, Object> sessionMap = ctx.sessionAttributeMap();

            ctx.json(sessionMap);
            ctx.render("/templates/property.html");
        });

        app.post("/add_property", ctx -> {
            String propertyName = ctx.formParam("property_name");
            int numberOfRooms =  Integer.parseInt(Objects.requireNonNull(ctx.formParam("number_of_rooms"))) ;
            int rent =  Integer.parseInt(Objects.requireNonNull(ctx.formParam("rent"))) ;
            String address = ctx.formParam("address");
            String contact = ctx.formParam("contact");
            //Get the new user unique_Id by accessing the key-value setAttributes() session set in /user_sign_up Url_end point
            Integer property_owner = ctx.sessionAttribute("user_ID");

            //Insert into property table property_information
            residence property_information = new residence(propertyName,numberOfRooms,rent,address,contact,property_owner);
            property_information.insert_information();
            Integer owner_property = property_information.UniqueID();
            System.out.println("owner_property");
            System.out.println(owner_property);
            ctx.sessionAttribute("propertyId",owner_property);

            ctx.render("/templates/tenant_form.html");

        });
        app.post("/addtenants", ctx -> {

            //Access the form input --->
            Integer propertyID = ctx.sessionAttribute("propertyId") ;
            System.out.println(propertyID);
            String name = ctx.formParam("tenant_name");
            String moveIn = ctx.formParam("move_in");
            System.out.println("print");
            System.out.println(moveIn);
            String employment_status = ctx.formParam("employment");
            String cell_number = ctx.formParam("cell_number");
            Integer payday = Integer.parseInt(ctx.formParam("pay_day"));
            Integer room = Integer.parseInt(ctx.formParam("room"));
            String room_price = ctx.formParam("room_price");
            String kin_name = ctx.formParam("kin_name");
            String kin_number = ctx.formParam("kin_number");

            Tenant tenant = new Tenant(propertyID,name,moveIn,employment_status,cell_number,payday,room,room_price,kin_name,kin_number);

            tenant.insert_information();



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
