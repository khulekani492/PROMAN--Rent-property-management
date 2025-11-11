package API_handlers;

import io.javalin.http.Handler;
import model.database.CRUD.Getunits;
import model.database.CRUD.Propertyinfo;

import java.util.ArrayList;

public class PropertyUnits {

    public Handler property_related_information(){
        return  ctx -> {

            Getunits property_list = new Getunits();
            ctx.render("/dashboard");
        };
    }
}
