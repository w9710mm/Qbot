package com.mm.qbot.controller;


import com.mikuac.shiro.annotation.GroupMessageHandler;
import com.mikuac.shiro.annotation.PrivateMessageHandler;
import com.mikuac.shiro.core.BotPlugin;
import com.mm.qbot.bean.pushMap.User;
import com.mm.qbot.service.AsoulPaperService;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

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
    }

    @PrivateMessageHandler
    public void asoulDataWeekly(){

    asoulPaperService.weeklyData(userList);

    }

    @GroupMessageHandler
    public void asoulImageWeekly(){

    }
}
