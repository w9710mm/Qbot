package com.mm.qbot.controller;

import com.mikuac.shiro.common.utils.MsgUtils;
import com.mikuac.shiro.core.Bot;
import com.mikuac.shiro.core.BotContainer;
import com.mm.qbot.bean.pushMap.TiktokPushMap;
import com.mm.qbot.bean.pushMap.User;
import com.mm.qbot.service.TikTokService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author meme
 * @version V0.0.1
 * @Package com.mm.qbot.controller
 * @Description:
 * @date 2021/11/10 8:59
 */
@Slf4j
@EnableScheduling
@Controller
@EnableAsync
public class TikTokController {


    private final Map<User, LinkedHashSet<Long>> groupMap=TiktokPushMap.getInstance().getGroupMap();
    @Resource
    private BotContainer botContainer;

    @Resource
    private TikTokService tikTokService;




    //或直接指定时间间隔，例如：5秒
@Async
    @Scheduled(initialDelay=1000, fixedDelay=1000)
    public void tikTokPush() {
        Map<Long, Bot> robots = botContainer.robots;


        for (Long aLong : robots.keySet()) {
            Bot  bot = robots.get(aLong);
            Set<Map.Entry<User,LinkedHashSet<Long>>> entries=groupMap.entrySet();
            for (Map.Entry<User,LinkedHashSet<Long>> entry:entries) {
                MsgUtils msgUtils = tikTokService.getVideo(entry.getKey().getUid());
                try {
                if (msgUtils==null){
                        log.debug(String.format("获取抖音视频%s失败", entry.getKey().getUid()));
                        Thread.sleep(1000 * 30);
                        continue;
                    }
                for (Long longs:entry.getValue()) {

                    log.info(String.format("推送给群%s抖音视频%s",longs,entry.getKey().getUid()));
                    bot.sendGroupMsg(longs,msgUtils.build(),false);
                }
                    bot.sendPrivateMsg(962349367, msgUtils.build(),false);
                Thread.sleep(1000*30);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
    }


}
