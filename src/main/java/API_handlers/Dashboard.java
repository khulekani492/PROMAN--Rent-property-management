package API_handlers;

import io.javalin.http.Handler;
import model.database.CRUD.Property_Status;
import model.database.CRUD.landlord;
import model.database.CRUD.propertyNames;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Dashboard {
    public Handler user_profile() {
        return ctx ->
        {
            ctx.req().getSession(false);
            System.out.println("STory time");
            System.out.println(ctx.cookieMap());
            //FETCH landlord properties
            landlord authenticate = new landlord();
            propertyNames default_properties = new propertyNames();
            Property_Status property_units = new Property_Status();

            String email = ctx.sessionAttribute("email");
            System.out.println("email " + email);
            try {
                ctx.req().changeSessionId();
                 Integer landlord_id =  authenticate.landlordId(email);
                //uses landlord_unique_id to fetch all of their properties , it accessed with the user_email
                //Access first property name. First convert landlord_properties to ArrayList
                Set<String> landlord_properties = default_properties.fetchAllproperty(landlord_id);
                ArrayList<String> default_property = new ArrayList<>(landlord_properties);
                Integer  total_properties = default_property.size();
                String property_name = ctx.sessionAttribute("current_property");
                System.out.println("Current property in memory : " + property_name);
                String first_property_name;
                try {
                    first_property_name = default_property.getFirst();
                    System.out.println("landlord First property : " + first_property_name );
                    if(!first_property_name.equals(property_name)){
                        System.out.println(property_name + "In memory" +" default name " + first_property_name);
                    }


                } catch (RuntimeException e) {
                    throw new RuntimeException(e);

                }

                System.out.println("first_property_name " + first_property_name);
                System.out.println("landlord_owner " + first_property_name);

                HashMap<Integer, ArrayList<String>> fetch_all = property_units.property_tenants(first_property_name,landlord_id);
                System.out.println("fetch all " + fetch_all);
                Integer occupied_units = fetch_all.size();

                //Calculate the occupancy percentage of the property
                String total_units = authenticate.total_property_units(first_property_name, landlord_id);
                ctx.sessionAttribute("property_unit",total_units);

                double occupancyRate = ((double) occupied_units / Integer.parseInt(total_units) ) * 100;

                //Vacant_rooms
                Integer vacant = Integer.parseInt( total_units ) - occupied_units;

                //Expected profit based off the number of units occupied
                String expected_profit = authenticate.property_estimated_profit(landlord_id,first_property_name );
                if (fetch_all.isEmpty()) {
                    Map<String, Object> data = new HashMap<>();
                    HashMap<String, HashMap<Integer, ArrayList<String>>> allPropertyUnits = new HashMap<>();
                    Map<String, Object> model1 = new HashMap<>();
                    HashMap<String, String> model = new HashMap<>();
                    HashMap<String, String> model2 = new HashMap<>();

                    //Keeps track of the updated name
                    ctx.sessionAttribute("current_property",first_property_name);
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
                    ctx.sessionAttribute("current_property",first_property_name);

                    allPropertyUnits.put("units", fetch_all);
                    System.out.println(fetch_all + "Occupied units");
                    model.put("total_properties", String.valueOf(total_properties));
                    model.put("occupied_units", String.valueOf(occupied_units));
                    model2.put("occupancyRate",String.valueOf(occupancyRate));
                    model2.put("vacant",String.valueOf(vacant ) );
                    model2.put("expected_profit",expected_profit);
                    //using landlord_unique_id to fetch all of their properties ,it accessed with the user_emails
                    model1.put("names", landlord_properties);
                    model1.put("name",property_name);

                    data.putAll(allPropertyUnits);
                    data.putAll(model1);
                    data.putAll(model);
                    data.putAll(model2);
                    System.out.println(data);
                    ctx.render("templates/dashboard.html", data);
                }

            } catch (Exception e) {
                System.out.println("something wrong");
                throw new RuntimeException(e);



          }

        };
    }
}
