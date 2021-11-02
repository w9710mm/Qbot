package com.mm.qbot.controller;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mikuac.shiro.core.Bot;
import com.mikuac.shiro.core.BotContainer;
import com.mikuac.shiro.core.BotPlugin;
import com.mikuac.shiro.dto.action.anntation.PrivateMessageHandler;
import com.mm.qbot.Exception.BilibiliException;
import com.mm.qbot.strategy.BilibiliStrategy;
import com.mm.qbot.utils.BilibiliApi;
import com.mm.qbot.utils.LevelDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

@EnableScheduling
@Controller
public class BilibiliGetNewDynamicController extends BotPlugin {



        private String currentDynamicId="587665819469591800";
         @Resource
        private   BotContainer botContainer;




    //或直接指定时间间隔，例如：5秒

    @Scheduled(initialDelay=1000, fixedRate=5000)
    public void configureTasks() {
         Map<Long, Bot> robots = botContainer.robots;
        LevelDB levelDB=LevelDB.getInstance();
        currentDynamicId= (String)levelDB.get("currentDynamicId");

        for (Long aLong : robots.keySet()) {
             Bot  bot = robots.get(aLong);


         JSONObject newDynamic = BilibiliApi.getNewDynamic("1359202", "268435455", currentDynamicId, "weball", "web");

            currentDynamicId=newDynamic.getJSONObject("data").getString("max_dynamic_id");
            JSONArray cards=newDynamic.getJSONObject("data").getJSONArray("cards");

            if (cards!=null) {
                for (Object object : cards) {
                    JSONObject card = (JSONObject) object;
                    try {
                        bot.sendPrivateMsg(962349367, BilibiliStrategy.dynamicStrategy(card).build(), false);
                    } catch (BilibiliException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println(currentDynamicId);
                System.err.println("执行静态定时任务时间: " + LocalDateTime.now());
            }
         }
        levelDB.put("currentDynamicId",currentDynamicId);

    }
}
