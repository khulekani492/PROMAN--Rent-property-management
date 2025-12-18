package Update_api;

import io.javalin.http.Handler;
import model.database.CRUD.Property_Status;
import model.database.CRUD.Tenant;

public class Remove {

    public Handler remove_tenant(){
        return ctx -> {
            System.out.println("Ghost town");
            //properties
            //tenant_information
            //MY only concern is the properties table tenant_user_id must be null
            Tenant tenant = new Tenant();
            Integer tenantId = ctx.sessionAttribute("TenantID");
            String tenant_name = ctx.sessionAttribute("tenant_name");
            String propertyName = ctx.sessionAttribute("current_property");
            Integer landlord_id =  ctx.sessionAttribute("landlordID");
            System.out.println("Tenant name from Memory " + tenant_name);

            String contact =  ctx.sessionAttribute("tenant_contact");
            if (tenantId == null) {
                tenantId =  tenant.tenant_ID(tenant_name,contact);
                if(tenantId == null){
                    System.out.println("No tenant Id " + tenantId);
                    ctx.render("templates/not_found.html");
                    return;
                }
            }
            Tenant tenant_leaves_room = new Tenant();
            try{
                tenant_leaves_room.vacant_unit(tenantId);
                System.out.println("Property Name : "+  propertyName);
                System.out.println("Property landlord Id : "+  landlord_id);
                ctx.sessionAttribute("current_units_property",new Property_Status().property_tenants(propertyName,landlord_id));
                System.out.println("Tenant has been removed : ");

                System.out.println(new Property_Status().property_tenants(propertyName,landlord_id) );
                ctx.redirect("/dashboard");

            } catch (Exception e) {
                System.out.println("Can not vacant the unit for  " + tenantId);
                ctx.render("templates/not_found.html");

            }


        };
    }
}
