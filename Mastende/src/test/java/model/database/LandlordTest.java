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
    String unhashedpassword ="TightSecurity";
    String hashedPassword =  hashPassword(unhashedpassword);
        System.out.println("hashed" + hashedPassword);
    Landlord newLandlord = new Landlord("mfokaMkhize","mkhize@2456@gmail.com",hashedPassword);

    //hash password

    Landlord anonewLandlord = new Landlord("sugarrush","sugar@gmail.com",hashedPassword);
  //  anonewLandlord.autocommitfalse();
        anonewLandlord.insert_information();

    assertEquals("mfokaMkhize", newLandlord.getUser_name());
    assertEquals("sugar@gmail.com", anonewLandlord.getUser_email());

    assertEquals(hashedPassword, newLandlord.getPassword());
        assertEquals(landlord.confirm_password("sugar@gmail.com"),hashedPassword)  ;
    //check plain string matches the hashed string test

  //  anonewLandlord.insert_information();

    assertTrue(checkPassword(unhashedpassword,hashedPassword));

    anonewLandlord.reverse();
    // check plain string matches the hashed string queried from the database


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