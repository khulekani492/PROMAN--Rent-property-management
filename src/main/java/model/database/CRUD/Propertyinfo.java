package model.database.CRUD;

import model.database.ConnectionAccess;

import java.sql.*;
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


    public String rent_payment(Integer tenantID) {
        String ery = """
        SELECT rent_payment_day\s
        FROM tenants_information\s
        WHERE tenant_user_id = ?;
   \s""";

        String rent = null;

        try (PreparedStatement pstmt = this.connection.prepareStatement(ery)) {
            pstmt.setInt(1, tenantID);
            try (ResultSet result = pstmt.executeQuery()) {
                if (result.next()) {
                    rent = result.getString("rent_payment_day");
                }
            }
        } catch (SQLException e) {
            if ("ERROR: prepared statement \"S_1\" already exists".equals(e.getMessage())){
                System.out.println(e.getMessage() + " dssd");
                // Handle the specific error: Deallocate the existing statement
                try (Statement stmt = connection.createStatement()) {
                    stmt.execute("DEALLOCATE S_1");
                } catch (SQLException deallocateEx) {
                    // Handle error during deallocation

                    deallocateEx.printStackTrace();
                }
                throw new RuntimeException("skip");


            }

        }

        return rent;
    }


public  HashMap<Integer,ArrayList<String>> fetchALL(String property_name){
    Propertyinfo property_list = new Propertyinfo();
    HashMap<Integer,ArrayList<String>> get_Unit_related = property_list.property_tenants(property_name);
    for (int i =1 ; i <= get_Unit_related.size() ; i++){
        ArrayList<String> property_per_unit = get_Unit_related.get(i);
        try {

            Integer AccessLast = Integer.valueOf(property_per_unit.get(3));
            // Access Zero for empty unit
            Integer first_one = Integer.valueOf(property_per_unit.get(0));
            String rent_day_tenant = null;
            try{
                 rent_day_tenant = property_list.rent_payment(AccessLast);
            } catch (RuntimeException e) {
                System.out.println("Dawg yami");
                throw new RuntimeException(e);
            }

            property_per_unit.add(rent_day_tenant);
            System.out.println(rent_day_tenant);
            //replace the key
            get_Unit_related.replace(first_one,property_per_unit);
            System.out.println("tenant_id " + AccessLast + "Choosen Day for rent " +  property_per_unit);
            System.out.println();
        } catch (NumberFormatException e){
            continue;
        }
    }

    return get_Unit_related;

} ;
    static void main(){
       Propertyinfo property_list = new Propertyinfo();

        HashMap<Integer,ArrayList<String>> get_Unit_related = property_list.property_tenants("Thornville_rooms");
        HashMap<Integer,ArrayList<String>> get_tenants = new HashMap<>();
        ///rent_day_tenant = Integer.valueOf(property_list.rent_payment(AccessLast));
        System.out.println(get_Unit_related);
        for (int i =1 ; i <= get_Unit_related.size() ; i++){
            ArrayList<String> property_per_unit = get_Unit_related.get(i);

            Integer AccessLast = null;
            //Skip unit with no tenants == null on index 3
            try {
                 AccessLast = Integer.valueOf(property_per_unit.get(3));
            } catch (NumberFormatException e){
                continue;
            }
                // Accessing the unit number which is Identical to the map key
                Integer first_one = Integer.valueOf(property_per_unit.get(0));
                String rent_day_tenant = null;
                try {
                    rent_day_tenant = property_list.rent_payment(AccessLast);
                } catch (Exception e) {
                    if("skip".equals(e.getMessage())){
                        continue;
                    }

                }

                property_per_unit.add(rent_day_tenant);
                System.out.println(rent_day_tenant);

                get_tenants.put(first_one,property_per_unit);
                System.out.println("tenant_id " + AccessLast + "Choosen Day for rent " +  property_per_unit);
                System.out.println();


            }
        System.out.println(get_tenants);
    }



}