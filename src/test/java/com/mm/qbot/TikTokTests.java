package com.mm.qbot;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mm.qbot.utils.BilibiliApi;
import com.mm.qbot.utils.TikTokApi;
import com.mm.qbot.utils.WeiBoApi;
import org.junit.Test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.Locale;

/**
 * @author meme
 * @version V0.0.1
 * @Package com.mm.qbot
 * @Description:
 * @date 2021/11/10 19:06
 */
public class TikTokTests {

    @Test
    public void test1(){
        JSONObject json = WeiBoApi.getNewDynamic("7519401668",1);
        SimpleDateFormat format = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.US);
        JSONArray cards = json.getJSONObject("data").getJSONArray("cards");
        String data=cards.getJSONObject(0).getJSONObject("mblog").getString("created_at");
        try {
            Date parse = format.parse(data);

//            System.out.println(parse.t);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        System.out.println(json.toJSONString());
    }

    @Test
    public void test2(){
        DateFormat format = new SimpleDateFormat("MMM dd HH:mm:ss yyyy", Locale.CHINESE);
        try {
            String data="Jul 03 16:32:55 2021";
//            String data="16:32:55 2021";
            Date parse = format.parse(data);
            System.out.println(parse);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
