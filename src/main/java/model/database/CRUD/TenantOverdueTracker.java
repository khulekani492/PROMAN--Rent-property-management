package model.database.CRUD;

import model.database.ConnectionAccess;
import org.postgresql.shaded.com.ongres.scram.common.bouncycastle.pbkdf2.Strings;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TenantOverdueTracker extends ConnectionAccess {
  private  Integer landlordId;


  public TenantOverdueTracker(){
      this.landlordId = landlordId;

  }
    public void call_postgres_func(Integer landlordId) {
        String sql = "SELECT update_overdue_date_by_landlord(?)";

        try (PreparedStatement pstmt = this.connection.prepareStatement(sql)) {

            pstmt.setInt(1, landlordId);
            pstmt.execute();

        } catch (SQLException e) {
            System.out.println("SQL Error: " + e.getMessage());
            throw new RuntimeException("Function failed");
        }
    }

public static  void main(Strings[] args) throws SQLException {
      Integer sa = 771;
      TenantOverdueTracker new_day = new TenantOverdueTracker();
      new_day.call_postgres_func(sa);
  }
}
