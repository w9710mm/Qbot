package com.mm.qbot.service;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mikuac.shiro.common.utils.MsgUtils;
import com.mm.qbot.Exception.BilibiliException;
import com.mm.qbot.bean.pushMap.User;
import com.mm.qbot.bean.pushMap.UserSubscribe;
import com.mm.qbot.bean.pushMap.UserSubscribeMap;
import com.mm.qbot.enumeration.NeedTopEnum;
import com.mm.qbot.enumeration.RelationActionEnum;
import com.mm.qbot.strategy.BilibiliStrategy;
import com.mm.qbot.utils.BilibiliApi;
import com.mm.qbot.utils.LevelDB;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.LinkedHashSet;
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
    public MsgUtils followUser(User uid){


        if (uid==null){
            return null;
        }
        JSONObject res = BilibiliApi.modifyRelation(Long.valueOf(uid.getUid()), RelationActionEnum.SUBSCRIBE);
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
    public boolean addSubscribe(Long qid, User subcribeId , @NotNull UserSubscribeMap userSubscribeMap, @NotNull Boolean isGroup, Map<User, LinkedHashSet<Long>> pushMap) {

        Map<Long, UserSubscribe> subscribeMap;
            if (isGroup) {
                subscribeMap=  userSubscribeMap.getGroupSubscribeMap();
            }else {
                subscribeMap=userSubscribeMap.getPrivateSubscribeMap();
            }
            if (subscribeMap.containsKey(qid)){
                UserSubscribe userSubscribe = subscribeMap.get(qid);
                LinkedHashSet<User> bids = userSubscribe.getBids();
                if (bids==null){
                    bids=new LinkedHashSet<>();
                    userSubscribe.setBids(bids);
                }
                bids.add(subcribeId);
            }
            if (!subscribeMap.containsKey(qid)){
            UserSubscribe userSubscribe=new UserSubscribe();
            userSubscribe.setId(qid);
            userSubscribe.setIsGroup(isGroup);
                LinkedHashSet<User> dis= userSubscribe.getBids();
            dis.add(subcribeId);
            subscribeMap.put(qid,userSubscribe);
         }


            if (pushMap.containsKey(subcribeId)){
                LinkedHashSet<Long> longs = pushMap.get(subcribeId);
                longs.add(qid);
            }
            if (!pushMap.containsKey(subcribeId)){
                LinkedHashSet<Long> longs=new LinkedHashSet<>();
                longs.add(qid);
                pushMap.put(subcribeId,longs);
            }

         return levelDB.put("userSubscribeMap", userSubscribeMap);

    }


    public User findUser(String text){

        Matcher matcher = uidPattern.matcher(text);

        User uid=new User();

        if (matcher.lookingAt()){
            JSONObject userInfo = BilibiliApi.getUserInfo(Long.valueOf(text));
            if (userInfo.getInteger("code")!=404){
                uid.setUid(userInfo.getJSONObject("data").getString("mid"));
                uid.setUname(userInfo.getJSONObject("data").getString("name"));
            }
        }
        if (!matcher.lookingAt()){
            JSONObject result=BilibiliApi.serachUser(text,"bili_user");
            JSONArray datas = result.getJSONObject("data").getJSONArray("result");
            if (datas!=null){
                JSONObject data = datas.getJSONObject(0);
                uid.setUid(data.getString("mid"));
                uid.setUname(data.getString("uname"));
            }
        }
        return  uid;
    }

    public MsgUtils undoSubscribe(long qid,
                                 int num,
                                 Map<Long, UserSubscribe> subscribeMap,
                                 Map<User, LinkedHashSet<Long>> pushMap){

        MsgUtils msgUtils = MsgUtils.builder();
        if (subscribeMap.containsKey(qid)){
            UserSubscribe userSubscribe = subscribeMap.get(qid);
            LinkedHashSet<User> bids = userSubscribe.getBids();
            if (bids.size()!=0&&bids.size()<=num){
                int count=1;

                for (User user :bids) {
                    if (count==num){
                        if (pushMap.containsKey(user)){
                            LinkedHashSet<Long> longs = pushMap.get(user);
                            longs.remove(qid);
                        }
                        bids.remove(user);
                        msgUtils.text("取消成功！");
                        break;
                    }
                    count++;
                }

            }
            else {
                if (bids.size()>=num){
                    msgUtils.text("取消失败，请检查序号是否正确。");
                }else{
                msgUtils.text("好像没有任何订阅信息。");
                }
            }
        }
        if (!subscribeMap.containsKey(qid)){
            msgUtils=MsgUtils.builder().text("好像没有任何订阅信息。");
        }
        return  msgUtils;
    }


    public MsgUtils undoAllSubscribe(long qid,
                                  Map<Long, UserSubscribe> subscribeMap,
                                  Map<User, LinkedHashSet<Long>> pushMap){

        MsgUtils msgUtils = MsgUtils.builder();
        if (subscribeMap.containsKey(qid)) {
            UserSubscribe userSubscribe = subscribeMap.get(qid);
            LinkedHashSet<User> bids = userSubscribe.getBids();
            for (User u:bids) {
                if (pushMap.containsKey(u)){
                    LinkedHashSet<Long> longs = pushMap.get(u);
                    for (Long q:longs) {
                        if (q.equals(qid)){
                            longs.remove(q);
                            break;
                        }
                    }
                }
            }
            userSubscribe.setBids(new LinkedHashSet<>());
            msgUtils.text("取消成功！");
        }else {
            msgUtils.text("取消失败，可能没有任何订阅信息");
        }
        return  msgUtils;
    }
    public MsgUtils showSubscribe(long qid,
                                  Map<Long, UserSubscribe> subscribeMap){

        MsgUtils msgUtils = MsgUtils.builder();

        if (subscribeMap.containsKey(qid)){
            UserSubscribe userSubscribe = subscribeMap.get(qid);
            Set<User> bids = userSubscribe.getBids();
            if (bids.size()!=0){
                if (!userSubscribe.getIsGroup()){
                    msgUtils.text("你订阅的UP主有这些：\n");
                }
                if (userSubscribe.getIsGroup()){
                    msgUtils.text("本群订阅的UP主有这些：\n");
                }
                int count=1;
                for (User user :bids) {
                    msgUtils.text(String.format("%d、 %s   %s\n",count,user.getUname(),user.getUid()));
                    count++;
                }
                msgUtils.text("uid信息+uid/用户名\n");
                msgUtils.text("取消订阅：取消动态+序号/all");
            }else {
                msgUtils.text("好像没有任何订阅信息");
            }
        }
        if (!subscribeMap.containsKey(qid)){
            msgUtils.text("好像没有任何订阅信息");
        }
        return  msgUtils;
    }

    public MsgUtils parsingShortLink(String url){
        String realLink = BilibiliApi.getRealLink(url);


        return BilibiliStrategy.parsingDynamicLink(realLink);
    }



    public MsgUtils parsingDynamic(String url) {
        MsgUtils msgUtils;

        try {
            JSONObject dynamicCard = BilibiliApi.getDynamicCard(url);
            msgUtils= BilibiliStrategy.dynamicStrategy(dynamicCard.getJSONObject("data").getJSONObject("card"));
        }catch (Exception e){
            e.printStackTrace();
            msgUtils=null;
        }

        return msgUtils;
    }

    public MsgUtils getUpInfo(String uid){
        MsgUtils msgUtils=MsgUtils.builder();
        JSONObject userInfo = BilibiliApi.getUserInfo(Long.valueOf(uid));
        JSONObject data = userInfo.getJSONObject("data");

        msgUtils.img(data.getString("face"));
        msgUtils.text(String.format("\n签名：%s",data.getString("sign")));
        msgUtils.text( String.format("\n昵称：（%s）:%s生日:%s",data.getString("name"),uid,data.getString("birthday")));
        msgUtils.text(String.format("等级：%s",data.getString("level")));
        msgUtils.text(String.format("性别：%s",data.getString("sex")));
        JSONObject upStat = BilibiliApi.getUpStat(uid);
        JSONObject upStatData=upStat.getJSONObject("data");

        msgUtils.text(String.format("\n播放量：%s 阅读量：%s 点赞数：%s ",upStatData.getJSONObject("archive").getString("view"),
         upStatData.getJSONObject("article").getString("view") ,upStatData.getString("likes") ));

        JSONObject relationStat = BilibiliApi.getRelationStat(uid);
        JSONObject relationStatData=relationStat.getJSONObject("data");

        msgUtils.text(String.format("\n粉丝：%s 关注:%s ",relationStatData.getString("follower"),relationStatData.getString("following")));
        return msgUtils;
    }
}
