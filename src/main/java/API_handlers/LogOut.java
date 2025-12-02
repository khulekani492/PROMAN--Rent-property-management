package API_handlers;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;

public class LogOut  {

    public Handler sign_out() {
        return ctx ->{
            jakarta.servlet.http.HttpSession session =   ctx.req().getSession(false);
             if (session != null){
                 session.invalidate();
                 ctx.sessionAttributeMap().clear();
                 ctx.render("templates/home.html");
                 System.out.println("Session destroyed ");
             }
        };

        }
    }


