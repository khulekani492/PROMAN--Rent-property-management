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
                Integer remaining_amount = recieved_amount -  debt ; //Minus the debt with the money received
                System.out.println("Debt exists and is lesser than the rent money paid , after deducting the debt " + remaining_amount);
                System.out.println();
                System.out.println("Received MONEY :   " + recieved_amount);
                System.out.println("Tenant Debt :   " + debt);
                System.out.println("Remaining amount after payment :  " + remaining_amount);

                if (remaining_amount.equals(original_rent)){
                    System.out.println("Tenant has no debt after settling ");
                    approve_to_rentBook.setAmount_paid(recieved_amount);
                    approve_to_rentBook.setStatus(true);
                    approve_to_rentBook.setTenant_Id(tenantId);
                    approve_to_rentBook.update_tenant_status();
                    approve_to_rentBook.setDebtString("0");
                } else if(remaining_amount > original_rent ){
                    approve_to_rentBook.setAmount_paid(recieved_amount);
                    approve_to_rentBook.setStatus(true);
                    approve_to_rentBook.setTenant_Id(tenantId);
                    approve_to_rentBook.update_tenant_status();

                    // Advance_payment period
                    Integer period = recieved_amount  % original_rent;
                    approve_to_rentBook.setDebtString("Advance_payment + " + period );
                } else {
                    Integer new_debt = original_rent - remaining_amount;
                    approve_to_rentBook.setAmount_paid(recieved_amount);
                    approve_to_rentBook.setStatus(true);
                    approve_to_rentBook.setTenant_Id(tenantId);
                    approve_to_rentBook.update_tenant_status();
                    approve_to_rentBook.setDebtString(String.valueOf(  new_debt));
                    System.out.println("paid less money than original rent");

                }

            }

        } else if (recieved_amount > original_rent) {
            System.out.println("recieved_amount is greater than original_rent " + recieved_amount);
            if (debt.equals(0)) {
                System.out.println("Tenant has no debt ");
                approve_to_rentBook.setAmount_paid(recieved_amount);
                approve_to_rentBook.setStatus(true);
                approve_to_rentBook.setTenant_Id(tenantId);
                approve_to_rentBook.update_tenant_status();

                // Advance_payment period
                Integer period = recieved_amount  % original_rent;
                approve_to_rentBook.setDebtString("Advance_payment + " + period );
            }
            //else if the tenant existing debt ia greater than money recieved
            else if (debt > recieved_amount) {

                System.out.println("+Advance payment Received MONEY :   " + recieved_amount);
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
                Integer settle_debt =recieved_amount -  debt ; //Minus the debt with the money received
                System.out.println("Debt exists and is lesser than the rent money paid , after deducting debt " + settle_debt);
                System.out.println()
                ;
                System.out.println("Received MONEY :   " + recieved_amount);
                System.out.println("Tenant Debt :   " + debt);
                System.out.println("new debt " + settle_debt);


                new Tenant().update_debt(0, tenantId); //tenant settles debt
                //Check if the Money left is enough to meet the month rent >
                if(settle_debt.equals(original_rent)){
                    System.out.println("Paid debt and and met the month rent");
                    approve_to_rentBook.setAmount_paid(recieved_amount);
                    approve_to_rentBook.setStatus(true);
                    approve_to_rentBook.setTenant_Id(tenantId);
                    approve_to_rentBook.update_tenant_status();
                    approve_to_rentBook.setDebtString("0");

                }else if( settle_debt > original_rent) {
                    System.out.println("Paid debt and made an advance payments");
                    Integer period = settle_debt % original_rent;
                    approve_to_rentBook.setAmount_paid(recieved_amount);
                    approve_to_rentBook.setStatus(true);
                    approve_to_rentBook.setTenant_Id(tenantId);
                    approve_to_rentBook.update_tenant_status();
                    approve_to_rentBook.setDebtString("Advance + " + period);
                }else {
                    System.out.println("Paid debt and did not meet the monthly rent");
                    Integer new_debt = original_rent - settle_debt;
                    approve_to_rentBook.setAmount_paid(recieved_amount);
                    approve_to_rentBook.setStatus(true);
                    approve_to_rentBook.setTenant_Id(tenantId);
                    approve_to_rentBook.update_tenant_status();
                    approve_to_rentBook.setDebtString(String.valueOf(  new_debt));

                }

            }

        } else {
            Integer new_debt = original_rent - recieved_amount;
            approve_to_rentBook.setAmount_paid(recieved_amount);
            approve_to_rentBook.setStatus(true);
            approve_to_rentBook.setTenant_Id(tenantId);
            approve_to_rentBook.update_tenant_status();
            approve_to_rentBook.setDebtString(String.valueOf(  new_debt));
            System.out.println("paid less money than original rent");
        }


    }

}