package model.database.CRUD;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class Getunits {
    public HashMap<Integer,ArrayList<String>> getOccupiedUnits (String property_name,Integer landlordId){
        Propertyinfo property_list = new Propertyinfo();

        HashMap<Integer,ArrayList<String>> get_Unit_related = property_list.property_tenants(property_name,landlordId);
        HashMap<Integer,ArrayList<String>> get_tenants = new HashMap<>();
        ArrayList<String> property_per_unit  = null;

        for (Integer n : get_Unit_related.keySet()){
             property_per_unit = get_Unit_related.get(n);


            Integer AccessLast = null;
            //Skip unit with no tenants == null on index 3
            try {
                AccessLast = Integer.valueOf(property_per_unit.get(3));
            } catch (NullPointerException e){
                continue;
            } catch (NumberFormatException e){
                continue;
            }
            // Accessing the unit number which is Identical to the map key
            Integer first_one = Integer.valueOf(property_per_unit.get(0));
            String rent_day_tenant = null;
            try {
                rent_day_tenant = property_list.rent_payment(AccessLast);
            } catch (RuntimeException e) {
                System.out.println("Erorr Message " + e.getMessage());
                if("skip".equals(e.getMessage())){
                    continue;
                }

            }
            //adding the rent
            property_per_unit.add(rent_day_tenant);
            get_tenants.put(first_one,property_per_unit);
        }
        return get_tenants;
    }
static void main(){
        Getunits per_property = new Getunits();
    System.out.println(per_property.getOccupiedUnits("Thornville_rooms",817)    ); ;
}

}
