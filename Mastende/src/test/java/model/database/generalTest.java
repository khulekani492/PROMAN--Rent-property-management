package model.database;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import static API.SecurityUtil.checkPassword;
import static API.SecurityUtil.hashPassword;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;
import java.io.*;
import java.sql.*;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

public class generalTest {

    private Connection connection;
    private static Properties props = new Properties();
    general landlord = new general();

    public generalTest() throws SQLException {
    }

    // Load database configuration before all tests
    @BeforeAll
    static void loadDatabaseConfig() {
        try (InputStream input = generalTest.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (input == null) {
                throw new IOException("db.properties not found in resources folder.");
            }
            props.load(input);
        } catch (IOException ex) {
            throw new RuntimeException("Failed to load database configuration", ex);
        }
    }

    @BeforeEach
    void setUp() throws SQLException {
        String url = props.getProperty("db.url");
        String user = props.getProperty("db.user");
        String password = props.getProperty("db.password");
        connection = DriverManager.getConnection(url, user, password);
        landlord.setConnection(connection);
       // manual transaction control
    }


    @AfterEach
    void tearDown() throws SQLException {
        String deleteProperties = "DELETE FROM properties";
        String deleteUsers = "DELETE FROM general_users";

        try (
                PreparedStatement stmt1 = connection.prepareStatement(deleteProperties);
                PreparedStatement stmt2 = connection.prepareStatement(deleteUsers)
        ) {
            stmt1.executeUpdate();  // delete from properties first
            stmt2.executeUpdate();  // then delete from general_users
        }

        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }


    // Mock password hashing for demonstration (replace with your real one)
    private String hashPassword(String plainText) {
        return "hashed_" + plainText;
    }

    private boolean checkPassword(String plainText, String hashed) {
        return hashed.equals("hashed_" + plainText);
    }

    @Test
    void testLandlordInitialization() throws SQLException {
        String unhashedPassword = "TightSecurity";
        String hashedPassword = hashPassword(unhashedPassword);

        general newLandlord = new general(
                "khule",
                "khule@gmail.com",
                hashedPassword,
                "tenant",
                "0826690384",
                "1st street somewhere",
                3
        );
        newLandlord.setConnection(connection);
         //Check  connection is the same
        assertEquals(connection,newLandlord.getConnection());
        assertEquals("khule", newLandlord.getUser_name());
        System.out.println(newLandlord.getUser_email());
        assertEquals("khule@gmail.com",newLandlord.getUser_email());
        assertEquals("0826690384",newLandlord.getContact());
        assertEquals(hashedPassword, newLandlord.getPassword());
        newLandlord.insert_information();
        Integer landlordId = newLandlord.UniqueID();

        assertNotNull(landlordId, "Landlord ID should not be null");

        // Create and link property
        residence property = new residence(3, 600, "yes");
        property.setlandlord(landlordId);
        property.insert_information();

        assertEquals(landlordId, property.getLandlordId(), "Landlord ID should match property record");
        assertTrue(checkPassword(unhashedPassword, hashedPassword));
    }

    @Test
    void testTenant() throws SQLException {
        String unhashedPassword = "Deadly@IKnow";
        String hashedPassword = hashPassword(unhashedPassword);

        general newTenant = new general(
                "Mkhize",
                "0826690384",
                "mKHIZE@34gmail.com",
                hashedPassword,
                "tenant",
                "1st street somewhere",
                4
        );
        newTenant.setConnection(connection);

        assertEquals("Mkhize", newTenant.getUser_name());
        assertEquals(hashedPassword, newTenant.getPassword());

        residence property = new residence(3, 600, "yes");

        newTenant.insert_information();

        Integer tenantId = newTenant.UniqueID();
        property.setTenantId(tenantId);

        assertNotNull(property.getTenantId(), "Tenant ID should not be null");
        assertEquals(tenantId, property.getTenantId(), "Property should reference the correct tenant ID");
    }

    @Test
    void testInsertAndQueryDebt() throws SQLException {
        String unhashedPassword = "Deadly@IKnow";
        String hashedPassword = hashPassword(unhashedPassword);

        general newLandlord = new general(
                "dao",
                "dao@gmail.com",
                hashedPassword,
                "tenant",
                "0826690384",
                "1st street somewhere",
                6
        );
        newLandlord.setConnection(connection);
        int price = 250;
        residence property = new residence(3, 600, "yes");

        newLandlord.insert_information();
        Integer landlordId = newLandlord.UniqueID();
        assertNotNull(landlordId, "Landlord ID should not be null");

        property.setlandlord(landlordId);

        property.insert_information();
        property.setDebt(price);

        property.update_debt();

        assertNotNull(property.getDebt(), "Debt should not be null after insert");
        assertEquals(price, property.getDebt(), "Debt should match the inserted value");

        Integer moneyOwed = property.queryDebt();
        assertNotNull(moneyOwed, "Queried debt should not be null");
        assertEquals(property.getDebt(), moneyOwed, "Queried debt should match the inserted value");


    }
    @Test
    void testTenantadded() throws SQLException {

        general newLandlord = new general(
                "dao",
                "0826690384",
                "tenant"
        );
        newLandlord.setConnection(connection);
        newLandlord.landlord_insert_tenant();
        Integer landlordId = newLandlord.UniqueID();
        assertNotNull(landlordId,
                "Landlord ID should not be null");
        assertNotNull(newLandlord.getContact(),
                "Debt should not be null after insert");
        assertNotNull(newLandlord.getUser_name(),
                "Debt should match the inserted value");
        assertNotNull(newLandlord.getUser_type());



    }


}
