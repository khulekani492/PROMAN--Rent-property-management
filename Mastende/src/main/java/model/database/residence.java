package model.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class residence extends connectionAcess implements  Property{
    private final String property_name;
    private final int number_of_rooms;
    private final int rent;
    private final String address;
    private final String contact;
    private final int landlordId;

    public residence(Connection connect, String property_name, int number_of_rooms,int rent,String address, String contact,int landlordId) {
        super(connect);
        this.property_name = property_name;
        this.number_of_rooms = number_of_rooms;
        this.rent = rent;
        this.address = address;
        this.contact = contact;
        this.landlordId = landlordId;

    }

    @Override
    public void insert_information() {
        String propertySQL = """
            INSERT INTO property (property_name, number_of_rooms, rent, address, contact,UserId)
            VALUES (?, ?, ?, ?, ?,?)
          """;
        try (PreparedStatement pstmt = connection.prepareStatement(propertySQL)) {
            pstmt.setString(1, this.property_name);
            pstmt.setInt(2, this.number_of_rooms);
            pstmt.setInt(3, this.rent);
            pstmt.setString(4, this.address);
            pstmt.setString(5, this.contact);
            pstmt.setInt(6,this.landlordId);
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
     * the logic of communicating with the database through the residence data access object
     * to update the table information.
     */

    public void addProperty_info(String property_name, int number_of_rooms, int rent, String address, String contact) {
        String propertySQL = """
            INSERT INTO residence (property_name, number_of_rooms, rent, address, contact)
            VALUES (?, ?, ?, ?, ?)
          """;
        try (PreparedStatement pstmt = connection.prepareStatement(propertySQL)) {
            pstmt.setString(1, property_name);
            pstmt.setInt(2, number_of_rooms);
            pstmt.setInt(3, rent);
            pstmt.setString(4, address);
            pstmt.setString(5, contact);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}

