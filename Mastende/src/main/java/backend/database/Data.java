package backend.database;

import java.sql.*;

// TODO: updateTenant, tenant_row, create property owner schema
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

            String tenantsTable = """
                CREATE TABLE IF NOT EXISTS tenants (
                    id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
                    landlord_residence INTEGER NOT NULL,
                    name TEXT,
                    move_in DATE,
                    move_out DATE,
                    employment VARCHAR(30),
                    cell_number TEXT,
                    pay_day TEXT,
                    room_number INTEGER,
                    FOREIGN KEY (landlord_residence) REFERENCES residence(id)
                );
            """;

            String residenceTable = """
                CREATE TABLE IF NOT EXISTS residence (
                    id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
                    property_name TEXT UNIQUE NOT NULL,
                    number_of_rooms INTEGER NOT NULL,
                    rent INTEGER NOT NULL,
                    address TEXT,
                    contact TEXT
                );
            """;
            stmt.execute(tenantsTable);
            stmt.execute(residenceTable);
        }
    }

    @Override
    public void addProperty_info(String property_name, String number_of_rooms, String rent, String address, String contact) {
        String propertySQL = """
                INSERT INTO residence (property_name,number_of_rooms,rent,address,contact) VALUES (?,?,?,?,?)
              """;
        try (PreparedStatement pstmt = conn.prepareStatement(propertySQL)) {
            pstmt.setString(1, property_name);
            pstmt.setString(2, number_of_rooms);
            pstmt.setString(3, rent);
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

    public void addNewtenant(String landlord_residence,String name, String move_in, String move_out, String employment, String cell_number, String pay_day,int room_number) {
        String propertySQL = """
                INSERT INTO tenants (landlord_residence,name,move_in,move_out,employment,cell_number,pay_day,room_number) VALUES (?,?,?,?,?,?,?,?)
              """;
        try (PreparedStatement pstmt = conn.prepareStatement(propertySQL)) {
            pstmt.setString(1, landlord_residence);
            pstmt.setString(2, name);
            pstmt.setString(3, move_in);
            pstmt.setString(4, move_out);
            pstmt.setString(5, employment);
            pstmt.setString(6, cell_number);
            pstmt.setString(7,pay_day);
            pstmt.setInt(8,room_number);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Update room status for a tenant.
     */
    public void roomStatus(String name, int room_no, String move_in_date, String employment_status, String cellphone,String pay_day) {
        try (Statement stmt = conn.createStatement()) {
                stmt.execute("PRAGMA foreign_keys = ON;");
            String tenantName = "UPDATE tenants SET name = ? WHERE room_no = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(tenantName)) {
                pstmt.setString(1, name);
                pstmt.setInt(2, room_no);
                pstmt.executeUpdate();
            }

            String updateMoveIn = "UPDATE tenants SET move_in = ? WHERE room_no = ?";
            try (PreparedStatement pstm = conn.prepareStatement(updateMoveIn)) {
                pstm.setString(1, move_in_date);
                pstm.setInt(2, room_no);
                pstm.executeUpdate();
            }

            String employment = "UPDATE tenants SET employment = ? WHERE room_no = ?";
            try (PreparedStatement pstm = conn.prepareStatement(employment)) {
                pstm.setString(1, employment_status);
                pstm.setInt(2, room_no);
                pstm.executeUpdate();
            }
            String cellphoneNo = "UPDATE tenants SET cell_number = ? WHERE room_no = ?";
            try (PreparedStatement pstm = conn.prepareStatement(cellphoneNo)) {
                pstm.setString(1,  cellphone);
                pstm.setInt(2, room_no);
                pstm.executeUpdate();
            }
            String rent_date = "UPDATE tenants SET pay_day = ? WHERE room_no = ?";
            try (PreparedStatement pstm = conn.prepareStatement(rent_date)) {
                pstm.setString(1,  pay_day);
                pstm.setInt(2, room_no);
                pstm.executeUpdate();
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
            Data db = new Data("jdbc:sqlite:property.db");
            //db.numberofRooms(5);
//            db.roomStatus("Khule", 1, 20250101, "Employed");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
