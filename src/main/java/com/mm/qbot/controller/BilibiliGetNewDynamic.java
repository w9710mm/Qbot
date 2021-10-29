package com.mm.qbot.controller;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mikuac.shiro.core.BotPlugin;
import com.mm.qbot.strategy.BilibiliStrategy;
import com.mm.qbot.utils.BilibiliApi;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;

@Configuration      //1.主要用于标记配置类，兼备Component的效果。
@EnableScheduling
public class BilibiliGetNewDynamic extends BotPlugin {



        private String currentDynamicId="586929370015414000";

        //3.添加定时任务

        //或直接指定时间间隔，例如：5秒
        @Scheduled(fixedRate=1000*60)
        private void configureTasks() {

            JSONObject newDynamic = BilibiliApi.getNewDynamic("1359202", "268435455", currentDynamicId, "weball", "web");

            currentDynamicId=newDynamic.getJSONObject("data").getString("max_dynamic_id");
            JSONArray cards=newDynamic.getJSONObject("data").getJSONArray("cards");
            for (Object object:cards) {
               JSONObject card=(JSONObject)object;
                String s = BilibiliStrategy.dynamicStrategy(card);
                System.out.println(s);
            }
            System.out.println(currentDynamicId);
            System.err.println("执行静态定时任务时间: " + LocalDateTime.now());

    }
}
