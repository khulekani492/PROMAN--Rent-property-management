package backend.database;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


import static org.junit.jupiter.api.Assertions.*;

public class DatabaseTest {
    private Data dummyconnection;
    private static final String D_URL = "jdbc:sqlite:sandle.db";


    public  void dropTable(){
        try (Connection conn = DriverManager.getConnection(D_URL)){
            Statement stmt = conn.createStatement();
            String deleteTab= """
                    DROP TABLE IF EXISTS tenants
                    """;
            stmt.execute(deleteTab);

        } catch (Exception e){
            throw new RuntimeException(e);

        }
    }

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


    @Test
    public  void  testTenantname() throws SQLException {
        dummyconnection  = new Data(D_URL);
        dummyconnection.roomStatus("Khulekani", 2, "2024-07-12", "yes");
        // Verify after insert
        try (Connection coon = DriverManager.getConnection(D_URL)){
            Statement stmt = coon.createStatement();{
                String sql = "SELECT name FROM tenants where room_no = 2";

                ResultSet rs =  stmt.executeQuery(sql);
                String igama = rs.getString("name");

                try{
                    System.out.printf(igama);
                } catch (Exception e) {
                    throw new RuntimeException(e);

                }
                assertEquals("Khulekani",igama );

                //Test the db upddate date table
                ResultSet date = stmt.executeQuery("SELECT move_in FROM tenants where room_no = 2");
                String movInStr = date.getString("move_in");
                assertEquals("2024-07-12",movInStr);


                ResultSet employment = stmt.executeQuery("SELECT move_in FROM tenants where room_no = 2");
                String status = date.getString("move_in");

            }

        }
    }

}