package model.database;

import API_handlers.PropertyUnits;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Transaction extends  ConnectionAccess {
    private  Integer  tenant_Id;
    private  Integer amount_paid;
    private  boolean status;


    public Transaction(){
        this.tenant_Id = tenant_Id;
        this.amount_paid = amount_paid;
        this.status = status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
   public void setTenant_Id(Integer id){
        this.tenant_Id = id;
   }

    public Integer getTenant_Id() {
        return tenant_Id;
    }

    public void setAmount_paid(Integer amount_paid){
        this.amount_paid = amount_paid;
   }

   public  void update_tenant_status() throws SQLException {
       String SQL = """
                UPDATE  tenants_information  SET status= ? WHERE tenant_user_id=?
                """;
       try(PreparedStatement  pstm = this.connection.prepareStatement(SQL)){
           pstm.setBoolean(1,this.status);
           pstm.setInt(2,this.tenant_Id);
           pstm.executeUpdate();
       } catch (SQLException e) {
           System.out.println(e.getMessage() + " with candles lit");
           throw new SQLException("already paid");

       }


   }
    public  void  record_payment() throws SQLException {
        String SQL = """
                INSERT INTO rent_book (tenant_user_id,amount_paid,status) VALUES (?,?,?)
                """;
        try(PreparedStatement  pstm = this.connection.prepareStatement(SQL)){
            pstm.setInt(1,this.tenant_Id);
            pstm.setInt(2,this.amount_paid);
            pstm.setBoolean(3,this.status);
            pstm.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("paid");


        }

    }

static  void main() throws SQLException {
        Transaction test = new Transaction();
        test.setStatus(true);
        test.setTenant_Id(807);
        test.update_tenant_status();

}
}
