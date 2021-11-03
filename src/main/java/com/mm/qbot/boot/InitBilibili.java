package com.mm.qbot.boot;

import com.alibaba.fastjson.JSONObject;
import com.mm.qbot.dto.*;
import com.mm.qbot.utils.LevelDB;
import netscape.javascript.JSObject;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;

import java.util.*;

/**
 * @author meme
 * @version V0.0.1
 * @Package com.mm.qbot.boot
 * @Description: 初始化bilibil
 * @date 2021/11/2 9:05
 */

@Component
public class InitBilibili {

    private final LevelDB levelDB = LevelDB.getInstance();

    private final BilibiliPushMap bilibiliPushMap = BilibiliPushMap.getInstance();

    private final WeiBoPushMap weiBoPushMap = WeiBoPushMap.getInstance();

    private final TiktokPushMap tiktokPushMap = TiktokPushMap.getInstance();


    @Bean
    public void initPushMap() {


        UserSubscribeMap usersubscribeMap =(UserSubscribeMap)levelDB.get("userSubscribeMap");

        if (usersubscribeMap==null){
            usersubscribeMap=UserSubscribeMap.getInstance();
            levelDB.put("userSubscribeMap",usersubscribeMap);
        }
        Map<Long, UserSubscribe> subscribeMap = usersubscribeMap.getSubscribeMap();

        Map<Long, Set<Long>> bilibiliGroupPushMap = bilibiliPushMap.getGroupMap();

        Map<Long, Set<Long>> bilibiliPrivatePushMap = bilibiliPushMap.getPrivateMap();

        Map<Long, Set<Long>> weiBoGroupPushMap = weiBoPushMap.getGroupMap();

        Map<Long, Set<Long>> weiBoPrivatePushMap = weiBoPushMap.getPrivateMap();

        Map<Long, Set<Long>> tiktokGroupPushMap = tiktokPushMap.getGroupMap();

        Map<Long, Set<Long>> tiktokPrivatePushMap = tiktokPushMap.getPrivateMap();



        subscribeMap.forEach((key, userSubscribe) -> {
            Set<Long> tikids = userSubscribe.getTikids();
            Set<Long> weiboids = userSubscribe.getWeiboids();
            Set<Long> bids = userSubscribe.getBids();
            if (userSubscribe.getIsGroup()) {
                for (Long id : weiboids){
                    if (!weiBoGroupPushMap.containsKey(id)){
                        Set<Long> longs=new HashSet<>();
                        longs.add(key);
                        weiBoGroupPushMap.put(id,longs);
                    }
                    if (weiBoGroupPushMap.containsKey(id)){
                        Set<Long> longs=weiBoGroupPushMap.get(id);
                        longs.add(key);
                    }
                }
                for (Long id : tikids){
                    if (!tiktokGroupPushMap.containsKey(id)){
                        Set<Long> longs=new HashSet<>();
                        longs.add(key);
                        tiktokGroupPushMap.put(id,longs);
                    }
                    if (tiktokGroupPushMap.containsKey(id)){
                        Set<Long> longs=tiktokGroupPushMap.get(id);
                        longs.add(key);
                    }
                }
                for (Long id : bids){
                    if (!bilibiliGroupPushMap.containsKey(id)){
                        Set<Long> longs=new HashSet<>();
                        longs.add(key);
                        bilibiliGroupPushMap.put(id,longs);
                    }
                    if (bilibiliGroupPushMap.containsKey(id)){
                        Set<Long> longs=bilibiliGroupPushMap.get(id);
                        longs.add(key);
                    }
                }

            }
            if (!userSubscribe.getIsGroup()) {

                for (Long id : bids) {
                    if (!bilibiliPrivatePushMap.containsKey(id)){
                        Set<Long> longs=new HashSet<>();
                        longs.add(key);
                        bilibiliPrivatePushMap.put(id,longs);
                    }
                    if (bilibiliPrivatePushMap.containsKey(id)){
                        Set<Long> longs=bilibiliPrivatePushMap.get(id);
                        longs.add(key);
                    }
                }
                for (Long id : weiboids) {
                    if (!weiBoPrivatePushMap.containsKey(id)){
                        Set<Long> longs=new HashSet<>();
                        longs.add(key);
                        weiBoPrivatePushMap.put(id,longs);
                    }
                    if (weiBoPrivatePushMap.containsKey(id)){
                        Set<Long> longs=weiBoPrivatePushMap.get(id);
                        longs.add(key);
                    }
                }
                for (Long id : tikids){
                    if (!tiktokPrivatePushMap.containsKey(id)){
                        Set<Long> longs=new HashSet<>();
                        longs.add(key);
                        tiktokPrivatePushMap.put(id,longs);
                    }
                    if (tiktokPrivatePushMap.containsKey(id)){
                        Set<Long> longs=tiktokPrivatePushMap.get(id);
                        longs.add(key);
                    }
                }

            }
        });

    }
}


