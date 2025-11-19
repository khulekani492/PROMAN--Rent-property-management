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

    //Gets property unit of property name for a specific landlord
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

//Query the entity table of a specific tenant and return rent
    public ArrayList<String> Finances(Integer tenantID) {
        String ery = """

      SELECT tenants_information.overdue_date, tenants_information.rent_payment_day
       FROM tenants_information inner join properties ON tenants_information.tenant_user_id = properties.tenant_user_id\s
       WHERE properties.tenant_user_id = ? ;
       \s
   \s""";

        ArrayList<String> rent_and_debt = new ArrayList<>();
        Get_date extract_date = new Get_date();

        try (PreparedStatement pstmt = this.connection.prepareStatement(ery)) {
            pstmt.setInt(1, tenantID);
            try (ResultSet result = pstmt.executeQuery()) {
                if (result.next()) {
                    rent_and_debt.add(String.valueOf(result.getInt("rent_payment_day")));
                     rent_and_debt.add(String.valueOf(result.getDate("overdue_date"))) ;

                    return extract_date.tenant_current_date(rent_and_debt);
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


            }

        }

        return rent_and_debt;
    }


    public void main(String[] args) throws SQLException {
        Propertyinfo pocket_it = new Propertyinfo();
        System.out.println(pocket_it.Finances(772));

    }
}