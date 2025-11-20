package model.database.CRUD;

import model.database.ConnectionAccess;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TenantOverdueTracker extends ConnectionAccess {
  private  Integer landlordId;


  public TenantOverdueTracker(Integer landlordId){
      this.landlordId = landlordId;

  }
  public void call_postgres_func() throws SQLException {
     String sql = """
              SELECT update_overdue_date_by_landlord(?);
              """ ;
      try(PreparedStatement pstmt = this.connection.prepareStatement(sql)){
                 pstmt.setInt(1,this.landlordId);
                 pstmt.executeUpdate();

      } catch (SQLException e){
          System.out.println(e.getMessage());
          throw new RuntimeException("ayi update");
      }
  }

}
