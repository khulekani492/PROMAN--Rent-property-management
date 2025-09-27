package model.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

public class Tenant extends  connectionAcess implements  Property{
    private final String name;
    private final LocalDate moveInDate ;
    private final LocalDate moveOut;
    private final String employment;
    private final String cell_number;
    private final String pay_day;
    private final  int room_number;
    private final  int  room_price;
    private final  int debt;
    private final  String kin_name;
    private final  String kin_number;



    public Tenant(Connection connect, String name, LocalDate moveInDate,LocalDate moveOut,String employment,String cell_number,
                  String pay_day,int room_number,int room_price,int debt,String kin_name,String kin_number) {
        super(connect);
        this.name = name;
        this.moveInDate = moveInDate;
        this.moveOut = moveOut;
        this.employment = employment;
        this.cell_number = cell_number;
        this.pay_day = pay_day;
        this.room_number = room_number;
        this.room_price = room_price;
        this.debt = debt;
        this.kin_name = kin_name;
        this.kin_number = kin_number;
    }

    @Override
    public void insert_information() {
         String addtenantSql = """
                 INSERT INTO tenants (propertyId,name,move_in,move_out,employment,cell_number,pay_day,room_number,Room_price,debt,kin_name,kin_number)
                 """;
         try (PreparedStatement pstm = connection.prepareStatement(addtenantSql)){
             pstm.setString(1,this.user_name);
             pstm.setString(2,this.user_email);
             pstm.setString(3,this.password);
             pstm.executeUpdate();

         }catch (SQLException e) {
             throw new RuntimeException();
         }
    }

    @Override
    public Integer UniqueID() {
        return 0;
    }
}
