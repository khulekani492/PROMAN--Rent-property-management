package api_login;

import model.database.CRUD.Tenant;
import model.database.CRUD.landlord;
import model.database.Transaction;

import java.sql.SQLException;
import java.util.Objects;

public class Type_of_payments {

    public void type_of_payment(
            Integer recieved_amount,
            Integer original_rent,
            Integer debt,
            Integer tenantId,
            Transaction approve_to_rentBook) throws SQLException {
        Tenant tenant_money = new Tenant();
        if (recieved_amount.equals(original_rent)) {

            System.out.println("siyafika");
            System.out.println(recieved_amount + " " + original_rent + " " + debt);
            System.out.println(debt);
            //Step 1 assess the tenant economic standing
            //Check if tenant_debt is zero and verify new payment and status true
            if (debt.equals(0)) {
                System.out.println("1");
                approve_to_rentBook.setAmount_paid(recieved_amount);
                approve_to_rentBook.setStatus(true);
                approve_to_rentBook.setTenant_Id(tenantId);
                approve_to_rentBook.update_tenant_status();
                return ;
            } else if ( debt > recieved_amount) {
                Integer money_paid_left = debt - recieved_amount;
                System.out.println("Deducts " + money_paid_left);
                new Tenant().update_debt(money_paid_left,tenantId);
                if (money_paid_left.equals(original_rent)) {
                    System.out.println("2");
                    return ;
                    // else determine how many months it covers
                } else if (money_paid_left > original_rent) {
                    System.out.println(money_paid_left / original_rent);
                    System.out.println("3");
                    approve_to_rentBook.setAmount_paid(money_paid_left);
                    approve_to_rentBook.setStatus(true);
                    approve_to_rentBook.setTenant_Id(tenantId);
                    approve_to_rentBook.update_tenant_status();
                    return ;
                }

            } //  update properties.rent_book
            else if (debt > recieved_amount) {
                System.out.println("Go home " + " " + recieved_amount + " " + debt);
                Integer total_difference = debt - recieved_amount;
                new Tenant().update_debt(total_difference, tenantId);

                 return;
            }
            else {
                //When payment_paid equals to debt owed by tenant ,settle debt of the previous, updates the tenant debt of the new month
                Integer new_amount = original_rent;
                new Tenant().update_debt(new_amount,tenantId);
                approve_to_rentBook.setAmount_paid(recieved_amount);
                approve_to_rentBook.setStatus(true);
                approve_to_rentBook.setTenant_Id(tenantId);
                approve_to_rentBook.update_tenant_status();
                return ;
            }
            return ;
        } else if (recieved_amount > original_rent && debt > 0) {
            Integer total_difference = recieved_amount - debt;
            //
            if (total_difference > 0) {
                new Tenant().update_debt(total_difference, tenantId);
                return;
            }
        }

    }
        static void main () throws SQLException {
            Type_of_payments update_debt = new Type_of_payments();
            Tenant tenantTest = new Tenant();
            // tenantTest.
            update_debt.type_of_payment( 400, 400, tenantTest.tenant_debt(772), 772,new Transaction());
            //Output debt = 800
        }

    }

