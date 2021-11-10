package com.mm.qbot.boot;

import com.alibaba.fastjson.JSONObject;
import com.mm.qbot.dto.*;
import com.mm.qbot.dto.pushMap.*;
import com.mm.qbot.utils.BilibiliApi;
import com.mm.qbot.utils.LevelDB;
import org.checkerframework.checker.units.qual.A;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @author meme
 * @version V0.0.1
 * @Package com.mm.qbot.boot
 * @Description: 初始化bilibil
 * @date 2021/11/2 9:05
 */

@Component
public class Init {

    private final LevelDB levelDB = LevelDB.getInstance();

    private final BilibiliPushMap bilibiliPushMap = BilibiliPushMap.getInstance();

    private final WeiBoPushMap weiBoPushMap = WeiBoPushMap.getInstance();

    private final TiktokPushMap tiktokPushMap = TiktokPushMap.getInstance();

    private final UserAgentList userAgentList=UserAgentList.getInstance();

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

        Map<Long, UserSubscribe> privateSubscribeMap = userSubscribeMap.getPrivateSubscribeMap();

        Map<Long, UserSubscribe> groupSubscribeMap  = userSubscribeMap.getGroupSubscribeMap();




        Map<User, LinkedHashSet<Long>> bilibiliGroupPushMap = bilibiliPushMap.getGroupMap();

        Map<User, LinkedHashSet<Long>> bilibiliPrivatePushMap = bilibiliPushMap.getPrivateMap();

        Map<User, LinkedHashSet<Long>> weiBoGroupPushMap = weiBoPushMap.getGroupMap();

        Map<User,LinkedHashSet<Long>> weiBoPrivatePushMap = weiBoPushMap.getPrivateMap();

        Map<User,LinkedHashSet<Long>> tiktokGroupPushMap = tiktokPushMap.getGroupMap();

        Map<User,LinkedHashSet<Long>> tiktokPrivatePushMap = tiktokPushMap.getPrivateMap();

        groupSubscribeMap.forEach((key, userSubscribe) -> {
            Set<User> tikids = userSubscribe.getTikid();
            Set<User> weiboids = userSubscribe.getWeiboids();
            Set<User> bids = userSubscribe.getBids();
            if (userSubscribe.getIsGroup()) {
                if (weiboids != null && weiboids.size() != 0) {
                    for (User id : weiboids) {
                        if (!weiBoGroupPushMap.containsKey(id)) {
                            LinkedHashSet<Long> longs = new LinkedHashSet<>();
                            longs.add(key);
                            weiBoGroupPushMap.put(id, longs);
                        }
                        if (weiBoGroupPushMap.containsKey(id)) {
                            LinkedHashSet<Long> longs = weiBoGroupPushMap.get(id);
                            longs.add(key);
                        }
                    }
                }
                if (tikids != null && tikids.size() != 0) {
                    for (User id : tikids) {
                        if (!tiktokGroupPushMap.containsKey(id)) {
                            LinkedHashSet<Long> longs = new LinkedHashSet<>();
                            longs.add(key);
                            tiktokGroupPushMap.put(id, longs);
                        }
                        if (tiktokGroupPushMap.containsKey(id)) {
                            LinkedHashSet<Long> longs = tiktokGroupPushMap.get(id);
                            longs.add(key);
                        }
                    }
                }
                if (bids != null && bids.size() != 0) {
                    for (User id : bids) {
                        if (!bilibiliGroupPushMap.containsKey(id)) {
                            LinkedHashSet<Long> longs = new LinkedHashSet<>();
                            longs.add(key);
                            bilibiliGroupPushMap.put(id, longs);
                        }
                        if (bilibiliGroupPushMap.containsKey(id)) {
                            LinkedHashSet<Long> longs = bilibiliGroupPushMap.get(id);
                            longs.add(key);
                        }
                    }
                }

            }
        });


