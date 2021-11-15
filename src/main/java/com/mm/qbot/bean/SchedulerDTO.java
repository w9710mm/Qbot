package com.mm.qbot.bean;

import lombok.Data;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;

/**
 * @author meme
 * @version V0.0.1
 * @Package com.mm.qbot.bean
 * @Description:
 * @date 2021/11/15 21:40
 */
@Data
public class SchedulerDTO {

    private JobDetail jobDetail;
    private CronTrigger cronTrigger;
}
