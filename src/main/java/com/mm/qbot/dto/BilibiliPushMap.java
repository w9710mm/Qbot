package com.mm.qbot.dto;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Map;

/**
 * @author meme
 * @version V0.0.1
 * @Package com.mm.qbot.dto
 * @Description:
 * @date 2021/11/1 22:13
 */
public class BilibiliPushMap {

   private final MultiValueMap<Long,UserSubscribe>  groupMap =new LinkedMultiValueMap<>();

    private final MultiValueMap<Long,UserSubscribe>  privateMap =new LinkedMultiValueMap<>();

    private static class  BilibiliPushMapInstance{
        private static final BilibiliPushMap Instance=new BilibiliPushMap();
    }
    private BilibiliPushMap(){}

    public  static BilibiliPushMap getBilibiliPushMap(){
        return BilibiliPushMapInstance.Instance;
    }
}
