package API_handlers;

import api_login.Type_of_payments;
import io.javalin.http.Handler;
import io.javalin.http.HttpStatus;
import model.database.CRUD.Tenant;
import model.database.Transaction;

import java.sql.SQLException;
import java.util.Map;

public class UpdateTenants {
    public Handler unmark_payment(){
        return ctx ->{
            // --- 1. Data Extraction ---
            Tenant undo_tenant_payment = new Tenant();

            // Getting IDs and Property Info (Necessary for payment logic and subsequent page reload if needed)
            String tenant_name = ctx.formParam("id");
            ctx.sessionAttribute("ID",tenant_name);
            //Integer
            Integer tenantId = undo_tenant_payment.tenant_ID(tenant_name);
            
            //removes payment from rent_book 
            undo_tenant_payment.unmark_tenant_rent(tenantId);
            
            //Unmark status tenant as paid
            undo_tenant_payment.reset_payment_status(tenantId);
            ctx.sessionAttribute("current_tenant_payment",tenantId);

                    };
    }

    }

