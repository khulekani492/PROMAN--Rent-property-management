package API;


import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static API.SessionUtil.fileSessionHandler;

import API_handlers.*;
import Invalidhandler.*;
import api_login.Record_rent;
import api_login.Validate_login;
import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinThymeleaf;
import jakarta.servlet.http.HttpSession;
import model.database.CRUD.Tenant;
import org.thymeleaf.web.servlet.JakartaServletWebApplication;


public class umuziAPI {
    public  static Javalin startServer(int port) throws SQLException {
        Javalin app = Javalin.create(config -> {
            config.jetty.modifyServletContextHandler(handler -> handler.setSessionHandler(fileSessionHandler()));
            config.fileRenderer(new JavalinThymeleaf());
            config.staticFiles.add("public");
        });
        //home_page.
        app.get("/", ctx ->{
           ctx.render("templates/home.html");
        });

        app.post("/page_theme",ctx -> {
            String theme_color = ctx.formParam("user_theme");
            System.out.println(theme_color);
            ctx.sessionAttribute("theme_color",theme_color);
            ctx.render("templates/home.html");


        });
        app.get("/login",
                ctx -> {
                  HashMap<String,String> model = new HashMap<>();
                  String themeColor = ctx.sessionAttribute("theme_color");
                  model.put("user_chosen_theme",themeColor);
                  ctx.render("/templates/login.html",model);
                });


        app.post("/verify_user",new Validate_login().authenticate());

        app.get("/tenant_form",ctx ->{
           ctx.render("/templates/property.html");

        });

        app.get("/logout",ctx -> {
             HttpSession session = ctx.req().getSession(false);
               if (session != null)   {   session.invalidate();    }
             ctx.redirect("/");
        });

        app.get("/dashboard",new Dashboard().user_profile());
        app.get("/landlord_sign_up",ctx ->{
            HashMap<String,String> model = new HashMap<>();
            String themeColor = ctx.sessionAttribute("theme_color");
            model.put("user_chosen_theme",themeColor);
            ctx.render("templates/landlord.html",model);
        });

        app.get("/add_property_unit",new Save_unit().save_property_unit());

        app.get("/tenant_profile/{id}/{unit}/{propertyRent}/{contact}",ctx ->{
            String tenant_name = ctx.pathParam("id");
            String tenant_unit = ctx.pathParam("unit");
            String property_rent = ctx.pathParam("propertyRent");
            String tenant_property = ctx.sessionAttribute("current_property");

            String tenant_contact = ctx.pathParam("contact");
            System.out.println("tenant property : " + tenant_property);
            System.out.println("tenant contact : " + tenant_contact);

           System.out.println("Unit  number : " + tenant_unit);
       //     System.out.println("tenant property : " + tenant_property);
            System.out.println("Tenant_name " + tenant_name);

            Tenant tenant = new Tenant();


            Integer tenant_id = tenant.tenant_ID(tenant_name,tenant_contact);

            ArrayList<String> next_of_Kin_Information = tenant.tenant_next_of_kin(tenant_id) ;

            String next_kin = next_of_Kin_Information.getFirst();
            String next_kin_number = next_of_Kin_Information.getLast();
            String tenantMoveIN = next_of_Kin_Information.get(1);
            String tenant_rent = next_of_Kin_Information.get(2);
            System.out.println("Additional information " + next_of_Kin_Information);
            //
            HashMap<String,String> model = new HashMap<>();
            String themeColor = ctx.sessionAttribute("theme_color");
            model.put("user_chosen_theme",themeColor);
            model.put("unit_number",tenant_unit);
            model.put("tenant_name",tenant_name);
            model.put("rent_amount",property_rent);
            model.put("kin_name",next_kin);
            model.put("kin_contact",next_kin_number);
            model.put("property_name",tenant_property);
            model.put("move_in_date",tenantMoveIN);
            model.put("rent_day",tenant_rent);

            ctx.render("templates/user_profile.html",model);
        });

        app.post("/update_property_info",ctx -> {

        });


        app.get("/log_out",new LogOut().sign_out());
        // Redirects to the unit page
        app.get("/add_property",ctx ->{

            HashMap<String,String> model = new HashMap<>();
            String themeColor = ctx.sessionAttribute("theme_color");
            model.put("user_chosen_theme",themeColor);

            ctx.render("templates/property.html",model);
        });

        Get_properties  properties = new Get_properties();

        app.post("/fetch_property_units",properties.display_property_units());

        app.get("/unit_already_paid/{property_name}/{user_email}/{unit}",properties.display_property_units());
        app.get("/payment_status/{property_name}/{user_email}/{unit}",properties.display_property_units());


        UpdateTenants tenant_unit_update = new UpdateTenants();
        app.post("unmark_payment/{id}/{unit}", tenant_unit_update.unmark_payment());

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
            Integer total_n_units = ctx.sessionAttribute("property_unit") ;
            String  property_name = ctx.sessionAttribute("current_property" );
            String  theme_color = ctx.sessionAttribute("theme_color" );
            String  check_Memory = ctx.sessionAttribute("add_property_profile");;

            if(check_Memory == null){
                check_Memory = "Yes";
            }
            System.out.println("property_name "+ property_name);
            ctx.sessionAttribute("property_unit", total_n_units);

            model.put("user_name",user_name);
            model.put("unit_add", String.valueOf(total_n_units));
            model.put("user_chosen_theme",theme_color);
            model.put("name", property_name);
            model.put("add_property_profile",check_Memory);

            System.out.println(total_n_units + "  property unit ");
            System.out.println(model + "  Property state for tenant form ");

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

    feedback = new Invalid_dateCreator();
    app.get("/error/Invalid_date",feedback.updateUser());

    feedback = new InvalidPropertyCreator();
    app.get("/error/same_address",feedback.updateUser());

    feedback = new SameUnitCreator();
    app.get("/error/same_unit",feedback.updateUser());

    feedback = new NoEmailCreator();
    app.get("/error/no_email",feedback.updateUser());

    feedback = new TenantNameCreator();
    app.get("/error/same_cell_number",feedback.updateUser());

    feedback = new WrongPasswordCreator();
    app.get("/error/wrong_password",feedback.updateUser());

    feedback = new DuplicateCreator();
    app.get("/error/duplicate_payment", feedback.updateUser());

    app.post("/updateTenants",ctx -> {
            ///Updating Previously occupied room with new tenant or individual update like rent price for that rent
        });
    app.post("/supabase_login",ctx -> {
        System.out.println("Supabase function ");
        System.out.println(ctx.body());
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
