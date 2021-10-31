package com.mm.qbot.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.context.annotation.Bean;

import java.util.List;

/**
 * @author meme
 * @version V0.0.1
 * @Package com.mm.qbot.dto
 * @Description: 用户订阅的类
 * @date 2021/10/31 23:08
 */


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserSubscribe {
    private Long id;

    //订阅的b用户
    private List<Long> bids;

    private List<Long> weiboids;

    private List<Long> Tikids;
    //是否订阅视频
    private Boolean isBVideo;

    //是否订阅
}

