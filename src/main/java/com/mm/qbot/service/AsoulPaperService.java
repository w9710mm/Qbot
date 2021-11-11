package com.mm.qbot.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mm.qbot.dao.mapper.WeeklyDataMapper;
import com.mm.qbot.dto.asouldata.WeeklyData;
import org.springframework.stereotype.Service;

/**
 * @author meme
 * @version V0.0.1
 * @Package com.mm.qbot.service
 * @Description:
 * @date 2021/11/10 15:46
 */
@Service
public class AsoulPaperService extends ServiceImpl<WeeklyDataMapper, WeeklyData> {

    public void selectUserPage() {

        WeeklyData weeklyData = baseMapper.selectById(1);

    }
}
