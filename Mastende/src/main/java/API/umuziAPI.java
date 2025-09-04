package API;

//import com.mitchellbosecke.pebble.PebbleEngine;
//import com.mitchellbosecke.pebble.template.PebbleTemplate;
import backend.database.Data;
import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import io.javalin.rendering.template.JavalinThymeleaf;

import java.sql.SQLException;
import java.util.Map;

public class umuziAPI {
    private static final String D_URL = "jdbc:sqlite:apiData.db";
    private final Data conn;

    {
        try {
            conn = new Data(D_URL);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public  static Javalin startServer(int port) throws SQLException {
        // Create a new Javalin app
        // private Data accessDatabase;
        Javalin app = Javalin.create(config -> {
            //config.staticFiles.add("/public", Location.CLASSPATH);


            config.fileRenderer(new JavalinThymeleaf());
        });

        app.get("/sign_up", ctx -> {

            //take from the form the id and put the information to the residence_table
            //respond ok or error in jsom
            ctx.render("/templates/hello.html", Map.of("name", "Mkhulex"));
        });

        app.get("/tenants", ctx -> {

            // take from the form the id and put the information to the tenants table
            // respond ok or error js
            ctx.render("/templates/sign_up.html");
        });

        app.start(port);
        return app;
    }

    /**
     * Main entry point of the application.
     * <p>
     * Starts the API server on port 7070.
     *
     * @param args command-line arguments (not used)
     * @throws SQLException if database initialization fails
     */
    public static void main(String[] args) throws SQLException {
        startServer(7070);
    }
}
