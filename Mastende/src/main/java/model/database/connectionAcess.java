package model.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public abstract class connectionAcess {
    protected static final String DB_URL = "jdbc:sqlite:dummytwo.db";
    protected Connection connection;

    public connectionAcess() throws SQLException {
        this.connection = DriverManager.getConnection(DB_URL);
    }
}
