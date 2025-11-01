package model.database;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class roomHist extends ConnectionAccess implements Property  {
    private final Integer propertyId ;
    private final Integer roomNo;
    private final String  tenantname;
    private final String contact;
    private final Date move_in;
    private final  Date move_out;



    public roomHist(Integer propertyId, Integer room, String tenantname, String contact, Date moveIn, Date moveOut) throws SQLException {
        super();
        this.propertyId = propertyId;
        this.roomNo = room;
        this.tenantname = tenantname;
        this.contact = contact;
        this.move_in = moveIn;
        this.move_out = moveOut;
    }

    @Override
    public void insert_information() {
        String propertySQL = """
    INSERT INTO room_history (propertyId, tenant_name, move_in, cellphone_number, room_number)
    VALUES (?, ?, ?, ?, ?)
""";

        try (PreparedStatement pstmt = connection.prepareStatement(propertySQL)) {
            pstmt.setInt(1, this.propertyId);
            pstmt.setString(2, this.tenantname);
            pstmt.setDate(3,this.move_in);
            pstmt.setString(4,this.contact);
            pstmt.setInt(5, this.roomNo);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public Integer UniqueID() {
        return 0;
    }
}
