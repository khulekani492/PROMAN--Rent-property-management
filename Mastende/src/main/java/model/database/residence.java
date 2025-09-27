package model.database;

import java.sql.Connection;

public class residene extends connectionAcess implements  Property{
    public residene(Connection connect) {
        super(connect);
    }

    @Override
    public void insert_information() {

    }

    @Override
    public Integer UniqueID() {
        return 0;
    }
}
