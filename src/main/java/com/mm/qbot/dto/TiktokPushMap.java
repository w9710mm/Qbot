package com.mm.qbot.dto;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

/**
 * @author meme
 * @version V0.0.1
 * @Package com.mm.qbot.dto
 * @Description:
 * @date 2021/11/2 8:59
 */
public class TiktokPushMap {

    private final  MultiValueMap<Long,UserSubscribe> groupMap =new LinkedMultiValueMap<>();

    private final  MultiValueMap<Long,UserSubscribe>  privateMap =new LinkedMultiValueMap<>();

    private TiktokPushMap(){}

    private static class  TiktokPushMapInstance{
        private static final TiktokPushMap Instance=new TiktokPushMap();
    }
    private TiktokPushMap getTiktokPushMap(){

        return TiktokPushMapInstance.Instance;
    }
}
