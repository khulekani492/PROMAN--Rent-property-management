package model.database.CRUD;

import java.util.ArrayList;
import java.util.HashMap;

public class Getunits {
    public HashMap<Integer,ArrayList<String>> getOccupiedUnits (String property_name,Integer landlordId){

        HashMap<Integer,ArrayList<String>> get_tenants = new HashMap<>();
        return get_tenants;
    }
static void main(){
        Property_Status per_property = new Property_Status();
       // per_property.getOccupiedUnits()
    System.out.println(per_property.property_tenants("Thornville_rooms",771)    ); ;
}

}
