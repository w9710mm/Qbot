package com.mm.qbot.controller;


import com.mikuac.shiro.annotation.GroupMessageHandler;
import com.mikuac.shiro.annotation.PrivateMessageHandler;
import com.mikuac.shiro.core.BotPlugin;
import com.mm.qbot.service.AsoulPaperService;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;

/**
 * @author 96234
 */
@Controller
public class AsoulPaperController extends BotPlugin {
    @Resource
    private     AsoulPaperService asoulPaperService;
    @PrivateMessageHandler
    public void asoulDataWeekly(){
    asoulPaperService.selectUserPage();

    }

    @GroupMessageHandler
    public void asoulImageWeekly(){

    }
}
