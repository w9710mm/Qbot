package com.mm.qbot;

import com.alibaba.fastjson.JSONObject;
import com.mm.qbot.utils.BilibiliApi;
import org.junit.jupiter.api.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SerchUserTests {



    @Test
    public void searchUser(){

          String   bvidMacher= "(最新动态 )([\\S]+)";
         Pattern bvidPattern = Pattern.compile(bvidMacher);

        Matcher matcher = bvidPattern.matcher("最新动态 11   ");


        JSONObject object = BilibiliApi.serachUser("大事", "bili_user");

        System.out.println(object);
    }

    @Test
    public void getRelation(){
        JSONObject jsonObject = BilibiliApi.getUserInfo(672328094L);

        System.out.println(jsonObject);
    }
}
