package backend.database;


import java.sql.*;
public class Database {
    private static final String DB_URL = "jdbc:sqlite:rentalRooms.db";

    public static void main(String srgs[]) throws SQLException {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement())
        {
            stmt.execute("PRAGMA foreign_keys = ON;");
            String schematenants = """
                CREATE TABLE IF NOT EXISTS tenants (
                    room_no INTEGER PRIMARY KEY AUTOINCREMENT,
                    name TEXT UNIQUE,
                    move_in DATE ,
                    move_out DATE,
                    employment VARCHAR(30)
                );
                """;
            String schemaRoomstatus = """
                CREATE TABLE IF NOT EXISTS occupied_rooms (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    rentedRoom INT NOT NULL,
                    paymentDate DATE not null,
                    debt INT  null,
                    period INT not null,
                    FOREIGN KEY(rentedRoom) REFERENCES tenants(room_no)
                );
                """;
            stmt.execute(schematenants);
            stmt.execute(schemaRoomstatus);

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
