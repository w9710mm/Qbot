package com.mm.qbot.service;

import com.mikuac.shiro.common.utils.MsgUtils;
import com.mikuac.shiro.core.Bot;
import com.mm.qbot.Scheduler.SchedulerQuartzJob;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author meme
 * @version V0.0.1
 * @Package com.mm.qbot.service
 * @Description:
 * @date 2021/11/14 15:04
 */

@Service
@Slf4j
public class SchedulerService {

    @Autowired
    private Scheduler scheduler;

    /**
     * 开始执行所有任务
     *
     * @throws SchedulerException
     */
    @Bean
    public void startJob() throws SchedulerException {
        log.info("开启日程表任务");
        scheduler.start();
    }


    private Boolean addJob(String name , String group , Date startTime,Date endTime, Bot bot, MsgUtils msgUtils, Long GroupId) throws SchedulerException {

        try {

       JobDataMap jobDataMap=new JobDataMap();
       jobDataMap.put("bot",bot);
       jobDataMap.put("msg",msgUtils);
       jobDataMap.put("GroupId",GroupId);
        JobDetail jobDetail = JobBuilder.newJob(SchedulerQuartzJob.class).withIdentity(name, group)
                .usingJobData(jobDataMap).build();
        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule("0 0 */5 * * ? *");
        CronTrigger cronTrigger = TriggerBuilder.newTrigger().withIdentity(name, group)
                .startAt(startTime).endAt(endTime).withSchedule(cronScheduleBuilder).build();
        scheduler.scheduleJob(jobDetail, cronTrigger);
        }catch (Exception E){
            E.printStackTrace();
            return false;
        }
        return true;
    }


    private void showJobs() throws SchedulerException {
        LinkedHashSet<JobKey> jobKeySet = scheduler.getJobKeys(GroupMatcher.anyGroup());
        for (JobKey jobKey:jobKeySet) {
            jobKey.ge
            JobDetail jobDetail = scheduler.getJobDetail(jobKey);
            jobDetail.getJobDataMap().
        }
    }


    private void deleteJob(String name ,String group ) throws SchedulerException {
        JobKey jobKey = new JobKey(name, group);
        JobDetail jobDetail = scheduler.getJobDetail(jobKey);
        if (jobDetail == null) {
            return;
        }
        scheduler.deleteJob(jobKey);
    }
}
