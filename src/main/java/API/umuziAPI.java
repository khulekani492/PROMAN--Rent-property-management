package API;


import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static API.SessionUtil.fileSessionHandler;

import API_handlers.*;
import Invalidhandler.*;
import api_login.Record_rent;
import api_login.Validate_login;
import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinThymeleaf;


public class umuziAPI {
    public  static Javalin startServer(int port) throws SQLException {
        Javalin app = Javalin.create(config -> {
            config.jetty.modifyServletContextHandler(handler -> handler.setSessionHandler(fileSessionHandler()));
            config.fileRenderer(new JavalinThymeleaf());
            config.staticFiles.add("public"); //Include Dependency to the jarfile
        });
        //home_page.
        app.get("/", ctx ->{
           ctx.render("templates/home.html");
        });

        app.get("/login",
                ctx -> {
                  ctx.render("/templates/login.html");
                });

        app.post("/verify_user",new Validate_login().authenticate());

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
            //TODO Empty the session variables to be blank for generating a new page
            // include -- <Add New Property>
            ctx.render("templates/property.html");
        });

        Get_properties  properties = new Get_properties();

        app.post("/fetch_property_units",properties.display_property_units());

        app.get("/unit_already_paid/{property_name}/{user_email}/{unit}",properties.display_property_units());
        app.get("/payment_status/{property_name}/{user_email}/{unit}",properties.display_property_units());

        app.get("/property_information", new PropertyUnits().property_related_information());

        Record_rent complete_payment_record = new Record_rent();
        app.post("/payment_status/{id}/{unit}",complete_payment_record.payment());
        /**
         * adds new user information to the database
         */
        Signup new_signee = new Signup();
        app.post("/user_sign_up", new_signee.sign_up());

/**
 * adds new user property information to the database
 */     AddProperty propertyInfo = new AddProperty();
        app.post("/add_property", propertyInfo.addproperty());

        app.get("/add_tenant",ctx -> {
            Map<String, Object> model = new HashMap<>();

            String user_name = ctx.sessionAttribute("user_name");
            Integer  propertyUnit = ctx.sessionAttribute("_unit");
            String  propertyName = ctx.sessionAttribute("property_name");

            model.put("user_name",user_name);
            model.put("unit_add", propertyUnit);
            model.put("name", propertyName);
           ctx.render("templates/tenant_form.html",model);
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

    feedback = new NoEmailCreator();
    app.get("/error/no_email",feedback.updateUser());

    feedback = new WrongPasswordCreator();
    app.get("/error/wrong_password",feedback.updateUser());

    feedback = new DuplicateCreator();
    app.get("/error/duplicate_payment", feedback.updateUser());

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

    public static void main(String[] args) throws SQLException {
        startServer(7070);
    }
}
