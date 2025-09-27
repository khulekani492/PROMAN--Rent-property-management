package model.database;

import java.sql.Connection;

public abstract class connectionAcess {

    protected Connection connection;

    public connectionAcess(Connection connect){
        this.connection = connect;
    }
}
