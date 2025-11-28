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
            Get_properties properties = new Get_properties();


            //FETCH landlord properties
            properties.display_property_units();
            propertyNames default_properties = new propertyNames();


            landlord user_id = new landlord();

            String email = ctx.sessionAttribute("email");
            //using landlord_unique_id to fetch all of their properties , it accessed with the user_email

            Set<String> landlord_properties = default_properties.fetchAllproperty(user_id.landlordId(email));


            //Access first property name. First convert landlord_properties to ArrayList
            ArrayList<String> default_property = new ArrayList<>(landlord_properties);
            String first_property_name;
            try {
                first_property_name = default_property.getFirst();
                System.out.println("default name " + first_property_name);
            } catch (RuntimeException e) {
                throw new RuntimeException(e);

            }
            Property_Status property_units = new Property_Status();
            landlord authenticate = new landlord();

            try {
                authenticate.landlordId(email);
            } catch (Exception e) {
                System.out.println("something wrong");
                throw new RuntimeException(e);
            }
            HashMap<Integer, ArrayList<String>> fetch_all = property_units.property_tenants(first_property_name, authenticate.landlordId(email));
            System.out.println("RESULTS : " + fetch_all);
            if (fetch_all.size() == 0) {
                HashMap<String, String> model = new HashMap<>();
                String property = ctx.sessionAttribute("property_name");
                model.put("no_units", "No units added for " + property);
                ctx.render("templates/dashboard.html", model);
            } else {
                Map<String, Object> data = new HashMap<>();
                HashMap<String, HashMap<Integer, ArrayList<String>>> allPropertyUnits = new HashMap<>();
                Map<String, Object> model1 = new HashMap<>();
                allPropertyUnits.put("units", fetch_all);
                System.out.println(fetch_all + "Occupied units");
                //using landlord_unique_id to fetch all of their properties ,it accessed with the user_emails


                model1.put("names", landlord_properties);
                data.putAll(allPropertyUnits);
                data.putAll(model1);
                System.out.println(data);
                ctx.render("templates/dashboard.html", data);
            }
        };
    }
}
