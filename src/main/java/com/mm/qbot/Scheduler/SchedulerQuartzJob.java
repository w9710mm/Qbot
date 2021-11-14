package com.mm.qbot.Scheduler;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * @author meme
 * @version V0.0.1
 * @Package com.mm.qbot.Scheduler
 * @Description:
 * @date 2021/11/14 14:56
 */
public class SchedulerQuartzJob implements Job {
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        JobDataMap jobDataMap = jobExecutionContext.getJobDetail().getJobDataMap();
    }
}
