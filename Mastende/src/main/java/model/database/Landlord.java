package model.database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Data Access class for User (the Landlord).
 * <p>
 * When the user signs in, this class is called and the information is added
 * to the <code>Users</code> database table. The table is then queried to get
 * the unique ID of the landlord.
 * </p>
 *
 * <p>
 * Through abstraction, the landlordId can be accessed via the abstract
 * instance. This is used to handle the foreign key relationship with
 * other tables. In the <code>Users</code> schema, the foreign key is
 * <code>propertyID</code>.
 * </p>
 *
 * <p>
 * To access it, refer to demonstration 1.2 of abstraction.
 * </p>
 */

public class Landlord extends connectionAcess implements Property{
   private final String user_name;
   private final String user_email;
   private final String password;


    public Landlord(String user_name, String user_email, String password) throws SQLException {
        super();
        this.user_name = user_name;
        this.user_email = user_email;
        this.password = password;
    }
    public Landlord() throws SQLException {
        super();
        this.user_name = "";
        this.user_email = "";
        this.password = "";

    }


    @Override
    public void insert_information() {
           String insertUserSQL = """
                   INSERT INTO users (user_name, user_email, password)
                   VALUES (?, ?, ?)
                   ON CONFLICT (user_email) DO NOTHING;
                   
                   """;
           try (PreparedStatement pstm = connection.prepareStatement(insertUserSQL)){
               pstm.setString(1,this.user_name);
               pstm.setString(2,this.user_email);
               pstm.setString(3,this.password);
               pstm.executeUpdate();
           }catch (SQLException e){
               throw new RuntimeException("Database update failed", e);
           }


    }

    public String getUser_passoword(String email) {

        String sql = "SELECT password FROM users WHERE user_email = ?";
        try (PreparedStatement pstm = connection.prepareStatement(sql)) {
            pstm.setString(1, email); // email is a String

            try (ResultSet rs = pstm.executeQuery()) { // use executeQuery() for SELECT
                if (rs.next()) { // move to the first row
                    String password = rs.getString("password"); // get password as String

                    return  password;
                } else {
                    // no user found with this email
                    return "not found";
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database query failed", e);
        }

    }
    /**
     * Get the unique ID using the user's email.
     * <p>
     * User emails are unique identifiers in their own right. For security,
     * this helps narrow the issue of fetching the wrong ID for a user causing leakages.
     * </p>
     */
//   TODO  Session One-06
//-- fix the bug of inserting for the same room
//-- add the user to the history table of rooms
//-- supabase auth to sign up user,set connection, migrate sqlite schema to postgres
    @Override
    public Integer UniqueID() {
        String reference_key = """
        SELECT id FROM Users WHERE user_email = ?;
    """;
        try (PreparedStatement pstmt = connection.prepareStatement(reference_key)) {
            pstmt.setString(1, this.user_email);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null; // if not found
}
}
