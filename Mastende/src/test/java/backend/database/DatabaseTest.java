package backend.database;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.sql.*;


import static org.junit.jupiter.api.Assertions.*;

public class DatabaseTest {
    private Data dummyconnection;
    private static final String D_URL = "jdbc:sqlite:sandle.db";

    @Test
    public void testSchema() throws SQLException {
        dummyconnection = new Data(D_URL);
        String sql = "SELECT COUNT(*) AS total FROM tenants";
        dummyconnection.numberofRooms(5);

        // Verify after insert
        try (Connection conn = DriverManager.getConnection(D_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            int updated_total = rs.getInt("total");
            assertEquals(10, updated_total);


        }


    }

//    String name, int room_no, int move_in_date, String employment_status


    @Test
    public  void  testTenantname() throws SQLException {
        dummyconnection.roomStatus("Khulekani", 2, 12072024, "yes");
        // Verify after insert

        try (Connection coon = DriverManager.getConnection(D_URL)){
            Statement stmt = coon.createStatement();{
                String sql = "SELECT name FROM tenants where room_no = 2";
//                ResultSet =  stmt.execute(sql);
            }

        }
    }




}