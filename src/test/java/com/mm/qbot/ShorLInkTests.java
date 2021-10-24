package com.mm.qbot;

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

        String articleShortLink="KHwQOk";

        String forwardShortLink="DBCvoG";


        BilibiliApi.getShortLink(forwardShortLink);

    }
}
