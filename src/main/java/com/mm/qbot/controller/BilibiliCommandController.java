package com.mm.qbot.controller;

import com.mikuac.shiro.annotation.GroupMessageHandler;
import com.mikuac.shiro.annotation.PrivateMessageHandler;
import com.mikuac.shiro.common.utils.MsgUtils;
import com.mikuac.shiro.core.Bot;
import com.mikuac.shiro.core.BotPlugin;
import com.mikuac.shiro.dto.event.message.GroupMessageEvent;
import com.mikuac.shiro.dto.event.message.PrivateMessageEvent;
import com.mm.qbot.Exception.BilibiliException;
import com.mm.qbot.dto.*;
import com.mm.qbot.service.BilibiliService;
import com.mm.qbot.strategy.BilibiliParsingStrategy;
import com.mm.qbot.utils.LevelDB;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;

/**
 * @author WWM
 * @version V0.0.1
 * @Package com.mm.qbot.controller
 * @Description: 解析bilibili
 * @date 2021/10/23 14:57
 */

@Controller
public class BilibiliCommandController extends BotPlugin {


    private final BilibiliPushMap bilibiliPushMap =BilibiliPushMap.getInstance();

    private final WeiBoPushMap weiBoPushMap =WeiBoPushMap.getInstance();

    private final TiktokPushMap tiktokPushMap =TiktokPushMap.getInstance();

    private final UserSubscribeMap userSubscribeMap=UserSubscribeMap.getInstance();

    private final LevelDB levelDB=LevelDB.getInstance();

    @Resource
    private BilibiliService bilibiliService;

      @PrivateMessageHandler(cmd="[Bb][Vv][a-zA-Z0-9]{10}")
      public int bvidParsingInPrivate(@NotNull Bot bot, PrivateMessageEvent event, Matcher m) {


        if (m!=null&&m.lookingAt()){
            try {

                MsgUtils msgUtils = BilibiliParsingStrategy.ParsingBID(m.group(0));
                if (event!=null) {
                    bot.sendPrivateMsg(event.getPrivateSender().getUserId(),msgUtils.build(),false);
                }
            } catch (BilibiliException e) {
                e.printStackTrace();
            }
        }
        return MESSAGE_BLOCK;
    }


    @GroupMessageHandler(cmd="[Bb][Vv][a-zA-Z0-9]{10}")
    public int bvidParsingInGroup(@NotNull Bot bot, GroupMessageEvent event, Matcher m) {


        if (m!=null&&m.lookingAt()){
            try {

                MsgUtils msgUtils = BilibiliParsingStrategy.ParsingBID(m.group(0));
                if (event!=null) {
                    bot.sendGroupMsg(event.getGroupId(),msgUtils.build(),false);
                }
            } catch (BilibiliException e) {
                e.printStackTrace();
            }
        }
        return MESSAGE_BLOCK;
    }

    @PrivateMessageHandler(cmd="(\\b订阅动态 )(\\S+)")
    public void privateSubscribeInPrivate(@NotNull Bot bot, PrivateMessageEvent event, Matcher m) {


        if (event!=null){
            Long qid =event.getPrivateSender().getUserId();
            Long uid = bilibiliService.followUser(m.group(2));
            if (uid==null){
                bot.sendPrivateMsg(qid,"订阅失败，请检查输入的uid或者昵称是否正确。\n例如：订阅动态 嘉然今天吃什么",false);
                return;
            }
            Map<Long, UserSubscribe> subscribeMap = userSubscribeMap.getSubscribeMap();
            if (!subscribeMap.containsKey(qid)){
                UserSubscribe userSubscribe=new UserSubscribe();
                userSubscribe.setId(qid);
                Set<Long> dis=new HashSet<>();
                dis.add(uid);
                userSubscribe.setBids(dis);
                subscribeMap.put(qid,userSubscribe);
            }
            if (subscribeMap.containsKey(qid)){
                UserSubscribe userSubscribe = subscribeMap.get(qid);
                Set<Long> bids = userSubscribe.getBids();
               if (bids==null){
                   bids=new HashSet<>();
                   userSubscribe.setBids(bids);
               }
               bids.add(uid);
            }

            Map<Long, Set<Long>> privateMap = bilibiliPushMap.getPrivateMap();

            if (privateMap.containsKey(uid)){
                Set<Long> longs = privateMap.get(uid);
                longs.add(qid);
            }
            if (!privateMap.containsKey(uid)){
                Set<Long> longs=new HashSet<>();
                longs.add(qid);
                privateMap.put(uid,longs);
            }
            bot.sendPrivateMsg(qid,"订阅成功！",false);
            levelDB.put("userSubscribeMap",userSubscribeMap);


        }
    }


