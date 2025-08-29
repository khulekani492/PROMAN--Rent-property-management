package backend.database;

//TODO updateTenet, tenant_row create property owner schema
import java.sql.*;

public class Database {
    private static final String DB_URL = "jdbc:sqlite:rentalRooms.db";
    private  static  final Connection conn;

    static {
        try {
            conn = DriverManager.getConnection(DB_URL);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public void newTenants(int room_numbers){
        //step 1 query total room the owner has
        String queryTotalRooms = "SELECT number_of_rooms FROM owner_property";
        int total_num = Integer.parseInt(queryTotalRooms);

        String tenant_row = """
                    INSERT INTO tenants (name,move_in,move_out,employment) VALUES
                    (null,null,null,null)
                    """;

        //step 2 create the amount of rows in database according to room numbers
        for(int room = 0; room < total_num;room++){


        }
    }

    public static void main(String srgs[]) throws SQLException {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement())
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

            stmt.execute(schematenants);
            stmt.execute(schemaRoomstatus);


            String dummtest = """
                    INSERT INTO tenants (name,move_in,move_out,employment) VALUES
                    (null,null,null,null)
                    """;

            String sql = "UPDATE tenants SET name = ? WHERE room_no = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, "khule");
                pstmt.setInt(2, 5);
                pstmt.executeUpdate();
            } catch (Exception e) {
                e.printStackTrace();
            }
            for (int i = 0; i < 5; i++) {
                stmt.execute(dummtest);
            }





        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
