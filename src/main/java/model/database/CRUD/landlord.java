package model.database.CRUD;

import model.database.ConnectionAccess;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
    public void main(String[] args){
        landlord baby_wait = new landlord();
        System.out.println(baby_wait.landlordId("khulekaniszondo6@gmail.com") );

    }
}
