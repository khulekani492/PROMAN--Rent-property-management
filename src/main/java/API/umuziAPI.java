package API;


import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static API.SessionUtil.fileSessionHandler;
import API_handlers.apiHandler;
import Invalidhandler.SameEmialCreator;
import Invalidhandler.SameUnit;
import Invalidhandler.SameUnitCreator;
import Invalidhandler.UpdateUser;
import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinThymeleaf;
import model.database.general;


public class umuziAPI {
    public  static Javalin startServer(int port) throws SQLException {
        apiHandler controller = new apiHandler();
        general error = new general();
        Javalin app = Javalin.create(config -> {
            config.jetty.modifyServletContextHandler(handler -> handler.setSessionHandler(fileSessionHandler()));
            config.fileRenderer(new JavalinThymeleaf());
            config.staticFiles.add("public"); //Include Dependency to the jarfile
        });
        //home_page
        app.get("/", ctx ->{
           ctx.render("templates/home.html");
        });


        app.get("/tenant_form",ctx ->{
           ctx.render("/templates/property.html");
        });
        app.get("/dashboard",ctx ->{
            String user_name = ctx.sessionAttribute("user_name");
            Map<String, Object> model = new HashMap<>();
            model.put("name",user_name);
            ctx.render("templates/dashboard.html",model);
        });

        app.get("/landlord_sign_up",ctx ->{
            ctx.render("templates/landlord.html");
        });
        app.get("/add_property_unit",ctx ->{
            String getUsername = ctx.sessionAttribute("user_name");
            HashMap<String,String> model = new HashMap<>();
            model.put("user_name",getUsername);
            ctx.render("templates/property.html",model);
        });
        app.get("/add_property",ctx ->{
            ctx.render("templates/property.html");
        });

        /**
         * adds new user information to the database
         */
        app.post("/user_sign_up",controller.sign_up());
        app.get("/user_sign_up/error",controller.errorMessage());
/**
 * adds new user property information to the database
 */
        app.post("/add_property", controller.addproperty());
        app.get("/add_tenant",ctx -> {
           ctx.render("templates/tenant_form.html");
        });
/**
 * adds tenant information to the database
 */ app.post("/add_tenants", controller.addTenant());

    UpdateUser feedback;
    feedback = new SameEmialCreator();
    app.get("/error/same_email",feedback.updateUser());
    feedback = new SameUnitCreator();
    app.get("/error/same_unit",feedback.updateUser());

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


        });
        app.get("/add_another_unit", ctx -> {
            Map<String, Object> model = new HashMap<>();
            // Example: populate values (replace with actual session or DB data)
            model.put("user_name", ctx.sessionAttribute("user_name"));
            model.put("residence_name", "My Residence"); // or fetch dynamically
            ctx.render("/templates/property_form.html",model);
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
