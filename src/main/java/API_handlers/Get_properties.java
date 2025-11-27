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

        return context -> {
            String property_name;
            landlord user_id = new landlord();
            try{
                 property_name = context.formParam("name");
                System.out.println(property_name + "Family");
                 context.sessionAttribute("dashBoard_current_property",property_name);
                System.out.println("prperty_first time"  + context.sessionAttribute("dashBoard_current_property"));

                 if(property_name == null){
                    String clone_property = context.pathParam("property_name");
                    String clone_email = context.pathParam("user_email");
                     String clone_unit = context.pathParam("unit");
                     System.out.println(clone_unit + "Rhymes ");
                    Property_Status property_units = new Property_Status();
                    landlord authenticate = new landlord();


                     try{
                         authenticate.landlordId(clone_email);
                     } catch (Exception e) {
                         System.out.println("something wrong");
                         throw new RuntimeException(e);
                     }
                     HashMap<Integer, ArrayList<String>> fetch_all = property_units.property_tenants(clone_property,authenticate.landlordId(clone_email));

                     if(fetch_all.size() == 0){
                         HashMap<String,String> model = new HashMap<>();
                         String property = context.sessionAttribute("property_name");
                         model.put("no_units","No units added for " + property);
                         context.render("templates/dashboard.html",model);
                     }else {
                         Map<String, Object> data = new HashMap<>();
                         HashMap<String,HashMap<Integer,ArrayList<String>>> model = new HashMap<>();
                         HashMap<String,String> model2 = new HashMap<>();
                         Map<String, Object> model1 = new HashMap<>();
                         System.out.println(fetch_all + "Occupied units");
                         model.put("units",fetch_all);
                         //using landlord_unique_id to fetch all of their properties ,it accessed with the user_email
                         String email = context.sessionAttribute("email");
                         propertyNames default_properties = new propertyNames();
//                         landlord user_id = new landlord();
                         Set<String> landlord_properties = default_properties.fetchAllproperty(user_id.landlordId(email));


                         model1.put("names",landlord_properties);
                         model2.put("unit",clone_unit);
                         data.putAll(model);
                         data.putAll(model1);
                         data.putAll(model2);

                         System.out.println("backup_dashboard");
                         System.out.println(data);
                         context.render("templates/dashboard.html",data);
                     }

                 }
            } catch (Exception e) {
                System.out.println(e.getMessage());
                //Handler auto_submit of empty space
                throw new RuntimeException(e);
            }
            String property_email = context.sessionAttribute("email");
            context.sessionAttribute("dashBoard_current_email",property_email);
            System.out.println("Useremail : " +  " " + property_email);

            Property_Status property_units = new Property_Status();
            landlord authenticate = new landlord();

            try{
                authenticate.landlordId(property_email);
            } catch (Exception e) {
                System.out.println("something wrong");
                throw new RuntimeException(e);
            }
            HashMap<Integer, ArrayList<String>> fetch_all = property_units.property_tenants(property_name,authenticate.landlordId(property_email));
            System.out.println("RESULTS : " + fetch_all);

            if(fetch_all.size() == 0){
                HashMap<String,String> model = new HashMap<>();
                String property = context.sessionAttribute("property_name");
                model.put("no_units","No units added for " + property);
                context.render("templates/dashboard.html",model);
            }else {
                Map<String, Object> data = new HashMap<>();


                HashMap<String,HashMap<Integer,ArrayList<String>>> allPropertyUnits = new HashMap<>();
                ;
                Map<String, Object> model1 = new HashMap<>();

                allPropertyUnits.put("units",fetch_all);
                System.out.println(fetch_all + "Occupied units");
                //using landlord_unique_id to fetch all of their properties ,it accessed with the user_email
                String email = context.sessionAttribute("email");
                propertyNames default_properties = new propertyNames();

                Set<String> landlord_properties = default_properties.fetchAllproperty(user_id.landlordId(email));

                model1.put("names",landlord_properties);
                data.putAll(allPropertyUnits);
                data.putAll(model1);
                System.out.println("woah");
                System.out.println(data);
                context.render("templates/dashboard.html",data);
            }
        };
    }


}
