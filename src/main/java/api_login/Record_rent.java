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
            Integer  tenantId = Integer.parseInt(ctx.formParam("id"));
            String property_name = String.valueOf(ctx.formParam("property_name"));
            System.out.println(property_name);
            System.out.println("rent : " + rent_amount );
            System.out.println("ID : " + tenantId );


            _payment.setTenant_Id(tenantId);
            _payment.setAmount_paid(rent_amount);
            _payment.setStatus(true);
            _payment.update_tenant_status(); // updates only the status to true when tenant is marked as paid

            HashMap<String,String> MODEL = new HashMap<>();
            try{
                _payment.record_payment();
                String email1 = ctx.sessionAttribute("email");
                System.out.print(email1 + "weeeh");
                String unit_paid = ctx.sessionAttribute("unit");
                MODEL.put("success",unit_paid);
                String clone_unit = ctx.pathParam("id");
//                System.out.println(clone_unit + "Tenant ID");
                Getunits property_units = new Getunits();
                landlord authenticate = new landlord();


                HashMap<Integer, ArrayList<String>> fetch_all = property_units.getOccupiedUnits(property_name,authenticate.landlordId(email1));

                if(fetch_all.size() == 0){

                    String property = ctx.sessionAttribute("property_name");
                    MODEL.put("no_units","No units added for " + property);
                    ctx.render("templates/dashboard.html",MODEL);
                }else {
                    Map<String, Object> data = new HashMap<>();
                    HashMap<String,HashMap<Integer,ArrayList<String>>> model = new HashMap<>();
                    HashMap<String,String> model2 = new HashMap<>();
                    Map<String, Object> model1 = new HashMap<>();
                    System.out.println(fetch_all + "Occupied units");
                    model.put("units",fetch_all);
                    //using landlord_unique_id to fetch all of their properties ,it accessed with the user_email
                    String email = ctx.sessionAttribute("email");
                    propertyNames default_properties = new propertyNames();

                    landlord user_id = new landlord();
                    Set<String> landlord_properties = default_properties.fetchAllproperty(user_id.landlordId(email));

                    model.put("units",fetch_all);
                    model1.put("names",landlord_properties);
                    model2.put("unit",clone_unit);
                    data.putAll(model);
                    data.putAll(model1);
                    data.putAll(model2);
                    data.putAll(MODEL);

                    System.out.println(data);
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
