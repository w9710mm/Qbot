package com.mm.qbot;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mm.qbot.strategy.BilibiliStrategy;
import com.mm.qbot.utils.BilibiliApi;
import org.junit.jupiter.api.Test;

public class RenewDynamicTests {


    @Test
    public void Renew() {

        JSONObject jsonObject = BilibiliApi.getNewDynamicNum("268435455", "586920011287906415");

//        if (jsonObject.getJSONObject("data").getInteger("update_num")>=1){
            JSONObject newDynamic = BilibiliApi.getNewDynamic("1359202", "268435455", "586915115027251377", "weball", "web");
            System.out.println(newDynamic.toJSONString());
        JSONArray jsonArray=newDynamic.getJSONObject("data").getJSONArray("cards");
        for (Object object:jsonArray) {
           JSONObject card= (JSONObject)object;
            String s = BilibiliStrategy.dynamicStrategy(card);
            System.out.println(s);
        }
//        }
    }
}
