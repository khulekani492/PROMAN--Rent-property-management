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

    public Integer total_property_units( String property_name,Integer landlordId) {

        String sql = """
                SELECT COUNT(*) AS total_rows
                FROM properties WHERE property_name=? AND landlord_user_id=?;""";

         Integer total_property_units  = 0;
        try {
            PreparedStatement pstm = this.connection.prepareStatement(sql);{
                pstm.setString(1,property_name);
                pstm.setInt(2,landlordId);
                ResultSet result = pstm.executeQuery();
                if(result.next()){
                    total_property_units = result.getInt("total_rows");

                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return total_property_units;
    }

    public String landlord_property_name(Integer landlordId) {

        String sql = """
                SELECT property_name FROM properties WHERE landlord_user_id =?
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

    public String property_estimated_profit(Integer landlordId, String property_name) {

        String sql = """
                SELECT SUM(CAST(property_rent AS NUMERIC)) AS property_estimated_profit
                FROM properties
                where landlord_user_id=? AND property_name=? AND occupation='yes' ;
                """;

        String username = "";
        try {
            PreparedStatement pstm = this.connection.prepareStatement(sql);{
                pstm.setInt(1,landlordId);
                pstm.setString(2,property_name);
                ResultSet result = pstm.executeQuery();
                if(result.next()){
                    username = result.getString("property_estimated_profit");

                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return username;
    }




    public String landlordEmail(Integer landlordId) {

        String sql = """
                SELECT email FROM general_users WHERE id =?
                limit 1""";

        String username_email = "";
        try {
            PreparedStatement pstm = this.connection.prepareStatement(sql);{
                pstm.setInt(1,landlordId);
                ResultSet result = pstm.executeQuery();
                if(result.next()){
                    username_email = result.getString("email");

                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return username_email;
    }

    public void main(String[] args){
        landlord baby_wait = new landlord();
        Integer humble = baby_wait.landlordId("khulekaniszondo6@gmail.com") ;
        System.out.println(baby_wait.property_estimated_profit(humble,"Thornville_rooms"));
        System.out.println();
        System.out.println(baby_wait.landlord_username(771));
        System.out.println(baby_wait.landlordEmail(771));
    }


//    public String landlord_units(ArrayList<String> due_units) {
//
//    }
}
