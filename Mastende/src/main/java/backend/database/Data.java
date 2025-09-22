package backend.database;

import javax.naming.Context;
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
                             password INTEGER NOT NULL,
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
                               pay_day TEXT NOT NULL,
                               room_number INTEGER NOT NULL,
                               Room_price Text,
                               debt Text,
                               kin_name Text,
                               kin_number Text,
                               FOREIGN KEY (propertyId) REFERENCES property(id)
                           );
            """;

            String grossIncome = """
                   CREATE TABLE IF NOT EXISTS tenants (
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
    public Integer landlordId(String name) {
        String reference_key = """
        SELECT id FROM residence WHERE property_name = ?;
    """;
        try (PreparedStatement pstmt = conn.prepareStatement(reference_key)) {
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


    @Override
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

    public void setPropertyname(String name){
        this.name = name;

    }
    public  String getPropertyname(){
        return  name;
    }

    public int getMastedeid() {
        String mastedeUser = """
        SELECT id FROM MastedeUsers ORDER BY created_at DESC LIMIT 1;
        """;

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(mastedeUser)) {

            if (rs.next()) {
                return rs.getInt("id"); // return the integer id
            } else {
                return -1; // return -1 if no record exists
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public void addNewtenant(int landlord_id, String name, String move_in, String move_out,
                             String employment, String cell_number, String pay_day, int room_number,
                             String room_price,
                             String kin_name,
                             String kin_number) {
          // get the mastedeUsers last row id to reference it as the foreign key
         int mastedeUser = getMastedeid();

         //Set the debt to zero when a new tenant is first added
         int  debt = 0;

        String tenantSQL = """
            INSERT INTO tenants (landlord_id, name, move_in, move_out, employment, cell_number, pay_day, room_number,Room_price,kin_name,kin_number,mastedeUser)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?,?,?,?,?,?)
          """;
        try (PreparedStatement pstmt = conn.prepareStatement(tenantSQL)) {
            pstmt.setInt(1, landlord_id);       // now matches INTEGER foreign key
            pstmt.setString(2, name);
            pstmt.setString(3, move_in);
            pstmt.setString(4, move_out);
            pstmt.setString(5, employment);
            pstmt.setString(6, cell_number);
            pstmt.setString(7, pay_day);
            pstmt.setInt(8, room_number);
            pstmt.setString(9,room_price);
            pstmt.setInt(10,debt);
            pstmt.setString(11, kin_name);
            pstmt.setString(12,kin_number);
            pstmt.setInt(13,mastedeUser);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * In case the user submit the same information twice this method is supposed to check
     * if the row already exists
     */
    public boolean rowExist_InProperty(String property_name,int number_of_rooms, String address,String contact){

        //Get property_name
        //check if
        String checkRow = """
                SELECT 1 FROM residence WHERE property_name=? AND number_of_rooms=? AND address=? AND contact=? LIMIT 1""";
        try (PreparedStatement pstmt = conn.prepareStatement(checkRow)) {
            pstmt.setString(1, property_name);
            pstmt.setInt(2, number_of_rooms);
            pstmt.setString(3, address);
            pstmt.setString(4, contact);
            ResultSet rs = pstmt.executeQuery();
            boolean exists = rs.next();
            if (exists){
                return true;
            }else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
      return true;
    };


    /**
     * TODO create a boolean method that check if  the row already exist in tenants table
     * rowExist_InTenants()
     */



    //Edit button
    public void updatePropertyInfo(String new_information,String column ){
        //How to determine the type of data to be updated ---> api job it will pass in the new_information and column to be updates



    }

    public void roomStatus(String name,
                           int room_no,
                           String move_in_date,
                           String employment_status,
                           String cellphone,
                           String pay_day,
                           String room_price,
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
            pstmt.setString(7, kin_name);
            pstmt.setString(8, kin_number);
            pstmt.setInt(9, room_no);
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
            Data db = new Data("jdbc:sqlite:ThugCommon.db");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void numberofRooms(int i) {
    }
}
