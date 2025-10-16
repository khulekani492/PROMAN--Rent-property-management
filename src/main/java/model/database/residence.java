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

public class residence extends connectionAcess implements  Property {
    private Integer property_unit;
    private Integer property_rent;
    private String occupation;
    private Integer debt;
    private Integer landlordId;
    private Integer tenantId;
    private Integer pay_day;
    private Integer FromDay;
    private Integer LastDay;

    public residence(Integer property_name, Integer property_rent, String occupation) throws SQLException {
        super();
        this.property_unit = property_name;
        this.property_rent = property_rent;
        this.occupation = occupation;
        this.landlordId = null;
        this.tenantId = null;
        this.debt = null;
        this.pay_day = null;
        this.FromDay = null;
        this.LastDay = null;
    }

    public residence() throws SQLException {
        super();
        this.property_unit = null;
        this.property_rent = null;
        this.occupation = "";
        this.landlordId = null;
        this.tenantId = null;
        this.debt = null;
        this.pay_day = null;
        this.FromDay = null;
        this.LastDay = null;
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

    public void setDebt(Integer debt) {
        this.debt = debt;
    }

    public void setRentDay(Integer pay_day){
        this.pay_day = pay_day;
    }
    public Integer getRentDay(){
        return this.pay_day;
    }

    public void setRangePayDays(Integer fromDay,Integer lastDay) {
        this.FromDay = fromDay;
        this.LastDay = lastDay;
    }

    public Integer getFromDay() {
        return FromDay;
    }

    public Integer getLastDay() {
        return LastDay;
    }

    public Integer getDebt() {
        return this.debt;
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
    public  void  Insert_tenatId(){
        String tenant = """
                UPDATE properties SET tenant_user_id = ? WHERE landlord_user_id = ?
                """;
        try (PreparedStatement pstm = this.connection.prepareStatement(tenant)){
            pstm.setInt(1,this.tenantId);
            pstm.setInt(2,landlordId);
            pstm.executeUpdate();
        }catch (SQLException e){
            throw new RuntimeException("Database update failed", e);
        }
    }
    @Override
    public void insert_information() {
        String propertySQL = """
    INSERT INTO properties (property_unit, property_rent, occupation,landlord_user_id)
    VALUES (?, ?, ?, ? )
""";

        try (PreparedStatement pstmt = connection.prepareStatement(propertySQL)) {
            pstmt.setInt(1, this.property_unit);
            pstmt.setInt(2, this.property_rent);
            pstmt.setString(3, this.occupation);
            pstmt.setInt(4,this.landlordId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    public void update_debt(){
        String sql = """
        UPDATE properties
        SET debt = ?
        WHERE landlord_user_id = ?;
    """;

        try (PreparedStatement pstmt = connection.prepareStatement(sql )) {
            pstmt.setInt(1, this.debt);
            pstmt.setInt(2, this.landlordId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public void update_payDay(){
        String sql = """
        UPDATE properties
        SET pay_day = ?
        WHERE landlord_user_id = ?;
    """;

        try (PreparedStatement pstmt = connection.prepareStatement(sql )) {
            pstmt.setInt(1, this.pay_day);
            pstmt.setInt(2, this.landlordId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

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

