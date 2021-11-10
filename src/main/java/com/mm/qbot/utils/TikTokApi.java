package com.mm.qbot.utils;

import com.alibaba.fastjson.JSONObject;

/**
 * @author meme
 * @version V0.0.1
 * @Package com.mm.qbot.utils
 * @Description:
 * @date 2021/11/10 9:02
 */
public class TikTokApi {

    private static JSONObject getNewVideo(String uid){
        String url=String.format("https://www.iesdouyin.com/web/api/v2/aweme/post/?sec_uid=%s&count=%d",uid, 21);


    }
}
