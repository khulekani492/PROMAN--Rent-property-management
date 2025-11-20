package schedule;

import model.database.CRUD.TenantOverdueTracker;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

@DisallowConcurrentExecution
public class Reset_status implements Job {
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        TenantOverdueTracker new_month_reset = new TenantOverdueTracker();
        new_month_reset.reset_all();


    }
}
