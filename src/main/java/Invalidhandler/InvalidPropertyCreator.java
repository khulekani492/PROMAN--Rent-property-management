package Invalidhandler;

public class InvalidPropertyCreator extends  UpdateUser {

    @Override
    public ErrorHandler error_handler() {
        return new Property_address();
    }
}
