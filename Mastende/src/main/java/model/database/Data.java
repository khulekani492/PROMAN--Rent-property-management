package model.database;

import java.sql.*;

// TODAY: SESSION --> PREVENTING THE DATABASE FROM SAVING THE SAME DATA ADDING
public class Data implements Property {
    private final Connection conn;
    private String name;

    public Data(String db_url) throws SQLException {
        this.conn = DriverManager.getConnection(db_url);
        copySchema();
    }

    /**
     * Create database schema (tables).
     */
    private void copySchema() throws SQLException {
        try (Statement stmt = conn.createStatement()) {

            stmt.execute("PRAGMA foreign_keys = ON;");

            String mastedeUsers = """
                 CREATE TABLE IF NOT EXISTS Users (
                             id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
                             user_name TEXT NOT NULL,
                             user_email TEXT,
                             password TEXT NOT NULL,
                             propertyId INTEGER NULL,
                             FOREIGN KEY (propertyId) REFERENCES property(id)
                         );
            """;

            String residenceTable = """
                 CREATE TABLE IF NOT EXISTS property (
                             id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
                             property_name TEXT NOT NULL,
                             number_of_rooms INTEGER NOT NULL,
                             rent INTEGER NOT NULL,
                             address TEXT,
                             contact TEXT,
                             UserId INTEGER,
                             FOREIGN KEY (UserId) REFERENCES Users(id)
                             );
            """;


            String tenantsTable = """
                   CREATE TABLE IF NOT EXISTS tenants (
                               id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
                               propertyId INTEGER NOT NULL,
                               name TEXT,
                               move_in DATE NOT NULL,
                               move_out DATE,
                               employment TEXT NOT NULL,
                               cell_number TEXT NOT NULL,
                               pay_day DATE NOT NULL,
                               room_number INTEGER NOT NULL,
                               Room_price INTEGER,
                               debt INTEGER,
                               kin_name Text,
                               kin_number Text,
                               FOREIGN KEY (propertyId) REFERENCES property(id)
                           );
            """;

            String grossIncome = """
                   CREATE TABLE IF NOT EXISTS gross_income (
                               id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
                               propertyId INTEGER NOT NULL,
                               month_name TEXT,
                               Profit TEXT,
                               Loss TEXT,
                               FOREIGN KEY (propertyId) REFERENCES Users(id)
                           );
            """;
            stmt.execute(mastedeUsers);
            stmt.execute(tenantsTable);
            stmt.execute(residenceTable);
            stmt.execute(grossIncome);
        }
    }





    public void addProperty_info(String property_name, int number_of_rooms, int rent, String address, String contact) {
        String propertySQL = """
            INSERT INTO residence (property_name, number_of_rooms, rent, address, contact)
            VALUES (?, ?, ?, ?, ?)
          """;
        try (PreparedStatement pstmt = conn.prepareStatement(propertySQL)) {
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



    public void roomStatus(String name,
                           int room_no,
                           String move_in_date,
                           String employment_status,
                           String cellphone,
                           String pay_day,
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

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2, move_in_date);
            pstmt.setString(3, employment_status);
            pstmt.setString(4, cellphone);
            pstmt.setString(5, pay_day);
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

    /**
     * Simple test runner.
     */
    public static void main(String[] args) {
        try {
            Data db = new Data("jdbc:sqlite:Thug.db");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void numberofRooms(int i) {
    }

    @Override
    public void insert_information() {
        System.out.println("Nigg");
    }

    @Override
    public Integer UniqueID() {
        return 0;
    }


}
