package com.mm.qbot.boot;

import com.alibaba.fastjson.JSONObject;
import com.mm.qbot.dto.*;
import com.mm.qbot.utils.BilibiliApi;
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


        UserSubscribeMap userSubscribeMapforDB = (UserSubscribeMap) levelDB.get("userSubscribeMap");
        UserSubscribeMap userSubscribeMap=UserSubscribeMap.getInstance();
        if (userSubscribeMapforDB!=null){
            userSubscribeMap.setGroupSubscribeMap(userSubscribeMapforDB.getGroupSubscribeMap());
            userSubscribeMap.setPrivateSubscribeMap(userSubscribeMapforDB.getPrivateSubscribeMap());
        }

        if (userSubscribeMapforDB == null) {
            levelDB.put("userSubscribeMap", userSubscribeMap);
        }

        Map<Long, UserSubscribe> subscribeMap = userSubscribeMap.getSubscribeMap();



        Map<User, Set<Long>> bilibiliGroupPushMap = bilibiliPushMap.getGroupMap();

        Map<User, Set<Long>> bilibiliPrivatePushMap = bilibiliPushMap.getPrivateMap();

        Map<User, Set<Long>> weiBoGroupPushMap = weiBoPushMap.getGroupMap();

        Map<User,Set<Long>> weiBoPrivatePushMap = weiBoPushMap.getPrivateMap();

        Map<User,Set<Long>> tiktokGroupPushMap = tiktokPushMap.getGroupMap();

        Map<User,Set<Long>> tiktokPrivatePushMap = tiktokPushMap.getPrivateMap();


        subscribeMap.forEach((key, userSubscribe) -> {
            Set<User> tikids = userSubscribe.getTikid();
            Set<User> weiboids = userSubscribe.getWeiboids();
            Set<User> bids = userSubscribe.getBids();
            if (userSubscribe.getIsGroup()) {
                if (weiboids != null && weiboids.size() != 0) {
                    for (User id : weiboids) {
                        if (!weiBoGroupPushMap.containsKey(id)) {
                            Set<Long> longs = new HashSet<>();
                            longs.add(key);
                            weiBoGroupPushMap.put(id, longs);
                        }
                        if (weiBoGroupPushMap.containsKey(id)) {
                            Set<Long> longs = weiBoGroupPushMap.get(id);
                            longs.add(key);
                        }
                    }
                }
                if (tikids != null && tikids.size() != 0) {
                    for (User id : tikids) {
                        if (!tiktokGroupPushMap.containsKey(id)) {
                            Set<Long> longs = new HashSet<>();
                            longs.add(key);
                            tiktokGroupPushMap.put(id, longs);
                        }
                        if (tiktokGroupPushMap.containsKey(id)) {
                            Set<Long> longs = tiktokGroupPushMap.get(id);
                            longs.add(key);
                        }
                    }
                }
                if (bids != null && bids.size() != 0) {
                    for (User id : bids) {
                        if (!bilibiliGroupPushMap.containsKey(id)) {
                            Set<Long> longs = new HashSet<>();
                            longs.add(key);
                            bilibiliGroupPushMap.put(id, longs);
                        }
                        if (bilibiliGroupPushMap.containsKey(id)) {
                            Set<Long> longs = bilibiliGroupPushMap.get(id);
                            longs.add(key);
                        }
                    }
                }

                if (!userSubscribe.getIsGroup()) {
                    if (bids != null && bids.size() != 0) {
                        for (User id : bids) {
                            if (!bilibiliPrivatePushMap.containsKey(id)) {
                                Set<Long> longs = new HashSet<>();
                                longs.add(key);
                                bilibiliPrivatePushMap.put(id, longs);
                            }
                            if (bilibiliPrivatePushMap.containsKey(id)) {
                                Set<Long> longs = bilibiliPrivatePushMap.get(id);
                                longs.add(key);
                            }
                        }
                    }
                    if (weiboids != null && weiboids.size() != 0) {
                        for (User id : weiboids) {
                            if (!weiBoPrivatePushMap.containsKey(id)) {
                                Set<Long> longs = new HashSet<>();
                                longs.add(key);
                                weiBoPrivatePushMap.put(id, longs);
                            }
                            if (weiBoPrivatePushMap.containsKey(id)) {
                                Set<Long> longs = weiBoPrivatePushMap.get(id);
                                longs.add(key);
                            }
                        }
                    }
                    if (tikids != null && tikids.size() != 0) {
                        for (User id : tikids) {
                            if (!tiktokPrivatePushMap.containsKey(id)) {
                                Set<Long> longs = new HashSet<>();
                                longs.add(key);
                                tiktokPrivatePushMap.put(id, longs);
                            }
                            if (tiktokPrivatePushMap.containsKey(id)) {
                                Set<Long> longs = tiktokPrivatePushMap.get(id);
                                longs.add(key);
                            }
                        }
                    }
                }
            }
        });

    }

    @Bean
    public void initBilibiliDynamicId(){

        JSONObject dynamic = BilibiliApi.getNewDynamic("1823651096", "268435455", "0", "webball", "web");
        Long   dynamicIdOffset=dynamic.getJSONObject("data").getLong("max_dynamic_id");
        bilibiliPushMap.setDynamicIdOffset(dynamicIdOffset);

    }

}


