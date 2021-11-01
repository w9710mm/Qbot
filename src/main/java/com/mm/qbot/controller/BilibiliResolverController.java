package com.mm.qbot.controller;

import com.mikuac.shiro.common.utils.MsgUtils;
import com.mikuac.shiro.core.Bot;
import com.mikuac.shiro.core.BotPlugin;
import com.mikuac.shiro.dto.action.anntation.GroupMessageHandler;
import com.mikuac.shiro.dto.action.anntation.PrivateMessageHandler;
import com.mikuac.shiro.dto.event.message.PrivateMessageEvent;
import com.mm.qbot.Exception.BilibiliException;
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

@EnableAsync
@Controller
public class BilibiliResolverController extends BotPlugin {

    public final String   bvidMacher= "[Bb][Vv][a-zA-Z0-9]{10}";

    public Pattern bvidPattern = Pattern.compile(bvidMacher);

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

  @PrivateMessageHandler(regex="[Bb][Vv][a-zA-Z0-9]{10}")
    public int onPrivat1eMessage(@NotNull Bot bot, @NotNull PrivateMessageEvent event,Matcher M) {
        new MsgUtils();


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










}
