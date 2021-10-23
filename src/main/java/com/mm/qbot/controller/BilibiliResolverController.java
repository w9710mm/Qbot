package com.mm.qbot.controller;

import com.mikuac.shiro.common.utils.MsgUtils;
import com.mikuac.shiro.core.Bot;
import com.mikuac.shiro.core.BotPlugin;
import com.mikuac.shiro.dto.event.message.GroupMessageEvent;
import com.mikuac.shiro.dto.event.message.PrivateMessageEvent;
import com.mm.qbot.BilibiliException;
import com.mm.qbot.strategy.BilibiliParsingStrategy;
import com.mm.qbot.utils.BilibiliApi;
import lombok.extern.java.Log;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

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
public class BilibiliResolverController extends BotPlugin {

    private final String   bvidMacher= "[Bb][Vv][a-zA-Z0-9]{10}";;
    Pattern bvidPattern = Pattern.compile(bvidMacher);


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
    @Override
    public int onGroupMessage(@NotNull Bot bot, @NotNull GroupMessageEvent event) {
        new MsgUtils();
        String rawMessage = event.getRawMessage();
        Matcher m  = bvidPattern.matcher(rawMessage);

        if (m.find()){
            try {
                MsgUtils msgUtils = BilibiliParsingStrategy.ParsingBID(m.group(0));
                bot.sendGroupMsg(event.getGroupId(),msgUtils.build(),true);
            } catch (BilibiliException e) {
                e.printStackTrace();
            }
        }


        return MESSAGE_IGNORE;
    }

}
