package model.database;

import java.sql.Connection;

public abstract class ConnectionAccess {
    protected Connection connection;
    public ConnectionAccess  () {
        this.connection = Database_access.getInstance().getConnection();
    }

}
