package Invalidhandler;

public class Invalid_dateCreator extends  UpdateUser{

    @Override
    public ErrorHandler error_handler() {
        return new Invalid_date();
    }
}
