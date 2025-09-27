package model.database;

import java.sql.Connection;

public class landlord extends connectionAcess implements Property{


    public landlord(Connection connect) {
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
