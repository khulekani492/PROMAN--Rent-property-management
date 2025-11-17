package model.database.CRUD;

import model.database.ConnectionAccess;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class landlord extends ConnectionAccess {

    public Integer landlordId(String user_email){
        String sql = """
                SELECT id FROM general_users WHERE email=?
                """;

         Integer id = null;
        try {
            PreparedStatement pstm = this.connection.prepareStatement(sql);{
                pstm.setString(1,user_email);
                ResultSet result = pstm.executeQuery();
                if(result.next()){
                    id = result.getInt("id");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return id;
    }
        public String landlord_username(Integer userID){
        String sql = """
                SELECT name FROM general_users WHERE id=?
                limit 1""";

         String username = "";
        try {
            PreparedStatement pstm = this.connection.prepareStatement(sql);{
                pstm.setInt(1,userID);
                ResultSet result = pstm.executeQuery();
                if(result.next()){
                   username = result.getString("name");

                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return username;
    }


    public String landlord_property_name(Integer landlordId) {

        String sql = """
                SELECT property_name FROM properties WHERE id=?
                limit 1""";

        String username = "";
        try {
            PreparedStatement pstm = this.connection.prepareStatement(sql);{
                pstm.setInt(1,landlordId);
                ResultSet result = pstm.executeQuery();
                if(result.next()){
                    username = result.getString("property_name");

                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return username;
    }

    public void main(String[] args){
        landlord baby_wait = new landlord();

        System.out.println(baby_wait.landlordId("khulekaniszondo6@gmail.com") );
        System.out.println(baby_wait.landlord_username(771));
    }


//    public String landlord_units(ArrayList<String> due_units) {
//
//    }
}
