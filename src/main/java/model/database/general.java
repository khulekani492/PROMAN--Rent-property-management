package model.database;

import io.javalin.http.Context;
import io.javalin.http.Handler;

import java.sql.*;

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

public class general extends ConnectionAccess implements Property{
   private final String user_name;
   private final String user_email;
   private  String password;
   private final String contact;
   private String user_type;

   public general(String user_name, String contact,String user_email,
                   String password, String user_type
    ) throws SQLException {
        super();
        this.user_name = user_name;
        this.contact = contact;
        this.user_email = user_email;
        this.password = password;
        this.user_type = user_type;
    }

    public general() throws SQLException {
        super();
        this.user_name = "";
        this.user_email = "";
        this.password = "";
        this.user_type = "";
        this.contact = "";
    }

    public general(String name, String number, String user_type) throws SQLException{
        super();
        this.user_name = name;
        this.contact  = number;
        this.user_email = "";
        this.password = "";
        this.user_type = user_type;
    }



public void  landlord_insert_tenant(){
        this.user_type = "tenant";
        this.password = "null";
        String insertUserSQL = """
                   INSERT INTO generaL_users (name, contact,user_type,password)
                   VALUES (?,?,?,?)
                   ON CONFLICT (email) DO NOTHING;
                   """;
        try (PreparedStatement pstm = this.connection.prepareStatement(insertUserSQL)){
            pstm.setString(1,this.user_name);
            pstm.setString(2,this.contact);
            pstm.setString(3,this.user_type);
            pstm.setString(4,this.password);
            pstm.executeUpdate();
        }catch (SQLException e){
            throw new RuntimeException("Database update failed", e);
        }
    }


    @Override
    public void insert_information() throws SQLException {
           String insertUserSQL = """
                   INSERT INTO generaL_users (name, contact,email,password
                   ,user_type)
                   VALUES (?,?,?,?,?);
                   """;
           try (PreparedStatement pstm = this.connection.prepareStatement(insertUserSQL)){
               pstm.setString(1,this.user_name);
               pstm.setString(2,this.contact);
               pstm.setString(3,this.user_email);
               pstm.setString(4,this.password);
               pstm.setString(5,this.user_type);
               pstm.executeUpdate();

           } catch (SQLException e) {
               if ("23505".equals(e.getSQLState())) {
                   // rethrow to be caught in your route handle
                   throw new SQLException("user exists", e);
               } else {
                   throw new SQLException("Insert failed: " + e.getMessage(), e);
               }
           }
    }
    public String getUser_name() {
        return this.user_name;
    }
    public  String getUser_email(){
        return this.user_email;
    }
    public  String getUser_type(){return  this.user_type;}
    public  String getContact(){
        return  this.contact;
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

    /** New session
     * Get the unique ID using the user's email.
     * <p>
     * User emails are unique identifiers in their own right. For security,
     * this helps narrow the issue of fetching the wrong ID for a user causing leakages.
     * </p>
     */



    @Override
    public Integer UniqueID() {

        String reference_key = """
        SELECT id FROM general_users WHERE email = ?;
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
    public Integer tenant_ID() {
        String reference_key = """
        SELECT id FROM general_users WHERE contact = ?;
    """;
        try (PreparedStatement pstmt = this.connection.prepareStatement(reference_key)) {
            pstmt.setString(1, this.contact);
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

    public void assignProperty(Integer december) {
        String propertySQL = """
    INSERT INTO properties (landlord_user_id)
    VALUES (?);
""";

        try (PreparedStatement pstmt = connection.prepareStatement(propertySQL)) {
            pstmt.setInt(1, december);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public String getTenantnamebyId(Integer tenantUniqueID) {

        String reference_key = """
        SELECT name FROM general_users WHERE id = ?;
    """;
        try (PreparedStatement pstmt = this.connection.prepareStatement(reference_key)) {
            pstmt.setInt(1, tenantUniqueID);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("name");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null; // if not found
    }
}
