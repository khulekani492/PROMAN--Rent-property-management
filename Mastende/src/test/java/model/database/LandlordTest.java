package model.database;

import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static API.SecurityUtil.checkPassword;
import static API.SecurityUtil.hashPassword;
import static org.junit.jupiter.api.Assertions.*;

class LandlordTest {
    @Test
    void testLandlordInitialization() throws SQLException {
    Landlord landlord = new Landlord();
    Landlord newLandlord = new Landlord("mfokaMkhize","mkhize@2456@gmail.com","TightSecurity");


    assertEquals("mfokaMkhize", newLandlord.getUser_name());
    assertEquals("mkhize@2456@gmail.com", newLandlord.getUser_email());
    assertEquals("TightSecurity", newLandlord.getPassword());

    //check plain string matches the hashed string
    String hashedPassword =  hashPassword(newLandlord.getPassword());

    assertTrue(checkPassword(newLandlord.getPassword(),hashedPassword));

}

}