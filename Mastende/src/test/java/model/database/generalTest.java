package model.database;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static API.SecurityUtil.checkPassword;
import static API.SecurityUtil.hashPassword;
import static org.junit.jupiter.api.Assertions.*;

class generalTest {
    @Test
    void testLandlordInitialization() throws SQLException {
        general landlord = new general();
        String unhashedpassword ="TightSecurity";
        String hashedPassword =  hashPassword(unhashedpassword);

        general newLandlord = new general("khule","khule@gmail.com",hashedPassword,"tenant","0826690384","1st street somewhere");

        newLandlord.insert_information();
        assertEquals("khule", newLandlord.getUser_name());
        assertEquals(hashedPassword, newLandlord.getPassword());
        newLandlord.autocommitfalse();

        //general_users User Id for landlord;
        Integer landlordId = newLandlord.UniqueID();
        assertNotNull(landlordId);
        //add foreign key of the landlord to they unit
        residence property = new residence(3,600,"yes",2,2,7);
        property.autocommitfalse();
        property.setlandlord(landlordId);
        property.insert_information();
        //check the foreign key get inserted in the property
        assertEquals(landlordId,property.getLandlordId());
        newLandlord.reverse();
        property.reverse();

        assertTrue(checkPassword(unhashedpassword,hashedPassword));

    }
    @Test
    void testTenant () throws SQLException {
        general landlord = new general();
        String unhashedpassword ="Deadly@IKnow";
        String hashedPassword =  hashPassword(unhashedpassword);
        general newLandlord = new general("Mkhize","Mkhize@gmail.com",hashedPassword,"tenant","0826690384","1st street somewhere");
        assertEquals("Mkhize", newLandlord.getUser_name());
        assertEquals(hashedPassword, newLandlord.getPassword());
        residence property = new residence(3,600,"yes",2,2,7);
        newLandlord.autocommitfalse();
        newLandlord.insert_information();
        Integer tenant =  newLandlord.UniqueID();
        property.setTenantId(tenant);

        assertNotNull(property.getTenantId());
        //check the foreign key get inserted in the property for tenants
        assertEquals(tenant,property.getTenantId());
        newLandlord.autocommitfalse();
    }

}