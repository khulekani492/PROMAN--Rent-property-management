package API_handlers;

import Invalidhandler.ErrorHandler;
import Invalidhandler.SameEmail;
import Invalidhandler.SameEmialCreator;
import Invalidhandler.UpdateUser;
import io.javalin.http.Handler;
import model.database.tenant;
import model.database.general;
import model.database.residence;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLOutput;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import static API.SecurityUtil.hashPassword;


public class apiHandler {
    /**
     * Gets the number of rooms the landlord has submitted.
     * <p>
     * Keeps rendering the same form repeatedly until the counter
     * reaches the last room.
     */
    public Handler errorMessage(){
        return ctx -> {
            ctx.result("ABCDeezNuts");
        };
    }

}