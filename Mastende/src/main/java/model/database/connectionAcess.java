package model.database;

import org.eclipse.jetty.webapp.AbsoluteOrdering;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public abstract class connectionAcess {
    protected static final String DB_URL = "jdbc:sqlite:classes.db";


    protected Connection connection;


    public connectionAcess() throws SQLException {
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
        System.out.println("connection runnning");

    }
}
