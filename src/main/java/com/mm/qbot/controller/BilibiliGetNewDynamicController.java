package com.mm.qbot.controller;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mikuac.shiro.core.Bot;
import com.mikuac.shiro.core.BotContainer;
import com.mikuac.shiro.core.BotPlugin;
import com.mm.qbot.Exception.BilibiliException;
import com.mm.qbot.dto.BilibiliPushMap;
import com.mm.qbot.dto.User;
import com.mm.qbot.strategy.BilibiliStrategy;
import com.mm.qbot.utils.BilibiliApi;
import com.mm.qbot.utils.LevelDB;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

@EnableScheduling
@Controller
@Slf4j
public class BilibiliGetNewDynamicController extends BotPlugin {



        private final BilibiliPushMap bilibiliPushMap=BilibiliPushMap.getInstance();

        private final Map<User, LinkedHashSet<Long>> privateMap=BilibiliPushMap.getInstance().getPrivateMap();

        private final Map<User, LinkedHashSet<Long>> groupMap=BilibiliPushMap.getInstance().getGroupMap();
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
            if (cards!=null&&cards.size()!=0) {
                for (Object object : cards) {
                    JSONObject card = (JSONObject) object;
                    User user=new User();
                    user.setUid( card.getJSONObject("desc").getString("uid"));
                    user.setUname(card.getJSONObject("desc").getString("uname"));
                    try {
                        if (privateMap.containsKey(user)){
                            for (Long qid:privateMap.get(user)) {
                                bot.sendPrivateMsg(qid, BilibiliStrategy.dynamicStrategy(card).build(), false);
                            }
                        }
                        if (groupMap.containsKey(user)){
                            for (Long qid:groupMap.get(user)) {
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
