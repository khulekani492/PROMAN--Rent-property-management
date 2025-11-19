package model.database.CRUD;


import model.database.ConnectionAccess;

import java.sql.SQLException;
import java.sql.Statement;

//Data operation --> set all payment_status to zero
public class Month extends ConnectionAccess  {

    private  final String sql = """
            UPDATE tenants_information SET status=false;\s
           \s""";

    public Month(){

    }

    public  void set_all(){


    }


}

