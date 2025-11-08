package Invalidhandler;

public class SameEmialCreator extends UpdateUser{
    @Override
    public ErrorHandler error_handler() {
        return new SameEmail();
    }
}
