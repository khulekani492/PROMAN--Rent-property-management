package schedule;

import model.database.CRUD.GetLandlords;
import model.database.CRUD.tracker.Tenants_rent_day;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class Overdue_tracker implements Job {


    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        GetLandlords get_landlords = new GetLandlords(); // each instance have
        model.database.CRUD.Tenants_rent_day tenants_Due_date = new model.database.CRUD.Tenants_rent_day();

        Set<Integer> track_tenant = get_landlords.fetchAll();

        for (Integer landlordId : track_tenant){
            try {
                HashMap<Integer,ArrayList<Integer>> track_payment_status =  tenants_Due_date.rent_due_tenants(landlordId);
                Set<Integer> current_units_due = track_payment_status.keySet();
                for (Integer landlord : current_units_due){
                    //Send email
                    //if current_dateNow >
                    System.out.println( landlord + " Landlord ");
                    System.out.println(track_payment_status.get(landlord) + " tenants_due");

                    //
                    //PersonEmail notification = new PersonEmail(landlordId,track_payment_status.get(landlord));

                }

            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        }
        Tenants_rent_day getAllTenantsDue = new Tenants_rent_day();

    }
}
