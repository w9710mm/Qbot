package com.mm.qbot.service;

import com.mm.qbot.Scheduler.SchedulerQuartzJob;
import com.mm.qbot.config.PaperQuartzScheduler;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author meme
 * @version V0.0.1
 * @Package com.mm.qbot.service
 * @Description:
 * @date 2021/11/14 15:04
 */

@Service
public class SchedulerService {


    @Autowired
    private PaperQuartzScheduler quartzScheduler;

    private void addJob(Scheduler scheduler,String name ,String group ,String time) throws SchedulerException {
        JobDetail jobDetail = JobBuilder.newJob(SchedulerQuartzJob.class).withIdentity("job2", "group2").build();
        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule("0 0/5 * * * ?");
        CronTrigger cronTrigger = TriggerBuilder.newTrigger().withIdentity("job2", "group2")
                .withSchedule(cronScheduleBuilder).build();
        scheduler.scheduleJob(jobDetail, cronTrigger);
    }


    private void showJobs(Scheduler scheduler,String name ,String group ,String time) throws SchedulerException {
        JobDetail jobDetail = JobBuilder.newJob(SchedulerQuartzJob.class).withIdentity("job2", "group2").build();
        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule("0 0/5 * * * ?");
        CronTrigger cronTrigger = TriggerBuilder.newTrigger().withIdentity("job2", "group2")
                .withSchedule(cronScheduleBuilder).build();
        scheduler.scheduleJob(jobDetail, cronTrigger);
    }


    private void deleteJob(Scheduler scheduler,String name ,String group ,String time) throws SchedulerException {
        JobDetail jobDetail = JobBuilder.newJob(SchedulerQuartzJob.class).withIdentity("job2", "group2").build();
        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule("0 0/5 * * * ?");
        CronTrigger cronTrigger = TriggerBuilder.newTrigger().withIdentity("job2", "group2")
                .withSchedule(cronScheduleBuilder).build();
        scheduler.scheduleJob(jobDetail, cronTrigger);
    }
}
