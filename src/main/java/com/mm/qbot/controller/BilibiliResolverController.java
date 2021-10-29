package com.mm.qbot.controller;

import com.mikuac.shiro.common.utils.MsgUtils;
import com.mikuac.shiro.core.Bot;
import com.mikuac.shiro.core.BotPlugin;
import com.mikuac.shiro.dto.action.anntation.GroupMessageHandler;
import com.mikuac.shiro.dto.action.anntation.PrivateMessageHandler;
import com.mikuac.shiro.dto.event.message.GroupMessageEvent;
import com.mikuac.shiro.dto.event.message.PrivateMessageEvent;
import com.mm.qbot.Exception.BilibiliException;
import com.mm.qbot.strategy.BilibiliParsingStrategy;
import org.jetbrains.annotations.NotNull;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Controller;

import java.util.List;
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



    @Override
    public int onPrivateMessage(@NotNull Bot bot, @NotNull PrivateMessageEvent event) {
        new MsgUtils();
        String rawMessage = event.getRawMessage();
        Matcher m  = bvidPattern.matcher(rawMessage);

        if (m.find()){
            try {
                MsgUtils msgUtils = BilibiliParsingStrategy.ParsingBID(m.group(0));
                bot.sendPrivateMsg(event.getUserId(),msgUtils.build(),false);
            } catch (BilibiliException e) {
                e.printStackTrace();
            }
        }

        return MESSAGE_IGNORE;
    }






}
