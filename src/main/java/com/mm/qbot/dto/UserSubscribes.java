package com.mm.qbot.dto;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author meme
 * @version V0.0.1
 * @Package com.mm.qbot.dto
 * @Description: 用户订阅类的集合
 * @date 2021/10/31 23:20
 */
@Component
public class UserSubscribes {

    private static final Map<Long,UserSubscribe> subscribeMap=new HashMap<>();

    private UserSubscribes(){};

    public Map<Long,UserSubscribe> getSubscribeMap(){
        return  subscribeMap;
    }
}
