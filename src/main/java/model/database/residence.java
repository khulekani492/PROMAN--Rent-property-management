package model.database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * residence class is responsible for inserting property information into the database. <br>
 * The {@code UserId} acts as a foreign key that links the property to it landlord . <br>
 * <p>
 * The constructor takes in a database {@link java.sql.Connection} along with tenant details,
 * and inserts the data into the {@code property} table.
 */

public class residence extends ConnectionAccess implements  Property {
    private Integer property_unit;
    private Integer property_rent;
    private String occupation;
    private Integer landlordId;
    private Integer tenantId;
    private String property_name;
    private String property_address;

    public residence(Integer property_unit, Integer property_rent,
                     String occupation,
                     String property_name,
                     String property_address
    ) throws SQLException {
        super();
        this.property_unit = property_unit;
        this.property_rent = property_rent;
        this.occupation = occupation;
        this.landlordId = null;
        this.tenantId = null;
        this.property_name = property_name;
        this.property_address = property_address;

    }

    public residence() throws SQLException {
        super();
        this.property_unit = null;
        this.property_rent = null;
        this.occupation = "";
        this.landlordId = null;
        this.tenantId = null;
        this.property_name = null;

    }

    public residence(Integer propertyUnit, Integer rent, String occupation, String propertyName) {
        this.property_unit = propertyUnit;
        this.property_rent = rent;
        this.occupation = occupation;
        this.property_name = propertyName;
    }

    public void setlandlord(Integer landlordId) {
        this.landlordId = landlordId;
    }

    public Integer getLandlordId() {
        return this.landlordId;
    }

    public void setTenantId(Integer tenantId) {
        this.tenantId = tenantId;
    }
    public Integer getTenantId() {
        return this.tenantId;
    }

    public void setProperty_Name(String propertyName) {
           this.property_name = propertyName;
    }

    public void setProperty_address(String address) {
           this.property_address = address;
    }


    public  void setProperty_unit(Integer unit){
        this.property_unit = unit;
    }

    public Integer queryDebt() {
        String sql = """
                SELECT debt  From properties WHERE landlord_user_id = ?;
                """;
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, this.landlordId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {

                    return rs.getInt("debt");
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
      return  null;
    }

    //prevents adding the tenants to the same  unit based on property_name  and unit number
    public  void  Insert_tenatId() throws SQLException {
        String tenant = """
                UPDATE properties SET tenant_user_id = ? WHERE landlord_user_id = ? and property_unit= ? and  property_name = ?
                """;
        try (PreparedStatement pstm = this.connection.prepareStatement(tenant)){
            pstm.setInt(1,this.tenantId);
            pstm.setInt(2,this.landlordId);
            pstm.setInt(3,this.property_unit);
            pstm.setString(4,this.property_name);
            pstm.executeUpdate();
        } catch (SQLException e) {
            if ("23505".equals(e.getSQLState())) {
                // rethrow to be caught in your route handler
                throw new SQLException("tenant exists", e);
            } else {
                throw new SQLException("Insert failed: " + e.getMessage(), e);
            }
        }}

    @Override
    public void insert_information() throws SQLException {
        String propertySQL = """
    INSERT INTO properties (property_unit,property_rent,occupation,landlord_user_id,property_name,property_address)
    VALUES (?, ?, ?, ?,?,?)
""";
        try (PreparedStatement pstmt = connection.prepareStatement(propertySQL)) {
            pstmt.setInt(1, this.property_unit);
            pstmt.setInt(2, this.property_rent);
            pstmt.setString(3, this.occupation);
            pstmt.setInt(4,this.landlordId);
            pstmt.setString(5,this.property_name);
            pstmt.setString(6,this.property_address);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            if ("23505".equals(e.getSQLState())) {
                // rethrow to be caught in your route handle
                throw new SQLException("unit number already taken by property", e);
            } else {
                throw new SQLException("Insert failed: " + e.getMessage(), e);
            }
        }

    }
//    public void update_debt(){
//        String sql = """
//        UPDATE properties
//        SET debt = ?
//        WHERE landlord_user_id = ?;
//    """;
//
//        try (PreparedStatement pstmt = connection.prepareStatement(sql )) {
//            pstmt.setInt(1, this.debt);
//            pstmt.setInt(2, this.landlordId);
//            pstmt.executeUpdate();
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//
//    }

//    public void update_payDay(){
//        String sql = """
//        UPDATE properties
//        SET pay_day = ?
//        WHERE landlord_user_id = ?;
//    """;
//
//        try (PreparedStatement pstmt = connection.prepareStatement(sql )) {
//            pstmt.setInt(1, this.pay_day);
//            pstmt.setInt(2, this.landlordId);
//            pstmt.executeUpdate();
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//
//    }

    public Integer querypayDay() {

        String sql = """
        SELECT pay_day FROM properties WHERE landlord_user_id = ? ;
        """;


        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, this.landlordId);
         //   pstmt.setInt(2,unit);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("pay_day");
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return  null;
    }

    @Override
    public Integer UniqueID() {
        String reference_key = """
        SELECT id FROM properties WHERE landlord_user_id = ?;
    """;
        try (PreparedStatement pstmt = connection.prepareStatement(reference_key)) {
           pstmt.setInt(1, this.landlordId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null; // if not found
    }

    /**
     * Updates landlord property information. <br>
     * <p>
     * This feature depends on the view layer for input, while the controller handles
     * the logic of communicating with the database through the property table data access object
     * to update the table information.
     */

}

