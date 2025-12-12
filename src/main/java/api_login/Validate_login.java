package api_login;

import io.javalin.http.Handler;
import jakarta.servlet.http.HttpSession;
import model.database.CRUD.Get_password;
import model.database.CRUD.landlord;
import model.database.CRUD.propertyNames;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static API.SecurityUtil.checkPassword;

public class Validate_login {

    public Handler
    authenticate(){
        return ctx -> {
            String user_email = ctx.formParam("email");
            String password = ctx.formParam("password");
            System.out.println("Password : " + password);


            Get_password hashed = new Get_password(user_email);
            if("NO USER FOUND".equals(hashed.compare())){
                ctx.sessionAttribute("login_email",user_email);
                ctx.redirect("/error/no_email");
                ctx.status(403).result("email does not exist");
                return;
            }
            boolean isMatch ;
            try{
                 isMatch = checkPassword(password, hashed.compare());
            } catch (RuntimeException e) {
                System.out.println("error");
                System.out.println(e.getMessage());
                throw new RuntimeException(e);
            }


            try {
                if (isMatch){
                   // ctx.sessionAttribute()
                    //Kill any session instance exist if password is a match
                    HttpSession session = ctx.req().getSession(false);
                    if (session != null)   {   session.invalidate();    }
                    ctx.sessionAttribute("email",user_email);

                    String email = ctx.sessionAttribute("email");
                    propertyNames default_properties = new propertyNames();
                    landlord authenticate = new landlord();
                    Integer landlord_id =  authenticate.landlordId(email);
                    Set<String> landlord_properties = default_properties.fetchAllproperty(landlord_id);
                    ArrayList<String> default_property = new ArrayList<>(landlord_properties);
                    Integer  total_properties = default_property.size();
                    String property_name = ctx.sessionAttribute("current_property");
                    System.out.println("Current property in memory : " + property_name);
                    String  first_property_name;
                    try {
                        first_property_name = default_property.getFirst();;
                    }catch (RuntimeException e) {
                        System.out.println("RUNNNNNNNNNNN");
                        Map<String, Object> data = new HashMap<>();
                        HashMap<String, HashMap<Integer, ArrayList<String>>> allPropertyUnits = new HashMap<>();
                        Map<String, Object> model1 = new HashMap<>();
                        HashMap<String, String> model = new HashMap<>();
                        HashMap<String, String> model2 = new HashMap<>();

                        String themeColor = ctx.sessionAttribute("theme_color");

                        // Keeps track of the updated name
                        ctx.sessionAttribute("current_property", "No properties Added");

                        String pro = ctx.sessionAttribute("current_property");
                        ctx.sessionAttribute("property_unit",0) ;

                        ctx.sessionAttribute("property_unit",0);
                        model.put("total_properties", String.valueOf(total_properties));
                        model.put("occupied_units", "0");

                        model2.put("occupancyRate", "0");
                        model2.put("vacant", "0");
                        model2.put("expected_profit", "0");

                        ctx.sessionAttribute("add_property_profile", "No");
                        model1.put("no_units", "No Units. Press +Add New Unit to add tenants ");
                        model1.put("names", landlord_properties); // Using landlord_unique_id to fetch all properties, accessed with user_emails
                        model1.put("name", property_name);
                        model1.put("user_chosen_theme", themeColor);
                        model1.put("no_properties",pro);

                        System.out.println("model_1 " + model1);

                        // Combine all model data
                        data.putAll(allPropertyUnits);
                        data.putAll(model1);
                        data.putAll(model);
                        data.putAll(model2);
                        System.out.println(data);

                        ctx.render("templates/dashboard.html", data);
                        return;

                    }

                    System.out.println();
                    ctx.sessionAttribute("current_property", first_property_name);
                    ctx.redirect("dashboard");
                } else {
                   // ctx.sessionAttribute("login_password",password);
                 //   ctx.redirect("/error/wrong_password");
                    ctx.status(403).result("Wrong password");

                }

            } catch (RuntimeException e) {
                System.out.println(e.getMessage());
                throw new RuntimeException(e);
            }

        };


        }
    }

