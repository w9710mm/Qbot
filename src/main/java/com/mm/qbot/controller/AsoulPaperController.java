package com.mm.qbot.controller;


import com.mikuac.shiro.annotation.GroupMessageHandler;
import com.mikuac.shiro.annotation.PrivateMessageHandler;
import com.mikuac.shiro.common.utils.MsgUtils;
import com.mikuac.shiro.core.Bot;
import com.mikuac.shiro.core.BotPlugin;
import com.mikuac.shiro.dto.action.common.ActionData;
import com.mikuac.shiro.dto.action.response.GroupFilesResp;
import com.mikuac.shiro.dto.event.message.GroupMessageEvent;
import com.mm.qbot.bean.pushMap.User;
import com.mm.qbot.service.AsoulPaperService;
import com.mm.qbot.utils.TimeUtils;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 96234
 */
@Controller
public class AsoulPaperController extends BotPlugin {
    @Resource
    private     AsoulPaperService asoulPaperService;

    private static List<User> userList;
    static {
        userList=new ArrayList<>();
        User ava=new User();
        ava.setUid("672346917");
        ava.setUname("向晚大魔王");
        User bella=new User();
        bella.setUid("672353429");
        bella.setUname("贝拉kira");
        User carol=new User();
        carol.setUid("351609538");
        carol.setUname("珈乐Carol");
        User diana=new User();
        diana.setUid("672328094");
        diana.setUname("嘉然今天吃什么");
        User eileen=new User();
        eileen.setUid("672342685");
        eileen.setUname("乃琳Queen");
        User asoul=new User();
        asoul.setUid("703007996");
        asoul.setUname("A-SOUL_Official");
        userList.add(ava);
        userList.add(bella);
        userList.add(carol);
        userList.add(diana);
        userList.add(eileen);
        userList.add(asoul);
    }

    @GroupMessageHandler(cmd = "本周数据",groupWhiteList = {760322595,959969138,1005018209
    })
    public void asoulDataWeekly(GroupMessageEvent event, Bot bot){
        ActionData<GroupFilesResp> groupRootFiles = bot.getGroupRootFiles(event.getGroupId());
        List<GroupFilesResp.Folders> folders = groupRootFiles.getData().getFolders();

        String folderid = null;
        for (GroupFilesResp.Folders f :folders) {
            if (("每周数据文本").equals(f.getFolderName())){
                folderid=f.getFolderId();
                break;
            }
        }
        if (folderid==null){
            return;
        }
        String url = asoulPaperService.weeklyData(userList);
        if (url==null){
            MsgUtils msgUtils=MsgUtils.builder().text("数据还没收集完成，请稍后再试");
            bot.sendGroupMsg(event.getGroupId(),msgUtils.build(),false);
            return;
        }
        int i = url.lastIndexOf("\\");
       String s=url.substring(i+1,url.length());

       bot.uploadGroupFile(event.getGroupId(),url,s,folderid);

    }

    @GroupMessageHandler(cmd = "本周图片数据",groupWhiteList = {760322595,959969138})
    public void asoulImageWeekly(GroupMessageEvent event, Bot bot){
        ActionData<GroupFilesResp> groupRootFiles = bot.getGroupRootFiles(event.getGroupId());
        List<GroupFilesResp.Folders> folders = groupRootFiles.getData().getFolders();

        String folderid = null;
        for (GroupFilesResp.Folders f :folders) {
           if (("每周数据图").equals(f.getFolderName())){
               folderid=f.getFolderId();
               break;
           }
        }
        if (folderid==null){
            return;
        }
        for (User user:userList) {
            String url = asoulPaperService.weeklyImage(user);
            if (url==null){
                MsgUtils msgUtils=MsgUtils.builder().text("数据还没收集完成，请稍后再试");
                bot.sendGroupMsg(event.getGroupId(),msgUtils.build(),false);
                return;
            }
            int i = url.lastIndexOf("\\");

            String s=url.substring(i+1,url.length());
            bot.uploadGroupFile(event.getGroupId(),url,s,folderid);
        }
    }
}
