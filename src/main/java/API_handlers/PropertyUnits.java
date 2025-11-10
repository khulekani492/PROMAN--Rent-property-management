package API_handlers;

import io.javalin.http.Handler;
import model.database.CRUD.Propertyinfo;

import java.util.ArrayList;

public class PropertyUnits {

    public Handler property_related_information(){
        return  ctx -> {

            Propertyinfo property_list = new Propertyinfo();
            //ArrayList<String> add_tenant_rent = property_list.property_tenants("Thornville_rooms").get(1);
            for (int i =0 ; i < 3; i++){
                ArrayList<String> get_Unit_related = property_list.property_tenants("Thornville_rooms").get(i);
                Integer tenant_id = Integer.valueOf(get_Unit_related.get(3));
                Integer rent_day_tenant = Integer.valueOf(property_list.rent_payment(tenant_id));
                get_Unit_related.addLast(String.valueOf( rent_day_tenant));
            }
        };
    }
}
