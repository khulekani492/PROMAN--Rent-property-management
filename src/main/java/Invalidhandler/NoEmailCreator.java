package Invalidhandler;

import io.javalin.http.Handler;

public class NoEmailCreator extends UpdateUser {
    @Override
    public ErrorHandler error_handler() {
        return new NoEmail();
    }
}
