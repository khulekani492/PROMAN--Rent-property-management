package API;


import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinThymeleaf;
import model.database.*;

import java.nio.MappedByteBuffer;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static API.SecurityUtil.hashPassword;
import static API.SessionUtil.fileSessionHandler;

//TODO https://javalin.io/tutorials/jetty-session-handling


public class umuziAPI {
    public  static Javalin startServer(int port) throws SQLException {
        apiHandler controllers = new apiHandler();

        Javalin app = Javalin.create(config -> {
            config.jetty.modifyServletContextHandler(handler -> handler.setSessionHandler(fileSessionHandler()));
            config.fileRenderer(new JavalinThymeleaf());
        });
        //home_page Url_end_point
        app.get("/", ctx ->{
           ctx.render("/templates/index.html");
        });
        app.get("/rentalmanageme@sign_up",ctx ->{
            ctx.render("templates/sign_up_user.html");
        });
        //sign_up session order /user_sign_up "/add_property" /addtenants"
        /**
         * adds new user information to the database
         */
        app.post("/user_sign_up",controllers.sign_up());
/**
 * adds new user property information to the database
 */
        app.post("/add_property", controllers.addproperty());

/**
 * adds tenant information to the database
 */
        app.post("/addtenants", controllers.addTenant());




        app.get("/rentalManagement@cuser_profile",ctx ->{
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
