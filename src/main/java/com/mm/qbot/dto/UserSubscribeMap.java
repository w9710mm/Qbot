package com.mm.qbot.dto;

import com.mm.qbot.utils.LevelDB;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import javax.annotation.Resource;
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
        private static final LevelDB levelDB=LevelDB.getInstance();
        private static UserSubscribeMap Instance;
        static{
            Instance= (UserSubscribeMap) levelDB.get("UserSubscribeMap");;
            if (Instance==null){
                Instance=new UserSubscribeMap();
                levelDB.put("UserSubscribeMap",Instance);
            }

        }

    }


    public static  UserSubscribeMap getInstance(){
        return  UserSubscribeMapInstance.Instance;
    }

    public static  void setInstance(UserSubscribeMap userSubscribeMap ){

        UserSubscribeMapInstance.Instance=userSubscribeMap;
    }


}
