package API;

//import com.mitchellbosecke.pebble.PebbleEngine;
//import com.mitchellbosecke.pebble.template.PebbleTemplate;
import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import io.javalin.rendering.template.JavalinThymeleaf;

import java.sql.SQLException;
import java.util.Map;

public class umuziAPI {
    public static Javalin startServer(int port) throws SQLException {
        // Create a new Javalin app
        Javalin app = Javalin.create(config -> {
            config.staticFiles.add("/young", Location.CLASSPATH);
            config.fileRenderer(new JavalinThymeleaf());
        });
        /**
         * GET /world
         * Returns all stored worlds as JSON.
         */
        app.get("/eish", ctx -> {
            ctx.render("/templates/hello.html", Map.of("name", "Mkhulex"));
        });

        /**
         * GET /world/{name}
         * Returns a specific world by its name.
         * If the world is not found, returns HTTP 404 with an error message.
         */
        app.get("/world/tenants", ctx -> {
            String name = ctx.pathParam("name");
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
