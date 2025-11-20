package schedule;

import model.database.CRUD.GetLandlords;
import model.database.CRUD.TenantOverdueTracker;
import model.database.CRUD.Tenants_rent_day;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import java.time.LocalDateTime;  // Import the LocalDateTime class
import java.time.format.DateTimeFormatter;

@DisallowConcurrentExecution
public class Overdue_tracker implements Job {


    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        GetLandlords get_landlords = new GetLandlords(); // each instance have
        Tenants_rent_day tenants = new Tenants_rent_day();
        TenantOverdueTracker update_overdue =  new TenantOverdueTracker();
        LocalDateTime  current_day = LocalDateTime.now();

        DateTimeFormatter get_day = DateTimeFormatter.ofPattern("dd");


        String current_date = current_day.format(get_day);
        Set<Integer> track_tenant = get_landlords.fetchAll();

        //Get all landlord with tenants due
        for (Integer landlordId : track_tenant){
            try {
                //Get ALL tenants due date who have not been marked "paid" as HASHMAP--->Key --> landlordId : [due_dates] of their tenants
                HashMap<Integer,ArrayList<Integer>> track_payment_status =  tenants.tenants_rent_date(landlordId);

                // current units due ---> a list of all the landlords
                Set<Integer> current_units_due = track_payment_status.keySet();

                // Accessing the due_dates list in the hashMap using landlord keys
                for (Integer landlord : current_units_due)
                {
                      ArrayList<Integer> tenants_still_due = track_payment_status.get(landlord);
                    System.out.println(landlord);
                    //Loop through the due_date List of each landlord , compare current_date from Java library time, if the current date is Greater,then tenant due_date
                    //call the postgres function ,pass landlordId and update the over_due column with the current(this is done by the function , column automatically updates when
                    //landlord is passed in the parameter
                    for (Integer due_date: tenants_still_due){
                        if( Integer.parseInt(current_date) > due_date){
                            //calls the postgres function to update the over_due date
                            update_overdue.call_postgres_func(landlordId);

                        }

                    }
                    //
                }

            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        }
        Tenants_rent_day getAllTenantsDue = new Tenants_rent_day();

    }
}
