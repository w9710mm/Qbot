package com.mm.qbot.controller;

import com.mikuac.shiro.common.utils.MsgUtils;
import com.mikuac.shiro.core.Bot;
import com.mikuac.shiro.core.BotContainer;
import com.mm.qbot.dto.pushMap.TiktokPushMap;
import com.mm.qbot.dto.pushMap.User;
import com.mm.qbot.service.TikTokService;
import com.mm.qbot.utils.LevelDB;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;

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
public class TikTokController {


    private final Map<User, LinkedHashSet<Long>> groupMap=TiktokPushMap.getInstance().getGroupMap();
    @Resource
    private BotContainer botContainer;

    @Resource
    private TikTokService tikTokService;




    //或直接指定时间间隔，例如：5秒

    @Scheduled(initialDelay=10000, fixedRate=1000 * 30*5)
    public void configureTasks() {
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

                    log.debug(String.format("推送给群%s抖音视频%s",longs,entry.getKey().getUid()));
                    bot.sendGroupMsg(longs,msgUtils.build(),false);
                }
                Thread.sleep(1000*30);
                bot.sendPrivateMsg(962349367, msgUtils.build(),false);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
    }


}
