package schedule;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import static org.quartz.JobBuilder.*;
import static org.quartz.TriggerBuilder.*;
import static org.quartz.SimpleScheduleBuilder.*;

public class QuartzTest {

    public static void main(String[] args) throws Exception {

        // 1. Create scheduler
        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler(); //consumes quartz properties files
        scheduler.start();

        // 2. Define the job
        JobDetail job = JobBuilder.newJob(Sawubona.class)
                .withIdentity("myJob", "group1")
                .build();

        // 3. Define the trigger
        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("myTrigger", "group1")
                .startNow()
                .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                        .withIntervalInSeconds(40)  // run every 5 seconds
                        .repeatForever())
                .build();

        // 4. Schedule the job
        scheduler.scheduleJob(job, trigger);
    }

}