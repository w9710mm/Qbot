package com.mm.qbot.controller;

import com.mikuac.shiro.common.utils.MsgUtils;
import com.mikuac.shiro.core.Bot;
import com.mikuac.shiro.core.BotPlugin;
import com.mikuac.shiro.dto.action.anntation.PrivateMessageHandler;
import com.mikuac.shiro.dto.event.message.PrivateMessageEvent;
import com.mm.qbot.Exception.BilibiliException;
import com.mm.qbot.strategy.BilibiliParsingStrategy;
import org.jetbrains.annotations.NotNull;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Controller;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.alibaba.fastjson.JSONPatch.OperationType.test;

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



  @PrivateMessageHandler(regex="[Bb][Vv][a-zA-Z0-9]{10}")
    public int onPrivat1eMessage(@NotNull Bot bot, @NotNull PrivateMessageEvent event,Matcher M) {
        new MsgUtils();


        if (M.lookingAt()){
            try {

                MsgUtils msgUtils = BilibiliParsingStrategy.ParsingBID(M.group(0));
                bot.sendPrivateMsg(event.getUserId(),msgUtils.build(),false);
            } catch (BilibiliException e) {
                e.printStackTrace();
            }
        }
        TT T=new TT();
        T.t();
        return MESSAGE_BLOCK;
    }










}
