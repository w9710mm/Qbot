package com.mm.qbot.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mikuac.shiro.common.utils.MsgUtils;
import com.mikuac.shiro.core.Bot;
import com.mikuac.shiro.core.BotContainer;
import com.mm.qbot.Exception.BilibiliException;
import com.mm.qbot.dto.pushMap.BilibiliPushMap;
import com.mm.qbot.dto.pushMap.User;
import com.mm.qbot.strategy.BilibiliStrategy;
import com.mm.qbot.utils.BilibiliApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.Resource;
import java.util.LinkedHashSet;
import java.util.Map;

/**
 * @author meme
 * @version V0.0.1
 * @Package com.mm.qbot.controller
 * @Description:
 * @date 2021/11/10 8:59
 */
@Slf4j
public class WeiboController {
    private final BilibiliPushMap bilibiliPushMap=BilibiliPushMap.getInstance();

    private final Map<User, LinkedHashSet<Long>> privateMap=BilibiliPushMap.getInstance().getPrivateMap();

    private final Map<User, LinkedHashSet<Long>> groupMap=BilibiliPushMap.getInstance().getGroupMap();
    @Resource
    private BotContainer botContainer;




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
                    JSONObject userInfo =card.getJSONObject("desc").getJSONObject("user_profile").getJSONObject("info");
                    user.setUid( userInfo.getString("uid"));
                    user.setUname(userInfo.getString("uname"));
                    try {
                        if (privateMap.containsKey(user)){
                            for (Long qid:privateMap.get(user)) {
                                MsgUtils msgUtils = BilibiliStrategy.dynamicStrategy(card);
                                if (msgUtils!=null) {
                                    bot.sendPrivateMsg(qid, msgUtils.build(), false);
                                }
                            }
                        }
                        if (groupMap.containsKey(user)){
                            for (Long qid:groupMap.get(user)) {
                                MsgUtils msgUtils = BilibiliStrategy.dynamicStrategy(card);
                                if (msgUtils!=null) {

                                    bot.sendGroupMsg(qid, msgUtils.build(), false);
                                }
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
