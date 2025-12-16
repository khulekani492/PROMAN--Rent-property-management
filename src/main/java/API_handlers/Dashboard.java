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

            System.out.println("STory time");
            System.out.println(ctx.cookieMap());
            //FETCH landlord properties

            landlord authenticate = new landlord();

             //propertyNames default_properties = new propertyNames();
              Property_Status property_units = new Property_Status();

               String email = ctx.sessionAttribute("email");

               ctx.req().changeSessionId();


                Integer landlord_id =  ctx.sessionAttribute("landlordID");
                System.out.println("landlordId " + landlord_id);
                String  login_user_name = ctx.sessionAttribute("loginUsername");
                 ctx.sessionAttribute("user_name",login_user_name);

                //uses landlord_unique_id to fetch all of their properties , it accessed with the user_email

                //First convert landlord_properties to ArrayList. Access first property name.


                String property_name = ctx.sessionAttribute("current_property");
                System.out.println("Current property in memory : " + property_name);
                String first_property_name;
                HashMap<Integer, ArrayList<String>>  fetch_all ;
                HashMap<Integer, ArrayList<String>>  fetch_all_memory = ctx.sessionAttribute("current_units_property") ;
                System.out.println( "In memorty : " + fetch_all_memory);
                try {

                    Set<String> landlord_properties = ctx.sessionAttribute("allProperties");
                    ArrayList<String> default_property = new ArrayList<>(landlord_properties);
                    Integer  total_properties = default_property.size();
                    first_property_name = default_property.getFirst();
                    if(!first_property_name.equals(property_name)){
                        ctx.sessionAttribute("current_property" ,property_name);
                        System.out.println(property_name + "In memory" +" default name " + first_property_name);
                     //   ctx.sessionAttribute("current_property",property_name);
                        //first_property_name = property_name;
                      //  fetch_all = property_units.property_tenants(first_property_name,landlord_id);
                        if(fetch_all_memory != null){
                            fetch_all = fetch_all_memory;
                            System.out.println("In memorry " + fetch_all);

                        }else{
                            fetch_all = property_units.property_tenants(property_name,landlord_id);
                            ctx.sessionAttribute("current_units_property",fetch_all);
                        }

                    } else{
                        System.out.println("landlord First property : " + first_property_name );
                        if(fetch_all_memory != null){
                            fetch_all = fetch_all_memory;
                            System.out.println("In memorry  session is the same" + fetch_all);

                        }else{
                            fetch_all = property_units.property_tenants(first_property_name,landlord_id);
                            ctx.sessionAttribute("current_units_property",fetch_all);
                        }

                    }

                    System.out.println("first_property_name " + first_property_name);
                    System.out.println("landlord_owner " + first_property_name);



                    Integer occupied_units = fetch_all.size();

                    //Calculate the occupancy percentage of the property
                    Integer total_units = authenticate.total_property_units(first_property_name, landlord_id);

                    System.out.println( first_property_name + " total units" + total_units);
                    ctx.sessionAttribute("property_unit",total_units);

                    double roundedOccupancy = ((double) occupied_units / total_units ) * 100;

                    // Round up to nearest integer
                    int occupancyRate  = (int) Math.ceil(roundedOccupancy);

                    //Vacant_rooms
                    Integer vacant = total_units  - occupied_units;
                    String expected_profit = authenticate.property_estimated_profit(landlord_id,first_property_name );

                    Map<String, Object> data = new HashMap<>();
                    HashMap<String, HashMap<Integer, ArrayList<String>>> allPropertyUnits = new HashMap<>();
                    Map<String, Object> model1 = new HashMap<>();
                    HashMap<String, String> model = new HashMap<>();
                    HashMap<String, String> model2 = new HashMap<>();
                    //ctx.sessionAttribute("current_property",first_property_name);

                    System.out.println("hi");
                    ctx.sessionAttribute("property_unit",total_units);
                    String themeColor = ctx.sessionAttribute("theme_color");
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
                    model1.put("user_chosen_theme",themeColor);
                    model1.put("user_name",login_user_name);
                    data.putAll(allPropertyUnits);
                    data.putAll(model1);
                    data.putAll(model);
                    data.putAll(model2);
                    System.out.println(data);
                    ctx.render("templates/dashboard.html", data);
                    //When first_property_name is null /dashboard is called without a property name available
                } catch (RuntimeException e) {
                    System.out.println("RUNNNNNNNNNNN "+ e.getMessage());
                    Map<String, Object> data = new HashMap<>();
                    HashMap<String, HashMap<Integer, ArrayList<String>>> allPropertyUnits = new HashMap<>();
                    Map<String, Object> model1 = new HashMap<>();
                    HashMap<String, String> model = new HashMap<>();
                    HashMap<String, String> model2 = new HashMap<>();

                    String themeColor = ctx.sessionAttribute("theme_color");

                    // Keeps track of the updated name
                    ctx.sessionAttribute("current_property", "No properties Added");
                    ctx.sessionAttribute("property_unit",0) ;

                    ctx.sessionAttribute("property_unit",0);
                    model.put("total_properties", "0");
                    model.put("occupied_units", "0");

                    model2.put("occupancyRate", "0");
                    model2.put("vacant", "0");
                    model2.put("expected_profit", "0");

                    ctx.sessionAttribute("add_property_profile", "No");
                    model1.put("no_units", "No Units. Press +Add New Unit to add tenants ");
                    //model1.put("names", landlord_propertie); // Using landlord_unique_id to fetch all properties, accessed with user_emails
                    model1.put("name", property_name);
                    model1.put("user_chosen_theme", themeColor);

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
                //Expected profit based off the number of units occupied

                if (fetch_all.isEmpty()) {
                    System.out.println("EMpty : " );
                    Map<String, Object> data = new HashMap<>();
                    HashMap<String, HashMap<Integer, ArrayList<String>>> allPropertyUnits = new HashMap<>();
                    Map<String, Object> model1 = new HashMap<>();
                    HashMap<String, String> model = new HashMap<>();
                    HashMap<String, String> model2 = new HashMap<>();
                    String themeColor = ctx.sessionAttribute("theme_color");
                    //Keeps track of the updated name
                    ctx.sessionAttribute("current_property",first_property_name);
                    allPropertyUnits.put("units", fetch_all);
                    System.out.println(fetch_all + "Occupied units");
                    model.put("total_properties", "0");
                    model.put("occupied_units", "0");
                    model2.put("occupancyRate","0");
                    model2.put("vacant","0" );
                    model2.put("expected_profit","0");
                    model1.put("no_units","No Units. press  +Add New Unit to add tenants ");
                    //using landlord_unique_id to fetch all of their properties ,it accessed with the user_emails
                    //model1.put("names", landlord_properties);
                    model1.put("name",property_name);
                    model1.put("user_chosen_theme",themeColor);
                    model1.put("user_name",login_user_name);
                    System.out.println("model_1 " + model1);
                    data.putAll(allPropertyUnits);
                    data.putAll(model1);
                    data.putAll(model);
                    data.putAll(model2);
                    ctx.render("templates/dashboard.html", data);
                }
//                else {
//                    Map<String, Object> data = new HashMap<>();
//                    HashMap<String, HashMap<Integer, ArrayList<String>>> allPropertyUnits = new HashMap<>();
//                    Map<String, Object> model1 = new HashMap<>();
//                    HashMap<String, String> model = new HashMap<>();
//                    HashMap<String, String> model2 = new HashMap<>();
//                    ctx.sessionAttribute("current_property",first_property_name);
//
//                    System.out.println("hi");
//                    ctx.sessionAttribute("property_unit",total_units);
//                    String themeColor = ctx.sessionAttribute("theme_color");
//                    allPropertyUnits.put("units", fetch_all);
//                    System.out.println(fetch_all + "Occupied units");
//                    model.put("total_properties", String.valueOf(total_properties));
//                    model.put("occupied_units", String.valueOf(occupied_units));
//                    model2.put("occupancyRate",String.valueOf(occupancyRate));
//                    model2.put("vacant",String.valueOf(vacant ) );
//                    model2.put("expected_profit",expected_profit);
//                    //using landlord_unique_id to fetch all of their properties ,it accessed with the user_emails
//                    model1.put("names", landlord_properties);
//                    model1.put("name",property_name);
//                    model1.put("user_chosen_theme",themeColor);
//                    model1.put("user_name",login_user_name);
//                    data.putAll(allPropertyUnits);
//                    data.putAll(model1);
//                    data.putAll(model);
//                    data.putAll(model2);
//                    System.out.println(data);
//                    ctx.render("templates/dashboard.html", data);
//                }
        };
    }
}
