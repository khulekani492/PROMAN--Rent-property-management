package model.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.IntBinaryOperator;

/**
 * residence class is responsible for inserting property information into the database. <br>
 * The {@code UserId} acts as a foreign key that links the property to it landlord . <br>
 * <p>
 * The constructor takes in a database {@link java.sql.Connection} along with tenant details,
 * and inserts the data into the {@code property} table.
 */

public class residence extends connectionAcess implements  Property{
    private final Integer property_unit;
    private final Integer property_rent;
    private final  String occupation;
    private final Integer debt;
    private  Integer landlordId;
    private  Integer tenantId;
    private final  Integer pay_day;
    private final Integer FromDay;
    private final Integer LastDay;

    public residence(Integer property_name, Integer property_rent, String occupation, Integer FromDay , Integer Lastday, Integer pay_day) throws SQLException {
        super();
        this.property_unit = property_name;
        this.property_rent = property_rent;
        this.occupation = occupation;
        this.landlordId = null;
        this.tenantId = null;
        this.debt = null;
        this.pay_day = pay_day;
        this.FromDay = FromDay;
        this.LastDay = Lastday;

    }

    public void setlandlord(Integer landlordId){
        this.landlordId = landlordId;

    };
    public Integer getLandlordId(){
      return this.landlordId;
    };

    @Override
    public void insert_information() {
        String propertySQL = """
    INSERT INTO properties (property_unit, property_rent, occupation, debt,pay_day,tenant_user_id,landlord_user_id,from_day,last_day)
    VALUES (?, ?, ?, ?, ?, ?, ?,?,?)
""";

        try (PreparedStatement pstmt = connection.prepareStatement(propertySQL)) {
            pstmt.setInt(1, this.property_unit);
            pstmt.setInt(2, this.property_rent);
            pstmt.setString(3, this.occupation);
            pstmt.setInt(4, this.debt);
            pstmt.setInt(5, this.pay_day);
            pstmt.setInt(6,this.landlordId);
            pstmt.setInt(7,this.tenantId);
            pstmt.setInt(8,this.FromDay);
            pstmt.setInt(9,this.LastDay);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public Integer UniqueID() {
        String reference_key = """
        SELECT id FROM property WHERE userId = ?;
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
    public void autocommitfalse() throws SQLException {
        this.connection.setAutoCommit(false);

    };

    public void reverse() throws SQLException {
        this.connection.setAutoCommit(false);
        this.connection.rollback();
    };
    public void assignProperty(Integer propertyid,Integer userId) {
        String propertySQL = """
                  UPDATE Users SET propertyId = ? WHERE id = ?
                """;
        try (PreparedStatement pstmt = connection.prepareStatement(propertySQL)) {
            pstmt.setInt(1, propertyid);
            pstmt.setInt(2, userId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

