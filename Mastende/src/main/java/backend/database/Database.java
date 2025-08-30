package backend.database;

//TODO updateTenet, tenant_row create property owner schema
import java.sql.*;

public class Database {
    private static final String DB_URL = "jdbc:sqlite:rentalRooms.db";

    public void numberofRooms(int room_numbers){
        //step 1 query total room the owner has
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement())
        {
            stmt.execute("PRAGMA foreign_keys = ON;");
            String tenant_row = """
                    INSERT INTO tenants (name,move_in,move_out,employment) VALUES
                    (null,null,null,null)
                    """;

            String queryTotalRooms = "SELECT number_of_rooms FROM residence";
            int total_num = Integer.parseInt(queryTotalRooms);

            //step 2 create the amount of rows in database according to room numbers
            for(int room = 0; room < total_num;room++){
                stmt.execute(tenant_row);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void roomStatus(String name,int room_no, int move_in_date,String employment_status){
        //step 1 query total room the owner has
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement())
        {
            stmt.execute("PRAGMA foreign_keys = ON;");

            // UPDATING Tenet name
            String tenant_name = """
                    UPDATE tenants SET name = ? WHERE room_no = ?
                    """;
            try (PreparedStatement pstmt = conn.prepareStatement(tenant_name)) {
                pstmt.setString(1, name);
                pstmt.setInt(2, room_no);
                pstmt.executeUpdate();
            }
            catch (SQLException e){
                e.printStackTrace();
            }

            String updateMoveindate = """
                    UPDATE tenants SET move_in = ? WHERE room_no = ?
                    """;
            try (PreparedStatement pstm = conn.prepareStatement(updateMoveindate)){
                pstm.setInt(1,move_in_date);
                pstm.setInt(2,room_no);

            }
            catch (SQLException e){
                e.printStackTrace();
            }


            String employment = """
                    UPDATE tenants SET move_in = ? WHERE room_no = ?
                    """;
            try (PreparedStatement pstm = conn.prepareStatement(employment)){
                pstm.setString(1,employment_status);
                pstm.setInt(2,room_no);

            }
            catch (SQLException e){
                e.printStackTrace();
            }

        } catch (SQLException e) {
            e.printStackTrace();
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
