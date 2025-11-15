package Invalidhandler;

public class WrongPasswordCreator extends  UpdateUser {

    @Override
    public ErrorHandler error_handler() {
        return new WrongPassword();
    }
}
