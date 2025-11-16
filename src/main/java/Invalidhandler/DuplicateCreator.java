package Invalidhandler;

public class DuplicateCreator extends UpdateUser {
    @Override
    public ErrorHandler error_handler() {

        return new Duplicate_payment();
    }
}
