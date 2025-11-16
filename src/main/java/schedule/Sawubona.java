package schedule;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class Sawubona implements Job {
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        for(int i =0 ; i < 5; i++){
            System.out.println("Peform : " + i);
        }
    }
}
