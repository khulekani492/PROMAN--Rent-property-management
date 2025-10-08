package model.database;

import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class LandlordTest {
    @Test
    void testLandlordInitialization() throws SQLException {
    Landlord landlord = new Landlord();
    Landlord newLandlord = new Landlord("mfokaMkhize","mkhize@2456@gmail.com","TightSecurity");


    assertEquals("mfokaMkhize", landlord.getUser_name());
    assertEquals("mkhize@2456@gmail.com", landlord.getUser_email());
    assertEquals("TightSecurity", landlord.getPassword());
}

}