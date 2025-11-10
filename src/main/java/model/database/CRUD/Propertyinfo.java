package model.database.CRUD;

import model.database.ConnectionAccess;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class Propertyinfo extends ConnectionAccess {
    private String property_name;
    private Integer tenant;

    public Propertyinfo(){
        this.property_name = null;
        this.tenant = null;
    }
    public void setProperty_name(String property_name) {
        this.property_name = property_name ;

    }

    public void set_tenantId(Integer tenant_) {
        this.tenant = tenant_ ;

    }
    public Integer get_TenantId() {
        return tenant;
    }

    public String getProperty_name() {
        return this.property_name;

    }

    public HashMap<Integer,ArrayList<String>> property_tenants(String property_name){
        ArrayList<String> property_status = new ArrayList<>();
        HashMap<Integer,ArrayList<String> > property_tenants = new HashMap<>();
        String propertyinfo = """
                SELECT property_unit,property_rent,occupation,tenant_user_id FROM properties WHERE property_name = ?""";
        try(PreparedStatement pstm = this.connection.prepareStatement(propertyinfo)){
            pstm.setString(1,property_name);
            ResultSet result = pstm.executeQuery();
            Integer unit_number = null;
            while (result.next()){
                unit_number = result.getInt("property_unit") ;
                property_status.add(String.valueOf(unit_number));
                property_status.add(result.getString("property_rent"));
                property_status.add(result.getString("occupation"));
                property_status.add(result.getString("tenant_user_id"));
                property_tenants.put(unit_number,property_status);
                property_status = new ArrayList<>();

            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return property_tenants;
    }


    public String rent_payment(Integer  tenantID){
        String getRent = """
                SELECT rent_payment_day FROM tenants_information WHERE tenant_user_id= ?""";
        try(PreparedStatement pstm = this.connection.prepareStatement(getRent)){
            pstm.setInt(1,tenantID);
            ResultSet result = pstm.executeQuery();
            String rent = null;
            if(result.next()){
                rent = result.getString("rent_payment_day");
            }
            return rent;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }



    static void main(){
       Propertyinfo property_list = new Propertyinfo();
//        System.out.println( as.rent_payment(807));
//        System.out.println(as.property_tenants());
//        ArrayList<String> modify = as.property_tenants().get(1);
//        modify.add(as.rent_payment(778));
//        System.out.println("List Modified : " + modify);
//        System.out.println(as.property_tenants());
        System.out.println(property_list.property_tenants("Thornville_rooms"));
         HashMap<Integer,ArrayList<String>> get_Unit_related = property_list.property_tenants("Thornville_rooms");

        for (int i =1 ; i <= get_Unit_related.size(); i++){
//            System.out.println(get_Unit_related.get(i));
            ArrayList<String> property_per_unit = get_Unit_related.get(i);
            try {
                Integer AccessLast = Integer.valueOf(property_per_unit.get(3));
                System.out.println("The Last index : " + AccessLast);
            } catch (NumberFormatException e){
                continue;
            }


//
//            Integer tenant_id = Integer.valueOf(get_Unit_related.get(3));
//            Integer rent_day_tenant = Integer.valueOf(property_list.rent_payment(tenant_id));
//            get_Unit_related.addLast(String.valueOf( rent_day_tenant));
        }
    }

}
