package Update_api;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import model.database.CRUD.Tenant;
import model.database.UPDATES.Change_Unit;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class UpdateUnit{

    public Handler change_tenant(){
        return ctx ->{
            String tenant_name = ctx.sessionAttribute("tenant_name");
            Integer propertyUnit = ctx.sessionAttribute("tenant_unit");
            String propertyName = ctx.sessionAttribute("current_property");

            //Get TenantId
            Tenant current_tenant = new Tenant();
            Integer  tenantId =  current_tenant.tenant_ID(tenant_name);
            Change_Unit update_tenant_unit = new Change_Unit(tenant_name,propertyUnit,propertyName,tenantId);

            update_tenant_unit.vacant_unit(propertyUnit);
            update_tenant_unit.Change_unit();
            HashMap<String,String> model  = new HashMap<>();

            model.put("update_message","Tenant changed Unit");
            ctx.render("templates/user_profile.html",model);
        };
    }

}
