package model.database;

import java.sql.Connection;

public class residence extends connectionAcess implements  Property{
    public residence(Connection connect) {
        super(connect);
    }

    @Override
    public void insert_information() {

    }




    @Override
    public Integer UniqueID(String name) {
        return 0;
    }
}
