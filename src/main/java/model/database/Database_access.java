package model.database;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Database_access {
    private static final Database_access Instance;

    static {
        try {
            Instance = new Database_access();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private final  Connection connection;


    private Database_access() throws SQLException {
        Properties props = new Properties();

        try (InputStream input = Database_access.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (input == null) {
                throw new IOException("db.properties not found in resources folder.");
            }
            props.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load db.properties: " + e.getMessage(), e);
        }
        String url = props.getProperty("db.url");
        String user = props.getProperty("db.user");
        String password = props.getProperty("db.password");
        this.connection = DriverManager.getConnection(url,user,password);

    }

    public static Database_access getInstance() {
        return Instance;
    }
    public Connection getConnection(){
        return connection;
    }
}
