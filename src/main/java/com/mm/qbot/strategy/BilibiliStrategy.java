package com.mm.qbot.strategy;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mm.qbot.enumeration.BiliBiliEnum;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    private static Map<String, Object> unpackForwarding(JSONObject json){

        Map<String,Object> pack=new HashMap<>();

        JSONObject card=JSONObject.parseObject(json.getString("card"));
        JSONObject user=card.getJSONObject("user");
        JSONObject desc=json.getJSONObject("desc");

        JSONObject origin=JSONObject.parseObject(card.getString("origin"));
        JSONObject originUser=JSONObject.parseObject(card.getString("origin_user"));



        pack.put("username",user.getString("uname"));

        pack.put("uid",user.getString("uid"));
        pack.put("url",String.format("https://t.bilibili.com/%s",desc.getString("dynamic_id")));
        pack.put("type", BiliBiliEnum.FORWARD.getValue());
        pack.put("content",card.getJSONObject("item").getString("content"));

        pack.put("org_type",BiliBiliEnum.getType(desc.getInteger("orig_type")));
        pack.put("org_username",originUser.getJSONObject("info").getString("uname"));
        pack.put("org_content",origin.getJSONObject("item").getString("description"));
        pack.put("org_url",String.format("https://t.bilibili.com/%s",desc.getString("orig_dy_id")));

        JSONArray pics=origin.getJSONObject("item").getJSONArray("pictures");
        List<String>  picUrls=new ArrayList<>();


        for (Object pic:pics) {
            JSONObject p=(JSONObject)pic;
            picUrls.add(p.getString("img_src"));
        }
        pack.put("pic",picUrls);


//        pack.put("org_type","");


        return pack;

    }

}
