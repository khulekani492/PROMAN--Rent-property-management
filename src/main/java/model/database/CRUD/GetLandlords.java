package model.database.CRUD;

import model.database.ConnectionAccess;
import schedule.Reminder;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

public class GetLandlords extends ConnectionAccess {
    public Set<Integer> fetchAll(){
        Set<Integer> landlords = new HashSet<>();
        String sql = """
             SELECT landlord_user_id FROM properties INNER JOIN tenants_information ON\s
             properties.tenant_user_id = tenants_information.tenant_user_id WHERE tenants_information.status=false
            \s""";

        try(Statement pstm = connection.createStatement()){
            ResultSet result = pstm.executeQuery(sql);
            while (result.next()){
                landlords.add( result.getInt("landlord_user_id")  ) ;

            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return landlords ;

    }
    public static void main(String[] args){
        GetLandlords omastede = new GetLandlords();
        System.out.println(omastede.fetchAll());
    }

}
