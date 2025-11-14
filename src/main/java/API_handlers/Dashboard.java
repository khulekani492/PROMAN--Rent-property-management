package API_handlers;

import io.javalin.http.Handler;
import model.database.CRUD.landlord;
import model.database.CRUD.propertyNames;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Dashboard {
    public Handler user_profile(){
        return  ctx ->
        {
            //FETCH landlord properties
            String email = ctx.sessionAttribute("email");
            propertyNames default_properties = new propertyNames();
            landlord user_id = new landlord();
            //using landlord_unique_id to fetch all of their properties , it accessed with the user_email
            Set<String> landlord_properties = default_properties.fetchAllproperty(user_id.landlordId(email));

            Map<String, Object> model = new HashMap<>();
            model.put("names",landlord_properties);
            ctx.render("templates/dashboard.html",model);
        };
    }
}
