package model.database;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

/**
 * Tenant class is responsible for inserting tenant information into the database. <br>
 * The {@code propertyId} acts as a foreign key that links the tenant to the landlord’s property. <br>
 * <p>
 * The constructor takes in a database {@link java.sql.Connection} along with tenant details,
 * and inserts the data into the {@code tenants} table .
 */

public class Tenant extends  connectionAcess implements  Property{
    private final Integer propertyId;
    private final String name;
    private final String moveInDate ;
    private final String moveOut;
    private final String employment;
    private final String cell_number;
    private final Integer pay_day;
    private final Integer   room_number;
    private final String  room_price;
    private final Integer debt;
    private final String kin_name;
    private final String kin_number;


    public Tenant(Integer propertyId, String name, String moveInDate,String employment,String cell_number,
                  Integer pay_day, Integer room_number,String room_price,String kin_name,String kin_number) throws SQLException {
        super();
        this.name = name;
        this.moveInDate = moveInDate;
        this.moveOut = null;
        this.employment = employment;
        this.cell_number = cell_number;
        this.pay_day = pay_day;
        this.room_number = room_number;
        this.room_price = room_price;
        this.debt = 0;
        this.kin_name = kin_name;
        this.kin_number = kin_number;
        this.propertyId =  propertyId;
    }

    @Override
    public void insert_information() {
         String addtenantSql = """
                 INSERT OR IGNORE INTO tenants (propertyId,name,move_in,move_out,employment,cell_number,pay_day,room_number,Room_price,debt,kin_name,kin_number) 
                 VALUES (?,?,?,?,?,?,?,?,?,?,?,?)
                 """;
         try (PreparedStatement pstm = connection.prepareStatement(addtenantSql)){
             pstm.setInt(1,this.propertyId);
             pstm.setString(2,this.name);
             pstm.setString(3, this.moveInDate);
             pstm.setString(4, this.moveOut);
             pstm.setString(5,this.employment);
             pstm.setString(6,this.cell_number);
             pstm.setInt(7, this.pay_day);
             pstm.setInt(8,this.room_number);
             pstm.setString(9,this.room_price);
             pstm.setInt(10,this.debt);
             pstm.setString(11,this.kin_name);
             pstm.setString(12,this.kin_number);
             pstm.executeUpdate();

         }catch (SQLException e) {
             throw new RuntimeException();
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
