package model.database;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static API.SecurityUtil.checkPassword;
import static API.SecurityUtil.hashPassword;
import static org.junit.jupiter.api.Assertions.*;

class landlordTest extends connectionAcess {


    landlordTest() throws SQLException {
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
    landlord landlord = new landlord();
    String unhashedpassword ="TightSecurity";
    String hashedPassword =  hashPassword(unhashedpassword);
    System.out.println("hashed" + hashedPassword);
    landlord newLandlord = new landlord("khule","khule@gmail.com",hashedPassword,"tenant","0826690384","1st street somewhere");
        newLandlord.insert_information();
    newLandlord.autocommitfalse();



    Integer landlordId = newLandlord.UniqueID();
    System.out.println(landlordId);
    residence property = new residence(3,600,"yes",2,2,7);
    property.autocommitfalse();
    property.setlandlord(landlordId);
    assertEquals("khule", newLandlord.getUser_name());

    //check the foreign key get inserted in the property
        assertEquals(landlord.confirm_password("khule@gmail.com"),hashedPassword)  ;

        assertEquals(landlordId,property.getLandlordId());
    property.insert_information();
    newLandlord.reverse();
    property.reverse();


    assertEquals(hashedPassword, newLandlord.getPassword());


  //  anonewLandlord.insert_information();

    assertTrue(checkPassword(unhashedpassword,hashedPassword));

    //anonewLandlord.reverse();
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