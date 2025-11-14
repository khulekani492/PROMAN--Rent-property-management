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

        app.get("/dashboard",new Dashboard().user_profile());
        app.get("/landlord_sign_up",ctx ->{
            ctx.render("templates/landlord.html");
        });
        app.get("/add_property_unit",new Save_unit().save_property_unit());

        // Redirects to the unit page
        app.get("/add_property",ctx ->{
            //Empty the session variables to be blank for generating a new page
            // include -- <Add New Property>

            ctx.render("templates/property.html");
        });

        Get_properties  properties = new Get_properties();

        app.post("/fetch_property_units",properties.display_property_units());

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
