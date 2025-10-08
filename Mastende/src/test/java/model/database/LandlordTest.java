package model.database;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static API.SecurityUtil.checkPassword;
import static API.SecurityUtil.hashPassword;
import static org.junit.jupiter.api.Assertions.*;

class LandlordTest extends connectionAcess {


    LandlordTest() throws SQLException {
    }
    @BeforeEach
    void beginTransaction() throws SQLException {
        super.connection.setAutoCommit(false);
    }

    @AfterEach
    void rollbackTransaction() throws SQLException {
        super.connection.rollback();  // Undo all test changes
    }
    @Test
    void testLandlordInitialization() throws SQLException {
    Landlord landlord = new Landlord();
    Landlord newLandlord = new Landlord("mfokaMkhize","mkhize@2456@gmail.com","TightSecurity");
    Landlord anonewLandlord = new Landlord("sugarrush","sugar@gmail.com","TightSecYTRrity");
    anonewLandlord.autocommitfalse();

    assertEquals("mfokaMkhize", newLandlord.getUser_name());
    assertEquals("mkhize@2456@gmail.com", newLandlord.getUser_email());
    assertEquals("TightSecurity", newLandlord.getPassword());

    //check plain string matches the hashed string test
    String hashedPassword =  hashPassword(newLandlord.getPassword());
    anonewLandlord.insert_information();

    assertTrue(checkPassword(newLandlord.getPassword(),hashedPassword));

    anonewLandlord.reverse();
    // check plain string matches the hashed string queried from the database
    assertTrue(landlord.confirm_password("mkhize@2456@gmail.com",newLandlord.getPassword()));

}
    @Test
    void testUseremail() throws SQLException {
        String sql = "SELECT * FROM users WHERE user_email = ?";
        try (PreparedStatement stmt = this.connection.prepareStatement(sql)) {
            stmt.setString(1, "khulekaniszondo6@gmail.com");
            ResultSet rs = stmt.executeQuery();
            assertTrue(rs.next(), "User not found in database");

            if(rs.next()){
                System.out.println(rs.getString("user_email"));
                assertEquals("khulekaniszondo6@gmail.com", rs.getString("user_email")
                );
            }

        }
    }

}