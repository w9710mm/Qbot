package com.mm.qbot.controller;

import com.mikuac.shiro.annotation.GroupMessageHandler;
import com.mikuac.shiro.annotation.PrivateMessageHandler;
import com.mikuac.shiro.common.utils.MsgUtils;
import com.mikuac.shiro.core.Bot;
import com.mikuac.shiro.core.BotPlugin;
import com.mikuac.shiro.dto.event.message.GroupMessageEvent;
import com.mikuac.shiro.dto.event.message.PrivateMessageEvent;
import com.mm.qbot.dto.*;
import com.mm.qbot.service.BilibiliService;
import com.mm.qbot.strategy.BilibiliParsingStrategy;
import com.mm.qbot.utils.LevelDB;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
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



        MsgUtils msgUtils = BilibiliParsingStrategy.ParsingBID(m.group(0));
        if (event!=null&&msgUtils!=null) {
            bot.sendPrivateMsg(event.getPrivateSender().getUserId(),msgUtils.build(),false);
        }
//               redisTemplate.opsForValue().set("1",msgUtils.build().getBytes());

        return MESSAGE_BLOCK;
    }


    @GroupMessageHandler(cmd="[Bb][Vv][a-zA-Z0-9]{10}")
    public int bvidParsingInGroup(@NotNull Bot bot, GroupMessageEvent event, Matcher m) {



        MsgUtils msgUtils = BilibiliParsingStrategy.ParsingBID(m.group(0));
        if (event!=null&&msgUtils!=null) {
            bot.sendGroupMsg(event.getGroupId(),msgUtils.build(),false);
        }
//               redisTemplate.opsForValue().set("1",msgUtils.build().getBytes());

        return MESSAGE_BLOCK;
    }

    @PrivateMessageHandler(cmd="(\\b订阅动态 )(\\S+)")
    public void privateSubscribeInPrivate(@NotNull Bot bot, PrivateMessageEvent event, Matcher m) {



    }


    @GroupMessageHandler(cmd="(\\b订阅动态 )(\\S+)")
    public void privateSubscribeInGroup(@NotNull Bot bot, GroupMessageEvent event, Matcher m) {

        long qid = Long.parseLong(event.getSender().getUserId());

        Long uid = bilibiliService.findUid(m.group(2));
        MsgUtils msgUtils=bilibiliService.followUser(uid);

        if (msgUtils!=null){
            boolean flag = bilibiliService.addSubscribe(qid, uid, userSubscribeMap, Boolean.FALSE, bilibiliPushMap.getPrivateMap());
            if (!flag){
                bot.sendGroupMsg(qid,msgUtils.build(),false);
                return;
            }
        }

        msgUtils=MsgUtils.builder().text("订阅失败，请检查输入的uid或者昵称是否正确。\n例如：群订阅动态 嘉然今天吃什么");
        bot.sendGroupMsg(qid,msgUtils.build(), false);

    }



    @GroupMessageHandler(cmd="(\\b群订阅动态 )(\\S+)")
    public void groupSubscribe(@NotNull Bot bot, GroupMessageEvent event, Matcher m) {

        long qid = event.getGroupId();

        Long uid = bilibiliService.findUid(m.group(2));
        MsgUtils msgUtils=bilibiliService.followUser(uid);

        if (msgUtils!=null){
            boolean flag = bilibiliService.addSubscribe(qid, uid, userSubscribeMap, Boolean.FALSE, bilibiliPushMap.getGroupMap());
            if (!flag){
                bot.sendGroupMsg(qid,msgUtils.build(),false);
                return;
            }
        }

        msgUtils=MsgUtils.builder().text("订阅失败，请检查输入的uid或者昵称是否正确。\n例如：群订阅动态 嘉然今天吃什么");
        bot.sendGroupMsg(qid,msgUtils.build(), false);

    }

    @PrivateMessageHandler(cmd="(\\b最新动态 )(\\S+)")
    public void getNewDynamicInPrivate(@NotNull Bot bot, PrivateMessageEvent event, Matcher m) {
        Long uid = bilibiliService.findUid(m.group(2));

        if (uid==null){
            return;
        }
        MsgUtils msgUtils = bilibiliService.getNewDynamic(uid);

        if (msgUtils==null){
            return;
        }
        bot.sendPrivateMsg(event.getPrivateSender().getUserId(),msgUtils.build(),false);

    }


    @GroupMessageHandler(cmd="(\\b最新动态 )(\\S+)")
    public void getNewDynamicInGroup(@NotNull Bot bot, GroupMessageEvent event, Matcher m) {

        Long uid = bilibiliService.findUid(m.group(2));

        if (uid==null){
            return;
        }
        MsgUtils msgUtils = bilibiliService.getNewDynamic(uid);

        if (msgUtils==null){
            return;
        }
        bot.sendGroupMsg(event.getGroupId(),msgUtils.build(),false);

    }

    @PrivateMessageHandler(cmd="b23\\.tv/([a-zA-Z0-9]{6})")
    public void shortLinkParsing(@NotNull Bot bot, PrivateMessageEvent event, Matcher m) {

        MsgUtils msgUtils = BilibiliParsingStrategy.ParsingBID(m.group(0));
        if (msgUtils==null){
            return;
        }
        bot.sendPrivateMsg(event.getPrivateSender().getUserId(),msgUtils.build(),false);
    }

    @GroupMessageHandler(cmd="b23\\.tv/([a-zA-Z0-9]{6})")
    public void shortLinkParsingInGroup(@NotNull Bot bot  , GroupMessageEvent event, Matcher m) {



        MsgUtils msgUtils = BilibiliParsingStrategy.ParsingBID(m.group(0));
        if (msgUtils==null){
            return;
        }
        bot.sendPrivateMsg(event.getGroupId(),msgUtils.build(),false);
    }


    @PrivateMessageHandler(cmd="(\\b最新视频 )(\\S+)")
    public void getNewVideoInPrivate(@NotNull Bot bot, PrivateMessageEvent event, Matcher m) {
        Long uid = bilibiliService.findUid(m.group(2));

        if (uid==null){
            return;
        }
        MsgUtils msgUtils = bilibiliService.getNewDynamic(uid);

        if (msgUtils==null){
            return;
        }
        bot.sendGroupMsg(event.getPrivateSender().getUserId(),msgUtils.build(),false);

    }


    @GroupMessageHandler(cmd="(\\b最新视频 )(\\S+)")
    public void getNewVideoInGroup(@NotNull Bot bot, GroupMessageEvent event, Matcher m) {

        Long uid = bilibiliService.findUid(m.group(2));

        if (uid==null){
            return;
        }
        MsgUtils msgUtils = bilibiliService.getNewDynamic(uid);

        if (msgUtils==null){
            return;
        }
        bot.sendGroupMsg(event.getGroupId(),msgUtils.build(),false);

    }
}
