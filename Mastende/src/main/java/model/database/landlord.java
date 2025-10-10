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

public class landlord extends connectionAcess implements Property{
   private final String user_name;
   private final String user_email;
   private final String password;
   private final String contact;
   private final String user_type;
   private  final String property_address;


    public landlord(String user_name, String user_email, String password, String user_type, String contact, String address) throws SQLException {
        super();
        this.user_name = user_name;
        this.user_email = user_email;
        this.password = password;
        this.user_type = user_type;
        this.contact = contact;
        this.property_address = address;
    }
    public landlord() throws SQLException {
        super();
        this.user_name = "";
        this.user_email = "";
        this.password = "";
        this.user_type = "";
        this.contact = "";
        this.property_address = "";
    }


    @Override
    public void insert_information() {
           String insertUserSQL = """
                   INSERT INTO generaL_users (name, contact, email,password,user_type,property_address)
                   VALUES (?,?,?,?,?,?)
                   ON CONFLICT (email) DO NOTHING;
                   
                   """;
           try (PreparedStatement pstm = this.connection.prepareStatement(insertUserSQL)){
               pstm.setString(1,this.user_name);
               pstm.setString(2,this.contact);
               pstm.setString(3,this.user_email);
               pstm.setString(4,this.password);
               pstm.setString(5,this.user_type);
               pstm.setString(6,this.property_address);
               pstm.executeUpdate();
           }catch (SQLException e){
               throw new RuntimeException("Database update failed", e);
           }


    }

    public String getUser_name() {
        return this.user_name;
    }
    public  String getUser_email(){
        return this.user_email;
    }

    public String getPassword(){
        return  this.password;
    }

    public String confirm_password(String email) {

        String sql = "SELECT password FROM users WHERE user_email = ?";
        try (PreparedStatement pstm = this.connection.prepareStatement(sql)) {
            pstm.setString(1, email); // email is a String

            try (ResultSet rs = pstm.executeQuery()) { // use executeQuery() for SELECT
                if (rs.next()) {
                    // get password as String
                    return rs.getString("password");
                } else {
                    return  "no password";
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database query failed", e);
        }

    }
    public void autocommitfalse() throws SQLException {
        this.connection.setAutoCommit(false);

    };

    public void reverse() throws SQLException {
        this.connection.setAutoCommit(false);
        this.connection.rollback();
    };
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
        try (PreparedStatement pstmt = this.connection.prepareStatement(reference_key)) {
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
