package schedule;

import model.database.CRUD.GetLandlords;
import model.database.CRUD.Rent_due_tenants;
import model.database.ConnectionAccess;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Reminder implements Job {


    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        //
        GetLandlords get_landlords = new GetLandlords();
        Rent_due_tenants tenants = new Rent_due_tenants();
        Set<Integer> send_email_landlord = get_landlords.fetchAll();
        boolean available = false;

        for (Integer landlordId : send_email_landlord){
            try {
                HashMap<Integer,ArrayList<Integer>> landlord_map =  tenants.rent_due_tenants(landlordId);
                Set<Integer> current_landlord = landlord_map.keySet();

                for (Integer wait : current_landlord){
                    //Send email
                    System.out.println( wait + " Landlord ");
                }

              } catch (Exception e) {
                throw new RuntimeException(e);
            }

        }

    }
}
