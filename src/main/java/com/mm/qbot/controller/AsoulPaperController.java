package com.mm.qbot.controller;


import com.mikuac.shiro.annotation.GroupMessageHandler;
import com.mikuac.shiro.core.BotPlugin;
import org.springframework.stereotype.Controller;

/**
 * @author 96234
 */
@Controller
public class AsoulPaperController extends BotPlugin {
    @GroupMessageHandler
    public void asoulDataWeekly(){

    }

    @GroupMessageHandler
    public void asoulImageWeekly(){

    }
}
