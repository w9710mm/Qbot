package com.mm.qbot.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mikuac.shiro.common.utils.MsgUtils;
import com.mm.qbot.utils.LevelDB;
import com.mm.qbot.utils.TikTokApi;
import com.mm.qbot.utils.TimeUtils;
import org.springframework.stereotype.Service;

/**
 * @author meme
 * @version V0.0.1
 * @Package com.mm.qbot.service
 * @Description:
 * @date 2021/11/10 15:46
 */
@Service
public class TikTokService {

    private LevelDB levelDB=LevelDB.getInstance();
    public MsgUtils getVideo(String uid){
        MsgUtils msgUtils=MsgUtils.builder();

        JSONObject jsonObject = TikTokApi.getNewVideo(uid);
        if (jsonObject.size()==0){
            return null;
        }
        JSONArray awemeList = jsonObject.getJSONArray("aweme_list");
        if (awemeList==null||awemeList.size()==0){
            msgUtils=null;
        }else {
            JSONObject aweme=awemeList.getJSONObject(0);
            String awemeId = levelDB.getString("TikTok" + aweme.getString("aweme_id"));
            if ("".equals(awemeId)||awemeId==null) {
                msgUtils.text(String.format("【%s】更新了抖音视频\n%s\n更新时间：%s\n%s\n",
                        aweme.getJSONObject("author").getString("nickname")
                        , aweme.getString("desc")
                        , TimeUtils.stampToDate(jsonObject.getLong("min_cursor"))
                        , "https://www.douyin.com/video/" + aweme.getString("aweme_id")));
                msgUtils.img(aweme.getJSONObject("video").getJSONObject("cover").getJSONArray("url_list").getString(0));
            levelDB.put("TikTok" + aweme.getString("aweme_id"),"1");
            }else {
                msgUtils=null;
            }
        }

        return  msgUtils;
    }

}
