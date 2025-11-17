package model.database.CRUD;

import model.database.ConnectionAccess;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Rent_due_tenants extends ConnectionAccess {


    public  HashMap<Integer, ArrayList<Integer>> rent_due_tenants(Integer landlord){
        HashMap<Integer,ArrayList<Integer>> landlord_tenants_due =  new HashMap<>();

        ArrayList<Integer> tenants_due = new ArrayList<>();

        String sql = """
            SELECT properties.tenant_user_id FROM properties INNER JOIN tenants_information ON properties.tenant_user_id = tenants_information.tenant_user_id 
            WHERE properties.landlord_user_id=? AND tenants_information.status=false;
            \s""";

        try {
            PreparedStatement pstm = this.connection.prepareStatement(sql);{
                pstm.setInt(1,landlord);
                ResultSet result = pstm.executeQuery();
                while (result.next()){
                    tenants_due.add(result.getInt("tenant_user_id"));

                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        landlord_tenants_due.put(landlord,tenants_due);
        return  landlord_tenants_due;
    }

    static void main(){
        Rent_due_tenants HEY = new Rent_due_tenants();
        System.out.println(HEY.rent_due_tenants(771));
    }
}
