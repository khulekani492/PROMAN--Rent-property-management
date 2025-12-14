package API_handlers;

import io.javalin.http.Handler;
import model.database.CRUD.Property_Status;

import model.database.CRUD.landlord;
import model.database.CRUD.propertyNames;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Get_properties {

    public Handler display_property_units(){

        return ctx -> {

            String property_name;
            HashMap<Integer, ArrayList<String>>  fetch_all_memory = ctx.sessionAttribute("current_units_property") ;
            String current_propertyINSession =  ctx.sessionAttribute("current_property");;
            landlord user_id = new landlord();
            propertyNames default_properties = new propertyNames();


            try{
                 property_name = ctx.formParam("name");

                System.out.println(property_name + ".......Fetching units");
            } catch (Exception e) {
                System.out.println(e.getMessage());
                //Handler auto_submit of empty space
                throw new RuntimeException(e);

            }


            Integer landlord_id =  ctx.sessionAttribute("landlordID");

            String  login_user_name = ctx.sessionAttribute("loginUsername");

            Property_Status property_units = new Property_Status();
           // landlord authenticate = new landlord();
            Set<String> landlord_properties = ctx.sessionAttribute("allProperties");
            ArrayList<String> default_property = new ArrayList<>(landlord_properties);
            Integer  total_properties = default_property.size();

          //  HashMap<Integer, ArrayList<String>> fetch_all = property_units.property_tenants(property_name, authenticate.landlordId(property_email));
            HashMap<Integer, ArrayList<String>>  fetch_all ;
            if(!current_propertyINSession.equals(property_name)){
                System.out.println(property_name + "form chose a new  property " +" in memory name " + current_propertyINSession);
                fetch_all = property_units.property_tenants(property_name,landlord_id);
                ctx.sessionAttribute("current_units_property",fetch_all);
                System.out.println("override the units value " + fetch_all);

            } else{
                System.out.println("landlord First property : " + property_name );
                fetch_all = fetch_all_memory;
                System.out.println("In memorry  session is the same" + fetch_all);
            }

            Integer occupied_units = fetch_all.size();
            //Calculate the occupancy percentage of the property
            Integer total_units = user_id.total_property_units(property_name,landlord_id);
            System.out.println( property_name + " total units" + total_units);

            ctx.sessionAttribute("property_unit",total_units);

            double roundedOccupancy = ((double) occupied_units / total_units ) * 100;
            // Round up to nearest integer
            int occupancyRate  = (int) Math.ceil(roundedOccupancy);


            //Vacant_rooms
            Integer vacant =  total_units  - occupied_units;
            //Expected profit based off the number of units occupied
            String expected_profit = user_id.property_estimated_profit(landlord_id,property_name );

            if (fetch_all.isEmpty()) {
                Map<String, Object> data = new HashMap<>();
                HashMap<String, HashMap<Integer, ArrayList<String>>> allPropertyUnits = new HashMap<>();
                Map<String, Object> model1 = new HashMap<>();
                HashMap<String, String> model = new HashMap<>();
                HashMap<String, String> model2 = new HashMap<>();

                ctx.sessionAttribute("current_property",property_name);

                allPropertyUnits.put("units", fetch_all);
                System.out.println(fetch_all + "Occupied units");
                model.put("total_properties", String.valueOf(total_properties));
                model.put("occupied_units", String.valueOf(occupied_units));
                model2.put("occupancyRate",String.valueOf(occupancyRate));
                model2.put("vacant",String.valueOf(vacant ) );
                model2.put("expected_profit","0");
                model1.put("no_units","No Units. press  +Add New Unit to add tenants ");
                //using landlord_unique_id to fetch all of their properties ,it accessed with the user_emails
                model1.put("names", landlord_properties);
                model1.put("name",property_name);
                model1.put("user_name",login_user_name);
                System.out.println("model_1 " + model1);
                data.putAll(allPropertyUnits);
                data.putAll(model1);
                data.putAll(model);
                data.putAll(model2);
                ctx.render("templates/dashboard.html", data);
            } else {
                Map<String, Object> data = new HashMap<>();
                HashMap<String, HashMap<Integer, ArrayList<String>>> allPropertyUnits = new HashMap<>();
                Map<String, Object> model1 = new HashMap<>();
                HashMap<String, String> model = new HashMap<>();
                HashMap<String, String> model2 = new HashMap<>();
                HashMap<String, String> model5 = new HashMap<>();

                String themeColor = ctx.sessionAttribute("theme_color");
                ctx.sessionAttribute("current_property",property_name);
                allPropertyUnits.put("units", fetch_all);
                System.out.println(fetch_all + "Occupied units");
                model.put("total_properties", String.valueOf(total_properties));
                model.put("occupied_units", String.valueOf(occupied_units));
                model2.put("occupancyRate",String.valueOf(occupancyRate));
                model2.put("vacant",String.valueOf(vacant ) );
                model2.put("expected_profit",expected_profit);
                //using landlord_unique_id to fetch all of their properties ,it accessed with the user_emails
                model1.put("names", landlord_properties);
                model1.put("user_chosen_theme",themeColor);
                model5.put("name",property_name);

                System.out.println(model5
                );
                data.putAll(allPropertyUnits);
                data.putAll(model1);
                data.putAll(model);
                data.putAll(model2);
                data.putAll(model5);
                System.out.println(data);
                ctx.render("templates/dashboard.html", data);
            }

        };
    }

}
