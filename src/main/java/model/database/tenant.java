package model.database;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Tenant class is responsible for inserting tenant information into the database. <br>
 * The {@code propertyId} acts as a foreign key that links the tenant to the landlord’s property. <br>
 * <p>
 * The constructor takes in a database {@link java.sql.Connection} along with tenant details,
 * and inserts the data into the {@code tenants} table .
 */

public class tenant extends ConnectionAccess implements  Property{
    private  Integer tenantId;
    private final Date moveInDate ;
    private final Date moveOut;
    private final String employment;
    private final String kin_name;
    private final String kin_number;
    private final String rent_payment_day;
    private final Integer debt;



    public tenant(Date moveInDate, String employment,String kin_name, String kin_number,String rentPaymentDay,  Integer debt) throws SQLException {
        super();
        this.tenantId = null;
        this.moveInDate = moveInDate;
        this.moveOut = null;
        this.employment = employment;
        this.kin_name = kin_name;
        this.kin_number = kin_number;
        this.rent_payment_day = rentPaymentDay;
        this.debt = debt;

    }


    public void setTenantId(Integer tenantId) {
        this.tenantId = tenantId;
    }

    @Override
    public void insert_information() {
        String addTenantSql = """
        INSERT INTO tenants_information (
            move_in,tenant_user_id ,employment,kin_name,kin_number,rent_payment_day,debt)
        VALUES (?,?,?,?,?,?,?)
    """;

        try (PreparedStatement pstm = connection.prepareStatement(addTenantSql)) {
            pstm.setDate(1, this.moveInDate); // java.sql.Date // null-safe
            pstm.setInt(2,this.tenantId);
            pstm.setString(3,this.employment);
            pstm.setString(4, this.kin_name);
            pstm.setString(5,this.kin_number);
            pstm.setString(6,this.rent_payment_day);
            pstm.setInt(7,this.debt);
            pstm.executeUpdate();
  //          roomHist addToHistory = new roomHist(this.propertyId,this.room_number,this.name,this.cell_number,this.moveInDate,this.moveOut);
            //addToHistory.insert_information();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Tenant insertion failed", e);
        }
    }

    @Override
    public Integer UniqueID() {
        return 0;
    }

    /**
     * Updates landlord tenant or room  information. <br>
     * <p>
     * This feature depends on the view layer for input, while the controller handles
     * the logic of communicating with the database through the residence data access object
     * to update the table information.
     */
    public void updateRoomStatus(String name,
                           int room_no,
                           String move_in_date,
                           String employment_status,
                           String cellphone,
                           int pay_day,
                           String room_price,
                           int debt,
                           String kin_name,
                           String kin_number) {
        String sql = """
        UPDATE tenants
        SET name = ?,
            move_in = ?,
            employment = ?,
            cell_number = ?,
            pay_day = ?,
            room_price = ?,
            debt = ?,
            kin_name = ?,
            kin_number = ?
        WHERE room_number = ?
    """;

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2, move_in_date);
            pstmt.setString(3, employment_status);
            pstmt.setString(4, cellphone);
            pstmt.setInt(5, pay_day);
            pstmt.setString(6, room_price);
            pstmt.setInt(7,debt);
            pstmt.setString(9, kin_name);
            pstmt.setString(9, kin_number);
            pstmt.setInt(10, room_no);
            pstmt.executeUpdate();

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected == 0) {
                System.out.println("No tenant found with room number: " + room_no);
            } else {
                System.out.println("Tenant updated successfully.");
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }

    }



}
