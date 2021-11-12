package com.mm.qbot.bean.pushMap;

import lombok.Data;

import java.io.Serializable;

/**
 * @author meme
 * @version V0.0.1
 * @Package com.mm.qbot.dto
 * @Description:
 * @date 2021/11/5 13:10
 */
@Data

public class User implements Serializable {

    private String uid;

    private String uname;
}