        privateSubscribeMap.forEach((key, userSubscribe) -> {
            Set<User> tikids = userSubscribe.getTikid();
            Set<User> weiboids = userSubscribe.getWeiboids();
            Set<User> bids = userSubscribe.getBids();
            if (!userSubscribe.getIsGroup()) {
                if (bids != null && bids.size() != 0) {
                    for (User id : bids) {
                        if (!bilibiliPrivatePushMap.containsKey(id)) {
                            LinkedHashSet<Long> longs = new LinkedHashSet<>();
                            longs.add(key);
                            bilibiliPrivatePushMap.put(id, longs);
                        }
                        if (bilibiliPrivatePushMap.containsKey(id)) {
                            LinkedHashSet<Long> longs = bilibiliPrivatePushMap.get(id);
                            longs.add(key);
                        }
                    }
                }
                if (weiboids != null && weiboids.size() != 0) {
                    for (User id : weiboids) {
                        if (!weiBoPrivatePushMap.containsKey(id)) {
                            LinkedHashSet<Long> longs = new LinkedHashSet<>();
                            longs.add(key);
                            weiBoPrivatePushMap.put(id, longs);
                        }
                        if (weiBoPrivatePushMap.containsKey(id)) {
                            LinkedHashSet<Long> longs = weiBoPrivatePushMap.get(id);
                            longs.add(key);
                        }
                    }
                }
                if (tikids != null && tikids.size() != 0) {
                    for (User id : tikids) {
                        if (!tiktokPrivatePushMap.containsKey(id)) {
                            LinkedHashSet<Long> longs = new LinkedHashSet<>();
                            longs.add(key);
                            tiktokPrivatePushMap.put(id, longs);
                        }
                        if (tiktokPrivatePushMap.containsKey(id)) {
                            LinkedHashSet<Long> longs = tiktokPrivatePushMap.get(id);
                            longs.add(key);
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

    @Bean
    public void initUaList(){
        List<String> uaList = userAgentList.getUaList();
        uaList.add("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/14.0.835.163 Safari/535.1");
        uaList.add("Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; WOW64; Trident/5.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; Media Center PC 6.0; InfoPath.3; .NET4.0C; .NET4.0E)");
        uaList.add("Mozilla/5.0 (Windows NT 6.1) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/13.0.782.41 Safari/535.1 QQBrowser/6.9.11079.201");
        uaList.add("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/94.0.4606.61 Safari/537.36");
        uaList.add("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.198 Safari/537.36 QIHU 360SE");
    }
    @Bean
    public void initWeiboPushMap(){
        Map<User, LinkedHashSet<Long>> groupMap = weiBoPushMap.getGroupMap();
        LinkedHashSet<Long> groups=new LinkedHashSet<>();
        groups.add(760322595L);
        groups.add(245530949L);
        groups.add(959969138L);
        groups.add(483068702L);
        groups.add(625773658L);

        User asoul=new User();
        asoul.setUid("7519401668");
        asoul.setUname("A-SOUL_Official");
        groupMap.put(asoul,groups);
    }

    @Bean
    public void initTikTokPushMap(){
        Map<User, LinkedHashSet<Long>> groupMap = tiktokPushMap.getGroupMap();
        LinkedHashSet<Long> groups=new LinkedHashSet<>();
        groups.add(760322595L);
        groups.add(245530949L);
        groups.add(959969138L);
        groups.add(483068702L);
        groups.add(625773658L);

        User ava=new User();
        ava.setUid("MS4wLjABAAAAxOXMMwlShWjp4DONMwfEEfloRYiC1rXwQ64eydoZ0ORPFVGysZEd4zMt8AjsTbyt");
        ava.setUname("向晚大魔王");

        User bella=new User();
        ava.setUid("MS4wLjABAAAAlpnJ0bXVDV6BNgbHUYVWnnIagRqeeZyNyXB84JXTqAS5tgGjAtw0ZZkv0KSHYyhP");
        ava.setUname("贝拉kira");

        User carol=new User();
        ava.setUid("MS4wLjABAAAAuZHC7vwqRhPzdeTb24HS7So91u9ucl9c8JjpOS2CPK-9Kg2D32Sj7-mZYvUCJCya");
        ava.setUname("珈乐Carol");

        User diana=new User();
        ava.setUid("MS4wLjABAAAA5ZrIrbgva_HMeHuNn64goOD2XYnk4ItSypgRHlbSh1c");
        ava.setUname("嘉然今天吃什么");

        User eileen=new User();
        ava.setUid("MS4wLjABAAAAxCiIYlaaKaMz_J1QaIAmHGgc3bTerIpgTzZjm0na8w5t2KTPrCz4bm_5M5EMPy92");
        ava.setUname("乃琳Queen");

        User asoul=new User();
        ava.setUid("MS4wLjABAAAAflgvVQ5O1K4RfgUu3k0A2erAZSK7RsdiqPAvxcObn93x2vk4SKk1eUb6l_D4MX-n");
        ava.setUname("A-SOUL_Official");

        groupMap.put(ava,groups);
        groupMap.put(bella,groups);
        groupMap.put(carol,groups);
        groupMap.put(diana,groups);
        groupMap.put(eileen,groups);
        groupMap.put(asoul,groups);




    }


}


