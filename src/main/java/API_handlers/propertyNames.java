package API_handlers;

import model.database.ConnectionAccess;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class propertyNames extends ConnectionAccess {
    public ArrayList<String> fetchAllproperty(Integer landlordId){
        ArrayList<String> user_properties = new ArrayList<>();
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
        propertyNames s = new propertyNames();

        System.out.println(s.fetchAllproperty(810));
    }
}
