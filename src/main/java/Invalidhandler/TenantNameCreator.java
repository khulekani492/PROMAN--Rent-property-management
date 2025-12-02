package Invalidhandler;

public class TenantNameCreator  extends  UpdateUser{
    @Override
    public ErrorHandler error_handler() {
        return new Tenant_name();
    }
}
