package Invalidhandler;

public class SameUnitCreator extends UpdateUser {
    @Override
    public ErrorHandler error_handler() {
        return new SameUnit();
    }
}
