package com.mm.qbot.controller;

import com.mikuac.shiro.annotation.GroupMessageHandler;
import com.mikuac.shiro.common.utils.ShiroUtils;
import com.mikuac.shiro.core.Bot;
import com.mikuac.shiro.core.BotPlugin;
import com.mikuac.shiro.dto.event.message.GroupMessageEvent;
import com.mm.qbot.config.PaperQuartzScheduler;
import org.quartz.SchedulerException;
import org.quartz.core.QuartzScheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 * @author meme
 * @version V0.0.1
 * @Package com.mm.qbot.controller
 * @Description:
 * @date 2021/11/14 14:26
 */

@Controller
public class QuartzController extends BotPlugin {

    @Autowired
    private PaperQuartzScheduler quartzScheduler;






    @GroupMessageHandler(cmd = "\b任务表",groupWhiteList ={245530949,706592235} )
    public void showQuartzJob(Bot bot, GroupMessageEvent event) {

    }
    @GroupMessageHandler(cmd = "\b\\[CQ:at,qq=([0-9]+)\\]ddl ([0-9]{4}-[0-9]{2}-[0-9]{2}) (.+)" )
    public void addQuartzJob(Bot bot, GroupMessageEvent event) {
        System.out.println(event.getRawMessage());
    }

    @GroupMessageHandler(cmd = "\b删除任务 ([0-9]+)",groupWhiteList ={245530949,706592235} )
    public void deleteQuartzJob(Bot bot, GroupMessageEvent event) {

    }

    @GroupMessageHandler
    public void deleteQ1uartzJob(Bot bot, GroupMessageEvent event) {

        System.out.println(event.getRawMessage());
    }
}


