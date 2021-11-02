package com.mm.qbot.controller;

import com.mikuac.shiro.common.utils.MsgUtils;
import com.mikuac.shiro.core.Bot;
import com.mikuac.shiro.core.BotPlugin;
import com.mikuac.shiro.dto.action.anntation.GroupMessageHandler;
import com.mikuac.shiro.dto.action.anntation.PrivateMessageHandler;
import com.mikuac.shiro.dto.event.message.GroupMessageEvent;
import com.mikuac.shiro.dto.event.message.PrivateMessageEvent;
import com.mm.qbot.Exception.BilibiliException;
import com.mm.qbot.dto.BilibiliPushMap;
import com.mm.qbot.dto.TiktokPushMap;
import com.mm.qbot.dto.UserSubscribeMap;
import com.mm.qbot.dto.WeiBoPushMap;
import com.mm.qbot.strategy.BilibiliParsingStrategy;
import com.mm.qbot.utils.LevelDB;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Controller;

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

      @PrivateMessageHandler(regex="[Bb][Vv][a-zA-Z0-9]{10}")
    public int BvidParsing(@NotNull Bot bot, @NotNull PrivateMessageEvent event,Matcher M) {


        if (M!=null&&M.lookingAt()){
            try {

                MsgUtils msgUtils = BilibiliParsingStrategy.ParsingBID(M.group(0));
                bot.sendPrivateMsg(event.getUserId(),msgUtils.build(),false);
                LevelDB instance = LevelDB.getInstance();
                instance.put("1","1");
//               redisTemplate.opsForValue().set("1",msgUtils.build().getBytes());
            } catch (BilibiliException e) {
                e.printStackTrace();
            }
        }
        return MESSAGE_BLOCK;
    }

    @GroupMessageHandler(regex="(\\b订阅动态 )(\\S+)")
    @PrivateMessageHandler(regex="(\\b订阅动态 )(\\S+)")
    public int privateSubscribe(@NotNull Bot bot, PrivateMessageEvent privateEvent, GroupMessageEvent groupEvent, Matcher M) {


        if (privateEvent!=null){
            long userId = privateEvent.getPrivateSender().getUserId();


        }
        if(groupEvent!=null) {

        }
        return MESSAGE_BLOCK;
    }


    @GroupMessageHandler(regex="(\\b群订阅动态 )(\\S+)")
    public int GroupSubscribe(@NotNull Bot bot,  GroupMessageEvent event,Matcher M) {

        if (M!=null&&M.lookingAt()){
            try {

                MsgUtils msgUtils = BilibiliParsingStrategy.ParsingBID(M.group(0));
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

    @PrivateMessageHandler(regex="(\\b最新动态 )(\\S+)")
    @GroupMessageHandler(regex="(\\b最新动态 )(\\S+)")
    public int GetNewDynamic(@NotNull Bot bot, PrivateMessageEvent privateEvent, GroupMessageEvent GroupEvent,Matcher M) {


        if (M!=null&&M.lookingAt()){
            try {

                MsgUtils msgUtils = BilibiliParsingStrategy.ParsingBID(M.group(0));
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

    @PrivateMessageHandler(regex="b23\\.tv/([a-zA-Z0-9]{6})")
    @GroupMessageHandler(regex="b23\\.tv/([a-zA-Z0-9]{6})")
    public int ShortLinkParsing(@NotNull Bot bot, PrivateMessageEvent privateEvent, GroupMessageEvent GroupEvent,Matcher M) {


        if (M!=null&&M.lookingAt()){
            try {

                MsgUtils msgUtils = BilibiliParsingStrategy.ParsingBID(M.group(0));
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
