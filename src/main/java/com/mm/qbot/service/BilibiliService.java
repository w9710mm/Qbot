package com.mm.qbot.service;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mm.qbot.enumeration.RelationActionEnum;
import com.mm.qbot.utils.BilibiliApi;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class BilibiliService {


    private String uidReg="[0-9]+";

    private Pattern uidPattern=Pattern.compile(uidReg);

    public Long followUser(String text){

        Matcher matcher = uidPattern.matcher(text);

        Long uid=null;

        if (matcher.lookingAt()){

            JSONObject res = BilibiliApi.modifyRelation(Long.valueOf(matcher.group()), RelationActionEnum.SUBSCRIBE);
            if (res.getInteger("message")==0) {
                uid=Long.valueOf(matcher.group());
            }
        }
        if (!matcher.lookingAt()){
            JSONObject result=BilibiliApi.serachUser(text,"bili_user");
            JSONArray datas = result.getJSONObject("data").getJSONArray("result");
            if (datas!=null){
                JSONObject data = datas.getJSONObject(0);
                Long mid = data.getLong("mid");
                JSONObject res = BilibiliApi.modifyRelation(mid, RelationActionEnum.SUBSCRIBE);
                if (res.getInteger("message")==0) {
                    uid=mid;
                }
            }
        }

        return uid;
    }
    
}
