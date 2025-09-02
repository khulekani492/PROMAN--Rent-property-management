package backend.database;

import java.sql.*;

// TODO: updateTenant, tenant_row, create property owner schema
public class Data implements Property {
    private final Connection conn;

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
                    room_no INTEGER PRIMARY KEY AUTOINCREMENT,
                    name TEXT,
                    move_in DATE,
                    move_out DATE,
                    employment VARCHAR(30)
                );
            """;

            String occupiedRoomsTable = """
                CREATE TABLE IF NOT EXISTS occupied_rooms (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    rentedRoom INT NOT NULL,
                    paymentDate DATE NOT NULL,
                    debt INT NULL,
                    period INT NOT NULL,
                    FOREIGN KEY(rentedRoom) REFERENCES tenants(room_no)
                );
            """;

            String residenceTable = """
                CREATE TABLE IF NOT EXISTS residence (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    property_name TEXT NOT NULL,
                    number_of_rooms INTEGER NOT NULL,
                    rent INTEGER NOT NULL,
                    address TEXT
                );
            """;

            stmt.execute(tenantsTable);
            stmt.execute(occupiedRoomsTable);
            stmt.execute(residenceTable);
        }
    }

    @Override
    public void numberofRooms(int room_numbers) throws SQLException {
        String sql = "INSERT INTO tenants (name, move_in, move_out, employment) VALUES (null, null, null, null)";

        try (Statement stmt = conn.createStatement()) {

            conn.setAutoCommit(false);
            for (int i = 0; i < room_numbers; i++) {
                stmt.execute(sql);
            }
            conn.commit();

        } catch (SQLException e) {
            throw e; // rollback not needed, connection closes anyway
        }
    }


    /**
     * Update room status for a tenant.
     */
    public void roomStatus(String name, int room_no, String move_in_date, String employment_status) {
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

        } catch (SQLException e) {
            e.printStackTrace();
        }

            }

    /**
     * Simple test runner.
     */
    public static void main(String[] args) {
        try {
            Data db = new Data("jdbc:sqlite:rentalRooms.db");
            db.numberofRooms(5);
//            db.roomStatus("Khule", 1, 20250101, "Employed");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
