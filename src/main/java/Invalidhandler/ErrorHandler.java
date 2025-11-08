package Invalidhandler;

import io.javalin.http.Handler;

public interface ErrorHandler {
    public void error_message(String message);
}
