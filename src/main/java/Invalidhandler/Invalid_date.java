package Invalidhandler;

import io.javalin.http.Handler;

import java.util.HashMap;

public class Invalid_date implements  ErrorHandler{
    @Override
    public Handler error_message() {
        return ctx ->{

            Integer chosen_unit = ctx.sessionAttribute("chosen_unit");
            String property_name = ctx.sessionAttribute("property_name");
            String invalid_date = ctx.sessionAttribute("error_moveIn");
            String tenant_name = ctx.sessionAttribute("tenant_name");
            String tenant_number = ctx.sessionAttribute("tenant_number");
            String tenant_pay = ctx.sessionAttribute("tenant_pay");
            String tenant_rent_payment = ctx.sessionAttribute("tenant_rent_payment");
            String tenant_kin_number = ctx.sessionAttribute("tenant_kin_number");
            String tenant_kin_name  = ctx.sessionAttribute("tenant_kin_name ");
            Integer tenant_debt  = ctx.sessionAttribute("tenant_debt");

            HashMap<String,String> model = new HashMap<>();
            model.put("Invalid_format", invalid_date);
            model.put("unit_add",String.valueOf(  chosen_unit) );
            model.put("tenant_name", tenant_name);
            model.put("tenant_number", tenant_number);
            model.put("tenant_rent_day",tenant_pay);
            model.put("tenant_rent_payment",tenant_rent_payment);
            model.put("tenant_kin_number",tenant_kin_number);
            model.put("tenant_rent_name",tenant_kin_name);
            model.put("tenant_debt",String.valueOf( tenant_debt) );
            model.put("name",property_name );
            String themeColor = ctx.sessionAttribute("theme_color");
            model.put("user_chosen_theme",themeColor);

            ctx.render("templates/tenant_form.html",model);
        };
    }
}
