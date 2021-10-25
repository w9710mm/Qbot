package com.mm.qbot;

import com.alibaba.fastjson.JSONObject;
import com.mm.qbot.Exception.BilibiliException;
import com.mm.qbot.strategy.BilibiliStrategy;
import com.mm.qbot.utils.BilibiliApi;
import org.junit.jupiter.api.Test;

/**
 * @author WWM
 * @version V0.0.1
 * @Package com.mm.qbot
 * @Description:
 * @date 2021/10/24 22:57
 */
public class ShorLInkTests {
    @Test
    void testShortTest(){
        String wordShortLink="wGyOYc";

        String picShortLink="b8VmQv";

        String videoShortLink ="7vtUI8";

        //TODO文章短链接需要额外处理
        String articleShortLink="KHwQOk";


        String forwardShortLink="DBCvoG";


        String forwardShortLink2="4oekHZ";


        try {
            JSONObject dynamicJson = BilibiliApi.getShortLink(picShortLink);
            System.out.println(dynamicJson.toJSONString());
            BilibiliStrategy.dynamicStrategy(dynamicJson.getJSONObject("data").getJSONObject("card"));
        } catch (BilibiliException e) {
            e.printStackTrace();
        }

    }
}
