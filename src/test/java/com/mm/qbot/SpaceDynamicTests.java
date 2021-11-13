package com.mm.qbot;

import com.alibaba.fastjson.JSONObject;
import com.mm.qbot.enumeration.NeedTopEnum;
import com.mm.qbot.utils.BilibiliApi;
import org.junit.Test;

public class SpaceDynamicTests {

    @Test
    public void test(){

        JSONObject spaceDynamic = BilibiliApi.getSpaceVideo(String.valueOf(174501086));
        System.out.println(spaceDynamic.toJSONString());

    }
}
