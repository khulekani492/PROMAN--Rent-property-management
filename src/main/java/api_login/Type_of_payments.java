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
            System.out.println("siyafika");
            System.out.println(recieved_amount + " " + original_rent + " " + debt);
            System.out.println(debt);
            //Step 1 assess the tenant existing debt
            //Check if tenant_debt is zero if true approve new payment and set payment_status true
            if (debt.equals(0)) {
                approve_to_rentBook.setAmount_paid(recieved_amount);
                approve_to_rentBook.setStatus(true);
                approve_to_rentBook.setTenant_Id(tenantId);
                approve_to_rentBook.update_tenant_status();

            }
            //else if the tenant existing debt ia greater than money recieved
            else if ( debt > recieved_amount) {
                Integer money_paid_left = debt - recieved_amount;
                System.out.println("Deducts ;" + money_paid_left);
                new Tenant().update_debt(money_paid_left,tenantId); //update tenant to the remaining debt
                approve_to_rentBook.setAmount_paid(original_rent);
                approve_to_rentBook.setStatus(true);
                approve_to_rentBook.setTenant_Id(tenantId);
                approve_to_rentBook.update_tenant_status();

            } else if (recieved_amount > debt) {
                Integer money_paid_left = recieved_amount - debt;
                System.out.println("Settles debt : " + money_paid_left);
                new Tenant().update_debt(0,tenantId);
                approve_to_rentBook.setAmount_paid(money_paid_left);
                approve_to_rentBook.setStatus(true);
                approve_to_rentBook.setTenant_Id(tenantId);
                approve_to_rentBook.update_tenant_status();
                //Check if money left after settling the debt is greater than original_rent --> update tenant.debt
                if (money_paid_left < original_rent){
                    new Tenant().update_debt(money_paid_left,tenantId);
                }
            }
            else {
                //When payment_paid equals to debt owed by tenant ,settle debt of the previous, updates the tenant debt of the new month
//                The amount received is exactly equal to the outstanding debt.
//                This means the tenant pays precisely the amount they owe, no more, no less.
                new Tenant().update_debt(original_rent,tenantId); //update to tenant debt
                approve_to_rentBook.setAmount_paid(recieved_amount);
                approve_to_rentBook.setStatus(true);
                approve_to_rentBook.setTenant_Id(tenantId);
                approve_to_rentBook.update_tenant_status();

            }

        }
        //Advance_payment_block add tenantId and period the advance payment covers
        else if (recieved_amount > original_rent) {
            Record_advance_payment sharedMap_advance_pays = new Record_advance_payment();
            HashMap<Integer,Integer> add_to_Map = sharedMap_advance_pays.current_property_advancePayments;
            Integer period = recieved_amount / original_rent;
            add_to_Map.put(tenantId,period);
            approve_to_rentBook.setAmount_paid(recieved_amount);
            approve_to_rentBook.setStatus(true);
            approve_to_rentBook.setTenant_Id(tenantId);
            approve_to_rentBook.update_tenant_status();
        }
        // if money received is lesser than original_rent , record the outstanding fee
        else {
            Integer outstanding_fee = original_rent - recieved_amount;
            Integer  add_debt = debt + outstanding_fee;
            new Tenant().update_debt(add_debt,tenantId);
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

