package com.mm.qbot;

import com.alibaba.fastjson.JSONObject;
import com.mm.qbot.enumeration.NeedTopEnum;
import com.mm.qbot.utils.BilibiliApi;
import org.junit.Test;

public class SpaceDynamicTests {

    @Test
    public void test(){

        JSONObject spaceDynamic = BilibiliApi.getSpaceDynamic(174501086L, 0L, NeedTopEnum.NOT_NEED);
        System.out.println(spaceDynamic.toJSONString());

    }
}
