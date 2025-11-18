package model.database.CRUD.tracker;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

public class Tenants_rent_day

{
    private static final Properties dbProps = new Properties();

    static {
        try (InputStream input = model.database.CRUD.Tenants_rent_day.class
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


    public HashMap<Integer, ArrayList<Integer>> rent_due_tenants_date(Integer landlord) {

        HashMap<Integer, ArrayList<Integer>> map = new HashMap<>();
        ArrayList<Integer> tenants_due = new ArrayList<>();


        String sql = """
                      SELECT tenants_information.rent_payment_day\s
                      FROM tenants_information\s
                      INNER JOIN properties\s
                      ON tenants_information.tenant_user_id = properties.tenant_user_id
                      WHERE properties.landlord_user_id = ?\s
                        AND tenants_information.status = false;
                  """;


        try (Connection connection = newConnection();
             PreparedStatement pstm = connection.prepareStatement(sql)) {

            pstm.setInt(1, landlord);

            try (ResultSet result = pstm.executeQuery()) {
                while (result.next()) {
                    tenants_due.add(result.getInt("rent_payment_day"));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("DB error in rent_due_tenants: " + e.getMessage(), e);
        }

        map.put(landlord, tenants_due);
        return map;
    }


    public static void main(String[] args) {
        Tenants_rent_day test = new Tenants_rent_day();
        System.out.println(test.rent_due_tenants_date(771));
    }


}
