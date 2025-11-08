package Invalidhandler;

import io.javalin.http.Handler;

public interface ErrorHandler {
    public Handler error_message();
}
