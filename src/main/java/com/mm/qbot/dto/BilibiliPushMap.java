package com.mm.qbot.dto;

import com.mm.qbot.utils.LevelDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.Map;

/**
 * @author meme
 * @version V0.0.1
 * @Package com.mm.qbot.dto
 * @Description:
 * @date 2021/11/1 22:13
 */
public class BilibiliPushMap implements Serializable {

   private final MultiValueMap<Long,UserSubscribe>  groupMap =new LinkedMultiValueMap<>();

    private final MultiValueMap<Long,UserSubscribe>  privateMap =new LinkedMultiValueMap<>();


    private static class  BilibiliPushMapInstance{
   LevelDB.getInstance().get("BilibiliPushMap");
        private static final BilibiliPushMap Instance=LevelDB.getInstance().get("BilibiliPushMap");
    }
    private BilibiliPushMap(){}

    public  static BilibiliPushMap getBilibiliPushMap(){
        return BilibiliPushMapInstance.Instance;
    }

}
