package com.mm.qbot.dto;

import com.mm.qbot.utils.LevelDB;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author meme
 * @version V0.0.1
 * @Package com.mm.qbot.dto
 * @Description:
 * @date 2021/11/2 8:59
 */


@Getter
@Setter
public class TiktokPushMap implements Serializable {


    private Map<User, LinkedHashSet<Long>> groupMap =new HashMap<>();

    private  Map<User, LinkedHashSet<Long>>  privateMap=new HashMap<>() ;

    private TiktokPushMap(){}



    private static class  TiktokPushMapInstance{
//        private static final LevelDB levelDB=LevelDB.getInstance();
//        private static TiktokPushMap Instance;
//        static{
//            Instance= (TiktokPushMap) levelDB.get("TiktokPushMap");;
//            if (Instance==null){
//                Instance=new TiktokPushMap();
//                levelDB.put("TiktokPushMap",Instance);
//            }
//
//        }
        private static final TiktokPushMap Instance=new TiktokPushMap();
    }
    public static TiktokPushMap getInstance(){

        return TiktokPushMapInstance.Instance;
    }
}
