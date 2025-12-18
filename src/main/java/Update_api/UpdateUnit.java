package Update_api;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import model.database.CRUD.Property_Status;
import model.database.CRUD.Tenant;
import model.database.CRUD.landlord;
import model.database.UPDATES.Change_Unit;
import model.database.residence;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.sql.Date;
import java.util.HashMap;
import java.util.Objects;

public class UpdateUnit{

    public Handler update_tenant(){
        return ctx ->{


            Integer propertyUnit = Integer.parseInt(Objects.requireNonNull(ctx.sessionAttribute("tenant_unit")));
            String propertyName = ctx.sessionAttribute("current_property");
            String tenant_name = ctx.sessionAttribute("tenant_name");
            System.out.println("Tenant name from Memory " + tenant_name);
            String contact =  ctx.sessionAttribute("tenant_contact");
            String property_rent = ctx.sessionAttribute("property_rent");

            //Get TenantId
            Tenant tenant = new Tenant();
            Integer tenantId = ctx.sessionAttribute("TenantID");
            if (tenantId == null) {
                tenantId =  tenant.tenant_ID(tenant_name,contact);
                if(tenantId == null){
                    ctx.render("templates/not_found.html");
                }
            }
            Integer landlord_id =  ctx.sessionAttribute("landlordID");
            Change_Unit update_tenant_unit = new Change_Unit(tenant_name,propertyUnit,propertyName,tenantId);
            update_tenant_unit.SetContact(contact);
            try {
                update_tenant_unit.update_TenantInformation();


            } catch (RuntimeException e) {
                ctx.render("templates/not_found.html");
                return;
            }

            String kin_name = ctx.sessionAttribute("kin_name");
            System.out.println("Kin Name " + kin_name);


            String kin_number = ctx.sessionAttribute("kin_contact");
            System.out.println("Kin Number " + kin_number);
            String rent_payment_day = ctx.sessionAttribute("tenant_rent");
            System.out.println("tenant Rent " + kin_number);
            String   moveIn = ctx.sessionAttribute("move_in");
            System.out.println("Mve IN " + moveIn);
            String  moveOut = ctx.sessionAttribute("move_out");

            Change_Unit update_additional_information = new Change_Unit(kin_name,kin_number,rent_payment_day,Date.valueOf(moveIn), null ,tenantId);
            update_additional_information.additional_tenantINFO();
            HashMap<String,String> model  = new HashMap<>();
            System.out.println("Stupid boy : ");
            System.out.println("Property Name : "+  propertyName);
            System.out.println("Property landlord Id : "+  landlord_id);
            System.out.println(new Property_Status().property_tenants(propertyName,landlord_id) );
            ctx.sessionAttribute("current_units_property",new Property_Status().property_tenants(propertyName,landlord_id));
            model.put("update_message","Tenant changed Unit");
            ctx.redirect("/dashboard");
        };
    }

    public Handler create_property_unit() {
        return ctx -> {
            String new_unit = ctx.formParam("unit_number");
            String amount = ctx.formParam("rent_amount");
            System.out.println("U  unit number " + new_unit);

            Integer Unit_updated = Integer.parseInt(new_unit);
            String propertyName = ctx.sessionAttribute("current_property");
            System.out.println("PROPERTY NAME");
            String email = ctx.sessionAttribute("email");
            System.out.println("Email : " + email);
            //ctx.sessionAttribute("current_units_property",null);

            //Get TenantId
            landlord getUserID = new landlord();
            Integer landlordId = getUserID.landlordId(email);
            String property_address = getUserID.landlord_property_address(landlordId);

            //DE create rows of the property units for the landlord
            residence create_rooms = new residence();
            create_rooms.setlandlord(landlordId);
            create_rooms.setProperty_Name(propertyName);
            create_rooms.setRent(amount);
            create_rooms.setProperty_address(property_address);
            create_rooms.setProperty_unit(Unit_updated);
            try {
                create_rooms.New_unit();
                ctx.sessionAttribute("current_units_property",new Property_Status().property_tenants(propertyName,landlordId));
                ctx.sessionAttribute("property_unit",Unit_updated);
               // ctx.redirect("/add_tenant");

                ctx.status(200).result("ok");
                return;

            } catch (SQLException e) {
                System.out.println("same unit Error for new unit");
                ctx.status(403).result("Unit exist");

            }

        };
    }

}
