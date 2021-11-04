package com.mm.qbot.service;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mikuac.shiro.common.utils.MsgUtils;
import com.mm.qbot.Exception.BilibiliException;
import com.mm.qbot.dto.UserSubscribe;
import com.mm.qbot.dto.UserSubscribeMap;
import com.mm.qbot.enumeration.NeedTopEnum;
import com.mm.qbot.enumeration.RelationActionEnum;
import com.mm.qbot.strategy.BilibiliParsingStrategy;
import com.mm.qbot.strategy.BilibiliStrategy;
import com.mm.qbot.utils.BilibiliApi;
import com.mm.qbot.utils.LevelDB;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class BilibiliService {


    private final String uidReg="[0-9]+";

    private final Pattern uidPattern=Pattern.compile(uidReg);

    private final LevelDB levelDB=LevelDB.getInstance();

    //关注一个账号
    public MsgUtils followUser(Long uid){


        if (uid==null){
            return null;
        }
        JSONObject res = BilibiliApi.modifyRelation(uid, RelationActionEnum.SUBSCRIBE);
        if (res.getInteger("message")!=0) {
            return null;
        }
        return MsgUtils.builder().text("订阅成功！");
    }

    //关注一个账号
    public MsgUtils getNewDynamic(Long uid){



        JSONObject res =BilibiliApi.getSpaceDynamic(uid,0L, NeedTopEnum.NOT_NEED);
        JSONArray cards=res.getJSONObject("data").getJSONArray("cards");
        if (cards==null) {
            return null;
        }
        JSONObject card = cards.getJSONObject(0);
        MsgUtils msgUtils;
        try {
             msgUtils = BilibiliStrategy.dynamicStrategy(card);
        } catch (BilibiliException e) {
            e.printStackTrace();
            return null;
        }

        return msgUtils;
    }


    //添加订阅信息
    public boolean addSubscribe(Long Qid, Long subcribeId , @NotNull UserSubscribeMap userSubscribeMap, Boolean isGroup,Map<Long, Set<Long>> pushMap) {

            Map<Long, UserSubscribe> subscribeMap = userSubscribeMap.getSubscribeMap();

            if (subscribeMap.containsKey(Qid)){
                UserSubscribe userSubscribe = subscribeMap.get(Qid);
                Set<Long> bids = userSubscribe.getBids();
                if (bids==null){
                    bids=new HashSet<>();
                    userSubscribe.setBids(bids);
                }
                bids.add(subcribeId);
            }
            if (!subscribeMap.containsKey(Qid)){
            UserSubscribe userSubscribe=new UserSubscribe();
            userSubscribe.setId(Qid);
            userSubscribe.setIsGroup(isGroup);
            Set<Long> dis= userSubscribe.getBids();
            dis.add(subcribeId);
            subscribeMap.put(Qid,userSubscribe);
         }


            if (pushMap.containsKey(subcribeId)){
                Set<Long> longs = pushMap.get(subcribeId);
                longs.add(Qid);
            }
            if (!pushMap.containsKey(subcribeId)){
                Set<Long> longs=new HashSet<>();
                longs.add(Qid);
                pushMap.put(subcribeId,longs);
            }

         return levelDB.put("userSubscribeMap", userSubscribeMap);

    }


    public Long findUid(String text){

        Matcher matcher = uidPattern.matcher(text);

        Long uid=null;

        if (matcher.lookingAt()){
            uid=Long.valueOf(matcher.group());
        }
        if (!matcher.lookingAt()){
            JSONObject result=BilibiliApi.serachUser(text,"bili_user");
            JSONArray datas = result.getJSONObject("data").getJSONArray("result");
            if (datas!=null){
                JSONObject data = datas.getJSONObject(0);
                uid = data.getLong("mid");
            }
        }
        return  uid;
    }
}
