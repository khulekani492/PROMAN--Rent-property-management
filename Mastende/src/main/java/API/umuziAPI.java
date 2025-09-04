package API;

import io.javalin.Javalin;

import java.sql.SQLException;
import java.util.Map;

public class umuziAPI {
    public static Javalin startServer(int port) throws SQLException {
//        apiHandler letsconnectDAO = new apiHandler();
        // Create a new Javalin app
        Javalin app = Javalin.create();

        /**
         * GET /world
         * Returns all stored worlds as JSON.
         */
        app.get("/property", ctx -> {


//            String result = String.valueOf(letsconnectDAO.restoreWorldNoname());
//            ctx.json(result);   // return JSON directly
        });

        /**
         * GET /world/{name}
         * Returns a specific world by its name.
         * If the world is not found, returns HTTP 404 with an error message.
         */
        app.get("/world/tenants", ctx -> {
            String name = ctx.pathParam("name");


        });

        app.post("/robot/{name}", ctx -> {


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
