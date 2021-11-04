package com.mm.qbot.controller;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mikuac.shiro.core.Bot;
import com.mikuac.shiro.core.BotContainer;
import com.mikuac.shiro.core.BotPlugin;
import com.mm.qbot.Exception.BilibiliException;
import com.mm.qbot.dto.BilibiliPushMap;
import com.mm.qbot.strategy.BilibiliStrategy;
import com.mm.qbot.utils.BilibiliApi;
import com.mm.qbot.utils.LevelDB;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;

@EnableScheduling
@Controller
@Slf4j
public class BilibiliGetNewDynamicController extends BotPlugin {



        private final BilibiliPushMap bilibiliPushMap=BilibiliPushMap.getInstance();

        private final Map<Long, Set<Long>> privateMap=BilibiliPushMap.getInstance().getPrivateMap();

        private final Map<Long,Set<Long>> groupMap=BilibiliPushMap.getInstance().getGroupMap();
         @Resource
        private   BotContainer botContainer;




    //或直接指定时间间隔，例如：5秒

    @Scheduled(initialDelay=10000, fixedRate=5000)
    public void configureTasks() {
         Map<Long, Bot> robots = botContainer.robots;
        JSONObject newDynamic = BilibiliApi.getNewDynamic("1823651096", "268435455", String.valueOf(bilibiliPushMap.getDynamicIdOffset()), "weball", "web");
        bilibiliPushMap.setDynamicIdOffset(newDynamic.getJSONObject("data").getLong("max_dynamic_id"));


        for (Long aLong : robots.keySet()) {
             Bot  bot = robots.get(aLong);



        JSONArray cards=newDynamic.getJSONObject("data").getJSONArray("cards");
            if (cards!=null) {
                for (Object object : cards) {
                    JSONObject card = (JSONObject) object;
                    Long uid=card.getJSONObject("desc").getLong("uid");
                    try {
                        if (privateMap.containsKey(uid)){
                            for (Long qid:privateMap.get(uid)) {
                                bot.sendPrivateMsg(qid, BilibiliStrategy.dynamicStrategy(card).build(), false);
                            }
                        }
                        if (groupMap.containsKey(uid)){
                            for (Long qid:groupMap.get(uid)) {
                                bot.sendGroupMsg(qid, BilibiliStrategy.dynamicStrategy(card).build(), false);
                            }
                        }

                    } catch (BilibiliException e) {
                        e.printStackTrace();
                    }
                }
                log.info("刷新动态");
            }
         }


    }
}
