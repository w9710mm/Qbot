package com.mm.qbot.controller;

import com.mikuac.shiro.annotation.GroupMessageHandler;
import com.mikuac.shiro.common.utils.MsgUtils;
import com.mikuac.shiro.common.utils.ShiroUtils;
import com.mikuac.shiro.core.Bot;
import com.mikuac.shiro.core.BotPlugin;
import com.mikuac.shiro.dto.event.message.GroupMessageEvent;
import com.mm.qbot.service.SchedulerService;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;

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
    private SchedulerService quartz;

    private  SimpleDateFormat       sf = new SimpleDateFormat("yyyy-MM-dd");

    @GroupMessageHandler(cmd = "\\b任务表",groupWhiteList ={483068702,245530949,706592235,1005018209} )
    public void showQuartzJob(Bot bot, GroupMessageEvent event) {
        try {

            MsgUtils msgUtils = quartz.showJobs();
            if (msgUtils==null){
                bot.sendGroupMsg(event.getGroupId(),MsgUtils.builder()
                        .text("当前没有任务").build(),false);
            }else {
                bot.sendGroupMsg(event.getGroupId(),msgUtils.build(),false);
            }
        } catch (SchedulerException e) {
            bot.sendGroupMsg(event.getGroupId(),MsgUtils.builder()
                    .text("当前没有任务").build(),false);
            e.printStackTrace();
        }
    }
    @GroupMessageHandler(cmd = "\\[CQ:at,qq=([0-9]+)\\] ddl ([0-9]{4}-[0-9]{2}-[0-9]{2}) (.+)" ,
            groupWhiteList ={483068702,1005018209,245530949,706592235})
    public void addQuartzJob(Bot bot, GroupMessageEvent event, Matcher m) {
        Date endTime;
        try {
            endTime = sf.parse(m.group(2));

        } catch (ParseException e) {
            e.printStackTrace();
            bot.sendGroupMsg(event.getGroupId(),MsgUtils.builder()
                    .text("输入格式错误，正确的例子：\n@某人ddl 2021-12-12 做某事").build(),false);
            return;
        }
        Calendar instance = Calendar.getInstance();
        instance.setTime(endTime);
        instance.add(Calendar.HOUR_OF_DAY,-12);
        Date startTime = instance.getTime();
        Date date=new Date();

        boolean before = startTime.before(date);
        if (before){
            bot.sendGroupMsg(event.getGroupId(),MsgUtils.builder()
                    .text("输入格式错误，可能是日期不对").build(),false);
            return;
        }
        try {

            quartz.addJob(m, String.valueOf(event.getGroupId()),startTime,endTime,bot);
        } catch (SchedulerException e) {
            e.printStackTrace();
            bot.sendGroupMsg(event.getGroupId(),MsgUtils.builder()
                    .text("添加错误，这可能是一个意外的原因导致的").build(),false);
            return;
        }
        bot.sendGroupMsg(event.getGroupId(),MsgUtils.builder()
                .text("添加定时任务成功").build(),false);
    }

    @GroupMessageHandler(cmd = "\\[CQ:at,qq=([0-9]+)\\] del",groupWhiteList ={483068702,245530949,706592235,1005018209} )
    public void deleteQuartzJob(Bot bot, GroupMessageEvent event,Matcher m) {
        try {
            ShiroUtils.getAtList(event.getRawMessage());
            boolean b = quartz.deleteJob(m.group(1), String.valueOf(event.getGroupId()));
            if (b){
                bot.sendGroupMsg(event.getGroupId(),MsgUtils.builder()
                        .text("删除成功").build(),false);
            }else {
                bot.sendGroupMsg(event.getGroupId(),MsgUtils.builder()
                        .text("删除失败，请检查输入是否正确").build(),false);
            }
        } catch (SchedulerException e) {
            e.printStackTrace();
            bot.sendGroupMsg(event.getGroupId(),MsgUtils.builder()
                    .text("删除失败，请检查输入是否正确").build(),false);
        }
    }
    @GroupMessageHandler( )
    public void d(Bot bot, GroupMessageEvent event,Matcher m) {
        System.out.println(event.getRawMessage());
        System.out.println( ShiroUtils.getAtList(event.getRawMessage()));
    }


}


