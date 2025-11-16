package api_login;

import io.javalin.http.Handler;
import model.database.Transaction;

import java.sql.SQLException;
import java.util.HashMap;

public class Record_rent {
    public Handler payment(){
        return ctx ->{
            Transaction _payment = new Transaction();
            String tenant_id = ctx.sessionAttribute("tenant");
            try{
                Integer rent_amount = Integer.parseInt( ctx.formParam("rent") );
                _payment.setAmount_paid(rent_amount);
            } catch (NumberFormatException e) {
                throw new RuntimeException(e);            }

            String unit = ctx.formParam("unit");
            try {
                Integer  tenantId = Integer.parseInt(ctx.formParam("id"));
                _payment.setTenant_Id(tenantId);
            } catch (NumberFormatException e) {
                throw new RuntimeException(e);
            }

            try{
                _payment.setStatus(true);
                _payment.record_payment();
            } catch (SQLException e) {
                System.out.println("Payment DID NOT GOT THROUGH " + e.getMessage());
                throw new RuntimeException(e);
            }


        };
    }
}
