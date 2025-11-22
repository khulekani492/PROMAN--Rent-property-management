package api_login;

import java.util.HashMap;

public class Record_advance_payment {
    public HashMap<Integer, Integer> current_property_advancePayments = new HashMap<>();

    public HashMap<Integer,Integer> check_current_property_advancePayments(){
        return current_property_advancePayments;
    }
}
