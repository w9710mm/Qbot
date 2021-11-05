package com.mm.qbot.dto;

import com.mm.qbot.utils.LevelDB;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import javax.annotation.Resource;
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
 * @date 2021/11/1 22:13
 */


@Data
public class BilibiliPushMap implements Serializable {


   private  Map<User, LinkedHashSet<Long>>  groupMap =new HashMap<>();

    private Map<User, LinkedHashSet<Long>> privateMap=new HashMap<>() ;

    private Long dynamicIdOffset=0L;

    private static class  BilibiliPushMapInstance{
//        private static final LevelDB levelDB=LevelDB.getInstance();
//        private static BilibiliPushMap Instance;
//        static{
//            Instance= (BilibiliPushMap) levelDB.get("BilibiliPushMap");;
//            if (Instance==null){
//                Instance=new BilibiliPushMap();
//                levelDB.put("BilibiliPushMap",Instance);
//            }
//
//        }
        private static final BilibiliPushMap Instance=new BilibiliPushMap();
    }
    private BilibiliPushMap(){}

    public  static BilibiliPushMap getInstance(){
        return BilibiliPushMapInstance.Instance;
    }

}
