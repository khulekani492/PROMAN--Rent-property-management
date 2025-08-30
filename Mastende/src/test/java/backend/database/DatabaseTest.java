package backend.database;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.sql.*;


import static org.junit.jupiter.api.Assertions.*;

public class DatabaseTest {
    private Database dummyconnection;
    private static final String D_URL = "jdbc:sqlite:rentalRooms.db";

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
     public void testNumberOfRooms() throws SQLException {
        dummyconnection = new Database(D_URL);
        String sql = "SELECT COUNT(*) AS total FROM tenants";
        try (Connection conn = DriverManager.getConnection(D_URL);
             Statement ptsm = conn.createStatement()){
             ResultSet database_rows = ptsm.executeQuery(sql);
             dummyconnection.numberofRooms(8);

            int total_rows = 0;

            if (database_rows.next()) {
                total_rows = database_rows.getInt("total");  // get the actual count
            }
            assertEquals(5,total_rows);

        }

    }

}