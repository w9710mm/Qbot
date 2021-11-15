package com.mm.qbot.service;

import com.mikuac.shiro.common.utils.MsgUtils;
import com.mikuac.shiro.core.Bot;
import com.mikuac.shiro.dto.action.common.ActionData;
import com.mikuac.shiro.dto.action.response.GroupMemberInfoResp;
import com.mikuac.shiro.dto.event.message.GroupMessageEvent;
import com.mm.qbot.Scheduler.SchedulerQuartzJob;
import com.mm.qbot.bean.SchedulerDTO;
import com.mm.qbot.bean.SchedulerList;
import com.mm.qbot.utils.LevelDB;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.regex.Matcher;

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

    private SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");

    private  static  LevelDB Db = LevelDB.getInstance();

    private static SchedulerList schedulerList;
    static {
         schedulerList =(SchedulerList) Db.getObject("schedulerList");
        if (schedulerList==null||schedulerList.getSchedulerDTOList().size()==0){
            schedulerList=SchedulerList.getInstance();
        }
    }
    /**
     * 开始执行所有任务
     *
     * @throws SchedulerException
     */
    @Bean
    public void startJob() throws SchedulerException {
        log.info("开启日程表任务");
        scheduler.start();
        SchedulerList schedulerList =(SchedulerList) Db.getObject("schedulerList");
        if (schedulerList!=null&&schedulerList.getSchedulerDTOList().size()!=0) {
            for (SchedulerDTO schedulerDTO : schedulerList.getSchedulerDTOList()) {
                scheduler.scheduleJob(schedulerDTO.getJobDetail(), schedulerDTO.getCronTrigger());
            }
        }
    }


    public Boolean addJob(Matcher m, String group , Date startTime, Date endTime, Bot bot) throws SchedulerException {

        try {
            ActionData<GroupMemberInfoResp> groupMemberInfo = bot.getGroupMemberInfo(Long.parseLong(group), Long.parseLong(m.group(1)),false);

            MsgUtils msgUtils=MsgUtils.builder().at(Long.parseLong(m.group(1))).text("ddl提醒：").text(m.group(3));
       JobDataMap jobDataMap=new JobDataMap();
       jobDataMap.put("bot",bot);
       jobDataMap.put("msg",msgUtils);
       jobDataMap.put("GroupId",group);
        JobDetail jobDetail = JobBuilder.newJob(SchedulerQuartzJob.class).withIdentity(m.group(1), group)
                .usingJobData(jobDataMap).withDescription(groupMemberInfo.getData().getNickname()
                                +"在"+simpleDateFormat.format(endTime)+"前完成"+m.group(3)).build();
        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule("0 0 */5 * * ? *");
        CronTrigger cronTrigger = TriggerBuilder.newTrigger().withIdentity(m.group(1), group)
                .startAt(startTime).endAt(endTime).withSchedule(cronScheduleBuilder).build();
        scheduler.scheduleJob(jobDetail, cronTrigger);
            List<SchedulerDTO> schedulerDTOList = schedulerList.getSchedulerDTOList();
           SchedulerDTO schedulerDTO=new SchedulerDTO();
           schedulerDTO.setJobDetail(jobDetail);
           schedulerDTO.setCronTrigger(cronTrigger);
            schedulerDTOList.add(schedulerDTO);
      Db.put("schedulerList",schedulerDTOList);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }


    public MsgUtils showJobs() throws SchedulerException {
        MsgUtils msgUtils=MsgUtils.builder();
        msgUtils.text("任务表\n");
        Set<JobKey> jobKeySet = scheduler.getJobKeys(GroupMatcher.anyGroup());
        if (jobKeySet.size()==0){
            return null;
        }
        int count=1;
        for (JobKey jobKey:jobKeySet) {
            JobDetail jobDetail = scheduler.getJobDetail(jobKey);
            msgUtils.text(String.valueOf(count)).text(".").text(jobDetail.getDescription()).text("\n");
            count++;
        }
        return msgUtils;
    }


    public boolean deleteJob(String name ,String group ) throws SchedulerException {
        JobKey jobKey = new JobKey(name, group);
        JobDetail jobDetail = scheduler.getJobDetail(jobKey);
        if (jobDetail == null) {
            return false;
        }
        scheduler.deleteJob(jobKey);
        List<SchedulerDTO> schedulerDTOList = schedulerList.getSchedulerDTOList();
        for (SchedulerDTO schedulerDTO:schedulerDTOList) {
            if (schedulerDTO.getJobDetail().equals(jobDetail)){
                schedulerDTOList.remove(schedulerDTO);
                break;
            }
        }
        Db.put("schedulerList",schedulerList);
        return true;
    }
}
