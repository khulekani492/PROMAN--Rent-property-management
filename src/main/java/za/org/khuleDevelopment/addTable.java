package za.org.khuleDevelopment;

import java.sql.Connection;

public class addTable {
    public static void main(String[] args) {
        configs config = new configs();
        Connection conn = config.established_connection();
        if (conn != null) {
            System.out.println("Connected to SQLite database");
            // Use the connection object
        } else {
            System.out.println("Failed to connect to SQLite database");
        }
    }


}
