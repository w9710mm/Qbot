package com.mm.qbot.boot;

import com.alibaba.fastjson.JSONObject;
import com.mm.qbot.dto.BilibiliPushMap;
import com.mm.qbot.dto.UserSubscribeMap;
import com.mm.qbot.utils.LevelDB;
import netscape.javascript.JSObject;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

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

    private final BilibiliPushMap bilibiliPushMap =BilibiliPushMap.getBilibiliPushMap();

    private final  UserSubscribeMap userSubscribeMap=UserSubscribeMap.getSubscribeMap();

//
//    @Bean
//    public  void InitPushMap(){
//        BilibiliPushMap bilibiliPushMap = (BilibiliPushMap) levelDB.get("bilibiliPushMap");
//        if (bilibiliPushMap==null){
//
//
//
//            levelDB.put("bilibiliPushMap", this.bilibiliPushMap);
//        }
//        if(bilibiliPushMap!=null){
//
//            System.out.println(bilibiliPushMap);
//
//
//        }
//
//    }

}
