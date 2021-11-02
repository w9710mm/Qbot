package com.mm.qbot.boot;

import com.alibaba.fastjson.JSONObject;
import com.mm.qbot.dto.*;
import com.mm.qbot.utils.LevelDB;
import netscape.javascript.JSObject;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author meme
 * @version V0.0.1
 * @Package com.mm.qbot.boot
 * @Description: 初始化bilibil
 * @date 2021/11/2 9:05
 */

@Component
public class InitBilibili {

    private final LevelDB levelDB =LevelDB.getInstance();

    private final BilibiliPushMap bilibiliPushMap =BilibiliPushMap.getInstance();

    private final WeiBoPushMap weiBoPushMap =WeiBoPushMap.getInstance();

    private final TiktokPushMap tiktokPushMap =TiktokPushMap.getInstance();

    private final UserSubscribeMap userSubscribeMap=UserSubscribeMap.getInstance();

    @Bean
    public  void InitPushMap(){

        MultiValueMap<Long, UserSubscribe> subscribeMap = userSubscribeMap.getSubscribeMap();

        MultiValueMap<Long, UserSubscribe> bilibiliGroupPushMap = bilibiliPushMap.getGroupMap();

        MultiValueMap<Long, UserSubscribe> bilibiliPrivatePushMap = bilibiliPushMap.getPrivateMap();

        MultiValueMap<Long, UserSubscribe>  weiBoGroupPushMap = weiBoPushMap.getGroupMap();

        MultiValueMap<Long, UserSubscribe>  weiBoPrivatePushMap = weiBoPushMap.getPrivateMap();

        MultiValueMap<Long, UserSubscribe> tiktokGroupPushMap = tiktokPushMap.getGroupMap();

        MultiValueMap<Long, UserSubscribe> tiktokPrivatePushMap = tiktokPushMap.getPrivateMap();



        for (Map.Entry<Long, List<UserSubscribe>> subscribe : subscribeMap.entrySet()) {
            List<UserSubscribe> userSubscribes = subscribe.getValue();
            for (UserSubscribe value:userSubscribes) {
                List<Long> tikids = value.getTikids();
                List<Long> weiboids = value.getWeiboids();
                List<Long> bids = value.getBids();
                if (value.getIsGroup()){
                    for (Long id:weiboids) weiBoGroupPushMap.add(id,value);
                    for (Long id:tikids) tiktokGroupPushMap.add(id,value);
                    for (Long id:bids) bilibiliGroupPushMap.add(id,value);

                }
                if (!value.getIsGroup()){

                    for (Long id:bids) bilibiliPrivatePushMap.add(id, value);
                    for (Long id:weiboids) weiBoPrivatePushMap.add(id,value);
                    for (Long id:tikids) tiktokPrivatePushMap.add(id,value);

                }
            }
        }
    }

}
