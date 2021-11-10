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
 * @date 2021/11/2 8:58
 */

@Getter
@Setter
public class WeiBoPushMap implements Serializable {


    private Map<User, LinkedHashSet<Long>> groupMap =new HashMap<>();

    private  Map<User, LinkedHashSet<Long>> privateMap=new HashMap<>() ;

    private Long dynamicIdOffset=0L;


    private static class  WeiBoPushMapInstance{
//        private static final LevelDB levelDB=LevelDB.getInstance();
//        private static WeiBoPushMap Instance;
//        static{
//            Instance= (WeiBoPushMap) levelDB.get("WeiBoPushMap");;
//            if (Instance==null){
//                Instance=new WeiBoPushMap();
//                levelDB.put("WeiBoPushMap",Instance);
//            }
//
//        }
        private static final WeiBoPushMap Instance=new WeiBoPushMap();
    }

    private WeiBoPushMap(){}

    public static WeiBoPushMap getInstance(){
        return WeiBoPushMapInstance.Instance;
    }
}
