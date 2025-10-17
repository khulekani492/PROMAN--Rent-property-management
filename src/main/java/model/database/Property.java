package model.database;

import java.sql.SQLException;

public interface Property {

    void insert_information() throws SQLException;
    Integer UniqueID();
}


