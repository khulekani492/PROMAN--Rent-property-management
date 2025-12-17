                                      package api_login;

import model.database.CRUD.Tenant;
import model.database.CRUD.landlord;
import model.database.Transaction;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Objects;

public class Type_of_payments {

    public void type_of_payment(
            Integer recieved_amount,
            Integer original_rent,
            Integer debt,
            Integer tenantId,
            Transaction approve_to_rentBook) throws SQLException {
        //if amount paid is the same as the original rent.
        if (recieved_amount.equals(original_rent)) {
            System.out.println("Payment Processing");
            System.out.println(recieved_amount + " " + original_rent + " " + debt);
            System.out.println(debt);
            //Step 1 assess the tenant existing debt
            //Check if tenant_debt is zero if true approve new payment and set payment_status true
            if (debt.equals(0)) {
                System.out.println("Tenant has no debt ");
                approve_to_rentBook.setAmount_paid(recieved_amount);
                approve_to_rentBook.setStatus(true);
                approve_to_rentBook.setTenant_Id(tenantId);
                approve_to_rentBook.update_tenant_status();
                approve_to_rentBook.setDebtString("0");

            }
            //else if the tenant existing debt ia greater than money recieved
            else if (debt > recieved_amount) {
                System.out.println("Received MONEY :   " + recieved_amount);
                System.out.println("Tenant Debt :   " + debt);
                Integer remaining_debt = debt - recieved_amount; //Minus the debt with the money received
                System.out.println("Debt exists and is greater than the rent money paid , after deducting debt " + remaining_debt);
                new Tenant().update_debt(remaining_debt, tenantId); //update tenant to the remaining debt
                approve_to_rentBook.setAmount_paid(original_rent);
                approve_to_rentBook.setStatus(true);
                approve_to_rentBook.setTenant_Id(tenantId);
                approve_to_rentBook.update_tenant_status();
                approve_to_rentBook.setDebtString(String.valueOf(remaining_debt));

            } else {
                Integer remaining_debt =recieved_amount -  debt ; //Minus the debt with the money received
                System.out.println("Debt exists and is lesser than the rent money paid , after deducting debt " + remaining_debt);
                System.out.println()
                ;
                System.out.println("Received MONEY :   " + recieved_amount);
                System.out.println("Tenant Debt :   " + debt);
                System.out.println("new debt " + remaining_debt);
                new Tenant().update_debt(remaining_debt, tenantId); //update tenant to the remaining debt
                approve_to_rentBook.setAmount_paid(original_rent);
                approve_to_rentBook.setStatus(true);
                approve_to_rentBook.setTenant_Id(tenantId);
                approve_to_rentBook.update_tenant_status();
                approve_to_rentBook.setDebtString(String.valueOf(remaining_debt));

            }
                //Advance_payment_block add tenantId and period the advance payment covers

                //else if the tenant existing debt ia greater than money recieved


                // if money received is lesser than original_rent , record the outstanding fee




//            void main () throws SQLException {
//                Type_of_payments update_debt = new Type_of_payments();
//                Tenant tenantTest = new Tenant();
//                // tenantTest.
//                update_debt.type_of_payment(400, 400, tenantTest.tenant_debt(772), 772, new Transaction());
//                //Output debt = 800
//            }

        }

    }
}