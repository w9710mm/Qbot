package com.mm.qbot.controller;

import com.mikuac.shiro.common.utils.MsgUtils;
import com.mikuac.shiro.core.Bot;
import com.mikuac.shiro.core.BotPlugin;
import com.mikuac.shiro.dto.action.anntation.GroupMessageHandler;
import com.mikuac.shiro.dto.action.anntation.PrivateMessageHandler;
import com.mikuac.shiro.dto.event.message.GroupMessageEvent;
import com.mikuac.shiro.dto.event.message.PrivateMessageEvent;
import com.mm.qbot.Exception.BilibiliException;
import com.mm.qbot.dto.*;
import com.mm.qbot.service.BilibiliService;
import com.mm.qbot.strategy.BilibiliParsingStrategy;
import com.mm.qbot.utils.LevelDB;
import org.checkerframework.checker.units.qual.A;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    @Autowired
    private BilibiliService bilibiliService;

      @PrivateMessageHandler(regex="[Bb][Vv][a-zA-Z0-9]{10}")
      public int BvidParsingInPrivate(@NotNull Bot bot, PrivateMessageEvent event,Matcher M) {


        if (M!=null&&M.lookingAt()){

            MsgUtils msgUtils = BilibiliParsingStrategy.ParsingBID(M.group(0));
            if (event!=null&&msgUtils!=null)
                bot.sendPrivateMsg(event.getPrivateSender().getUserId(),msgUtils.build(),false);
//               redisTemplate.opsForValue().set("1",msgUtils.build().getBytes());
        }
        return MESSAGE_BLOCK;
    }


    @GroupMessageHandler(regex="[Bb][Vv][a-zA-Z0-9]{10}")
    public int BvidParsingInGroup(@NotNull Bot bot, GroupMessageEvent event,Matcher M) {


        if (M!=null&&M.lookingAt()){

            MsgUtils msgUtils = BilibiliParsingStrategy.ParsingBID(M.group(0));
            if (event!=null&&msgUtils!=null)
                bot.sendGroupMsg(event.getGroupId(),msgUtils.build(),false);
//               redisTemplate.opsForValue().set("1",msgUtils.build().getBytes());
        }
        return MESSAGE_BLOCK;
    }

    @PrivateMessageHandler(regex="(\\b订阅动态 )(\\S+)")
    public void privateSubscribeInPrivate(@NotNull Bot bot, PrivateMessageEvent event, Matcher M) {

        if (event!=null){
            long Qid=event.getPrivateSender().getUserId();
            Long uid = bilibiliService.followUser(M.group(2));
            if (uid==null){
                bot.sendPrivateMsg(Qid,"订阅失败，请检查输入的uid或者昵称是否正确。\n例如：订阅动态 嘉然今天吃什么",false);
                return;
            }
            bilibiliService.addSubscribe(Qid,uid,userSubscribeMap,Boolean.FALSE,bilibiliPushMap.getPrivateMap());
            bot.sendPrivateMsg(Qid,"订阅成功！",false);
        }
    }


    @GroupMessageHandler(regex="(\\b订阅动态 )(\\S+)")
    public void privateSubscribeInGroup(@NotNull Bot bot, GroupMessageEvent event, Matcher M) {

        if (event!=null) {
            long Qid = Long.parseLong(event.getSender().getUserId());
            Long uid = bilibiliService.followUser(M.group(2));
            if (uid == null) {
                bot.sendGroupMsg(Qid, "订阅失败，请检查输入的uid或者昵称是否正确。\n例如：订阅动态 嘉然今天吃什么", false);
                return;
            }
            bilibiliService.addSubscribe(Qid,uid,userSubscribeMap,Boolean.FALSE,bilibiliPushMap.getPrivateMap());
            bot.sendGroupMsg(Qid, "订阅成功！", false);
        }
    }



    @GroupMessageHandler(regex="(\\b群订阅动态 )(\\S+)")
    public void GroupSubscribe(@NotNull Bot bot,  GroupMessageEvent event,Matcher M) {

        if (event!=null) {
            long Qid =event.getGroupId();
            Long uid = bilibiliService.followUser(M.group(2));
            if (uid == null) {
                bot.sendGroupMsg(Qid, "订阅失败，请检查输入的uid或者昵称是否正确。\n例如：群订阅动态 嘉然今天吃什么", false);
                return;
            }
            bilibiliService.addSubscribe(Qid,uid,userSubscribeMap,Boolean.TRUE,bilibiliPushMap.getGroupMap());
            bot.sendGroupMsg(Qid, "订阅成功！", false);
        }
    }

    @GroupMessageHandler(regex="(\\b最新动态 )(\\S+)")
    public void GetNewDynamicInPrivate(@NotNull Bot bot, PrivateMessageEvent event ,Matcher M) {

//        if (event)

    }


    @GroupMessageHandler(regex="(\\b最新动态 )(\\S+)")
    public int GetNewDynamicInGroup(@NotNull Bot bot, GroupMessageEvent Event ,Matcher M) {


        if (M!=null&&M.lookingAt()){

            MsgUtils msgUtils = BilibiliParsingStrategy.ParsingBID(M.group(0));
//                bot.sendPrivateMsg(event.getUserId(),msgUtils.build(),false);
            LevelDB instance = LevelDB.getInstance();
            instance.put("1","1");
//               redisTemplate.opsForValue().set("1",msgUtils.build().getBytes());
        }
        return MESSAGE_BLOCK;
    }

    @PrivateMessageHandler(regex="b23\\.tv/([a-zA-Z0-9]{6})")
    public int ShortLinkParsing(@NotNull Bot bot, PrivateMessageEvent privateEvent, Matcher M) {


        if (M!=null&&M.lookingAt()){

            MsgUtils msgUtils = BilibiliParsingStrategy.ParsingBID(M.group(0));
//                bot.sendPrivateMsg(event.getUserId(),msgUtils.build(),false);
            LevelDB instance = LevelDB.getInstance();
            instance.put("1","1");
//               redisTemplate.opsForValue().set("1",msgUtils.build().getBytes());
        }
        return MESSAGE_BLOCK;
    }

    @GroupMessageHandler(regex="b23\\.tv/([a-zA-Z0-9]{6})")
    public int ShortLinkParsingInGroup(@NotNull Bot bot  , GroupMessageEvent Event,Matcher M) {


        if (M!=null&&M.lookingAt()){

            MsgUtils msgUtils = BilibiliParsingStrategy.ParsingBID(M.group(0));
//                bot.sendPrivateMsg(event.getUserId(),msgUtils.build(),false);
            LevelDB instance = LevelDB.getInstance();
            instance.put("1","1");
//               redisTemplate.opsForValue().set("1",msgUtils.build().getBytes());
        }
        return MESSAGE_BLOCK;
    }

}
