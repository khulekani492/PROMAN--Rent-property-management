package backend.database;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.sql.*;


import static org.junit.jupiter.api.Assertions.*;

public class DatabaseTest {
    private Data dummyconnection;
    private static final String D_URL = "jdbc:sqlite:deadman.db";

//    @AfterEach
//    public  void  droptable()throws  SQLException{
//        String droptable = """
//                DROP table tenants;
//                """;
//        try (Connection conn = DriverManager.getConnection(D_URL)){
//            Statement exct = conn.createStatement();
//            exct.execute(droptable);
//
//        }
//    }

    @Test
    public void testSchema() throws SQLException {
        dummyconnection = new Data(D_URL);

        String sql = "SELECT COUNT(*) AS total FROM tenants";
        int total_rows = 0;

        // First query (then close it before inserting)
        try (Connection conn = DriverManager.getConnection(D_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                total_rows = rs.getInt("total");
            }
        }
        System.out.printf("total_rows",total_rows);
        // Now run insert safely
        dummyconnection.numberofRooms(5);

        // Verify after insert
        try (Connection conn = DriverManager.getConnection(D_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            int updated_total = rs.getInt("total");
            assertEquals(5, updated_total);
        }
        try (Connection conn = DriverManager.getConnection(D_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                total_rows = rs.getInt("total");
            }
        }
        System.out.printf("total_rows",total_rows);
    }


}