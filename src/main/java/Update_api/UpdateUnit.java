package Update_api;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import model.database.CRUD.Tenant;
import model.database.CRUD.landlord;
import model.database.UPDATES.Change_Unit;
import model.database.residence;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.util.HashMap;

public class UpdateUnit{

    public Handler change_tenant(){
        return ctx ->{
            String tenant_name = ctx.sessionAttribute("tenant_name");
            Integer propertyUnit = ctx.sessionAttribute("tenant_unit");
            String propertyName = ctx.sessionAttribute("current_property");
            String contact =  ctx.sessionAttribute("tenant_contact");
            //Get TenantId
            Tenant current_tenant = new Tenant();
            Integer  tenantId =  current_tenant.tenant_ID(tenant_name,contact);
            Change_Unit update_tenant_unit = new Change_Unit(tenant_name,propertyUnit,propertyName,tenantId);

            update_tenant_unit.vacant_unit(propertyUnit);
            update_tenant_unit.Change_unit();
            HashMap<String,String> model  = new HashMap<>();

            model.put("update_message","Tenant changed Unit");
            ctx.render("templates/user_profile.html",model);
        };
    }

    public Handler update_property_unit() {
        return ctx -> {
            String new_unit = ctx.formParam("new_unit");
            Integer propertyUnit = ctx.sessionAttribute("tenant_unit");
            String propertyName = ctx.sessionAttribute("current_property");
            String email = ctx.sessionAttribute("login_email");
            //Get TenantId
            landlord getUserID = new landlord();
            Integer landlordId = getUserID.landlordId(email);
            String property_address = getUserID.landlord_property_address(landlordId);
            //DE create rows of the property units for the landlord
            residence create_rooms = new residence();
            create_rooms.setlandlord(landlordId);
            create_rooms.setProperty_Name(propertyName);
            create_rooms.setProperty_address(property_address);
            try {
                create_rooms.setProperty_unit(Integer.parseInt(new_unit));
                create_rooms.setTotal_units();
                return;
            } catch (SQLException e) {
                System.out.println("please");
                ctx.redirect("error/same_unit");

            }

            HashMap<String,String> model  = new HashMap<>();
            model.put("update_message","Tenant changed Unit");
            ctx.render("templates/user_profile.html",model);
        };
    }

}
