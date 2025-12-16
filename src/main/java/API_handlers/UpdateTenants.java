package API_handlers;

import api_login.Type_of_payments;
import io.javalin.http.Handler;
import io.javalin.http.HttpStatus;
import model.database.CRUD.Property_Status;
import model.database.CRUD.Tenant;
import model.database.Transaction;

import java.sql.SQLException;
import java.util.Map;

public class UpdateTenants {
    public Handler unmark_payment(){
        return ctx ->{
            // --- 1. Data Extraction ---
            System.out.println("Unmarking the payment");
            Tenant undo_tenant_payment = new Tenant();

            // Getting IDs and Property Info (Necessary for payment logic and subsequent page reload if needed)
            String tenant_name = ctx.formParam("id");
            String tenant_contact = ctx.pathParam("tenantContact");
            ctx.sessionAttribute("ID",tenant_name);
            //String contact = ctx.sessionAttribute("tenant_contact");
            //Integer
            Integer tenantId = undo_tenant_payment.tenant_ID(tenant_name,tenant_contact);
            
            //removes payment from rent_book 
            undo_tenant_payment.unmark_tenant_rent(tenantId);
            
            //Unmark status tenant as paid
            undo_tenant_payment.reset_payment_status(tenantId);

            Integer landlord_id =  ctx.sessionAttribute("landlordID");
            String propertyName =  ctx.sessionAttribute("current_property");

            System.out.println(propertyName + "   property Name");
            System.out.println(landlord_id + "    landlord Id");

            //Updates the units list in the caches to the latest value in the db
            ctx.sessionAttribute("current_units_property",new Property_Status().property_tenants(propertyName,landlord_id));
            ctx.sessionAttribute("current_tenant_payment",tenantId);
            ctx.status(200);
            ctx.json(Map.of(
                    "success", true,
                    "unit", "00" + " till I reach Christ Conscious",
                    "message", "Payment recorded  successfully"
            ));


                    };
    }

    }

