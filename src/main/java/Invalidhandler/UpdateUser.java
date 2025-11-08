package Invalidhandler;

import io.javalin.http.Handler;

public abstract class UpdateUser {

    public abstract ErrorHandler error_handler();

    public Handler updateUser(){
        ErrorHandler update_user = error_handler();
        return update_user.error_message();
    }

}
