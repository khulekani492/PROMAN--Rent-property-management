package schedule;

import model.database.CRUD.GetLandlords;
import model.database.CRUD.Tenants_rent_day;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class Reminder implements Job {


    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        //
        GetLandlords get_landlords = new GetLandlords(); // each instance have
        Tenants_rent_day tenants = new Tenants_rent_day();
        Set<Integer> send_email_landlord = get_landlords.fetchAll();

        for (Integer landlordId : send_email_landlord){
            try {
                HashMap<Integer,ArrayList<Integer>> landlord_map =  tenants.rent_due_tenants(landlordId);
                Set<Integer> current_landlord = landlord_map.keySet();

                for (Integer landlord : current_landlord){
                    //Send email
                    PersonEmail notification = new PersonEmail(landlordId,landlord_map.get(landlord));
                    System.out.println(notification.send_email() );
                }

              } catch (Exception e) {
                throw new RuntimeException(e);
            }

        }

    }
}
