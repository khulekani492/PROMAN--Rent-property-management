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

    public HashMap<Integer,ArrayList<String>> property_tenants(String property_name,Integer landlord){
        ArrayList<String> property_status = new ArrayList<>();
        HashMap<Integer,ArrayList<String> > property_tenants = new HashMap<>();
        String propertyinfo = """
                SELECT property_unit,property_rent,occupation,tenant_user_id FROM properties WHERE property_name = ? AND landlord_user_id= ?""";
        try(PreparedStatement pstm = this.connection.prepareStatement(propertyinfo)){
            pstm.setString(1,property_name);
            pstm.setInt(2,landlord);
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
                    deallocateEx.printStackTrace();
                }
                throw new RuntimeException("skip");


            } else if ("ERROR: prepared statement \"s_1\" already exists".equals(e.getMessage())) {

                try (Statement stmt = connection.createStatement()) {
                    stmt.execute("DEALLOCATE s_1");
                } catch (SQLException deallocateEx) {
                    deallocateEx.printStackTrace();
                }
                throw new RuntimeException("skip");
            }

        }

        return rent;
    }




}