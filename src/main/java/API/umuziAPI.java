package API;


import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static API.SessionUtil.fileSessionHandler;

import API_handlers.*;
import Invalidhandler.*;
import Update_api.UpdateUnit;
import api_login.Record_rent;
import api_login.Validate_login;
import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinThymeleaf;
import jakarta.servlet.http.HttpSession;
import model.database.CRUD.Property_Status;
import model.database.CRUD.Tenant;
import model.database.UPDATES.Change_Unit;


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


        app.get("/tenant_profile/{id}/{unit}//{propertyRent}/{contact}",ctx ->{

            String tenant_name = ctx.pathParam("id");
            ctx.sessionAttribute("Tenant_name",tenant_name);
            String tenant_unit = ctx.pathParam("unit");
            ctx.sessionAttribute("Tenant_unit",tenant_unit);
            String property_rent = ctx.pathParam("propertyRent");
            ctx.sessionAttribute("property_rent",property_rent);

            String tenant_property = ctx.sessionAttribute("current_property");

            String tenant_contact = ctx.pathParam("contact");
            ctx.sessionAttribute("tenant_contact",tenant_contact);
            System.out.println("tenant name : " + tenant_name);
            System.out.println("tenant contact : " + tenant_contact);

           ///System.out.println("Unit  number : " + tenant_unit);
            ///System.out.println("tenant property : " + tenant_property);
            /// System.out.println("Tenant_name " + tenant_name);

            Tenant tenant = new Tenant();

            //Error null
            Integer tenant_id = tenant.tenant_ID(tenant_name,tenant_contact);
            System.out.println("tenant Id " + tenant_id);
            if (tenant_id == null) {
                ctx.render("templates/not_found.html");
                return;
                }

            else {
                System.out.println("Tenant ID NOT NULL");
                ctx.sessionAttribute("TenantID",tenant_id);
                }



            ArrayList<String> next_of_Kin_Information = tenant.tenant_next_of_kin(tenant_id) ;

            //Renders the user_profile without any next of kin name
            if(next_of_Kin_Information == null){
                System.out.println("HI the tenant has no next of kin information");
                HashMap<String,String> model = new HashMap<>();
                String themeColor = ctx.sessionAttribute("theme_color");
                model.put("user_chosen_theme",themeColor);
                model.put("unit_number",tenant_unit);
                model.put("tenant_contact",tenant_contact);
                model.put("tenant_name",tenant_name);
                model.put("rent_amount",property_rent);
                ctx.render("templates/user_profile.html",model);
                return;
            }
            String  next_kin   = next_of_Kin_Information.getFirst();
            String next_kin_number = next_of_Kin_Information.getLast();
            String tenantMoveIN = next_of_Kin_Information.get(1);
            String tenant_rent = next_of_Kin_Information.get(2);
            ///System.out.println("Additional information " + next_of_Kin_Information);
            HashMap<String,String> model = new HashMap<>();
            String themeColor = ctx.sessionAttribute("theme_color");
            model.put("user_chosen_theme",themeColor);
            model.put("unit_number",tenant_unit);
            model.put("tenant_contact",tenant_contact);
            model.put("tenant_name",tenant_name);
            model.put("rent_amount",property_rent);
            model.put("kin_name",next_kin);
            model.put("kin_contact",next_kin_number);
            model.put("property_name",tenant_property);
            model.put("move_in_date",tenantMoveIN);
            model.put("rent_day",tenant_rent);

            ctx.render("templates/user_profile.html",model);
        });

        app.get("create_new_unit",ctx -> {
            String current_property = ctx.sessionAttribute("current_property");

            HashMap<String,String> model = new HashMap<>();
            model.put("property_name",current_property);
            String themeColor = ctx.sessionAttribute("theme_color");
            model.put("user_chosen_theme",themeColor);

            ctx.render("templates/new_unit.html",model);
        });
        app.post("/update_property_info",ctx -> {

            Integer tenant_ID = ctx.sessionAttribute("TenantID");
            String tenant_unit = ctx.sessionAttribute("Tenant_unit");
            String amount = ctx.formParam("rent_amount");
            //Error handling
            String unitN = ctx.formParam("unit_number");
            System.out.println("Unit Number : " + unitN );
            Integer Unit_updated ;

            if(!unitN.equals(tenant_unit)) {
                Unit_updated = Integer.parseInt(unitN);
            }else {
                Unit_updated = Integer.parseInt(unitN);
            }

            String tenant_name = ctx.sessionAttribute("Tenant_name");

            String tenant_property = ctx.sessionAttribute("current_property");

            Integer landlordId = ctx.sessionAttribute("landlordID");

            Change_Unit updates_property = new Change_Unit(tenant_name,Unit_updated,tenant_property,tenant_ID);
            //Update amount .
            try{
                updates_property.setTenant_property(tenant_property);
                updates_property.setTenant_unit(Unit_updated);
                updates_property.update_unit_rent(amount);
                ctx.sessionAttribute("current_units_property",new Property_Status().property_tenants(tenant_property,landlordId));
                ctx.redirect("/dashboard");
            } catch (RuntimeException e){
                System.out.println(e.getMessage());
                //ctx.result("Update for rent failed because : " + e.getMessage());
                //ctx.status(403).result("units information not updated");
                HashMap<String,String> model = new HashMap<>();
                model.put("no_updates",e.getMessage());
                ctx.render("templates/user_profile", model);
            }
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

       // app.get("/unit_already_paid/{property_name}/{user_email}/{unit}",properties.display_property_units());
        //app.get("/payment_status/{property_name}/{user_email}/{unit}",properties.display_property_units());

        UpdateTenants tenant_unit_update = new UpdateTenants();
        app.post("unmark_payment/{id}/{unit}/{tenantContact}", tenant_unit_update.unmark_payment());

        app.get("/property_information", new PropertyUnits().property_related_information());

        //add new unit to an existing one
        UpdateUnit new_unit_added = new UpdateUnit() ;
        app.post("/add_new_unit",new_unit_added.create_property_unit());

        Record_rent complete_payment_record = new Record_rent();
        app.post("/payment_status/{id}/{unit}/{tenantContact}",complete_payment_record.payment());
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
       app.post("/update_tenant",ctx -> {
           String action = ctx.formParam("action");

           if(action.equals("save") ){
               System.out.println("New updates for existing tenant : user action Save "  );
               //Updates on tenant information --> Tenant name, Tenant_contact  OR  Rent_day Gets Updated

               String propertyUnit = ctx.formParam("unit_number");
               String propertyName = ctx.formParam("property_name");

               String tenant_rent_day = ctx.formParam("rent_day");
               String tenant_name = ctx.formParam("tenant_name_input");
               System.out.println("Tenant name :  " + tenant_rent_day);
               String tenant_contact =  ctx.formParam("tenant_contact");

               String kin_name = ctx.formParam("kin_name");
               System.out.println("KIN NAME " + kin_name);
               String kin_contact = ctx.formParam("kin_contact");
               System.out.println("KIN contact " + kin_contact);
               String move_in = ctx.formParam("move_in");
               System.out.println("KIN moveIn " + kin_contact);
               String move_out = ctx.formParam("move_out");

               ctx.sessionAttribute("tenant_name",tenant_name);
               ctx.sessionAttribute("tenant_unit",propertyUnit);
               ctx.sessionAttribute("_property_name",propertyName);
               ctx.sessionAttribute("tenant_contact",tenant_contact);
               ctx.sessionAttribute("tenant_rent",tenant_rent_day);
               ctx.sessionAttribute("kin_name",kin_name);
               ctx.sessionAttribute("kin_contact",kin_contact);
               ctx.sessionAttribute("move_in",move_in);
               ctx.sessionAttribute("move_out",move_out);

               ctx.redirect("/updating_tenant");
           }else {
               System.out.println("remove tenant");
           }

       });
       UpdateUnit new_tenant_updates = new UpdateUnit();
       app.get("/updating_tenant", new_tenant_updates.update_tenant());
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

        app.exception(SQLException.class, (e, ctx) -> {
            ctx.status(503).result("Service temporarily unavailable");
        });

        app.error(404, ctx -> {
            ctx.render("templates/not_found.html");
        });


        app.start(port);
        return app;
    }

    public static void main(String[] args) throws SQLException {
        startServer(7070);
    }
}
