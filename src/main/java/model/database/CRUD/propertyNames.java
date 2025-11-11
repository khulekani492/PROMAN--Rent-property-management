package model.database.CRUD;

import model.database.ConnectionAccess;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class propertyNames extends ConnectionAccess {
    public Set <String> fetchAllproperty(Integer landlordId){
        Set<String> user_properties = new HashSet<>();
        String sql = """
                SELECT property_name From properties WHERE landlord_user_id =?;\s
                """;
        try {
            PreparedStatement pstm = this.connection.prepareStatement(sql);{
                pstm.setInt(1,landlordId);
                ResultSet propertyNames = pstm.executeQuery();
                while(propertyNames.next()){
                    user_properties.add(propertyNames.getString("property_name"));
                }
                return user_properties;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    static public  void main(String[] args) {
        propertyNames getAllproperty = new propertyNames();
        landlord user_id = new landlord();
        System.out.println(getAllproperty.fetchAllproperty(user_id.landlordId("khulekaniszondo6@gmail.com")));
    }
}
