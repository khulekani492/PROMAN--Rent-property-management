package model.database.CRUD;

import model.database.ConnectionAccess;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class Propertyinfo extends ConnectionAccess {
    private String property_name;
    private  String email;
    private Integer tenant;

    //How do I query all the property_name for a landlord
    //properties
    //
    public Propertyinfo(){
        this.property_name = null;
        this.tenant = null;
        this.email = null;
    }

    public void setProperty_name(String property_name) {
        this.property_name = property_name ;

    }

    public void set_tenantId(Integer tenant_) {
        this.tenant = tenant_ ;

    }
    public Integer getTenantId() {
        return tenant;
    }

    public String getProperty_name() {
        return this.property_name;

    }

    public Set<String> getLandlordProperties(int landlordId) {
        String query = "SELECT property_name FROM properties WHERE landlord_user_id = ?";
        Set<String> propertyNames = new HashSet<>(); // ✅ prevents duplicates

        try (PreparedStatement stmt = this.connection.prepareStatement(query)) {
            stmt.setInt(1, landlordId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    propertyNames.add(rs.getString("property_name"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return propertyNames;
    }

    //options
    public List<HashMap<String, String>> getLandlordProperty(String property_name) {
        String query = """
        SELECT property_unit, property_rent, occupation  FROM properties WHERE property_name = ?;
    """;

        List<HashMap<String, String>> allProperties = new ArrayList<>();

        try (PreparedStatement pstmt = this.connection.prepareStatement(query)) {
            pstmt.setString(1, property_name);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) { // ✅ Loop through all rows
                    HashMap<String, String> propertyData = new HashMap<>();
                    propertyData.put("property_unit", rs.getString("property_unit"));
                    propertyData.put("property_rent", rs.getString("property_rent"));
                    propertyData.put("occupation", rs.getString("occupation"));
                    allProperties.add(propertyData); // ✅ Add each property map to list
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return allProperties; // ✅ Return all landlord properties
    }

    public Integer tenant_payment_day(Integer tenantId) {
        String reference_key = """
        SELECT rent_payment_day FROM tenant_information WHERE tenant_user_id = ?;
    """;
        try (PreparedStatement pstmt = this.connection.prepareStatement(reference_key)) {
            pstmt.setInt(1, this.getTenantId());
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("rent_payment_day");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null; // if not found

    }




}
