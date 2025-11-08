package API_handlers;

import Invalidhandler.ErrorHandler;
import Invalidhandler.SameEmail;
import Invalidhandler.SameEmialCreator;
import Invalidhandler.UpdateUser;
import io.javalin.http.Handler;
import model.database.tenant;
import model.database.general;
import model.database.residence;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLOutput;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import static API.SecurityUtil.hashPassword;


public class apiHandler {

    public Handler sign_up() {
        return ctx -> {
            try {
                String user_name = ctx.formParam("user_name");

                ctx.sessionAttribute("user_name", user_name);
                String contact = ctx.formParam("contact");
                String email = ctx.formParam("user_email");
                String password = ctx.formParam("password");
                String user_type = ctx.formParam("user_type");
                ctx.sessionAttribute("user_name",user_name);
                //String property_name = ctx.formParam("property_name");

                // hash password
                String hashedPassword = hashPassword(password);

                // insert user into database
                general new_user = new general(user_name, contact, email, hashedPassword,user_type);
                try{
                    new_user.insert_information();
                    Integer new_userID = new_user.UniqueID();
                    // set session attributes
                    ctx.sessionAttribute("user_ID", new_userID);
                    ctx.sessionAttribute("password", hashedPassword);

                    // set session attributes the username and property_name
                    String landlord_username = ctx.formParam("user_name");
                    Map<String, Object> sessionMap = ctx.sessionAttributeMap();
                    ctx.json(sessionMap);

                    Map<String, Object> model = new HashMap<>();
                    model.put("name", landlord_username);
                    model.put("Unit",4);
                    model.put("rent",400);
                    model.put("rent_date",7);
                    ctx.redirect("/add_property_unit");

                } catch (SQLException e){
                    if ("user exists".equals(e.getMessage())) {
                        System.out.println("messages s");
                        ctx.redirect("/user_sign_up/error");
                    } else {
                        ctx.status(500).result("Unexpected error: " + e.getMessage());
                     //   ctx.redirect("/user_sign_up/error");
                    }
                }


                // get new user ID


            } catch (SQLException e) {
                ctx.status(500).result("Database wonder error: " + e.getMessage());
                e.printStackTrace();
            } catch (Exception e) {
                ctx.status(400).result("Error: " + e.getMessage());
                e.printStackTrace();
            }
        };
    }

    public Handler addproperty(){
        return  ctx ->{
           try{

            Integer propertyUnit = Integer.valueOf(Objects.requireNonNull(ctx.formParam("property_unit")));

            String property_Name = ctx.formParam("property_Name");
            String property_address = ctx.formParam("property_Address");
            String occupation = ctx.formParam("occupation");
            Integer rent = Integer.valueOf(Objects.requireNonNull(ctx.formParam("rent")));

            ctx.sessionAttribute("property_name", property_Name);
            ctx.sessionAttribute("property_unit",propertyUnit);
            ctx.sessionAttribute("property address",property_address);
            ctx.sessionAttribute("occupation",occupation);
            Integer property_owner = ctx.sessionAttribute("user_ID");

            //Insert INTO property table property_information
            residence property_information = new residence(propertyUnit,rent,occupation, property_Name);
            property_information.setlandlord(property_owner);
            property_information.setProperty_address(property_address);
            property_information.setProperty_Name(property_Name);
           //TRY AND EXCEPT
            //entity relationship with the Users table
               try {
                   property_information.insert_information();
               } catch (Exception e) {
                  ctx.redirect("/error/unit");
                   return;
               }

            Integer owner_property = property_information.UniqueID();
               System.out.println("properties_id " + owner_property);
               String property_name =  ctx.sessionAttribute("property_name");
               ctx.sessionAttribute("_unit",propertyUnit);
               Map<String, Object> model = new HashMap<>();
               model.put("unit_add", propertyUnit);
               model.put("residence_name",property_name);
               ctx.sessionAttribute("residence",property_name);


               if ("yes".equals(occupation)) {
                   System.out.println("yes status");
                   ctx.redirect("/add_tenant");
                   //ctx.render("/templates/tenant_form.html", model);
               } else {
                   System.out.println("no status");
                   ctx.render("/templates/dashboard.html", model);
               }

           } catch (Exception e) {
               ctx.status(400).result("Error chill: " + e.getMessage());
               e.printStackTrace();
           }
    };
    }
    /**
     * Gets the number of rooms the landlord has submitted.
     * <p>
     * Keeps rendering the same form repeatedly until the counter
     * reaches the last room.
     */
    public Handler errorMessage(){
        return ctx -> {
            ctx.result("ABCDeezNuts");
        };
    }
    public Handler addTenant() {
        return ctx -> {
            try {
                // Insert tenant into general_user table for landlord manual insertion
                String name = ctx.formParam("tenant_name");
                String number = ctx.formParam("cell_number");
                String user_type = "tenant";

                general addnewTenant = new general(name, number, user_type);
                addnewTenant.landlord_insert_tenant();

                // Access the unique ID from the general_user table
                Integer tenantUniqueID = addnewTenant.tenant_ID();
                System.out.println("Tenant ID: " + tenantUniqueID);

                // Update properties table with tenant unique ID
                Integer landlordId = ctx.sessionAttribute("user_ID");
                Integer property_unit = ctx.sessionAttribute("property_unit");
                String property_Name = ctx.sessionAttribute("property_name");
                String property = ctx.sessionAttribute("residence");
                System.out.println("Property_name from cookie session: " + property_Name );
                System.out.println("Property_residence: " + property );
                System.out.println("Landlord ID: " + landlordId);
                System.out.println("Property unit: " + property_unit);

                residence addTenantUnit = new residence();
                addTenantUnit.setProperty_unit(property_unit);
                addTenantUnit.setProperty_Name(property_Name);
                addTenantUnit.setlandlord(landlordId);
                addTenantUnit.setTenantId(tenantUniqueID);

                String tenant_name = addnewTenant.getTenantnamebyId(tenantUniqueID);
                System.out.println(tenant_name + " tenant_name by Id");


                try {
                    addTenantUnit.Insert_tenatId();
                } catch (SQLException e) {
                    ctx.redirect("/user_sign_up/error");
                    e.printStackTrace();  // <-- ensures the exception appears in your terminal
                    System.err.println("Error: " + e.getMessage());

                }
                // Additional tenant info
                Date moveIn = Date.valueOf(ctx.formParam("move_in"));
                String employment_status = ctx.formParam("employment");
                String kin_name = ctx.formParam("kin_name");
                String kin_number = ctx.formParam("kin_number");

                tenant additional_information = new tenant(moveIn, employment_status, kin_name, kin_number);
                additional_information.setTenantId(tenantUniqueID);
                additional_information.insert_information();

                // Render dashboard with updated model
                String username = ctx.sessionAttribute("name");
                String property_name = ctx.sessionAttribute("property_Name");
                Integer unit_number = ctx.sessionAttribute("_unit");

                Map<String, Object> model = new HashMap<>();
                model.put("name", property_name);
                model.put("unit_add", unit_number);
                model.put("tenant_name", tenant_name);

                ctx.redirect("/dashboard");
                //ctx.render("/templates/dashboard.html", model);

            } catch (SQLException e) {
                ctx.status(500).json("error: " + e.getMessage());
                e.printStackTrace();
            } catch (Exception e) {
                ctx.status(400).result("Error: " + e.getMessage());
                e.printStackTrace();
            }
        };
    }
}