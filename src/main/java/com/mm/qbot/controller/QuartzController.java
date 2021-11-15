package com.mm.qbot.controller;

import com.mikuac.shiro.annotation.GroupMessageHandler;
import com.mikuac.shiro.core.Bot;
import com.mikuac.shiro.core.BotPlugin;
import com.mikuac.shiro.dto.event.message.GroupMessageEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

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





    @GroupMessageHandler(cmd = "\b任务表",groupWhiteList ={245530949,706592235} )
    public void showQuartzJob(Bot bot, GroupMessageEvent event) {

    }
    @GroupMessageHandler(cmd = "\b\\[CQ:at,qq=([0-9]+)\\]ddl ([0-9]{4}-[0-9]{2}-[0-9]{2}) (.+)" )
    public void addQuartzJob(Bot bot, GroupMessageEvent event, Matcher m) {
        System.out.println(event.getRawMessage());
    }

    @GroupMessageHandler(cmd = "\b\\[CQ:at,qq=([0-9]+)\\]del",groupWhiteList ={245530949,706592235} )
    public void deleteQuartzJob(Bot bot, GroupMessageEvent event,Matcher m) {

    }

    @GroupMessageHandler
    public void deleteQ1uartzJob(Bot bot, GroupMessageEvent event) {

        System.out.println(event.getRawMessage());
    }
}


