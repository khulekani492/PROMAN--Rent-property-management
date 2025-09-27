package model.database;

import java.sql.Connection;
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
   private String DB_URL = "jdbc:sqlite:properties.db";

    public landlord(Connection connect) {
        super(connect);
    }


    @Override
    public void insert_information() {


    }


    @Override
    public Integer UniqueID(String name) {
        String reference_key = """
        SELECT id FROM residence WHERE property_name = ?;
    """;
        try (PreparedStatement pstmt = connector.prepareStatement(reference_key)) {
            pstmt.setString(1, name);
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
