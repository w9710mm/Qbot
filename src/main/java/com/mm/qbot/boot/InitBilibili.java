package com.mm.qbot.boot;

import com.mm.qbot.dto.BilibiliPushMap;
import com.mm.qbot.utils.LevelDB;
import org.springframework.stereotype.Component;

/**
 * @author meme
 * @version V0.0.1
 * @Package com.mm.qbot.boot
 * @Description: 初始化bilibil
 * @date 2021/11/2 9:05
 */

@Component
public class InitBilibili {

    private final LevelDB levelDB =LevelDB.getInstance();

    private final BilibiliPushMap bilibiliPushMap =BilibiliPushMap.getBilibiliPushMap();



}
