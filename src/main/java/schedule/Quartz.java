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


          JobDetail morning_reminder = newJob(Reminder.class)
                  .withIdentity("second_reminder","group1")
                  .build();

          JobDetail afternoon_reminder = newJob(Reminder.class)
                .withIdentity("emails","group1")
                .build();

        JobDetail overdue_tracker = newJob(Overdue_tracker.class)
                .withIdentity("due_date","group1")
                .build();

        Trigger trigger0 = newTrigger().withIdentity("update_trigger")
                .startNow().withSchedule(cronSchedule("0 0 1 * * ?"))
                .build()
                ;

        Trigger trigger = newTrigger().
                withIdentity("my_trigger","group1")
                .startNow().withSchedule(dailyAtHourAndMinute(10, 5))
                .build();

                  Trigger trigger2 = newTrigger().
                withIdentity("second_trigger","group2")
                .startNow().withSchedule(dailyAtHourAndMinute(17,45))
                .build();


        scheduler.scheduleJob(morning_reminder,trigger);
        scheduler.scheduleJob(afternoon_reminder,trigger2);
        scheduler.scheduleJob(overdue_tracker,trigger0);



    }


    }
