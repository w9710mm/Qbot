package com.mm.qbot;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mm.qbot.Exception.BilibiliException;
import com.mm.qbot.strategy.BilibiliStrategy;
import com.mm.qbot.utils.BilibiliApi;
import org.junit.jupiter.api.Test;

public class RenewDynamicTests {


    @Test
    public void Renew() {


//        if (jsonObject.getJSONObject("data").getInteger("update_num")>=1){
            JSONObject newDynamic = BilibiliApi.getNewDynamic("1823651096", "268435455", "587592349757451973", "weball", "web");
            System.out.println(newDynamic.toJSONString());
        JSONArray jsonArray=newDynamic.getJSONObject("data").getJSONArray("cards");
        for (Object object:jsonArray) {
           JSONObject card= (JSONObject)object;
            String s = null;
            try {
                s = BilibiliStrategy.dynamicStrategy(card).build();
            } catch (BilibiliException e) {
                e.printStackTrace();
            }
            System.out.println(s);
        }
//        }
    }
}
