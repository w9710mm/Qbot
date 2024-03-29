package com.mm.qbot.boot;

import com.alibaba.fastjson.JSONObject;
import com.mm.qbot.bean.*;
import com.mm.qbot.bean.pushMap.*;
import com.mm.qbot.enumeration.PathEnum;
import com.mm.qbot.utils.BilibiliApi;
import com.mm.qbot.utils.LevelDB;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.*;

/**
 * @author meme
 * @version V0.0.1
 * @Package com.mm.qbot.boot
 * @Description: 初始化bilibil
 * @date 2021/11/2 9:05
 */

@Component
@Slf4j
public class Init {

    private final LevelDB levelDB = LevelDB.getInstance();

    private final BilibiliPushMap bilibiliPushMap = BilibiliPushMap.getInstance();

    private final WeiBoPushMap weiBoPushMap = WeiBoPushMap.getInstance();

    private final TiktokPushMap tiktokPushMap = TiktokPushMap.getInstance();

    private final UserAgentList userAgentList=UserAgentList.getInstance();

    @Autowired
    private Scheduler scheduler;




    @Bean
    public  void initFilePath(){

        File dynamicImageFile=new File(PathEnum.getType(PathEnum.DYNAMIC_IMAGE_FILE.getId()));
        File asoulPaPerImageFile=new File(PathEnum.getType(PathEnum.ASOUL_PAPER_IMAGE_FILE.getId()));

        File asoulPaPerTextFile=new File(PathEnum.getType(PathEnum.ASOUL_PAPER_TEXT_FILE.getId())) ;
        File weiboImageFile=new File(PathEnum.getType(PathEnum.WEIBO_IMAGE_FILE.getId())) ;

        File levelFile=new File(PathEnum.getType(PathEnum.LEVEL_DB.getId()));
        if (!asoulPaPerImageFile.exists()){
            log.debug("周报图片文件夹不存在，重新创建");
            if (!asoulPaPerImageFile.mkdirs()){
                log.error("周报图片文件夹创建失败");
            }
        }

        if (!dynamicImageFile.exists()){
            log.debug("动态图片文件夹不存在，重新创建");
            if (!dynamicImageFile.mkdirs()){
                log.error("动态文件夹创建失败");
            }
        }

        if (!asoulPaPerTextFile.exists()){
            log.debug("周报文本文件夹不存在，重新创建");
            if (!asoulPaPerTextFile.mkdirs()){
                log.error("周报文本文件夹创建失败");
            }
        }

        if (!weiboImageFile.exists()){
            log.debug("微博图片文件夹不存在，重新创建");
            if (!weiboImageFile.mkdirs()){
                log.error("微博图片文件夹创建失败");
            }
        }
        if (!levelFile.exists()){
            log.debug("leveldb文件夹不存在，重新创建");
            if (!levelFile.mkdirs()){
                log.error("leveldb文件夹创建失败");
            }
        }

    }
   @Bean
    public void initPushMap() {

        log.info("载入推送表……");
        UserSubscribeMap userSubscribeMapforDB = (UserSubscribeMap) levelDB.getObject("userSubscribeMap");
        UserSubscribeMap userSubscribeMap=UserSubscribeMap.getInstance();
        if (userSubscribeMapforDB!=null){
            log.debug("推送表不存在！重新生成中。");
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

        log.info("正在还原推送表");
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

//    @Bean
    public void init1(){
        UserSubscribeMap userSubscribeMap=UserSubscribeMap.getInstance();
        Map<Long, UserSubscribe> groupSubscribeMap = userSubscribeMap.getGroupSubscribeMap();
        Map<Long, UserSubscribe> privateSubscribeMap = userSubscribeMap.getPrivateSubscribeMap();


        UserSubscribe a706592235=new UserSubscribe();
        User ava=new User();
        ava.setUid("672346917");
        ava.setUname("向晚大魔王");
        User bella=new User();
        bella.setUid("672353429");
        bella.setUname("贝拉kira");
        User carol=new User();
        carol.setUid("351609538");
        carol.setUname("珈乐Carol");
        User diana=new User();
        diana.setUid("672328094");
        diana.setUname("嘉然今天吃什么");
        User eileen=new User();
        eileen.setUid("672342685");
        eileen.setUname("乃琳Queen");
        User asoul=new User();
        asoul.setUid("703007996");
        asoul.setUname("A-SOUL_Official");
        User ap=new User();
        ap.setUid("1185499676");
        ap.setUname("ASOUL周报");

        User xs=new User();
        xs.setUid("44002411");
        xs.setUname("小松不漫步");

        User tp=new User();
        tp.setUid("247210788");
        tp.setUname("嘉心糖周报");

        User aza=new User();
        aza.setUid("480680646");
        aza.setUname("阿萨Aza");
        LinkedHashSet<User> l1=new LinkedHashSet<>();
        l1.add(asoul);
        l1.add(ava);
        l1.add(bella);
        l1.add(carol);
        l1.add(diana);
        l1.add(eileen);
        l1.add(ap);

        a706592235.setIsGroup(true);
        a706592235.setBids(l1);
        groupSubscribeMap.put(706592235L,a706592235);

        LinkedHashSet<User> l2=new LinkedHashSet<>();

        l2.add(asoul);
        l2.add(ava);
        l2.add(bella);
        l2.add(carol);
        l2.add(diana);
        l2.add(eileen);
        l2.add(ap);
        l2.add(xs);

        UserSubscribe a959969138=new UserSubscribe();
        a959969138.setIsGroup(true);
        groupSubscribeMap.put(959969138L,a959969138);

        LinkedHashSet<User> l3=new LinkedHashSet<>();

        l3.add(asoul);
        l3.add(ava);
        l3.add(bella);
        l3.add(carol);
        l3.add(diana);
        l3.add(eileen);
        l3.add(tp);
        UserSubscribe a245530949=new UserSubscribe();
        a245530949.setIsGroup(true);
        groupSubscribeMap.put(245530949L,a245530949);

        LinkedHashSet<User> l4=new LinkedHashSet<>();

       l4.add(aza);
        UserSubscribe a=new UserSubscribe();
        a.setIsGroup(false);
        privateSubscribeMap.put(1093195408L,a);
        levelDB.put("userSubscribeMap",userSubscribeMap);

    }

    @Bean
    public void initBilibiliDynamicId(){

        JSONObject dynamic = BilibiliApi.getNewDynamic("1823651096", "268435455", "0", "webball", "web");
        Long   dynamicIdOffset=dynamic.getJSONObject("data").getLong("max_dynamic_id");
        bilibiliPushMap.setDynamicIdOffset(dynamicIdOffset);

    }

    @Bean
    public void initUaList(){
        log.info("初始化随机UA");
        List<String> uaList = userAgentList.getUaList();
        uaList.add("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/14.0.835.163 Safari/535.1");
        uaList.add("Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; WOW64; Trident/5.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; Media Center PC 6.0; InfoPath.3; .NET4.0C; .NET4.0E)");
        uaList.add("Mozilla/5.0 (Windows NT 6.1) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/13.0.782.41 Safari/535.1 QQBrowser/6.9.11079.201");
        uaList.add("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/94.0.4606.61 Safari/537.36");
        uaList.add("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.198 Safari/537.36 QIHU 360SE");
    }
    @Bean
    public void initWeiboPushMap(){
        log.info("初始化微博推送表");
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
        log.info("初始化抖音推送表");
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
        bella.setUid("MS4wLjABAAAAlpnJ0bXVDV6BNgbHUYVWnnIagRqeeZyNyXB84JXTqAS5tgGjAtw0ZZkv0KSHYyhP");
        bella.setUname("贝拉kira");
        User carol=new User();
        carol.setUid("MS4wLjABAAAAuZHC7vwqRhPzdeTb24HS7So91u9ucl9c8JjpOS2CPK-9Kg2D32Sj7-mZYvUCJCya");
        carol.setUname("珈乐Carol");
        User diana=new User();
        diana.setUid("MS4wLjABAAAA5ZrIrbgva_HMeHuNn64goOD2XYnk4ItSypgRHlbSh1c");
        diana.setUname("嘉然今天吃什么");
        User eileen=new User();
        eileen.setUid("MS4wLjABAAAAxCiIYlaaKaMz_J1QaIAmHGgc3bTerIpgTzZjm0na8w5t2KTPrCz4bm_5M5EMPy92");
        eileen.setUname("乃琳Queen");
        User asoul=new User();
        asoul.setUid("MS4wLjABAAAAflgvVQ5O1K4RfgUu3k0A2erAZSK7RsdiqPAvxcObn93x2vk4SKk1eUb6l_D4MX-n");
        asoul.setUname("A-SOUL_Official");
        groupMap.put(ava,groups);
        groupMap.put(bella,groups);
        groupMap.put(carol,groups);
        groupMap.put(diana,groups);
        groupMap.put(eileen,groups);
        groupMap.put(asoul,groups);


    }



}


