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
          // first round of emails for tenants due date  -->Job if landlord has tenants due that day a reminder
          JobDetail morning_reminder = newJob(Reminder.class)
                  .withIdentity("second_reminder","group1")
                  .build();
          // second round of emails for tenants due date -->Job if landlord has tenants due that day a  second reminder
          JobDetail afternoon_reminder = newJob(Reminder.class)
                .withIdentity("emails","group1")
                .build();
          //Fires  update tenant who are pass due date overdue_date column
        JobDetail overdue_tracker = newJob(Overdue_tracker.class)
                .withIdentity("due_date","group1")
                .build();
          //Fires at the first day of a new month --> Job reset over_due_date == to the first day of the month  and reset all payment_status.
        JobDetail reset_pay_status = newJob(Reset_status.class)
                .withIdentity("new_month","group2")
                .build();

        Trigger trigger0 = newTrigger().withIdentity("update_trigger")
                .startNow().withSchedule(cronSchedule("0 0 1 * * ?"))
                .build()
                ;

        Trigger trigger1 = newTrigger().
                withIdentity("my_trigger","group1")
                .startNow().withSchedule(dailyAtHourAndMinute(10, 5))
                .build();

        Trigger trigger2 = newTrigger().
                withIdentity("second_trigger","group2")
                .startNow().withSchedule(dailyAtHourAndMinute(17,45))
                .build();

          Trigger trigger3 = newTrigger().withIdentity("update_trigger")
                  .startNow().withSchedule(cronSchedule("0 0 0 1 * ?"))
                  .build()
                  ;

          scheduler.scheduleJob(overdue_tracker,trigger0);
          scheduler.scheduleJob(morning_reminder,trigger1);
          scheduler.scheduleJob(afternoon_reminder,trigger2);
          scheduler.scheduleJob(reset_pay_status,trigger3);





    }


    }
