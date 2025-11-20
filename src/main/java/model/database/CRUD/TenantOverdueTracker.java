package model.database.CRUD;

import model.database.ConnectionAccess;
import org.postgresql.shaded.com.ongres.scram.common.bouncycastle.pbkdf2.Strings;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

public class TenantOverdueTracker extends ConnectionAccess {
    private static final Properties dbProps = new Properties();

    static {
        try (InputStream input = Tenants_rent_day.class
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
    public void call_postgres_func(Integer landlordId) {
        String sql = "SELECT update_overdue_date_by_landlord(?)";

        try (PreparedStatement pstmt = newConnection().prepareStatement(sql)) {

            pstmt.setInt(1, landlordId);
            pstmt.execute();

        } catch (SQLException e) {
            System.out.println("SQL Error: " + e.getMessage());
            throw new RuntimeException("Function failed");
        }
    }
    public void reset_all() {
        String sql = "UPDATE tenants_information SET status = false, overdue_date=CURRENT_DATE ";
        try (Statement stmt = newConnection().createStatement()) {
            int rowsAffected = stmt.executeUpdate(sql);  // returns number of rows affected
            System.out.println("Rows updated: " + rowsAffected);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

public static  void main(String[] args) throws SQLException {
      Integer sa = 771;
      TenantOverdueTracker new_day = new TenantOverdueTracker();
//      new_day.call_postgres_func(sa);
        new_day.reset_all();
  }
}
