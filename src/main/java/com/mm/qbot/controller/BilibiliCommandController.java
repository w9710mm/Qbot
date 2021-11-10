package com.mm.qbot.controller;import com.mikuac.shiro.annotation.GroupMessageHandler;import com.mikuac.shiro.annotation.PrivateMessageHandler;import com.mikuac.shiro.common.utils.MsgUtils;import com.mikuac.shiro.core.Bot;import com.mikuac.shiro.core.BotPlugin;import com.mikuac.shiro.dto.event.message.GroupMessageEvent;import com.mikuac.shiro.dto.event.message.PrivateMessageEvent;import com.mm.qbot.dto.pushMap.BilibiliPushMap;import com.mm.qbot.dto.pushMap.User;import com.mm.qbot.dto.pushMap.UserSubscribe;import com.mm.qbot.dto.pushMap.UserSubscribeMap;import com.mm.qbot.service.BilibiliService;import com.mm.qbot.strategy.BilibiliStrategy;import lombok.extern.slf4j.Slf4j;import org.jetbrains.annotations.NotNull;import org.springframework.stereotype.Controller;import javax.annotation.Resource;import java.util.LinkedHashSet;import java.util.Map;import java.util.regex.Matcher;/** * @author WWM * @version V0.0.1 * @Package com.mm.qbot.controller * @Description: 解析bilibili * @date 2021/10/23 14:57 */@Controller@Slf4jpublic class BilibiliCommandController extends BotPlugin {    private final BilibiliPushMap bilibiliPushMap =BilibiliPushMap.getInstance();    private final UserSubscribeMap userSubscribeMap=UserSubscribeMap.getInstance();    @Resource    private BilibiliService bilibiliService;      @PrivateMessageHandler(cmd="[Bb][Vv][a-zA-Z0-9]{10}",userWhiteList = 962349367)      public void bvidParsingInPrivate(@NotNull Bot bot, PrivateMessageEvent event, Matcher m) {          log.info(String.format("解析BV号：%s",m.group(0)));        MsgUtils msgUtils = BilibiliStrategy.parsingBID(m.group(0));        if (event!=null&&msgUtils!=null) {            bot.sendPrivateMsg(event.getPrivateSender().getUserId(),msgUtils.build(),false);            log.info(String.format("发送给私人%sBV%s信息",event.getPrivateSender().getUserId(),m.group(0)));        }    }    @GroupMessageHandler(cmd="[Bb][Vv][a-zA-Z0-9]{10}",userWhiteList = 962349367)    public void bvidParsingInGroup(@NotNull Bot bot, GroupMessageEvent event, Matcher m) {        log.info(String.format("解析BV号：%s",m.group(0)));        MsgUtils msgUtils = BilibiliStrategy.parsingBID(m.group(0));        if (event!=null&&msgUtils!=null) {            bot.sendGroupMsg(event.getGroupId(),msgUtils.build(),false);        }        log.info(String.format("发送给群%sBV%s信息",event.getGroupId(),m.group(0)));    }    @PrivateMessageHandler(cmd="(\\b订阅动态 )(\\S+)",userWhiteList = 962349367)    public void privateSubscribeInPrivate(@NotNull Bot bot, PrivateMessageEvent event, Matcher m) {        long qid = Long.parseLong(String.valueOf(event.getPrivateSender().getUserId()));        log.info(String.format("%d订阅%s的动态",qid,m.group(2)));        User uid = bilibiliService.findUser(m.group(2));        MsgUtils msgUtils=bilibiliService.followUser(uid);        if (msgUtils!=null){            boolean flag = bilibiliService.addSubscribe(qid, uid, userSubscribeMap, Boolean.FALSE, bilibiliPushMap.getPrivateMap());            if (flag){                bot.sendPrivateMsg(qid,msgUtils.build(),false);                return;            }        }        log.error(String.format("%d订阅%s的动态失败",qid,m.group(2)));        msgUtils=MsgUtils.builder().text("订阅失败，请检查输入的uid或者昵称是否正确。\n例如：订阅动态 嘉然今天吃什么");        bot.sendPrivateMsg(qid,msgUtils.build(), false);    }    @GroupMessageHandler(cmd="(\\b订阅动态 )(\\S+)",userWhiteList = 962349367)    public void privateSubscribeInGroup(@NotNull Bot bot, GroupMessageEvent event, Matcher m) {        long qid = Long.parseLong(event.getSender().getUserId());        log.info(String.format("%d订阅%s的动态",qid,m.group(2)));        User uid = bilibiliService.findUser(m.group(2));        MsgUtils msgUtils=bilibiliService.followUser(uid);        if (msgUtils!=null){            boolean flag = bilibiliService.addSubscribe(qid, uid, userSubscribeMap, Boolean.FALSE, bilibiliPushMap.getPrivateMap());            if (flag){                bot.sendGroupMsg(qid,msgUtils.build(),false);                return;            }        }        log.error(String.format("%d订阅%s的动态失败",qid,m.group(2)));        msgUtils=MsgUtils.builder().text("订阅失败，请检查输入的uid或者昵称是否正确。\n例如：订阅动态 嘉然今天吃什么");        bot.sendGroupMsg(qid,msgUtils.build(), false);    }    @GroupMessageHandler(cmd="(\\b群订阅动态 )(\\S+)",userWhiteList = 962349367)    public void groupSubscribe(@NotNull Bot bot, GroupMessageEvent event, Matcher m) {        long qid = event.getGroupId();        log.info(String.format("群%d订阅%s的动态",qid,m.group(2)));        User uid = bilibiliService.findUser(m.group(2));        MsgUtils msgUtils=bilibiliService.followUser(uid);        if (msgUtils!=null){            boolean flag = bilibiliService.addSubscribe(qid, uid, userSubscribeMap, Boolean.TRUE, bilibiliPushMap.getGroupMap());            if (flag){                bot.sendGroupMsg(qid,msgUtils.build(),false);                return;            }        }        log.error(String.format("群%d订阅%s的动态失败",qid,m.group(2)));        msgUtils=MsgUtils.builder().text("订阅失败，请检查输入的uid或者昵称是否正确。\n例如：群订阅动态 嘉然今天吃什么");        bot.sendGroupMsg(qid,msgUtils.build(), false);    }    @PrivateMessageHandler(cmd="(\\b最新动态 )(\\S+)",userWhiteList = 962349367)    public void getNewDynamicInPrivate(@NotNull Bot bot, PrivateMessageEvent event, Matcher m) {        log.info(String.format("%s获取%s的最新动态",event.getPrivateSender().getUserId(),m.group(2)));          User uid = bilibiliService.findUser(m.group(2));        if (uid==null){            log.debug(String.format("%s获取%s的最新动态失败",event.getPrivateSender().getUserId(),m.group(2)));            return;        }        MsgUtils msgUtils = bilibiliService.getNewDynamic(Long.valueOf(uid.getUid()));        if (msgUtils==null){            log.debug(String.format("%s获取%s的最新动态失败",event.getPrivateSender().getUserId(),m.group(2)));            return;        }        bot.sendPrivateMsg(event.getPrivateSender().getUserId(),msgUtils.build(),false);    }    @GroupMessageHandler(cmd="(\\b最新动态 )(\\S+)",userWhiteList = 962349367)    public void getNewDynamicInGroup(@NotNull Bot bot, GroupMessageEvent event, Matcher m) {        log.info(String.format("群%s获取%s的最新动态",event.getGroupId(),m.group(2)));        User uid = bilibiliService.findUser(m.group(2));        if (uid==null){            log.debug(String.format("群%s获取%s的最新动态失败",event.getGroupId(),m.group(2)));            return;        }        MsgUtils msgUtils = bilibiliService.getNewDynamic(Long.valueOf(uid.getUid()));        if (msgUtils==null){            log.debug(String.format("群%s获取%s的最新动态失败",event.getGroupId(),m.group(2)));            return;        }        bot.sendGroupMsg(event.getGroupId(),msgUtils.build(),false);    }    @PrivateMessageHandler(cmd="b23\\.tv/([a-zA-Z0-9]{6})",userWhiteList = 962349367)    public void shortLinkParsingInPrivate1(@NotNull Bot bot, PrivateMessageEvent event, Matcher m) {        log.info(String.format("%s解析短链接：%s",event.getPrivateSender().getUserId(),m.group(1)));        MsgUtils msgUtils = bilibiliService.parsingShortLink(m.group(1));        if (msgUtils!=null) {            bot.sendPrivateMsg(event.getPrivateSender().getUserId(),msgUtils.build(),false);            return;        }        log.debug(String.format("%s解析短链接：%s失败",event.getPrivateSender().getUserId(),m.group(1)));    }    @PrivateMessageHandler(cmd="b23\\.tv\\\\/([a-zA-Z0-9]{6})",userWhiteList = 962349367)    public void shortLinkParsingInPrivate2(@NotNull Bot bot, PrivateMessageEvent event, Matcher m) {        log.info(String.format("%s解析短链接：%s",event.getPrivateSender().getUserId(),m.group(1)));        MsgUtils msgUtils = bilibiliService.parsingShortLink(m.group(1));        if (msgUtils!=null) {            bot.sendPrivateMsg(event.getPrivateSender().getUserId(),msgUtils.build(),false);            return;        }        log.debug(String.format("%s解析短链接：%s失败",event.getPrivateSender().getUserId(),m.group(1)));    }    @GroupMessageHandler(cmd="b23\\.tv/([a-zA-Z0-9]{6})",userWhiteList = 962349367)    public void shortLinkParsingInGroup1(@NotNull Bot bot  , GroupMessageEvent event, Matcher m) {        log.info(String.format("群%s解析短链接：%s",event.getGroupId(),m.group(1)));        MsgUtils msgUtils = bilibiliService.parsingShortLink(m.group(1));        if (msgUtils==null){            log.debug(String.format("群%s解析短链接：%s失败",event.getGroupId(),m.group(1)));            return;        }        bot.sendGroupMsg(event.getGroupId(),msgUtils.build(),false);    }    @GroupMessageHandler(cmd="b23\\.tv\\\\/([a-zA-Z0-9]{6})",userWhiteList = 962349367)    public void shortLinkParsingInGroup2(@NotNull Bot bot  , GroupMessageEvent event, Matcher m) {        log.info(String.format("群%s解析短链接：%s",event.getGroupId(),m.group(1)));        MsgUtils msgUtils = bilibiliService.parsingShortLink(m.group(1));        if (msgUtils==null){            log.debug(String.format("群%s解析短链接：%s失败",event.getGroupId(),m.group(1)));            return;        }        bot.sendGroupMsg(event.getGroupId(),msgUtils.build(),false);    }    @PrivateMessageHandler(cmd="t\\.bilibili\\.com/([0-9]+)",userWhiteList = 962349367)    public void dynamicInPrivate1(@NotNull Bot bot  , PrivateMessageEvent event, Matcher m) {        log.info(String.format("%s解析动态：%s",event.getPrivateSender().getUserId(),m.group(1)));        MsgUtils msgUtils = bilibiliService.parsingDynamic(m.group(1));        if (msgUtils==null){            log.info(String.format("%s解析动态：%s失败",event.getPrivateSender().getUserId(),m.group(1)));            return;        }        bot.sendPrivateMsg(event.getPrivateSender().getUserId(),msgUtils.build(),false);    }    @PrivateMessageHandler(cmd="m\\.bilibili\\.com/dynamic/([0-9]+)",userWhiteList = 962349367)    public void dynamicInPrivate2(@NotNull Bot bot  , PrivateMessageEvent event, Matcher m) {        log.info(String.format("%s解析动态：%s",event.getPrivateSender().getUserId(),m.group(1)));        MsgUtils msgUtils = bilibiliService.parsingDynamic(m.group(1));        if (msgUtils==null){            log.info(String.format("%s解析动态：%s失败",event.getPrivateSender().getUserId(),m.group(1)));            return;        }        bot.sendPrivateMsg(event.getPrivateSender().getUserId(),msgUtils.build(),false);    }    @GroupMessageHandler(cmd="m\\.bilibili\\.com/dynamic/([0-9]+)",userWhiteList = 962349367)    public void dynamicInGroup1(@NotNull Bot bot  , GroupMessageEvent event, Matcher m) {        log.info(String.format("群%s解析动态：%s失败",event.getGroupId(),m.group(1)));        MsgUtils msgUtils = bilibiliService.parsingDynamic(m.group(1));        if (msgUtils==null){            log.info(String.format("群%s解析动态：%s失败",event.getGroupId(),m.group(1)));            return;        }        bot.sendGroupMsg(event.getGroupId(),msgUtils.build(),false);    }    @GroupMessageHandler(cmd="t\\.bilibili\\.com/([0-9]+)",userWhiteList = 962349367)    public void dynamicInGroup2(@NotNull Bot bot  , GroupMessageEvent event, Matcher m) {        log.info(String.format("群%s解析动态：%s失败",event.getGroupId(),m.group(1)));        MsgUtils msgUtils = bilibiliService.parsingDynamic(m.group(1));        if (msgUtils==null){            log.info(String.format("群%s解析动态：%s失败",event.getGroupId(),m.group(1)));            return;        }        bot.sendGroupMsg(event.getGroupId(),msgUtils.build(),false);    }    @PrivateMessageHandler(cmd="(\\b最新视频 )(\\S+)",userWhiteList = 962349367)    public void getNewVideoInPrivate(@NotNull Bot bot, PrivateMessageEvent event, Matcher m) {        User uid = bilibiliService.findUser(m.group(2));        if (uid==null){            return;        }        MsgUtils msgUtils = bilibiliService.getNewDynamic(Long.valueOf(uid.getUid()));        if (msgUtils==null){            return;        }        bot.sendGroupMsg(event.getPrivateSender().getUserId(),msgUtils.build(),false);    }    @GroupMessageHandler(cmd="(\\b最新视频 )(\\S+)",userWhiteList = 962349367)    public void getNewVideoInGroup(@NotNull Bot bot, GroupMessageEvent event, Matcher m) {        User uid = bilibiliService.findUser(m.group(2));        if (uid==null){            return;        }        MsgUtils msgUtils = bilibiliService.getNewDynamic(Long.valueOf(uid.getUid()));        if (msgUtils==null){            return;        }        bot.sendGroupMsg(event.getGroupId(),msgUtils.build(),false);    }    @GroupMessageHandler(cmd="(\\b动态列表)",userWhiteList = 962349367)    public void showSubscribeListInGroup(@NotNull Bot bot, GroupMessageEvent event) {          long qid= Long.parseLong(event.getSender().getUserId());        Map<Long, UserSubscribe> subscribeMap = userSubscribeMap.getPrivateSubscribeMap();        MsgUtils msgUtils = bilibiliService.showSubscribe(qid, subscribeMap);        bot.sendGroupMsg(event.getGroupId(),msgUtils.build(),true);    }    @PrivateMessageHandler(cmd="(\\b动态列表)",userWhiteList = 962349367)    public void showSubscribeListInPrivate(@NotNull Bot bot, PrivateMessageEvent event) {        long qid= event.getPrivateSender().getUserId();        Map<Long, UserSubscribe> subscribeMap = userSubscribeMap.getPrivateSubscribeMap();        MsgUtils msgUtils = bilibiliService.showSubscribe(qid, subscribeMap);        bot.sendPrivateMsg(qid,msgUtils.build(),true);    }    @PrivateMessageHandler(cmd="(\\b群动态列表)",userWhiteList = 962349367)    public void showGroupSubscribeList(@NotNull Bot bot, GroupMessageEvent event) {        long qid= event.getGroupId();        Map<Long, UserSubscribe> subscribeMap = userSubscribeMap.getGroupSubscribeMap();        MsgUtils msgUtils = bilibiliService.showSubscribe(qid, subscribeMap);        bot.sendGroupMsg(qid,msgUtils.build(),true);    }    @PrivateMessageHandler(cmd="(\\b取消动态 )([0-9]+)",userWhiteList = 962349367)    public void undoSubscribeInPrivate(@NotNull Bot bot, @NotNull PrivateMessageEvent event, @NotNull Matcher m) {        long qid= event.getPrivateSender().getUserId();        int num= Integer.parseInt(m.group(2));        Map<Long, UserSubscribe> subscribeMap = userSubscribeMap.getPrivateSubscribeMap();        Map<User, LinkedHashSet<Long>> privateMap = bilibiliPushMap.getPrivateMap();        MsgUtils utils = bilibiliService.undoSubscribe(qid, num, subscribeMap, privateMap);        bot.sendGroupMsg(qid,utils.build(),true);    }    @GroupMessageHandler(cmd="(\\b取消动态 )([0-9]+)",userWhiteList = 962349367)    public void undoSubscribeInGroup(@NotNull Bot bot, @NotNull GroupMessageEvent event, @NotNull Matcher m) {        long qid= event.getGroupId();        int num= Integer.parseInt(m.group(2));        Map<Long, UserSubscribe> subscribeMap = userSubscribeMap.getPrivateSubscribeMap();        Map<User, LinkedHashSet<Long>> privateMap = bilibiliPushMap.getPrivateMap();        MsgUtils utils = bilibiliService.undoSubscribe(qid, num, subscribeMap, privateMap);        bot.sendGroupMsg(qid,utils.build(),true);    }    @GroupMessageHandler(cmd="(\\b群取消动态 )([0-9]+)",userWhiteList = 962349367)    public void undoGroupSubscribe(@NotNull Bot bot, @NotNull GroupMessageEvent event, @NotNull Matcher m) {        long qid= event.getGroupId();        int num= Integer.parseInt(m.group(2));        Map<Long, UserSubscribe> subscribeMap = userSubscribeMap.getGroupSubscribeMap();        Map<User, LinkedHashSet<Long>> groupMap = bilibiliPushMap.getGroupMap();        MsgUtils utils = bilibiliService.undoSubscribe(qid, num, subscribeMap, groupMap);        bot.sendGroupMsg(qid,utils.build(),true);    }    @GroupMessageHandler(cmd="(\\b群取消动态 )all",userWhiteList = 962349367)    public void undoAllGroupSubscribe(@NotNull Bot bot, @NotNull GroupMessageEvent event) {        long qid= event.getGroupId();        Map<Long, UserSubscribe> subscribeMap = userSubscribeMap.getGroupSubscribeMap();        Map<User, LinkedHashSet<Long>> groupMap = bilibiliPushMap.getGroupMap();        MsgUtils utils = bilibiliService.undoAllSubscribe(qid, subscribeMap, groupMap);        bot.sendGroupMsg(qid,utils.build(),true);    }    @GroupMessageHandler(cmd="(\\b取消动态 )all",userWhiteList = 962349367)    public void undoAllPrivateSubscribeInGroup(@NotNull Bot bot, @NotNull GroupMessageEvent event) {        long qid= Long.parseLong(event.getSender().getUserId());        Map<Long, UserSubscribe> subscribeMap = userSubscribeMap.getPrivateSubscribeMap();        Map<User, LinkedHashSet<Long>> groupMap = bilibiliPushMap.getPrivateMap();        MsgUtils utils = bilibiliService.undoAllSubscribe(qid, subscribeMap, groupMap);        bot.sendGroupMsg(event.getGroupId(),utils.build(),true);    }    @PrivateMessageHandler(cmd="(\\b取消动态 )all",userWhiteList = 962349367)    public void undoAllPrivateSubscribeInPrivate(@NotNull Bot bot, @NotNull PrivateMessageEvent event) {        long qid= event.getPrivateSender().getUserId();        Map<Long, UserSubscribe> subscribeMap = userSubscribeMap.getPrivateSubscribeMap();        Map<User, LinkedHashSet<Long>> privateMap = bilibiliPushMap.getPrivateMap();        MsgUtils utils = bilibiliService.undoAllSubscribe(qid, subscribeMap, privateMap);        bot.sendPrivateMsg(qid,utils.build(),true);    }    @PrivateMessageHandler(cmd="(\\buid信息 )(\\S+)",userWhiteList = 962349367)    public void undoAllPrivateSubscribeInPrivate(@NotNull Bot bot, @NotNull PrivateMessageEvent event,Matcher m) {        User user = bilibiliService.findUser(m.group(2));        MsgUtils msgUtils = bilibiliService.getUpInfo(user.getUid());        bot.sendPrivateMsg(event.getPrivateSender().getUserId(),msgUtils.build(),false);    }}