package model.database.CRUD;

import model.database.ConnectionAccess;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.*;

public class Property_Status extends ConnectionAccess {
    private String property_name;
    private Integer tenant;


    private static final Properties dbProps = new Properties();

    static {
        try (InputStream input = Tenants_rent_day.class
                .getClassLoader()
                .getResourceAsStream("db.properties")) {

            if (input == null) {
                throw new IOException("db.properties not found in resources");
            }
            dbProps.load(input);

        } catch (IOException e) {
            throw new RuntimeException("Failed to load db.properties", e);
        }
    }


    // Create a NEW DB connection every time
    private Connection newConnection() throws SQLException {
        String url = dbProps.getProperty("db.url");
        String user = dbProps.getProperty("db.user");
        String password = dbProps.getProperty("db.password");
        return DriverManager.getConnection(url, user, password);
    }

    public Property_Status(){
        this.property_name = null;
        this.tenant = null;
    }
    public void setProperty_name(String property_name) {
        this.property_name = property_name ;
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
        try(PreparedStatement pstm = newConnection().prepareStatement(propertyinfo)){
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

        try (PreparedStatement pstmt = newConnection().prepareStatement(ery)) {
            pstmt.setInt(1, tenantID);
            try (ResultSet result = pstmt.executeQuery()) {
                if (result.next()) {
                    rent_and_debt.add(String.valueOf(result.getInt("rent_payment_day")));
                     rent_and_debt.add(String.valueOf(result.getDate("overdue_date"))) ;

                    return extract_date.tenant_current_date(rent_and_debt);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error fetching user_information :" + e.getMessage());
           throw new RuntimeException(e.getMessage());
        }

        return rent_and_debt;
    }


    public void main(String[] args) throws SQLException {
        Property_Status pocket_it = new Property_Status();
        System.out.println(pocket_it.Finances(772));

    }
}