package com.mm.qbot.strategy;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mikuac.shiro.common.utils.MsgUtils;
import com.mm.qbot.Exception.BilibiliException;
import com.mm.qbot.enumeration.BiliBiliEnum;
import com.mm.qbot.utils.BilibiliApi;
import com.mm.qbot.utils.ImageUtils;
import com.mm.qbot.utils.StringUtils;
import com.mm.qbot.utils.TimeUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class BilibiliStrategy {


    private static final String DYNAMIC_LINK_REGEX ="(dynamic|video|read/mobile)(/)([a-zA-Z0-9]+)";
    private static final  Pattern DYNAMIC_LINK_PATTERN = Pattern.compile(DYNAMIC_LINK_REGEX);

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

                    case (2048):
                        msgUtils = unFilmReviews(json);

                        break;

                    //直播信息
                    case (4308):
                        msgUtils = unpackLive(json);
                        break;
                    default:
                        return null;

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
        msgUtils.text(String.format("(%s)】的动态\n",pack.get("uid")));
        pack.put("url", String.format("https://t.bilibili.com/%s", desc.getString("dynamic_id")));
        msgUtils.text(String.format("%s\n",pack.get("url")));
        pack.put("type", BiliBiliEnum.FORWARD.getValue());

        pack.put("content", StringUtils.omitString(card.getJSONObject("item").getString("content"),72));
        msgUtils.text(String.format("%s：%s\n",pack.get("username"),pack.get("content")));
        pack.put("time", TimeUtils.stampToDate(desc.getLong("timestamp") * 1000));
        msgUtils.text(String.format("发布时间:%s\n",pack.get("time")));

        pack.put("org_type", BiliBiliEnum.getType(desc.getInteger("orig_type")));
        pack.put("org_username", originUser.getJSONObject("info").getString("uname"));
        pack.put("org_uid", originUser.getJSONObject("info").getString("uid"));

//        msgUtils.text(String.format("转发内容：【%s（%s）】的[%s]\n",pack.get("org_username"),pack.get("org_uid"),pack.get("org_type")));

        msgUtils.text(String.format("%s\n",pack.get("org_url")));

        List<String> picUrls = new ArrayList<>();
        switch (desc.getInteger("orig_type")) {
            //图片动态
            case (2):
                pack.put("org_content", StringUtils.omitString(origin.getJSONObject("item").getString("description"),72));
                msgUtils.text(String.format("%s：%s\n",pack.get("org_username"),pack.get("org_content")));

                pack.put("org_time", TimeUtils.stampToDate(origin.getJSONObject("item").getLong("upload_time") * 1000));
                JSONArray pics=origin.getJSONObject("item").getJSONArray("pictures");
                if (pics.size()==1){
                    String url=pics.getJSONObject(0).getString("img_src");
                    msgUtils.img(url+"@1000w_1000h");
                }
                if (pics.size()!=1){
                    for (Object o:pics) {
                        JSONObject pic =(JSONObject)o;
                        String url=pic.getString("img_src");
//                        int lastIndexOf = url.lastIndexOf(".");
//                        url=url.substring(0,lastIndexOf)+".png@1000w_1000h";
                        picUrls.add(url+"@1000w_1000h");
                    }
                    String path = ImageUtils.syntheticImage(picUrls);
                    if (path!=null){
                        msgUtils.img("file:///"+path);

                    }
                }
                msgUtils.text(String.format("发布时间:%s\n",pack.get("org_time")));

                break;
            //文字动态
            case (4):

                pack.put("org_content", StringUtils.omitString(origin.getJSONObject("item").getString("content"),72));
                msgUtils.text(String.format("%s：%s\n",pack.get("org_username"),pack.get("org_content")));
                pack.put("org_time", TimeUtils.stampToDate(origin.getJSONObject("item").getLong("timestamp") * 1000));
                msgUtils.text(String.format("发布时间:%s\n",pack.get("org_time")));
                break;
            //视频投稿
            case (8):
                pack.put("title", origin.getString("title"));
                pack.put("aid", origin.getString("aid"));
                pack.put("duration",TimeUtils.changeTimeFormat(origin.getInteger("duration")));

                pack.put("org_content", StringUtils.omitString(origin.getString("dynamic"),72));
                msgUtils.text(String.format("%s：%s\n",pack.get("org_username"),pack.get("org_content")));
                msgUtils.text(String.format("%s\n",pack.get("title")));
                pack.put("link", origin.getString("short_link_v2"));
                pack.put("desc", origin.getString("desc"));
                msgUtils.text(String.format("简介：%s\n",StringUtils.omitString((String) pack.get("desc"),50)));
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
                msgUtils.text(String.format("%s：\n",pack.get("org_username")));
                msgUtils.text(String.format("%s\n",pack.get("title")));
                pack.put("org_time", TimeUtils.stampToDate(origin.getLong("publish_time") * 1000));
                JSONArray imgs=origin.getJSONArray("image_urls");
                if (!"".equals(origin.getString("banner_url"))){
                    msgUtils.img(origin.getString("banner_url"));
                }else {
                    msgUtils.img(String.valueOf(origin.getJSONArray("origin_image_urls").get(0)));
                }
                pack.put("org_content", StringUtils.omitString(origin.getString("summary"),72));
                msgUtils.text(String.format("\n%s\n",pack.get("org_content")));

//                if (!"".equals(origin.getString("banner_url"))){
//                    msgUtils.img(origin.getString("banner_url"));
//                }
//                picUrls.add(origin.getString("origin_image_urls"));
                msgUtils.text(String.format("https://www.bilibili.com/read/cv%s\n",origin.getString("id")));

                msgUtils.text(String.format("发布时间:%s\n",pack.get("org_time")));

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
        msgUtils.text(String.format("(%s)】的动态\n",pack.get("uid")));

        pack.put("url", String.format("https://t.bilibili.com/%s", desc.getString("dynamic_id")));

        msgUtils.text(String.format("%s\n",pack.get("url")));
        pack.put("content", StringUtils.omitString(card.getJSONObject("item").getString("description"),72));
        JSONArray pics = card.getJSONObject("item").getJSONArray("pictures");
        msgUtils.text(String.format("%s：%s\n",pack.get("username"),pack.get("content")));
        if (pics.size()==1){
            String url=pics.getJSONObject(0).getString("img_src");
            msgUtils.img(url+"@1000w_1000h");
        }
        if (pics.size()!=1){
            for (Object o:pics) {
                JSONObject pic =(JSONObject)o;
                String url=pic.getString("img_src");
//                        int lastIndexOf = url.lastIndexOf(".");
//                        url=url.substring(0,lastIndexOf)+".png@1000w_1000h";
                picUrls.add(url+"@1000w_1000h");
            }
            String path = ImageUtils.syntheticImage(picUrls);
            if (path!=null){
                msgUtils.img("file:///"+path);

            }
        }
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
        pack.put("content", StringUtils.omitString(card.getJSONObject("item").getString("content"),72));
        pack.put("url", String.format("https://t.bilibili.com/%s", desc.getString("dynamic_id")));
        pack.put("pics",picUrls);
        msgUtils.text(String.format("【%s",pack.get("username")));
        msgUtils.text(String.format("(%s)】的动态\n",pack.get("uid")));
        msgUtils.text(String.format("%s\n",pack.get("url")));
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
        pack.put("content",StringUtils.omitString(card.getString("dynamic"),72));
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
        msgUtils.text(String.format("(%s)】的动态\n",pack.get("uid")));
        msgUtils.text(String.format("%s\n",pack.get("url")));
        msgUtils.text(String.format("%s：%s\n",pack.get("username"),pack.get("content")));
        msgUtils.text(String.format("%s\n",pack.get("title"),pack.get("aid")));
        msgUtils.text(String.format("简介：%s\n",StringUtils.omitString((String) pack.get("desc"),50)));


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


        pack.put("time",TimeUtils.stampToDate(desc.getLong("timestamp")*1000));
        pack.put("username",card.getJSONObject("author").getString("name"));
        pack.put("uid",card.getJSONObject("author").getString("mid"));
        pack.put("content",StringUtils.omitString(card.getString("summary"),72));

        pack.put("title",card.getString("title"));
        pack.put("url", String.format("https://t.bilibili.com/%s", desc.getString("dynamic_id")));

        msgUtils.text(String.format("【%s",pack.get("username")));
        msgUtils.text(String.format("(%s)】的动态\n",pack.get("uid")));
        msgUtils.text((String) pack.get("url"));
        msgUtils.text(String.format("\n%s：\n",pack.get("username")));
        msgUtils.text(String.format("%s\n",pack.get("title")));

        if (!"".equals(card.getString("banner_url"))){
            msgUtils.img(card.getString("banner_url"));
        }else {
            msgUtils.img(String.valueOf(card.getJSONArray("image_urls").get(0)));
        }
        msgUtils.text(String.format("%s\n",pack.get("content")));
        msgUtils.text(String.format("https://www.bilibili.com/read/cv%s\n",card.getString("id")));
        msgUtils.text(String.format("发布时间:%s\n",pack.get("time")));


//        pack.put("pics",picUrls);
//        msgUtils.img(card.getString("origin_image_urls"));


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
        msgUtils.text(String.format("(%s)】的动态\n",pack.get("uid")));
        msgUtils.text(String.format("%s开启了直播：%s\n",pack.get("username"),pack.get("title")));
        pack.put("url", String.format("https://t.bilibili.com/%s", desc.getString("dynamic_id")));

        msgUtils.text(String.format("%s\n",pack.get("url")));
        msgUtils.text(String.format("分区：%s-%s\n",pack.get("parent_area"),pack.get("area")));
        msgUtils.text(String.format("开启时间：%s\n",pack.get("time")));
        msgUtils.img(card.getJSONObject("live_play_info").getString("cover"));
        msgUtils.text(String.format("\n%s",pack.get("link")));

        return msgUtils;
    }


    private static MsgUtils unFilmReviews(JSONObject json){
        MsgUtils msgUtils=MsgUtils.builder();
        JSONObject card = JSONObject.parseObject(json.getString("card"));
        JSONObject desc = json.getJSONObject("desc");
        msgUtils.text(String.format("【%s（%s）】的动态\n%s\n%s评价了【%s】：%s\n",card.getJSONObject("user").getString("uname"),
                card.getJSONObject("user").getString("uid"),
                String.format("https://t.bilibili.com/%s", desc.getString("dynamic_id")),
                        card.getJSONObject("user").getString("uname"),
                        card.getJSONObject("sketch").getString("title"),
                        card.getJSONObject("vest").getString("content")));
        msgUtils.img(card.getJSONObject("sketch").getString("cover_url"));
        msgUtils.text(card.getJSONObject("sketch").getString("target_url"));
        return msgUtils;
    }

    public  static MsgUtils parsingBID(String bid) {
        JSONObject json = BilibiliApi.getVideoByBid(bid);
        if (json.getInteger("code")!=0){
            log.error(String.format("解析BV号：%s失败",bid));
            return  null;
        }

        JSONObject data = json.getJSONObject("data");
        JSONObject stat = data.getJSONObject("stat");
        MsgUtils msg=new MsgUtils();

        msg.text(String.format("%s\n",data.getString("title")));
        msg.text(String.format("分区：%s\n",data.getString("tname")));
        msg.text(String.format("介绍：%s\n",data.getString("desc")));
        msg.text(String.format("硬币：%d  ",stat.getInteger("coin")));
        msg.text(String.format("点赞：%d  ",stat.getInteger("coin")));
        msg.text(String.format("收藏：%d  ",stat.getInteger("favorite")));
        msg.text(String.format("转发：%d  ",stat.getInteger("reply")));
        msg.text(String.format("播放：%d  ",stat.getInteger("view")));
        msg.text(String.format("集数：%d  ",data.getJSONArray("pages").size()));
        msg.text(String.format("发布时间：%s\n", TimeUtils.stampToDate(data.getLong("ctime")*1000)));
        msg.text(String.format("时长：%s\n", TimeUtils.changeTimeFormat(data.getInteger("duration"))));
        msg.text(String.format("UP主：%s(%d)\n",data.getJSONObject("owner").getString("name"),
                data.getJSONObject("owner").getInteger("mid")));
        msg.text(String.format("https://www.bilibili.com/video/%s",data.getString("bvid")));
        msg.img(data.getString("pic"));



//        video.getInteger()
        return msg;
    }


    public  static MsgUtils parsingDynamicLink(String urL) {

        Matcher matcher = DYNAMIC_LINK_PATTERN.matcher(urL);
        MsgUtils msgUtils;
        JSONObject object;
        matcher.find();
        switch (matcher.group(1)) {
            case ("dynamic") -> {
                object = BilibiliApi.getDynamicCard(matcher.group(3));
                try {
                    msgUtils = BilibiliStrategy.dynamicStrategy(object.getJSONObject("data").getJSONObject("card"));
                } catch (BilibiliException e) {
                    msgUtils = null;
                    e.printStackTrace();
                }
            }
            case ("video") -> msgUtils = BilibiliStrategy.parsingBID(matcher.group(3));
            case ("read/mobile") ->
                    msgUtils = null;
            default -> msgUtils = null;
        }



//        video.getInteger()
        return msgUtils;
    }


}
