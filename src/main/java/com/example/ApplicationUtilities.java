package com.example;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.stereotype.Component;

@Component
public class ApplicationUtilities {

    public static void runCacheSchedule() {
        try {
            JobDetail job = JobBuilder.newJob(QuartzOneMinuteJob.class).withIdentity("one", "test group").build();
            org.quartz.Trigger trigger = TriggerBuilder
                    .newTrigger()
                    .withIdentity("one", "test group")
                    .withSchedule(
                            CronScheduleBuilder.cronSchedule("0 */1 * ? * *")
                    ).build();

            Scheduler scheduler = new StdSchedulerFactory().getScheduler();
            scheduler.start();
            scheduler.scheduleJob(job, trigger);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        try {
            JobDetail job = JobBuilder.newJob(QuartzTwoMinuteJob.class).withIdentity("two", "test group").build();
            org.quartz.Trigger trigger = TriggerBuilder
                    .newTrigger()
                    .withIdentity("two", "test group")
                    .withSchedule(
                            CronScheduleBuilder.cronSchedule("0 */2 * ? * *")
                    ).build();

            Scheduler scheduler = new StdSchedulerFactory().getScheduler();
            scheduler.start();
            scheduler.scheduleJob(job, trigger);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
