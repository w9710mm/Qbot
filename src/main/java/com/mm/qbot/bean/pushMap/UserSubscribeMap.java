package com.mm.qbot.bean.pushMap;

import lombok.Data;
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

@Data
public class UserSubscribeMap implements Serializable {




    private Map<Long,UserSubscribe> privateSubscribeMap =new HashMap<>();


    private Map<Long,UserSubscribe> groupSubscribeMap =new HashMap<>();

    private UserSubscribeMap(){};


    private static class UserSubscribeMapInstance{
        private static UserSubscribeMap Instance=new UserSubscribeMap();


        }




    public static  UserSubscribeMap getInstance(){
        return  UserSubscribeMapInstance.Instance;
    }

    public static  void setInstance(UserSubscribeMap userSubscribeMap ){

        UserSubscribeMapInstance.Instance=userSubscribeMap;
    }


}
