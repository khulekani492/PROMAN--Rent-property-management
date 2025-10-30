package API;


import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static API.SessionUtil.fileSessionHandler;
import API_handlers.apiHandler;
import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinThymeleaf;



public class umuziAPI {
    public  static Javalin startServer(int port) throws SQLException {
        apiHandler controller = new apiHandler();

        Javalin app = Javalin.create(config -> {
            config.jetty.modifyServletContextHandler(handler -> handler.setSessionHandler(fileSessionHandler()));
            config.fileRenderer(new JavalinThymeleaf());
            config.staticFiles.add("public/"); //how to include dependecy to the jarfile in target
        });
        //home_page Url_end_point.
        app.get("/", ctx ->{
           ctx.render("/public/index.html");
        });

        app.get("templates/sign_up_user.html",ctx ->{
            ctx.render("templates/sign_up_user.html");
        });



        app.get("/public/templates/sign_up_user.html",ctx ->{
            ctx.render("templates/sign_up_user.html");
        });
        app.get("/rentalmanageme@sign_up",ctx ->{
            ctx.render("templates/sign_up_user.html");
        });
        app.get("/tenant_form",ctx ->{
           ctx.render("/templates/property.html");
        });
        //sign_up session order /user_sign_up "/add_property" /addtenants"
        /**
         * adds new user information to the database
         */
        app.post("/user_sign_up",controller.sign_up());
/**
 * adds new user property information to the database
 */
        app.post("/add_property", controller.addproperty());

/**
 * adds tenant information to the database
 */
        app.post("/add_tenants", controller.addTenant());


//log in Session
//        app.get("/rentalmanagemnt@log_in?" controllers.);




        app.get("/rentalmanagemnt@log_in?cuser_profile",ctx ->{
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
