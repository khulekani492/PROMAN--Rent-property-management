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
    public Integer tenant_ID(String property_name) {

        String sql = """
               SELECT general_users.id FROM general_users\s
               inner join\s
               properties\s
               ON properties.tenant_user_id = general_users.id WHERE general_users.name=? ;""";

            Integer id = null;
        try {
            PreparedStatement pstm = this.connection.prepareStatement(sql);{
                pstm.setString(1,property_name);
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

        public void update_debt(Integer new_amount,Integer tenant_Id) {

            String sql = """
    UPDATE tenants_information t1
    SET debt = ?
    FROM properties p
    WHERE t1.tenant_user_id = ?
      AND t1.tenant_user_id = p.tenant_user_id;
""";
            Integer updated_debt = null;
        try {
            PreparedStatement pstm = this.connection.prepareStatement(sql);{
                pstm.setInt(1,new_amount);
                pstm.setInt(2,tenant_Id);
                 pstm.execute();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public Integer tenant_debt(Integer tenant_Id) {

        String sql = """
               SELECT tenants_information.debt FROM tenants_information\s
               inner join\s
               properties\s
               ON tenants_information.tenant_user_id =properties.tenant_user_id WHERE tenants_information.tenant_user_id=? ;""";
            Integer updated_debt = null;
        try {
            PreparedStatement pstm = this.connection.prepareStatement(sql);{
                pstm.setInt(1,tenant_Id);
                ResultSet result = pstm.executeQuery();
                if(result.next()){
                    updated_debt = result.getInt("debt");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return updated_debt;
    }




    public Integer tenant_property_rent(Integer tenant_Id) {
        String sql = """
               SELECT property_rent FROM properties\s
               inner join\s
               tenants_information\s
               ON tenants_information.tenant_user_id =properties.tenant_user_id WHERE tenants_information.tenant_user_id=? ;""";

        Integer property_rent = null;
        try {
            PreparedStatement pstm = this.connection.prepareStatement(sql);{
                pstm.setInt(1,tenant_Id);
                ResultSet result = pstm.executeQuery();
                if(result.next()){
                    property_rent = result.getInt("property_rent");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return property_rent;
    }



    public void main(String[] args){
        Tenant baby_wait = new Tenant();
        System.out.println("DOING GOOD");

        System.out.println(baby_wait.tenant_ID("Andiswa Mandoda"));
       // System.out.println(baby_wait.landlord_username(772));
        //System.out.println(baby_wait.landlordEmail(772));
    }
}
