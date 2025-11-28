package api_login;

import io.javalin.http.Handler;
import io.javalin.http.HttpStatus; // Import HttpStatus to return 200 OK
import model.database.CRUD.Getunits;
import model.database.CRUD.Tenant;
import model.database.CRUD.landlord;
import model.database.CRUD.propertyNames;
import model.database.Transaction;

import java.sql.SQLException;
import java.util.*;

public class Record_rent {
    public Handler payment(){
        return ctx ->{
            // --- 1. Data Extraction ---
            Transaction _payment = new Transaction();
            Tenant tenant_relatedInfo = new Tenant();


            // Note: Integer.parseInt will throw NumberFormatException if ctx.formParam("rent") is null or non-numeric.

            Integer rent_amount = Integer.parseInt( ctx.formParam("rent") );
            String unit = ctx.formParam("unit");



            // Getting IDs and Property Info (Necessary for payment logic and subsequent page reload if needed)
            String tenant_name = ctx.formParam("id");

             ctx.sessionAttribute("ID",tenant_name);
            //Integer
            Integer tenantId = tenant_relatedInfo.tenant_ID(tenant_name);

            ctx.sessionAttribute("current_tenant_payment",tenantId);
            Tenant tenant_property = new Tenant();
            String property_name = tenant_property.tenant_property_name(tenantId);
            Integer property_rent = tenant_property.tenant_property_rent(tenantId);

            // Set session attributes (needed if you want to redirect later, but not required for AJAX success)
            ctx.sessionAttribute("check_tenant_id",tenantId);
            ctx.sessionAttribute("propertyName",property_name);
            ctx.sessionAttribute("propertyRent",property_rent);

            Integer confirms = ctx.sessionAttribute("check_tenant_id");

            // --- 2. Payment Processing ---
            Type_of_payments determine_pay = new Type_of_payments();
            determine_pay.type_of_payment(rent_amount,property_rent,tenant_property.tenant_debt(tenantId),tenantId,_payment);


            System.out.println("LATEST UPDATE: Payment processed for ID: " + tenantId + ", Unit: " + unit);

            // --- 3. Database Write & Response ---
            try{
                _payment.record_payment();

                // ⭐ CRITICAL FIX: DO NOT RENDER THE DASHBOARD HERE ⭐
                // The client-side JavaScript is waiting for a simple confirmation.

                // Return a simple HTTP 200 OK status to the client-side fetch.
                // The client-side JavaScript will then execute the success block and update the UI.
                ctx.status(HttpStatus.OK);
                ctx.json(Map.of("success", true, "unit", unit)); // Optional: send a small JSON response

                // If you absolutely needed to refresh the client side to update the Thymeleaf model:
                // ctx.status(HttpStatus.OK); // You'd still send 200, then client JS calls window.location.reload();

            } catch (SQLException e) {
                if("paid".equals(e.getMessage())){
                    // If payment is duplicate, return an error status (e.g., 400 Bad Request)
                    ctx.status(HttpStatus.BAD_REQUEST);
                    ctx.result("Payment already recorded for this month.");
                    // The client-side catch block will handle this status.
                } else {
                    // For general DB errors, return 500
                    ctx.status(HttpStatus.INTERNAL_SERVER_ERROR);
                    ctx.result("Database Error: " + e.getMessage());
                    throw new RuntimeException(e);
                }
            }

            // --- 4. Removed Code ---
            // The entire block for fetching units, property names, and calling ctx.render()
            // has been REMOVED because it was causing the AJAX failure.
            // When using AJAX, the client handles the display update.

        };
    }
}