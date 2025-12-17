package api_login;

import API_handlers.Dashboard;
import io.javalin.http.Handler;
import io.javalin.http.HttpStatus; // Import HttpStatus to return 200 OK
import model.database.CRUD.*;
import model.database.Transaction;

import java.sql.SQLException;
import java.util.*;
public class Record_rent {
    public Handler payment(){
        return ctx ->{
            // --- 1. Data Extraction with Proper Error Handling ---
            Transaction _payment = new Transaction();
            Tenant tenant_relatedInfo = new Tenant();

            String rent_amount = null;
            String unit = null;
            String tenant_name = null;
            String contact = null;

            try {
                // Extract all required parameters
                rent_amount = ctx.formParam("rent");
                unit = ctx.formParam("unit");
                tenant_name = ctx.formParam("id");
                contact = ctx.formParam("tenant_contact") ;
                ctx.sessionAttribute("tenant_contact",contact);
                //ctx.sessionAttribute("current_units_property",null);
                System.out.println("Tenant cellphone : " + contact);
                System.out.println("DEBUG - Payment parameters:");
                System.out.println("Rent amount: " + rent_amount);
                System.out.println("Unit: " + unit);
                System.out.println("Tenant name: " + tenant_name);
                System.out.println("All form params: " + ctx.formParamMap());

                // Validate required parameters
                if (rent_amount == null || rent_amount.trim().isEmpty()) {
                    ctx.status(400).result("Missing required parameter: rent");
                    return;
                }

                if (unit == null || unit.trim().isEmpty()) {
                    ctx.status(400).result("Missing required parameter: unit");
                    return;
                }

                if (tenant_name == null || tenant_name.trim().isEmpty()) {
                    ctx.status(400).result("Missing required parameter: id");
                    return;
                }

                // Parse rent amount
                int money_paid = Integer.parseInt(rent_amount.trim());

                // Validate rent is positive
                if (money_paid <= 0) {
                    ctx.status(400).result("Rent amount must be greater than 0");
                    return;
                }

                // Get tenant ID
                Integer tenantId = tenant_relatedInfo.tenant_ID(tenant_name,contact);
                System.out.println("tenantId " + tenantId);
                if (tenantId == null) {
                    ctx.status(404).result("Tenant not found: " + tenant_name);
                    return;
                }

                // Set session attributes
                ctx.sessionAttribute("ID", tenant_name);
                ctx.sessionAttribute("current_tenant_payment", tenantId);

                Tenant tenant_property = new Tenant();
                String property_name = tenant_property.tenant_property_name(tenantId);
                Integer property_rent = tenant_property.tenant_property_rent(tenantId);

                System.out.println("I got say : " + property_name);
                ctx.sessionAttribute("check_tenant_id", tenantId);
                ctx.sessionAttribute("propertyName", property_name);
                ctx.sessionAttribute("propertyRent", property_rent);
                System.out.println("Original rent : " + property_rent);
                // --- 2. Payment Processing ---
                Type_of_payments determine_pay = new Type_of_payments();
                determine_pay.type_of_payment(money_paid, property_rent,
                        tenant_property.tenant_debt(tenantId),
                         tenantId, _payment);

                System.out.println("LATEST UPDATE: Payment processed for ID: " + tenantId + ", Unit: " + unit);


                // --- 3. Database Write & Respon se ---
                try {
                    _payment.setTenant_Id(tenantId);
                    _payment.record_payment();
                    System.out.println("No debate ");

                    Integer landlord_id =  ctx.sessionAttribute("landlordID");
               // Success response
                    ctx.status(200);
                    ctx.sessionAttribute("current_units_property",new Property_Status().property_tenants(property_name,landlord_id));
                    ctx.json(Map.of(
                            "success", true,
                            "unit", unit,
                            "message", "Payment recorded  successfully",
                            "debt_status", _payment.getDebtString()
                    ));


                } catch (SQLException e) {
                    if("paid".equals(e.getMessage())) {
                        ctx.status(409); // 409 Conflict for duplicate payment
                        ctx.json(Map.of(
                                "error", true,
                                "message", "Payment already recorded for this month."
                        ));
                    } else {
                        ctx.status(500);
                        ctx.json(Map.of(
                                "error", true,
                                "message", "Database Error: " + e.getMessage()
                        ));
                    }
                }

            } catch (NumberFormatException e) {
                // Handle invalid rent format
                ctx.status(400);
                ctx.json(Map.of(
                        "error", true,
                        "message", "Invalid rent amount format. Must be a number."
                ));
            } catch (Exception e) {
                // Catch any other exceptions
                e.printStackTrace();
                ctx.status(500);
                ctx.json(Map.of(
                        "error", true,
                        "message", "Internal server error: " + e.getMessage()
                ));
            }
        };
    }
}