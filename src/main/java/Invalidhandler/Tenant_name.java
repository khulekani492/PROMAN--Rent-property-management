package Invalidhandler;

import io.javalin.http.Handler;

import java.util.HashMap;
import java.util.Map;

public class Tenant_name implements  ErrorHandler{

    @Override
    public Handler error_message() {
        return ctx -> {
            Map<String, Object> model = new HashMap<>();
            Integer property_total_unit = ctx.sessionAttribute("property_unit");
            String tenant_name = ctx.sessionAttribute("tenant_name");
            String tenant_number = ctx.sessionAttribute("tenant_number");
            String tenant_pay = ctx.sessionAttribute("tenant_pay");
            String tenant_moveIn = ctx.sessionAttribute("tenant_MoveIn");
            String tenant_kin_number = ctx.sessionAttribute("tenant_kin_number");
            String tenant_kin_name = ctx.sessionAttribute("tenant_kin_number");
            String rent_payment_day = ctx.sessionAttribute("tenant_rent_payment");
            Integer tenant_debt = ctx.sessionAttribute("debt");
            String themeColor = ctx.sessionAttribute("theme_color");
            model.put("user_chosen_theme",themeColor);
            String  propertyName = ctx.sessionAttribute("property_name");
            Integer  chosen_unit = ctx.sessionAttribute("chosen_unit");

            model.put("tenant_name",tenant_name);
            model.put("tenant_pay",tenant_pay);
            model.put("tenant_number",tenant_number);
            model.put("tenant_moveIn",tenant_moveIn);
            model.put("tenant_kin_number",tenant_kin_number);
            model.put("tenant_kin_name",tenant_kin_name);
            model.put("tenant_rent_day",rent_payment_day);
            model.put("tenant_debt",tenant_debt);
            model.put("unit_add", chosen_unit);
            model.put("property_total_unit",property_total_unit);

            model.put("name", propertyName);

            model.put("error_tenant","Tenant with this number already exist");

            System.out.println(model);
            ctx.render("templates/tenant_form.html",model);
        };

    }


}
