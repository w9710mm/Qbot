package com.mm.qbot.dto;

import org.springframework.stereotype.Component;

import java.io.Serializable;
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
public class UserSubscribeMap implements Serializable {

    private static final Map<Long,UserSubscribe> subscribeMap=new HashMap<>();

    private UserSubscribeMap(){};


    private static class UserSubscribeMapInstance{
        private static final UserSubscribeMap Instance=new UserSubscribeMap();
    }
    public static UserSubscribeMap getSubscribeMap(){
        return  UserSubscribeMapInstance.Instance;
    }
}
