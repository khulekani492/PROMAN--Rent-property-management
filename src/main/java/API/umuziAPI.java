package API;


import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static API.SessionUtil.fileSessionHandler;

import API_handlers.*;
import Invalidhandler.SameEmialCreator;
import Invalidhandler.SameUnitCreator;
import Invalidhandler.UpdateUser;
import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinThymeleaf;
import javassist.util.proxy.ProxyFactory;
import model.database.CRUD.Getunits;
import model.database.CRUD.landlord;
import model.database.CRUD.propertyNames;
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
            //FETCH landlord properties
            String email = ctx.sessionAttribute("email");
            propertyNames default_properties = new propertyNames();
            landlord user_id = new landlord();
            //using landlord_unique_id to fetch all of their properties , it accessed with the user_email
            Set<String> landlord_properties = default_properties.fetchAllproperty(user_id.landlordId(email));

            Map<String, Object> model = new HashMap<>();
            model.put("names",landlord_properties);
            ctx.render("templates/dashboard.html",model);
        });
        app.get("/landlord_sign_up",ctx ->{
            ctx.render("templates/landlord.html");
        });
        app.get("/add_property_unit",ctx ->{
            String getUsername = ctx.sessionAttribute("user_name");
            String propertyName = ctx.sessionAttribute("property_name");
            String propertyAddress = ctx.sessionAttribute("property_address");
            System.out.println(propertyAddress + " played the game");
            HashMap<String,String> model = new HashMap<>();
            model.put("user_name",getUsername);
            model.put("property_name",propertyName);
            model.put("property_address",propertyAddress);
            ctx.render("templates/property.html",model);
        });
        app.get("/add_property",ctx ->{
            ctx.render("templates/property.html");
        });
        app.post("/fetch_property_units",context -> {
            String property_name = context.formParam("name");
            String property_email = context.sessionAttribute("email");
            System.out.println("What they want " + property_name);

            Getunits property_units = new Getunits();
            landlord authenticate = new landlord();
            ;
            System.out.println("property_landlord " +authenticate.landlordId(property_email) );
            HashMap<Integer, ArrayList<String>> fetch_all = property_units.getOccupiedUnits(property_name,authenticate.landlordId(property_email));
            System.out.println("RESULTS : " + fetch_all);
            if(fetch_all.size() == 0){
                HashMap<String,String> model = new HashMap<>();
                String property = context.sessionAttribute("property_name");
                model.put("no_units","No units added for " + property );
                context.render("templates/dashboard.html",model);
            }else {
                HashMap<String,HashMap<Integer,ArrayList<String>>> model = new HashMap<>();
                System.out.println(fetch_all + "Occupied units");
                model.put("units",fetch_all);
                System.out.println(model);
                System.out.println(fetch_all);

                context.render("templates/dashboard.html",model);
            }

        });

        app.get("/property_information", new PropertyUnits().property_related_information());

        /**
         * adds new user information to the database
         */
        Signup new_signee = new Signup();
        app.post("/user_sign_up", new_signee.sign_up());
        //app.get("/user_sign_up/error",controller.errorMessage());
/**
 * adds new user property information to the database
 */     AddProperty propertyInfo = new AddProperty();
        app.post("/add_property", propertyInfo.addproperty());
        app.get("/add_tenant",ctx -> {
           ctx.render("templates/tenant_form.html");
        });
/**
 * adds tenant information to the database
 */
    AddTenant property_tenant = new AddTenant();
    app.post("/add_tenants", property_tenant.addTenant());

    UpdateUser feedback;
    feedback = new SameEmialCreator();
    app.get("/error/same_email",feedback.updateUser());

    feedback = new SameUnitCreator();
    app.get("/error/same_unit",feedback.updateUser());

    app.post("/updateTenants",ctx -> {
            ///Updating Previously occupied room with new tenant or individual update like rent price for that rent
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
