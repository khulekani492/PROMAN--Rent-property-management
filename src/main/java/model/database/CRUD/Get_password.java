package model.database.CRUD;

import model.database.ConnectionAccess;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Get_password extends ConnectionAccess {
    private  String user_email;


    public Get_password(String user_email){
        this.user_email = user_email;


    }

    public String getUser_email(){
        return this.user_email;
    }

    public String compare() {
        String fetch = "SELECT password FROM general_users WHERE email = ?";

        try {
            PreparedStatement pstm = this.connection.prepareStatement(fetch);
            pstm.setString(1, this.user_email);

            ResultSet result = pstm.executeQuery();

            if (result.next()) {
                String pass = result.getString("password");
                return pass;
            } else {
                    return  "NO USER FOUND";
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}
