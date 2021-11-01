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

    MultiValueMap<Long,Long> groupMap =new LinkedMultiValueMap<>();

    MultiValueMap<Long,Long>  privateMap =new LinkedMultiValueMap<>();

    private static BilibiliPushMap bilibiliPushMap=new BilibiliPushMap();
    private BilibiliPushMap(){}

    private BilibiliPushMap getBilibiliPushMap(){
        return bilibiliPushMap;
    }
}
