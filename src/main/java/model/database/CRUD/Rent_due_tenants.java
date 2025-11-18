package model.database.CRUD;

import model.database.ConnectionAccess;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.*;

public class Rent_due_tenants {

    private static final Properties dbProps = new Properties();

    static {
        try (InputStream input = Rent_due_tenants.class
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


    // Create a NEW DB connection every time
    private Connection newConnection() throws SQLException {
        String url = dbProps.getProperty("db.url");
        String user = dbProps.getProperty("db.user");
        String password = dbProps.getProperty("db.password");
        return DriverManager.getConnection(url, user, password);
    }


    public HashMap<Integer, ArrayList<Integer>> rent_due_tenants(Integer landlord) {

        HashMap<Integer, ArrayList<Integer>> map = new HashMap<>();
        ArrayList<Integer> tenants_due = new ArrayList<>();

        String sql = """
            SELECT properties.property_unit 
            FROM properties 
            INNER JOIN tenants_information 
            ON properties.tenant_user_id = tenants_information.tenant_user_id
            WHERE properties.landlord_user_id = ? 
              AND tenants_information.status = false;
        """;

        try (Connection connection = newConnection();
             PreparedStatement pstm = connection.prepareStatement(sql)) {

            pstm.setInt(1, landlord);

            try (ResultSet result = pstm.executeQuery()) {
                while (result.next()) {
                    tenants_due.add(result.getInt("property_unit"));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("DB error in rent_due_tenants: " + e.getMessage(), e);
        }

        map.put(landlord, tenants_due);
        return map;
    }


    public static void main(String[] args) {
        Rent_due_tenants test = new Rent_due_tenants();
        System.out.println(test.rent_due_tenants(771));
    }
}


