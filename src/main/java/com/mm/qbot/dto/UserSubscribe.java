package com.mm.qbot.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.context.annotation.Bean;

import java.io.Serializable;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

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
public class UserSubscribe  implements Serializable {
    private Long id;

    //订阅的b用户
    private LinkedHashSet<User> bids=new LinkedHashSet<>();

    private LinkedHashSet<User> weiboids=new LinkedHashSet<>();

    private LinkedHashSet<User> tikid =new LinkedHashSet<>();
    //是否订阅视频
    private Boolean isBVideo =Boolean.TRUE;

    private Boolean isDynamic=Boolean.TRUE;

    private Boolean isLive=Boolean.FALSE;

    private  Boolean isArticle=Boolean.TRUE;


    //是否是群
    private Boolean isGroup;
    //是否订阅
}

