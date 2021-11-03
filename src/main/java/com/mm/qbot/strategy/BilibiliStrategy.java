package com.mm.qbot.strategy;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mikuac.shiro.common.utils.MsgUtils;
import com.mm.qbot.Exception.BilibiliException;
import com.mm.qbot.enumeration.BiliBiliEnum;
import com.mm.qbot.utils.TimeUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BilibiliStrategy {
    public static MsgUtils dynamicStrategy(JSONObject json) throws BilibiliException {

        Integer type = json.getJSONObject("desc").getInteger("type");
        Map<String, Object> res = null;
        MsgUtils msgUtils;
        msgUtils=MsgUtils.builder();


            try {


                switch (type) {
                    //转发

                    case (1):
                        msgUtils = unpackForwarding(json);
                        break;

                    //图片动态
                    case (2):
                        msgUtils = unpackPic(json);
                        break;
                    //文字动态
                    case (4):
                        msgUtils = unpackWord(json);
                        break;
                    //视频投稿
                    case (8):
                        msgUtils = unpackVideo(json);

                        break;
                    case (16):
                        break;

                    //文章动态
                    case (64):
                        msgUtils = unpackArticle(json);

                        break;

                    //直播信息
                    case (4308):
                        msgUtils = unpackLive(json);
                        break;


                }
            }catch (Exception e){
                throw  new BilibiliException("转换异常，抛弃"+json.toJSONString());
        }

        return msgUtils;

    }

    private static MsgUtils unpackForwarding(JSONObject json) {
        new MsgUtils();
        MsgUtils msgUtils;
        msgUtils=MsgUtils.builder();
        Map<String, Object> pack = new HashMap<>();

        JSONObject card = JSONObject.parseObject(json.getString("card"));
        JSONObject user = card.getJSONObject("user");
        JSONObject desc = json.getJSONObject("desc");

        JSONObject origin = JSONObject.parseObject(card.getString("origin"));
        JSONObject originUser = JSONObject.parseObject(card.getString("origin_user"));


        pack.put("username", user.getString("uname"));
        msgUtils.text(String.format("【%s",pack.get("username")));
        pack.put("uid", user.getString("uid"));
        msgUtils.text(String.format("(%s)】更新了动态\n",pack.get("uid")));
        pack.put("url", String.format("https://t.bilibili.com/%s", desc.getString("dynamic_id")));
        msgUtils.text(String.format("%s\n",pack.get("url")));
        pack.put("type", BiliBiliEnum.FORWARD.getValue());

        pack.put("content", card.getJSONObject("item").getString("content"));
        msgUtils.text(String.format("%s：%s\n",pack.get("username"),pack.get("content")));
        pack.put("time", TimeUtils.stampToDate(desc.getLong("timestamp") * 1000));
        msgUtils.text(String.format("发布时间:%s\n",pack.get("time")));

        pack.put("org_type", BiliBiliEnum.getType(desc.getInteger("orig_type")));
        pack.put("org_username", originUser.getJSONObject("info").getString("uname"));
        pack.put("org_uid", originUser.getJSONObject("info").getString("uid"));
        msgUtils.text(String.format("转发内容：【%s（%s）】的[%s]\n",pack.get("org_username"),pack.get("org_uid"),pack.get("org_type")));

        pack.put("org_url", String.format("https://t.bilibili.com/%s", desc.getString("orig_dy_id")));
        msgUtils.text(String.format("%s\n",pack.get("org_url")));

        List<String> picUrls = new ArrayList<>();
        switch (desc.getInteger("orig_type")) {
            //图片动态
            case (2):
                pack.put("org_content", origin.getJSONObject("item").getString("description"));
                msgUtils.text(String.format("%s：%s\n",pack.get("org_username"),pack.get("org_content")));

                pack.put("org_time", TimeUtils.stampToDate(origin.getJSONObject("item").getLong("upload_time") * 1000));
                JSONArray pics=origin.getJSONObject("item").getJSONArray("pictures");
                for (Object o:pics) {
                  JSONObject pic =(JSONObject)o;
                  picUrls.add(pic.getString("img_src"));
                  msgUtils.img(pic.getString("img_src"));
                }
                msgUtils.text(String.format("发布时间:%s\n",pack.get("org_time")));

                break;
            //文字动态
            case (4):

                pack.put("org_content", origin.getJSONObject("item").getString("content"));
                msgUtils.text(String.format("%s：%s\n",pack.get("org_username"),pack.get("org_content")));
                pack.put("org_time", TimeUtils.stampToDate(origin.getJSONObject("item").getLong("timestamp") * 1000));
                msgUtils.text(String.format("发布时间:%s\n",pack.get("org_time")));
                break;
            //视频投稿
            case (8):
                pack.put("title", origin.getString("title"));
                pack.put("aid", origin.getString("aid"));
                pack.put("duration",TimeUtils.changeTimeFormat(origin.getInteger("duration")));

                pack.put("org_content", origin.getString("dynamic"));
                msgUtils.text(String.format("%s：%s\n",pack.get("org_username"),pack.get("org_content")));
                msgUtils.text(String.format("标题：%s(%s)\n",pack.get("title"),pack.get("aid")));
                pack.put("link", origin.getString("short_link_v2"));
                pack.put("desc", origin.getString("desc"));
                msgUtils.text(String.format("简介：%s",pack.get("desc")));
                pack.put("org_time", TimeUtils.stampToDate(origin.getLong("pubdate") * 1000));
                msgUtils.text(String.format("发布时间:%s (%s)\n",pack.get("org_time"),pack.get("duration")));
                pack.put("view", origin.getJSONObject("stat").getString("view"));
                pack.put("like", origin.getJSONObject("stat").getString("like"));
                pack.put("favorite", origin.getJSONObject("stat").getString("favorite"));
                pack.put("share", origin.getJSONObject("stat").getString("share"));
                pack.put("coin", origin.getJSONObject("stat").getString("coin"));
                picUrls.add(origin.getString("pic"));

                msgUtils.img(origin.getString("pic"));
                msgUtils.text(String.format("\n%s", pack.get("link")));
                break;

            case (16):
                break;

            //文章动态
            case (64):
                pack.put("title", origin.getString("title"));
                pack.put("org_content", origin.getString("summary"));
                msgUtils.text(String.format("%s：%s\n",pack.get("org_username"),pack.get("org_content")));
                msgUtils.text(String.format("标题：%s\n",pack.get("title")));
                pack.put("org_time", TimeUtils.stampToDate(origin.getLong("publish_time") * 1000));
                msgUtils.text(String.format("发布时间:%s\n",pack.get("org_time")));
                JSONArray imgs=origin.getJSONArray("image_urls");
                if (!"".equals(origin.getString("banner_url"))){
                    picUrls.add(origin.getString("banner_url"));
                    msgUtils.img(origin.getString("banner_url"));
                }


                picUrls.add(origin.getString("origin_image_urls"));
                msgUtils.img(origin.getString("origin_image_urls"));
                for (Object o:imgs) {
                    picUrls.add((String)o);
                }


                break;

            //直播信息
            case (4308):
                break;

        }


        pack.put("pics", picUrls);


//        pack.put("org_type","");


        return msgUtils;

    }

    private static MsgUtils unpackPic(JSONObject json){

        new MsgUtils();
        MsgUtils msgUtils;
        msgUtils=MsgUtils.builder();
        Map<String, Object> pack = new HashMap<>();
        JSONObject card = JSONObject.parseObject(json.getString("card"));
        JSONObject desc = json.getJSONObject("desc");
        List<String> picUrls = new ArrayList<>();


        pack.put("type", BiliBiliEnum.PICTURE.getValue());

        pack.put("username",card.getJSONObject("user").getString("name"));
        pack.put("uid",card.getJSONObject("user").getString("uid"));
        msgUtils.text(String.format("【%s",pack.get("username")));
        msgUtils.text(String.format("(%s)】更新了动态\n",pack.get("uid")));

        pack.put("url", String.format("https://t.bilibili.com/%s", desc.getString("dynamic_id")));

        msgUtils.text(String.format("%s\n",pack.get("url")));
        pack.put("content", card.getJSONObject("item").getString("description"));
        JSONArray pics = card.getJSONObject("item").getJSONArray("pictures");
        msgUtils.text(String.format("%s：%s\n",pack.get("username"),pack.get("content")));
        for (Object pic : pics) {
            JSONObject p = (JSONObject) pic;
            picUrls.add(p.getString("img_src"));
            msgUtils.img(p.getString("img_src"));
        }
        pack.put("pics", picUrls);

        pack.put("time",TimeUtils.stampToDate(desc.getLong("timestamp")*1000));
        msgUtils.text(String.format("\n发布时间：%s\n",pack.get("time")));
        return msgUtils;
    }
    private static MsgUtils unpackWord(JSONObject json){
        Map<String, Object> pack = new HashMap<>();
        JSONObject card = JSONObject.parseObject(json.getString("card"));
        JSONObject desc = json.getJSONObject("desc");
        new MsgUtils();
        MsgUtils msgUtils;
        msgUtils=MsgUtils.builder();

        pack.put("type", BiliBiliEnum.WORD.getValue());
        List<String> picUrls = new ArrayList<>();
        pack.put("username",card.getJSONObject("user").getString("uname"));
        pack.put("uid",card.getJSONObject("user").getString("uid"));
        pack.put("time",TimeUtils.stampToDate(desc.getLong("timestamp")*1000));
        pack.put("content", card.getJSONObject("item").getString("content"));
        pack.put("url", String.format("https://t.bilibili.com/%s", desc.getString("dynamic_id")));
        pack.put("pics",picUrls);
        msgUtils.text(String.format("【%s",pack.get("username")));
        msgUtils.text(String.format("(%s)】更新了动态\n",pack.get("uid")));
        msgUtils.text(String.format("%s\n",pack.get("url")));
        msgUtils.text(String.format("[%s]\n",pack.get("type")));
        msgUtils.text(String.format("%s：%s\n",pack.get("username"),pack.get("content")));
        msgUtils.text(String.format("发布时间:%s\n",pack.get("time")));



        return msgUtils;
    }

    private static MsgUtils unpackVideo(JSONObject json){
        Map<String, Object> pack = new HashMap<>();
        List<String> picUrls = new ArrayList<>();
        new MsgUtils();
        MsgUtils msgUtils;
        msgUtils=MsgUtils.builder();

        pack.put("type", BiliBiliEnum.VIDEO.getValue());


        JSONObject card = JSONObject.parseObject(json.getString("card"));
        JSONObject desc = json.getJSONObject("desc");
        pack.put("username",card.getJSONObject("owner").getString("name"));
        pack.put("uid",card.getJSONObject("owner").getString("mid"));
        pack.put("aid",card.getString("aid"));
        pack.put("url", String.format("https://t.bilibili.com/%s", desc.getString("dynamic_id")));
        pack.put("type",BiliBiliEnum.getType(desc.getInteger("type")));
        pack.put("title",card.getString("title"));
        pack.put("content",card.getString("dynamic"));
        pack.put("desc",card.getString("desc"));
        pack.put("tname",card.getString("tname"));
        pack.put("org_time", TimeUtils.stampToDate(card.getLong("ctime") * 1000));
        pack.put("view", card.getJSONObject("stat").getString("view"));
        pack.put("like", card.getJSONObject("stat").getString("like"));
        pack.put("favorite", card.getJSONObject("stat").getString("favorite"));
        pack.put("share", card.getJSONObject("stat").getString("share"));
        pack.put("coin", card.getJSONObject("stat").getString("coin"));
        pack.put("link",card.getString("short_link_v2"));
        pack.put("count",card.getString("videos"));
        pack.put("duration",TimeUtils.changeTimeFormat(card.getInteger("duration")));
       picUrls.add(card.getString("pic"));
       pack.put("pics",picUrls);


        msgUtils.text(String.format("【%s",pack.get("username")));
        msgUtils.text(String.format("(%s)】更新了动态\n",pack.get("uid")));
        msgUtils.text(String.format("%s\n",pack.get("url")));
        msgUtils.text(String.format("%s：%s\n",pack.get("username"),pack.get("content")));
        msgUtils.text(String.format("标题：%s(%s)\n",pack.get("title"),pack.get("aid")));
        msgUtils.text(String.format("简介：%s\n",pack.get("desc")));


        msgUtils.text(String.format("发布时间:%s (%s)\n",pack.get("org_time"),pack.get("duration")));
        msgUtils.img(card.getString("pic"));
        msgUtils.text(String.format( "\n%s\n",pack.get("link")));
        return msgUtils;
    }

    private static MsgUtils unpackArticle(JSONObject json){
        Map<String, Object> pack = new HashMap<>();
        JSONObject card = JSONObject.parseObject(json.getString("card"));
        JSONObject desc = json.getJSONObject("desc");
        JSONArray imgs=card.getJSONArray("image_urls");

        new MsgUtils();
        MsgUtils msgUtils;
        msgUtils=MsgUtils.builder();
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
        msgUtils.img(card.getString("origin_image_urls"));


        return msgUtils;
    }
    private static MsgUtils unpackLive(JSONObject json){
        MsgUtils msgUtils;
        msgUtils=MsgUtils.builder();
        Map<String, Object> pack = new HashMap<>();
        JSONObject card = JSONObject.parseObject(json.getString("card"));
        JSONObject desc = json.getJSONObject("desc");
        List<String> picUrls = new ArrayList<>();

        pack.put("type", BiliBiliEnum.LIVE.getValue());
        pack.put("title",card.getJSONObject("live_play_info").getString("title"));
        pack.put("parent_area",card.getJSONObject("live_play_info").getString("parent_area_name"));
        pack.put("area",card.getJSONObject("live_play_info").getString("area_name"));
        pack.put("username",desc.getJSONObject("user_profile").getJSONObject("info").getString("uname"));
        pack.put("uid",desc.getJSONObject("user_profile").getJSONObject("info").getString("uid"));
        pack.put("time",TimeUtils.stampToDate(card.getJSONObject("live_play_info").getLong("live_start_time")*1000));
        pack.put("link",card.getJSONObject("live_play_info").getString("link"));
       picUrls.add(card.getJSONObject("live_play_info").getString("cover"));
        pack.put("pics", picUrls);
        msgUtils.text(String.format("【%s",pack.get("username")));
        msgUtils.text(String.format("(%s)】更新了动态\n",pack.get("uid")));
        msgUtils.text(String.format("%s开启了直播：%s\n",pack.get("username"),pack.get("title")));
        pack.put("url", String.format("https://t.bilibili.com/%s", desc.getString("dynamic_id")));

        msgUtils.text(String.format("%s\n",pack.get("url")));
        msgUtils.text(String.format("分区：%s-%s\n",pack.get("parent_area"),pack.get("area")));
        msgUtils.text(String.format("开启时间：%s\n",pack.get("time")));
        msgUtils.img(card.getJSONObject("live_play_info").getString("cover"));
        msgUtils.text(String.format("\n%s",pack.get("link")));

        return msgUtils;
    }


}
