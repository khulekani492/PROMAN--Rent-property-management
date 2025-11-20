package api_login;

import io.javalin.http.Handler;
import model.database.CRUD.Getunits;
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
            Integer rent_amount = Integer.parseInt( ctx.formParam("rent") );
            String unit = ctx.formParam("unit");
            ctx.sessionAttribute("unit",unit);
            Integer  tenantId = Integer.parseInt(ctx.formParam("id"));
            String property_name = String.valueOf(ctx.formParam("property_name"));
            System.out.println("amount : " + property_name);
            System.out.println("rent : " + rent_amount );
            System.out.println("ID : " + tenantId );

            _payment.setTenant_Id(tenantId);
            _payment.setAmount_paid(rent_amount);
            _payment.setStatus(true);
            _payment.update_tenant_status(); // updates only the status to true when tenant is marked as paid

            HashMap<String,String> success_status = new HashMap<>();
            try{
                _payment.record_payment();
                String email1 = ctx.sessionAttribute("email");
                System.out.print(email1 + "current email in ram memory");
                String unit_paid = ctx.sessionAttribute("unit");
                success_status.put("success",unit_paid);
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
                    Set<String> landlord_properties = default_properties.fetchAllproperty(user_id.landlordId(email));

                    model_units.put("units",fetch_all);
                    model_property_names.put("names",landlord_properties);

                    data.putAll(model_units);
                    data.putAll(model_property_names);
                    data.putAll(success_status);
                    System.out.println("Successful new updates");
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
