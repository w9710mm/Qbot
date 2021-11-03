package com.mm.qbot.strategy;

import com.alibaba.fastjson.JSONObject;
import com.mikuac.shiro.common.utils.MsgUtils;
import com.mm.qbot.Exception.BilibiliException;
import com.mm.qbot.utils.BilibiliApi;
import com.mm.qbot.utils.TimeUtils;

/**
 * @author WWM
 * @version V0.0.1
 * @Package com.mm.qbot.strategy
 * @Description: jiexi
 * @date 2021/10/22 15:07
 */
public class BilibiliParsingStrategy {


    public static String  ParsingShortLink(String url){
        return "test";

    }

    public static  String ParsingLink(String url){
        return "test";

    }

    public  static MsgUtils ParsingBID(String bid) {
        JSONObject json = BilibiliApi.getVideoByBid(bid);
        if (json.getInteger("code")!=0){
            return  null;
        }

        JSONObject data = json.getJSONObject("data");
        JSONObject stat = data.getJSONObject("stat");
        MsgUtils msg=new MsgUtils();

        msg.text(String.format("标题：%s\n",data.getString("title")));
        msg.text(String.format("分区：%s\n",data.getString("tname")));
        msg.text(String.format("介绍：%s\n",data.getString("desc")));
        msg.text(String.format("硬币：%d  ",stat.getInteger("coin")));
        msg.text(String.format("点赞：%d  ",stat.getInteger("coin")));
        msg.text(String.format("收藏：%d  ",stat.getInteger("favorite")));
        msg.text(String.format("转发：%d  ",stat.getInteger("reply")));
        msg.text(String.format("播放：%d  ",stat.getInteger("view")));
        msg.text(String.format("集数：%d  ",data.getJSONArray("pages").size()));
        msg.text(String.format("发布时间：%s\n", TimeUtils.stampToDate(data.getLong("ctime"))));
        msg.text(String.format("时长：%s\n", TimeUtils.changeTimeFormat(data.getInteger("duration"))));
        msg.text(String.format("UP主：%s(%d)\n",data.getJSONObject("owner").getString("name"),
                data.getJSONObject("owner").getInteger("mid")));
        msg.text(String.format("url：https://www.bilibili.com/video/%s",data.getString("bvid")));
        msg.img(data.getString("pic"));






//        video.getInteger()
        return msg;
    }


    public static String ParsingCard(String bid ){


        return "test";
    }


}
