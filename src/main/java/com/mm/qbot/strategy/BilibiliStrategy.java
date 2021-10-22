package com.mm.qbot.strategy;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mm.qbot.enumeration.BiliBiliEnum;
import com.mm.qbot.utils.TimeUtils;
import org.apache.ibatis.annotations.Case;
import org.checkerframework.checker.units.qual.C;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BilibiliStrategy {
    public static String dynamicStrategy(JSONObject json) {
        Integer type = json.getJSONObject("desc").getInteger("type");
        Map<String, Object> res = null;
        switch (type) {
            //转发

            case (1):
                res = unpackForwarding(json);
                break;

            //图片动态
            case (2):
                res = unpackPic(json);
                break;
            //文字动态
            case (4):
                res = unpackWord(json);
                break;
            //视频投稿
            case (8):
                res = unpackVideo(json);

                break;
            case (16):
                break;

            //文章动态
            case (64):
                res = unpackArticle(json);

                break;

            //直播信息
            case (4308):
                res = unpackLive(json);
                break;


        }

        return "test";

    }

    private static Map<String, Object> unpackForwarding(JSONObject json) {

        Map<String, Object> pack = new HashMap<>();

        JSONObject card = JSONObject.parseObject(json.getString("card"));
        JSONObject user = card.getJSONObject("user");
        JSONObject desc = json.getJSONObject("desc");

        JSONObject origin = JSONObject.parseObject(card.getString("origin"));
        JSONObject originUser = JSONObject.parseObject(card.getString("origin_user"));


        pack.put("username", user.getString("uname"));

        pack.put("uid", user.getString("uid"));
        pack.put("url", String.format("https://t.bilibili.com/%s", desc.getString("dynamic_id")));
        pack.put("type", BiliBiliEnum.FORWARD.getValue());
        pack.put("content", card.getJSONObject("item").getString("content"));
        pack.put("time", TimeUtils.stampToDate(desc.getLong("timestamp") * 1000));

        pack.put("org_type", BiliBiliEnum.getType(desc.getInteger("orig_type")));
        pack.put("org_url", String.format("https://t.bilibili.com/%s", desc.getString("orig_dy_id")));

        List<String> picUrls = new ArrayList<>();
        switch (desc.getInteger("orig_type")) {
            //图片动态
            case (2):
                pack.put("org_content", origin.getJSONObject("item").getString("description"));


                pack.put("org_time", TimeUtils.stampToDate(origin.getJSONObject("item").getLong("upload_time") * 1000));

                break;
            //文字动态
            case (4):

                pack.put("org_content", origin.getJSONObject("item").getString("content"));
                pack.put("org_time", TimeUtils.stampToDate(origin.getJSONObject("item").getLong("timestamp") * 1000));

                break;
            //视频投稿
            case (8):
                pack.put("title", origin.getString("title"));
                pack.put("aid", origin.getString("aid"));
                pack.put("link", origin.getString("short_link"));
                pack.put("desc", origin.getString("desc"));
                pack.put("org_time", TimeUtils.stampToDate(origin.getLong("pubdate") * 1000));
                pack.put("view", origin.getJSONObject("stat").getString("view"));
                pack.put("like", origin.getJSONObject("stat").getString("like"));
                pack.put("favorite", origin.getJSONObject("stat").getString("favorite"));
                pack.put("share", origin.getJSONObject("stat").getString("share"));
                pack.put("coin", origin.getJSONObject("stat").getString("coin"));
                pack.put("org_content", origin.getString("dynamic"));


                picUrls.add(origin.getString("pic"));
                break;

            case (16):
                break;

            //文章动态
            case (64):
                pack.put("title", origin.getString("title"));
                pack.put("org_content", origin.getString("summary"));
                JSONArray imgs=origin.getJSONArray("image_urls");
                if ("".equals(origin.getString("banner_url"))){
                    picUrls.add(origin.getString("banner_url"));
                }

                pack.put("org_time", TimeUtils.stampToDate(origin.getLong("publish_time") * 1000));
                picUrls.add("https://i0.hdslb.com/bfs/article/f8e7cdd89e3593755733950ae63bf166c3c9c7b0.jpg");
                for (Object o:imgs) {
                    picUrls.add((String)o);
                }


                break;

            //直播信息
            case (4308):
                break;

        }


        pack.put("pics", picUrls);
        pack.put("org_username", originUser.getJSONObject("info").getString("uname"));
        pack.put("org_uid", originUser.getJSONObject("info").getString("uid"));


//        pack.put("org_type","");


        return pack;

    }

    private static Map<String, Object> unpackPic(JSONObject json){


        Map<String, Object> pack = new HashMap<>();
        JSONObject card = JSONObject.parseObject(json.getString("card"));
        JSONObject desc = json.getJSONObject("desc");
        List<String> picUrls = new ArrayList<>();


        pack.put("type", BiliBiliEnum.PICTURE.getValue());

        pack.put("username",card.getJSONObject("user").getString("name"));
        pack.put("uid",card.getJSONObject("user").getString("uid"));
        pack.put("url", String.format("https://t.bilibili.com/%s", desc.getString("dynamic_id")));
        JSONArray pics = card.getJSONObject("item").getJSONArray("pictures");
        for (Object pic : pics) {
            JSONObject p = (JSONObject) pic;
            picUrls.add(p.getString("img_src"));
        }
        pack.put("pics", picUrls);
        pack.put("content", card.getJSONObject("item").getString("description"));
        pack.put("time",TimeUtils.stampToDate(desc.getLong("timestamp")*1000));

        return pack;
    }
    private static Map<String, Object> unpackWord(JSONObject json){
        Map<String, Object> pack = new HashMap<>();
        JSONObject card = JSONObject.parseObject(json.getString("card"));
        JSONObject desc = json.getJSONObject("desc");

        pack.put("type", BiliBiliEnum.WORD.getValue());


        List<String> picUrls = new ArrayList<>();
        pack.put("username",card.getJSONObject("user").getString("name"));
        pack.put("uid",card.getJSONObject("user").getString("uid"));
        pack.put("time",TimeUtils.stampToDate(desc.getLong("timestamp")*1000));
        pack.put("content", card.getJSONObject("item").getString("content"));
        pack.put("url", String.format("https://t.bilibili.com/%s", desc.getString("dynamic_id")));
        pack.put("pics",picUrls);


        return pack;
    }

    private static Map<String, Object> unpackVideo(JSONObject json){
        Map<String, Object> pack = new HashMap<>();
        List<String> picUrls = new ArrayList<>();

        pack.put("type", BiliBiliEnum.VIDEO.getValue());


        JSONObject card = JSONObject.parseObject(json.getString("card"));
        JSONObject desc = json.getJSONObject("desc");
        pack.put("username",card.getJSONObject("owner").getString("name"));
        pack.put("url", String.format("https://t.bilibili.com/%s", desc.getString("dynamic_id")));
        pack.put("type",BiliBiliEnum.getType(desc.getInteger("type")));
        pack.put("title",card.getString("title"));
        pack.put("content",card.getString("dynamic"));
        pack.put("desc",card.getString("dedsc"));
        pack.put("tname",card.getString("tname"));
        pack.put("org_time", TimeUtils.stampToDate(card.getLong("ctime") * 1000));
        pack.put("view", card.getJSONObject("stat").getString("view"));
        pack.put("like", card.getJSONObject("stat").getString("like"));
        pack.put("favorite", card.getJSONObject("stat").getString("favorite"));
        pack.put("share", card.getJSONObject("stat").getString("share"));
        pack.put("coin", card.getJSONObject("stat").getString("coin"));
        pack.put("link",card.getString("short_link"));
        pack.put("count",card.getString("videos"));

       picUrls.add(card.getString("pic"));

       pack.put("pics",picUrls);
        return pack;
    }

    private static Map<String, Object> unpackArticle(JSONObject json){
        Map<String, Object> pack = new HashMap<>();
        JSONObject card = JSONObject.parseObject(json.getString("card"));
        JSONObject desc = json.getJSONObject("desc");
        JSONArray imgs=card.getJSONArray("image_urls");

        pack.put("type", BiliBiliEnum.ARTICLE.getValue());

        List<String> picUrls = new ArrayList<>();

        pack.put("time",TimeUtils.stampToDate(desc.getLong("timestamp")*1000));
        pack.put("username",card.getJSONObject("author").getString("name"));
        pack.put("uid",card.getJSONObject("author").getString("mid"));
        pack.put("content",card.getString("summary"));
        pack.put("title",card.getString("title"));

        for (Object o:imgs) {
            picUrls.add((String)o);
        }

        if ("".equals(card.getString("banner_url"))){
            picUrls.add(card.getString("banner_url"));
        }

        pack.put("pics",picUrls);


        return pack;
    }
    private static Map<String, Object> unpackLive(JSONObject json){
        Map<String, Object> pack = new HashMap<>();
        JSONObject card = JSONObject.parseObject(json.getString("card"));
        JSONObject desc = json.getJSONObject("desc");
        List<String> picUrls = new ArrayList<>();

        pack.put("type", BiliBiliEnum.LIVE.getValue());



        pack.put("parent_area",card.getJSONObject("live_play_info").getString("parent_area_name"));
        pack.put("area",card.getJSONObject("live_play_info").getString("area_name"));
        pack.put("username",desc.getJSONObject("user_profile").getJSONObject("info").getString("uname"));
        pack.put("uid",desc.getJSONObject("user_profile").getJSONObject("info").getString("uid"));
        pack.put("time",TimeUtils.stampToDate(desc.getLong("timestamp")*1000));
        pack.put("link",card.getJSONObject("live_play_info").getString("link"));
       picUrls.add(card.getJSONObject("live_play_info").getString("cover"));
        pack.put("pics", picUrls);

        return pack;
    }


}
