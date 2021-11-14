package com.mm.qbot;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mm.qbot.Exception.BilibiliException;
import com.mm.qbot.bean.pushMap.User;
import com.mm.qbot.enumeration.NeedTopEnum;
import com.mm.qbot.service.AsoulPaperService;
import com.mm.qbot.service.BilibiliService;
import com.mm.qbot.strategy.BilibiliStrategy;
import com.mm.qbot.utils.BilibiliApi;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

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


    @Test
    public void RenewVideo() {


//        if (jsonObject.getJSONObject("data").getInteger("update_num")>=1){
        JSONObject newDynamic = BilibiliApi.getSpaceVideo("672328094");
        System.out.println(newDynamic);
//        }
    }

    @Test
    public void d() {


//        if (jsonObject.getJSONObject("data").getInteger("update_num")>=1){
        JSONObject newDynamic = new JSONObject();

        System.out.println(newDynamic.getJSONObject("1").getJSONObject("1"));
        System.out.println(1);
//        }
    }

    @Test
    public void RenewDy() {

        User u=new User();
        u.setUid("672328094");
        u.setUname("嘉然今天吃什么");
        List<User> userList=new ArrayList<>();
        userList.add(u);
        AsoulPaperService bilibiliService=new AsoulPaperService();
        bilibiliService.weeklyData(userList);

//        }
    }
}
