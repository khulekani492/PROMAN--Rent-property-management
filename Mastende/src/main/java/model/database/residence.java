package model.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class residence extends connectionAcess implements  Property{
    private String property_name;
    private int number_of_rooms;
    private int rent;
    private String address;
    private String contact;

    public residence(Connection connect, String property_name, int number_of_rooms,int rent,String address, String contact) {
        super(connect);
        this.property_name = property_name;
        this.number_of_rooms = number_of_rooms;
        this.rent = rent;
        this.address = address;
        this.contact = contact;

    }

    @Override
    public void insert_information() {
        String propertySQL = """
            INSERT INTO property (property_name, number_of_rooms, rent, address, contact,UserId)
            VALUES (?, ?, ?, ?, ?)
          """;
        try (PreparedStatement pstmt = connector.prepareStatement(propertySQL)) {
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

    @Override
    public Integer UniqueID(String name) {
        String reference_key = """
        SELECT id FROM property WHERE userId = ?;
    """;
        try (PreparedStatement pstmt = connector.prepareStatement(reference_key)) {
            pstmt.setString(1, name);
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
}

