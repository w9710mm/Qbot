package com.mm.qbot.dto;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

/**
 * @author meme
 * @version V0.0.1
 * @Package com.mm.qbot.dto
 * @Description:
 * @date 2021/11/2 8:58
 */
public class WeiBoPushMap {

    private final   MultiValueMap<Long,UserSubscribe> groupMap =new LinkedMultiValueMap<>();

    private final   MultiValueMap<Long,UserSubscribe>  privateMap =new LinkedMultiValueMap<>();

    private static class  WeiBoPushMapInstance{
        private static final WeiBoPushMap Instance=new WeiBoPushMap();
    }

    private WeiBoPushMap(){}

    private WeiBoPushMap getWeiBoPushMap(){
        return WeiBoPushMapInstance.Instance;
    }
}
