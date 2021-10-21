package com.mm.qbot.strategy;

import com.alibaba.fastjson.JSONObject;
import com.mm.qbot.enumeration.BiliBiliEnum;

import java.util.HashMap;
import java.util.Map;

public class BilibiliStrategy {
    public static String dynamicStrategy(JSONObject json){
        Integer type =json.getJSONObject("desc").getInteger("type");
        switch (type){
            //转发

            case(1):
                unpackForwarding(json);
                break;

            //图片动态
            case(2): break;
            //文字动态
            case(4): break;
            //视频投稿
            case(8):break;
            case(16):break;

            //文章动态
            case(64):break;

            //直播信息
            case(4308):break;


        }

        return "test";

    }

    private static Map<String, String> unpackForwarding(JSONObject json){

        Map<String,String> pack=new HashMap<>();

        JSONObject card=JSONObject.parseObject(json.getString("card"));
        JSONObject user=card.getJSONObject("user");
        JSONObject desc=card.getJSONObject("desc");

        JSONObject origin=JSONObject.parseObject(card.getString("origin"));
        JSONObject originUser=JSONObject.parseObject(card.getString("origin_user"));



        pack.put("username",user.getString("uname"));

        pack.put("uid",user.getString("uid"));
        pack.put("url",String.format("https://t.bilibili.com/%s",desc.getString("dynamic_id")));
        pack.put("type", BiliBiliEnum.FORWARD.getValue());
        pack.put("content",card.getJSONObject("item").getString("content"));

        BiliBiliEnum.ARTICLE


        pack.put("org_type","");
//        pack.

        return pack;

    }

}
