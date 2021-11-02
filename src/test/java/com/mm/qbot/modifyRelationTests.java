package com.mm.qbot;

import com.mm.qbot.enumeration.RelationActionEnum;
import com.mm.qbot.utils.BilibiliApi;
import org.junit.jupiter.api.Test;

/**
 * @author meme
 * @version V0.0.1
 * @Package com.mm.qbot
 * @Description:
 * @date 2021/11/1 21:30
 */
public class modifyRelationTests {

    @Test
    void subscribe (){


        BilibiliApi.modifyRelation(8386957L, RelationActionEnum.SUBSCRIBE);
    }
}