    @GroupMessageHandler(cmd="(\\b订阅动态 )(\\S+)")
    public void privateSubscribeInGroup(@NotNull Bot bot, GroupMessageEvent event, Matcher m) {


        if (event!=null) {
            Long qid = Long.valueOf(event.getSender().getUserId());
            Long uid = bilibiliService.followUser(m.group(2));
            if (uid == null) {
                bot.sendPrivateMsg(qid, "订阅失败，请检查输入的uid或者昵称是否正确。\n例如：订阅动态 嘉然今天吃什么", false);
                return;
            }
            Map<Long, UserSubscribe> subscribeMap = userSubscribeMap.getSubscribeMap();
            if (!subscribeMap.containsKey(qid)) {
                UserSubscribe userSubscribe = new UserSubscribe();
                userSubscribe.setId(qid);
                Set<Long> dis = new HashSet<>();
                dis.add(uid);
                userSubscribe.setBids(dis);
                subscribeMap.put(qid, userSubscribe);

                Map<Long, Set<Long>> privateMap = bilibiliPushMap.getPrivateMap();

                if (privateMap.containsKey(uid)) {
                    Set<Long> longs = privateMap.get(uid);
                    longs.add(qid);
                }
                if (!privateMap.containsKey(uid)) {
                    Set<Long> longs = new HashSet<>();
                    longs.add(qid);
                    privateMap.put(uid, longs);
                }

            }

            bot.sendPrivateMsg(qid, "订阅成功！", false);
        }
    }



    @GroupMessageHandler(cmd="(\\b群订阅动态 )(\\S+)")
    public void groupSubscribe(@NotNull Bot bot, GroupMessageEvent event, Matcher M) {

        if (M!=null&&M.lookingAt()){
            try {

                MsgUtils msgUtils = BilibiliParsingStrategy.ParsingBID(M.group(0));
                LevelDB instance = LevelDB.getInstance();
                instance.put("1","1");
            } catch (BilibiliException e) {
                e.printStackTrace();
            }
        }
    }

    @GroupMessageHandler(cmd="(\\b最新动态 )(\\S+)")
    public int getNewDynamicInPrivate(@NotNull Bot bot, PrivateMessageEvent event, Matcher m) {


        if (m!=null&&m.lookingAt()){
            try {

                MsgUtils msgUtils = BilibiliParsingStrategy.ParsingBID(m.group(0));
//                bot.sendPrivateMsg(event.getUserId(),msgUtils.build(),false);
                LevelDB instance = LevelDB.getInstance();
                instance.put("1","1");
//               redisTemplate.opsForValue().set("1",msgUtils.build().getBytes());
            } catch (BilibiliException e) {
                e.printStackTrace();
            }
        }
        return MESSAGE_BLOCK;
    }


    @GroupMessageHandler(cmd="(\\b最新动态 )(\\S+)")
    public int getNewDynamicInGroup(@NotNull Bot bot, GroupMessageEvent event, Matcher m) {


        if (m!=null&&m.lookingAt()){
            try {

                MsgUtils msgUtils = BilibiliParsingStrategy.ParsingBID(m.group(0));
//                bot.sendPrivateMsg(event.getUserId(),msgUtils.build(),false);
                LevelDB instance = LevelDB.getInstance();
                instance.put("1","1");
//               redisTemplate.opsForValue().set("1",msgUtils.build().getBytes());
            } catch (BilibiliException e) {
                e.printStackTrace();
            }
        }
        return MESSAGE_BLOCK;
    }

    @PrivateMessageHandler(cmd="b23\\.tv/([a-zA-Z0-9]{6})")
    public int shortLinkParsing(@NotNull Bot bot, PrivateMessageEvent privateEvent, Matcher m) {


        if (m!=null&&m.lookingAt()){
            try {

                MsgUtils msgUtils = BilibiliParsingStrategy.ParsingBID(m.group(0));
//                bot.sendPrivateMsg(event.getUserId(),msgUtils.build(),false);
                LevelDB instance = LevelDB.getInstance();
                instance.put("1","1");
//               redisTemplate.opsForValue().set("1",msgUtils.build().getBytes());
            } catch (BilibiliException e) {
                e.printStackTrace();
            }
        }
        return MESSAGE_BLOCK;
    }

    @GroupMessageHandler(cmd="b23\\.tv/([a-zA-Z0-9]{6})")
    public int shortLinkParsingInGroup(@NotNull Bot bot  , GroupMessageEvent event, Matcher m) {


        if (m!=null&&m.lookingAt()){
            try {

                MsgUtils msgUtils = BilibiliParsingStrategy.ParsingBID(m.group(0));
//                bot.sendPrivateMsg(event.getUserId(),msgUtils.build(),false);
                LevelDB instance = LevelDB.getInstance();
                instance.put("1","1");
//               redisTemplate.opsForValue().set("1",msgUtils.build().getBytes());
            } catch (BilibiliException e) {
                e.printStackTrace();
            }
        }
        return MESSAGE_BLOCK;
    }

}
