package api_login;

import io.javalin.http.Handler;
import model.database.CRUD.Getunits;
import model.database.CRUD.Tenant;
import model.database.CRUD.landlord;
import model.database.CRUD.propertyNames;
import model.database.Transaction;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Record_rent {
    public Handler payment(){
        return ctx ->{
            Transaction _payment = new Transaction();
            Tenant tenant_relatedInfo = new Tenant();
            Integer rent_amount = Integer.parseInt( ctx.formParam("rent") );
            String unit = ctx.formParam("unit");
            ctx.sessionAttribute("unit",unit);

            String  tenant_name =  String.valueOf (ctx.formParam("id"));
            Integer tenantId = tenant_relatedInfo.tenant_ID(tenant_name);
            //Get tenant_property_name
            Tenant tenant_property = new Tenant();

            String property_name = tenant_property.tenant_property_name(tenantId);

            //Get property_rent
            Integer property_rent = tenant_property.tenant_property_rent(tenantId);
            //Set the property_name to the session
            ctx.sessionAttribute("propertyName",property_name);

            //Run the payments, check the tenant debt if they exist makes required deduction
            Type_of_payments determine_pay = new Type_of_payments();
            determine_pay.type_of_payment(rent_amount,property_rent,tenant_property.tenant_debt(tenantId),tenantId,_payment);

            ctx.sessionAttribute("propertyRent",property_rent);
            //
            System.out.println("LATEST UPDATE: ");
            System.out.println("property_name : " + property_name);
            System.out.println("rent : " + rent_amount );
          //  determine_pay.type_of_payment(rent_amount);
            System.out.println("ID : " + tenantId );
//              insert to rent_book the correct amount the type_of_payments determines the final calculation
//            _payment.setTenant_Id(tenantId);
//            _payment.setAmount_paid(rent_amount);
//            _payment.setStatus(true);
//            _payment.update_tenant_status(); // updates only the status to true when tenant is marked as paid

            HashMap<String,String> success_status = new HashMap<>();
            try{
                _payment.record_payment();
                String email1 = ctx.sessionAttribute("email");
                System.out.print(email1 + "current email in ram memory");
                String unit_paid = ctx.sessionAttribute("unit");
                Getunits property_units = new Getunits();
                landlord authenticate = new landlord();
                HashMap<Integer, ArrayList<String>> fetch_all = property_units.getOccupiedUnits(property_name,authenticate.landlordId(email1));
                if(fetch_all.size() == 0){

                    String property = ctx.sessionAttribute("property_name");
                    success_status.put("no_units","No units added for " + property);
                    ctx.render("templates/dashboard.html",success_status);
                }else {
                    Map<String, Object> data = new HashMap<>();
                    HashMap<String,HashMap<Integer,ArrayList<String>>> model_units = new HashMap<>();
                    Map<String, Object> model_property_names = new HashMap<>();
                    System.out.println(fetch_all + " Occupied units");
                    //using landlord_unique_id to fetch all of their properties ,it accessed with the user_email
                    String email = ctx.sessionAttribute("email");
                    System.out.println(" email in Memory is session " + email);
                    propertyNames default_properties = new propertyNames();
                    landlord user_id = new landlord();

                    success_status.put("success",unit_paid);
                    System.out.println("Wait Now");
                    System.out.println(success_status);
                    Set<String> landlord_properties = default_properties.fetchAllproperty(user_id.landlordId(email));

                    model_units.put("units",fetch_all);
                    model_property_names.put("names",landlord_properties);


                    data.putAll(model_units);
                    data.putAll(model_property_names);
                    data.putAll(success_status);
                    System.out.println("Successful new updates");
                    //ctx.redirect("/payment_status/" + property_name +"/"+ email +"/" +unit);
                    ctx.render("templates/dashboard.html",data);
                }
                {
                }
            } catch (SQLException e) {
                if("paid".equals(e.getMessage())){
                    ctx.sessionAttribute("error",e.getMessage());
                    ctx.sessionAttribute("unit",unit);
                    ctx.redirect("/error/duplicate_payment");
                    return;
                }

                throw new RuntimeException(e);
            }


        };
    }
}
