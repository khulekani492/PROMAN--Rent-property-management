package model.database.CRUD;

import model.database.ConnectionAccess;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Tenant extends ConnectionAccess {

    public String tenantName(Integer tenantId){
        String sql = """
                SELECT name FROM general_users WHERE id=?""";
        try {
            PreparedStatement pstm = this.connection.prepareStatement(sql);{
                pstm.setInt(1,tenantId);
                ResultSet result = pstm.executeQuery();
                if(result.next()){
                  String tenant_name = result.getString("name");
                  return  tenant_name;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
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

    public String tenant_property_name(Integer tenant_Id) {

        String sql = """
               SELECT property_name FROM properties\s
               inner join\s
               tenants_information\s
               ON tenants_information.tenant_user_id =properties.tenant_user_id WHERE tenants_information.tenant_user_id=? ;""";

        String username = "";
        try {
            PreparedStatement pstm = this.connection.prepareStatement(sql);{
                pstm.setInt(1,tenant_Id);
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
        Tenant baby_wait = new Tenant();
        System.out.println("DOING GOOD");
        System.out.println(baby_wait.tenant_property_name(772) );
       // System.out.println(baby_wait.landlord_username(772));
        //System.out.println(baby_wait.landlordEmail(772));
    }




}
