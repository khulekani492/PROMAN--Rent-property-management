package schedule;
import org.quartz.*;
import org.quartz.impl.StdScheduler;
import org.quartz.impl.StdSchedulerFactory;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.CronScheduleBuilder.dailyAtHourAndMinute;
import static org.quartz.JobBuilder.*;
import static org.quartz.TriggerBuilder.*;
import static org.quartz.SimpleScheduleBuilder.*;

public class Quartz {
      static void main() throws SchedulerException {
        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
        scheduler.start();

        JobDetail test_job = newJob(Reminder.class)
                .withIdentity("emails","group1")
                .build();
        JobDetail test_job2 = newJob(Reminder.class)
                .withIdentity("second_reminder","group1")
                .build();

        Trigger trigger = newTrigger().
                withIdentity("my_trigger","group1")
                .startNow().withSchedule(dailyAtHourAndMinute(12, 5))
                .build();

                  Trigger trigger2 = newTrigger().
                withIdentity("second_trigger","group2")
                .startNow().withSchedule(dailyAtHourAndMinute(12,10))
                .build();


        scheduler.scheduleJob(test_job,trigger);
        scheduler.scheduleJob(test_job2,trigger2);



    }


    }
