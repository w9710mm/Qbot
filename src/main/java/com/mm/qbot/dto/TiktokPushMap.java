package com.mm.qbot.dto;

import com.mm.qbot.utils.LevelDB;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.io.Serializable;

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


    private   MultiValueMap<Long,UserSubscribe> groupMap =new LinkedMultiValueMap<>();

    private   MultiValueMap<Long,UserSubscribe>  privateMap =new LinkedMultiValueMap<>() ;

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
