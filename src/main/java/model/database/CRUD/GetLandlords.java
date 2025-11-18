package model.database.CRUD;

import model.database.ConnectionAccess;
import schedule.Reminder;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

public class GetLandlords {

    // Load DB config once
    private static final Properties dbProps = new Properties();

    static {
        try (InputStream input = GetLandlords.class
                .getClassLoader()
                .getResourceAsStream("db.properties")) {

            if (input == null) {
                throw new IOException("db.properties not found in resources");
            }
            dbProps.load(input);

        } catch (IOException e) {
            throw new RuntimeException("Failed to load db.properties", e);
        }
    }


    /**
     * Gets a NEW database connection every time.
     * This is safe for Quartz, avoids stale connections,
     * avoids SSL timeouts, and works with cloud Postgres.
     */
    private Connection newConnection() throws SQLException {
        String url = dbProps.getProperty("db.url");
        String user = dbProps.getProperty("db.user");
        String password = dbProps.getProperty("db.password");
        return DriverManager.getConnection(url, user, password);
    }


    /**
     * Fetch all landlord IDs.
     * Each call uses its own fresh connection.
     */
    public Set<Integer> fetchAll() {
        Set<Integer> landlords = new HashSet<>();

        String sql = """
            SELECT landlord_user_id\s
            FROM properties\s
            INNER JOIN tenants_information\s
            ON properties.tenant_user_id = tenants_information.tenant_user_id\s
            WHERE tenants_information.status = false
       \s""";

        try (Connection connection = newConnection();
             Statement stmt = connection.createStatement();
             ResultSet result = stmt.executeQuery(sql)) {

            while (result.next()) {
                landlords.add(result.getInt("landlord_user_id"));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Database error: " + e.getMessage(), e);
        }

        return landlords;
    }


    public static void main(String[] args) {
        GetLandlords gl = new GetLandlords();
        System.out.println(gl.fetchAll());
    }
}
