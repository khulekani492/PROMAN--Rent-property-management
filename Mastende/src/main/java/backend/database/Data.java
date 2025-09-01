package backend.database;

//TODO updateTenet, tenant_row create property owner schema
import java.sql.*;

public class Database implements Property {
    private final String dbUrl;

    public Database(String db_url) throws SQLException {
        this.dbUrl = db_url;
        copySchema();
    }

    private void copySchema() throws SQLException {
        try (Connection conn = DriverManager.getConnection(dbUrl);
             Statement stmt = conn.createStatement()) {
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS tenants (
                    room_no INTEGER PRIMARY KEY AUTOINCREMENT,
                    name TEXT,
                    move_in INTEGER,
                    move_out INTEGER,
                    employment TEXT
                )
            """);
        }
    }

    @Override
    public void numberofRooms(int room_numbers) throws SQLException {
        String sql = "INSERT INTO tenants (name, move_in, move_out, employment) VALUES (null, null, null, null)";

        try (Connection conn = DriverManager.getConnection(dbUrl);
             Statement stmt = conn.createStatement()) {

            conn.setAutoCommit(false);
            for (int i = 0; i < room_numbers; i++) {
                stmt.execute(sql);
            }
            conn.commit();

        } catch (SQLException e) {
            throw e; // rollback not needed because conn auto-closes
        }
    }

    public void roomStatus(String name, int room_no, int move_in_date, String employment_status) {
        try (Connection conn = DriverManager.getConnection(dbUrl)) {

            // enable foreign keys
            try (Statement stmt = conn.createStatement()) {
                stmt.execute("PRAGMA foreign_keys = ON;");
            }

            String tenant_name = "UPDATE tenants SET name = ? WHERE room_no = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(tenant_name)) {
                pstmt.setString(1, name);
                pstmt.setInt(2, room_no);
                pstmt.executeUpdate();
            }

            String updateMoveindate = "UPDATE tenants SET move_in = ? WHERE room_no = ?";
            try (PreparedStatement pstm = conn.prepareStatement(updateMoveindate)) {
                pstm.setInt(1, move_in_date);
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
}



public void getfirstrow(){
        String sql = """
                FR
                """;
    }
    public void copySchema(){
        try (Statement stmt = conn.createStatement())
        {
            stmt.execute("PRAGMA foreign_keys = ON;");
            String schematenants = """
                CREATE TABLE IF NOT EXISTS tenants (
                    room_no INTEGER PRIMARY KEY AUTOINCREMENT,
                    name TEXT ,
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

            String propertyschema = """
       CREATE TABLE IF NOT EXISTS residence (
        id INTEGER PRIMARY KEY AUTOINCREMENT,
        property_name TEXT NOT NULL,
        number_of_rooms INTEGER NOT NULL,
        rent INTEGER NOT NULL,
        address TEXT
    );
""";


            stmt.execute(schematenants);
            stmt.execute(schemaRoomstatus);
            stmt.execute(propertyschema);


            String dummtest = """
                    INSERT INTO tenants (name,move_in,move_out,employment) VALUES
                    (null,null,null,null)
                    """;

            String sql = "UPDATE tenants SET name = ? WHERE room_no = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, "khule");
                pstmt.setInt(2, 5);
//                pstmt.executeUpdate();
            } catch (Exception e) {
                e.printStackTrace();
            }
//            for (int i = 0; i < 5; i++) {
//                stmt.execute(dummtest);
//            }





        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    public static void main(String srgs[]) throws SQLException {

//        try (Connection conn = DriverManager.getConnection(DB_URL);
//             Statement stmt = conn.createStatement())
//        {
//            stmt.execute("PRAGMA foreign_keys = ON;");
//            String schematenants = """
//                CREATE TABLE IF NOT EXISTS tenants (
//                    room_no INTEGER PRIMARY KEY AUTOINCREMENT,
//                    name TEXT ,
//                    move_in DATE ,
//                    move_out DATE,
//                    employment VARCHAR(30)
//                );
//                """;
//            String schemaRoomstatus = """
//                CREATE TABLE IF NOT EXISTS occupied_rooms (
//                    id INTEGER PRIMARY KEY AUTOINCREMENT,
//                    rentedRoom INT NOT NULL,
//                    paymentDate DATE not null,
//                    debt INT  null,
//                    period INT not null,
//                    FOREIGN KEY(rentedRoom) REFERENCES tenants(room_no)
//                );
//                """;
//
//            String propertyschema = """
//       CREATE TABLE IF NOT EXISTS residence (
//        id INTEGER PRIMARY KEY AUTOINCREMENT,
//        property_name TEXT NOT NULL,
//        number_of_rooms INTEGER NOT NULL,
//        rent INTEGER NOT NULL,
//        address TEXT
//    );
//""";
//
//
//            stmt.execute(schematenants);
//            stmt.execute(schemaRoomstatus);
//            stmt.execute(propertyschema);
//
//
//            String dummtest = """
//                    INSERT INTO tenants (name,move_in,move_out,employment) VALUES
//                    (null,null,null,null)
//                    """;
//
//            String sql = "UPDATE tenants SET name = ? WHERE room_no = ?";
//            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
//                pstmt.setString(1, "khule");
//                pstmt.setInt(2, 5);
//                pstmt.executeUpdate();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            for (int i = 0; i < 5; i++) {
//                stmt.execute(dummtest);
//            }
//
//
//
//
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }

    }
}
