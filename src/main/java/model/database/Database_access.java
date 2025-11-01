package model.database;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
//To do implementing a singleton design pattern to reduce perfromance overhead when connecting the database
public class connectionAcess {
    private static  connectionAcess Instance;

    static {
        try {
            Instance = new connectionAcess();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private final  Connection connection;


    private connectionAcess() throws SQLException {
        Properties props = new Properties();

        try (InputStream input = connectionAcess.class.getClassLoader().getResourceAsStream("db.properties")) {
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

    public static connectionAcess getInstance() {
        return Instance;
    }
    public Connection getConnection(){
        return connection;
    }
}
