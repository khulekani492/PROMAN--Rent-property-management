package Invalidhandler;

import io.javalin.http.Handler;

import java.util.HashMap;

public class Duplicate_payment implements ErrorHandler {

    @Override
    public Handler error_message() {
        return ctx ->{
            String before_state = ctx.sessionAttribute("property_name");
            System.out.println("Before state " + before_state);
            String before_state_email = ctx.sessionAttribute("email");
            String unit = ctx.sessionAttribute("unit");
            System.out.println("Before state email " + before_state_email);
            ctx.redirect("/unit_already_paid/" + before_state +"/"+before_state_email +"/" +unit);


        } ;
    }
}
