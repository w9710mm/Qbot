package com.mm.qbot.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mikuac.shiro.common.utils.MsgUtils;
import com.mm.qbot.utils.*;
import org.checkerframework.checker.units.qual.A;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * @author meme
 * @version V0.0.1
 * @Package com.mm.qbot.service
 * @Description:
 * @date 2021/11/10 15:46
 */
@Service
public class WeiboService {


    private final LevelDB levelDB=LevelDB.getInstance();

    private  final SimpleDateFormat format = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.US);
    private  final SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    public MsgUtils getWeibo(String uid){
        MsgUtils msgUtils=MsgUtils.builder();

        JSONObject jsonObject = WeiBoApi.getNewDynamic(uid,1);

        JSONArray cards = jsonObject.getJSONObject("data").getJSONArray("cards");


        if (cards==null||cards.size()==0){
            msgUtils=null;
        }else {
            try {
            JSONObject card1=cards.getJSONObject(0);
            JSONObject card2=cards.getJSONObject(1);
            JSONObject mblog1=card1.getJSONObject("mblog");
            JSONObject mblog2=card2.getJSONObject("mblog");

            Date date1 = null;

                date1 = format.parse(mblog1.getString("created_at"));

            Date date2 = format.parse(mblog2.getString("created_at"));

            JSONObject mblog=mblog1;
            Date date=date1;
            JSONObject card=card1;
            if (date1.before(date2)){
                mblog=mblog2;
                date=date2;
                card=card2;
            }

            String cardId = levelDB.getString(card.getString("itemid"));
            if ("".equals(cardId)||cardId==null) {
                String text;
                if (mblog.getString("raw_text")==null||"".equals(mblog.getString("raw_text"))){
                    text= StringUtils.delHtmlTags(mblog.getString("text"));
                }else {
                    text=mblog.getString("raw_text");
                }
                msgUtils.text(String.format("【%s】更新了微博\n%s:%s\n%s\n更新时间：%s\n",
                        mblog.getJSONObject("user").getString("screen_name")
                        , mblog.getJSONObject("user").getString("screen_name")
                        , text
                        , String.format("https://weibo.com/%s/%s",uid,mblog.getString("bid"))
                        ,format2.format(date)));
                if (mblog.getInteger("pic_num")!=0){
                    JSONArray picArray=mblog.getJSONArray("pics");
                    if (picArray.size()==1){
                        msgUtils.img(picArray.getJSONObject(0).getString("url"));
                    }else {
                        ArrayList<String> pics=new ArrayList<>();
                        for (Object o:picArray) {
                           JSONObject pic=(JSONObject)o;
                           pics.add(pic.getString("url"));
                        }
                        try {
                            String s = ImageUtils.syntheticImage(pics);
                            msgUtils.img("file:///"+s);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }
//                msgUtils.img(aweme.getJSONObject("video").getJSONObject("cover").getJSONArray("url_list").getString(0));
                levelDB.put(card.getString("itemid"),"1");

            }else {
                msgUtils=null;
            }
            } catch (ParseException e) {
                e.printStackTrace();
                msgUtils=null;
            }
        }

        return  msgUtils;
    }
}
