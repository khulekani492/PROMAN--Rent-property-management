package Invalidhandler;

 public abstract class UpdateUser {

    public abstract ErrorHandler error_handler();

    public void updateUser(String message){
        ErrorHandler update_user = error_handler();
        update_user.error_message(message);
    }

}
